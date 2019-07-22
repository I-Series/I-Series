package org.lmelaia.iseries.library;

import com.google.gson.*;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.library.LibraryException.LibraryCreationException;
import org.lmelaia.iseries.library.LibraryException.LibraryFetchException;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Handles the file management for a Library.
 * <p>
 * <p>
 * This class provides the ability to:
 * <ul>
 * <li>Write entries to file under a unique path
 * within the library folder.</li>
 * <li>Sort entries on file by entry specific data
 * through the {@link EntrySorter} class</li>
 * <li>Retrieve entries from file.</li>
 * <li>Delete and unindex entries.</li>
 * </ul>
 * <p>
 * while handling the sorting, updating,
 * indexing and io of entry files.
 */
class LibraryFileManager {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The name of the folder used to store entries that
     * could not be sorted.
     */
    private static final String UNSORTED_PATH_NAME = "/Unsorted";

    /**
     * The name of the file which holds the index of all tracked
     * entries.
     */
    private static final String INDEX_FILE_NAME = "/index.json";

    /**
     * The name of the file which holds the json data
     * of an entry.
     */
    private static final String ENTRY_FILE_NAME = "/entry.json";

    /**
     * Gson instance used to convert json objects
     * to a json string and back.
     */
    private static final Gson GSON;

    /**
     * Maps each tracked entry to it's UUID.
     */
    private final Map<String, File> index = new HashMap<>();

    /**
     * The path of this library.
     */
    private final File path;

    /**
     * This libraries index file.
     */
    private final File indexFile;

    /**
     * The entry sorted used to sort entries
     * within this library.
     */
    private final EntrySorter entrySorter;

    /**
     * True if the library was non-existent
     * before initialization, false
     * otherwise.
     */
    private final boolean isNew;

    /**
     * The library object using this instance.
     */
    private Library library = null;

    /*
     * Constructs the Gson object.
     */
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    //************
    //Constructors
    //************

    /**
     * Constructs a new library file manager.
     *
     * @param path   the path to the library on disk.
     * @param sorter the entry sorter used to sort
     *               entries on disk.
     * @throws LibraryCreationException if the library
     *                                  is non-existent and could not be created.
     * @throws LibraryFetchException    if the library
     *                                  could not be read from file or written to memory.
     */
    LibraryFileManager(File path, EntrySorter sorter, Library lib, ProgressTracker progressTracker)
            throws LibraryCreationException, LibraryFetchException {
        this.path = path;
        indexFile = new File(path.getAbsolutePath() + "/index.json");
        this.entrySorter = sorter;
        isNew = !indexFile.exists();

        path.mkdirs();
        try {
            indexFile.createNewFile();
        } catch (IOException e) {
            LOG.error("Failed to create library: " + path.getAbsolutePath(), e);
            throw new LibraryCreationException("Could not create library", e);
        }

        try {
            this.writeToLibrary(lib, progressTracker);
        } catch (IOException | JsonSyntaxException e) {
            LOG.error("Failed to load library " + path.getAbsolutePath(), e);
            throw new LibraryFetchException("Could not load library ", e);
        }
    }

    //*************************************
    //Protected and package private methods
    //*************************************

    /**
     * @return {@code true} if the library
     * was created upon initialization,
     * {@code false} otherwise.
     */
    protected boolean isNewInstance() {
        return isNew;
    }

    /**
     * Writes an entry to file and the entry index.
     * If the entry already exists on file and on the
     * index, it will be updated to reflect the changes
     * on file.
     *
     * @param entry the entry to add/update.
     * @param pt    progress tracker to the track the progress of
     *              the add/update operation.
     * @throws IOException if the entry files cannot be
     *                     created/updated or the index cannot be
     *                     updated.
     */
    void add(LibraryEntryBase entry, ProgressTracker pt) throws IOException {
        File oldPath = index.get(entry.getUUID());
        File newPath = getPathFromEntry(entry);

        if (oldPath == null) {
            LOG.debug("Insertion of entry: " + newPath);
            oldPath = newPath;
        } else {
            LOG.debug("Update of entry: " + newPath);
        }

        pt.setMax(2);

        File datafile = new File(oldPath.getAbsolutePath() + ENTRY_FILE_NAME);
        entry.setPath(newPath);

        write(entry.getMetadata(), datafile);
        index.put(entry.getUUID(), oldPath);

        if (!oldPath.getAbsolutePath().equals(newPath.getAbsolutePath())) {
            move(oldPath, newPath, pt);
            index.put(entry.getUUID(), newPath);
        } else {
            pt.setMax(2);
        }

        pt.setPosition(2);

        writeIndex();
        pt.complete();
    }

    /**
     * Retrieves an entry from file by it's UUID.
     *
     * @param UUID the universal unique ID of the entry.
     * @return the entry retrieved from file or {@code null}
     * if the entry does not exist.
     * @throws IOException if the entry files
     *                     cannot be read from file.
     */
    LibraryEntryBase get(String UUID) throws IOException {
        return get(index.get(UUID));
    }

    /**
     * Removes the entry from the index. This
     * does NOT remove ANY files.
     *
     * @param entry the entry to unindex.
     * @return the path the entry files are located in.
     * @throws IOException if the index cannot be modified.
     */
    File unindex(LibraryEntryBase entry) throws IOException {
        LOG.debug("Removal of entry: " + index.get(entry.getUUID()));
        File f = index.remove(entry.getUUID());
        writeIndex();
        return f;
    }

    /**
     * Removes the entry from the index and removes
     * all files related to the entry.
     *
     * @param entry   the entry to delete.
     * @param tracker progress tracker to track the progress
     *                of the delete operation.
     * @return the path the entry files were located in.
     * @throws IOException if the files cannot be deleted
     *                     or the index cannot be modified.
     */
    File delete(LibraryEntryBase entry, ProgressTracker tracker) throws IOException {
        LOG.debug("Deletion of entry: " + index.get(entry.getUUID()));
        File f = deleteFolder(index.remove(entry.getUUID()), tracker);
        writeIndex();
        return f;
    }

    String getPath() {
        return path.getPath();
    }

    /**
     * @return the size of the library on file in bytes.
     */
    long getLibrarySize() {
        return getPathSize(path.toPath());
    }

    //***************
    //Private methods
    //***************

    /**
     * Reads all entries from file and writes
     * them to the provided library object.
     *
     * @param library the library to populate.
     * @throws IOException if the index cannot be
     *                     read or modified.
     */
    private void writeToLibrary(Library library, ProgressTracker progressTracker) throws IOException {
        this.library = library;
        writeToLibrary(progressTracker);
    }

    /**
     * Writes the map ({@link #index}) to file.
     *
     * @throws IOException if the index file cannot be
     *                     written to.
     */
    private void writeIndex() throws IOException {
        JsonObject indexObj = new JsonObject();

        for (Map.Entry<String, File> entry : index.entrySet()) {
            indexObj.add(entry.getKey(),
                    new JsonPrimitive(
                            entry.getValue().getAbsolutePath().replace(path.getAbsolutePath(), "")
                                    .replace("\\", "/")
                    )
            );
        }

        write(indexObj, indexFile);
    }

    /**
     * Reads the index from file and
     * stores the data within the map
     * ({@link #index})
     *
     * @throws IOException if the index file
     *                     cannot be read.
     */
    private void readIndex() throws IOException {
        index.clear();
        JsonObject indexObj = read(indexFile);

        if (indexObj != null)
            for (Map.Entry<String, JsonElement> entry : indexObj.entrySet()) {
                index.put(entry.getKey(), new File(
                        path.getAbsolutePath() + entry.getValue().getAsString()));
            }
    }

    /**
     * Uses data within the entry to create
     * a unique path for it within the library.
     *
     * @param entry the entry to create a path from.
     * @return the relative path for the entry.
     */
    private File getPathFromEntry(LibraryEntryBase entry) {
        String givenFolderName = entrySorter.getRelativeFilePath(entry, path);
        File givenFolder;

        File givenFile = new File(path.getAbsolutePath()
                + "/" + givenFolderName);

        if (givenFolderName == null || givenFile.exists())
            givenFolder = new File(path.getAbsolutePath()
                    + "/" + entry.getUUID());
        else
            givenFolder = givenFile;

        givenFolder.mkdirs();
        return givenFolder;
    }

    /**
     * Reads each entry on the index from file
     * and stores them on the library object
     * given to this file manager instance.
     *
     * @throws IOException if the index or an entry
     *                     cannot be read from file.
     */
    private void writeToLibrary(ProgressTracker progressTracker) throws IOException {
        readIndex();

        ArrayList<String> missingKeys = new ArrayList<>();
        ArrayList<String> corruptedKeys = new ArrayList<>();

        progressTracker.setMax(index.values().size());
        progressTracker.setPosition(1);

        int entriesRead = 0;

        for (File entryFile : index.values()) {
            try {
                LOG.debug(
                        "Reading entry " + ++entriesRead + " of " + index.size() + ": " + entryFile.getAbsolutePath()
                );

                LibraryEntryBase entry = get(entryFile);

                //Corrupted Entry
                if (entry == null) {
                    LOG.warn("Corrupted entry: " + entryFile.getAbsolutePath());
                    library.addCorruptedEntry(entryFile.getAbsolutePath());
                    continue;
                }

                library.getMapping().put(entry.getUUID(), entry);
                entry.setOwner(library);
                entry.setPath(index.get(entry.getUUID()));
            } catch (FileNotFoundException e) {
                LOG.warn("Missing entry: " + entryFile.getAbsolutePath(), e);

                for (Map.Entry<String, File> entry : index.entrySet()) {
                    if (entry.getValue().getAbsolutePath().equals(entryFile.getAbsolutePath())) {
                        missingKeys.add(entry.getKey());
                        library.addMissingEntry(entry.getValue().getAbsolutePath());
                        break;
                    }
                }
            } catch (JsonSyntaxException jse) {
                LOG.warn("Corrupted entry: " + entryFile.getAbsolutePath(), jse);

                for (Map.Entry<String, File> entry : index.entrySet()) {
                    if (entry.getValue().getAbsolutePath().equals(entryFile.getAbsolutePath())) {
                        corruptedKeys.add(entry.getKey());
                        library.addCorruptedEntry(entry.getValue().getAbsolutePath());
                        break;
                    }
                }
            }
            progressTracker.increment();
        }

        for (String key : missingKeys) {
            index.remove(key);
        }

        for (String key : corruptedKeys) {
            index.remove(key);
        }

        writeIndex();
        progressTracker.complete();
    }

    /**
     * Retrieves an entry from an entry metadata file.
     *
     * @param entryFolder the folder the entry metadata
     *                    file is located in.
     * @return the constructed entry from metadata file.
     * @throws IOException if the metadata file cannot
     *                     be read.
     */
    private LibraryEntryBase get(File entryFolder) throws IOException {
        JsonObject data = read(new File(entryFolder.getAbsolutePath() + ENTRY_FILE_NAME));

        //Has no type. Invalid entry.
        if (!data.has("type"))
            return null;

        String type = data.get("type").getAsString();

        if (type.equals(LibraryEntry.TYPE))
            return new LibraryEntry(data);
        else if (type.equals(NamedLibraryEntry.TYPE))
            return new NamedLibraryEntry(data);
        else return null;
    }

    /**
     * Writes a json object to file.
     *
     * @param data     the json object to write.
     * @param dataFile the file to write the json to.
     * @throws IOException if the file cannot be written
     *                     to.
     */
    private void write(JsonObject data, File dataFile) throws IOException {
        String dataStr = GSON.toJson(data);

        FileWriter writer = new FileWriter(dataFile);
        writer.write(dataStr);
        writer.flush();
        writer.close();
    }

    /**
     * Constructs a json object from a json
     * String within a file.
     *
     * @param dataFile the file that contains the
     *                 json String.
     * @return the constructed json object.
     * @throws IOException if the file cannot be read.
     */
    private JsonObject read(File dataFile) throws IOException {
        StringBuilder dataStr = new StringBuilder();

        FileReader reader = new FileReader(dataFile);

        int letter;
        while ((letter = reader.read()) != -1) {
            dataStr.append((char) letter);
        }

        reader.close();

        return GSON.fromJson(dataStr.toString(), JsonObject.class);
    }

    /**
     * Deletes a folder from disk.
     *
     * @param folder  the folder to delete.
     * @param tracker progress tracker to track the progress
     *                of the delete operation.
     * @return the folder that was deleted.
     * @throws IOException if a file or folder cannot be deleted.
     */
    private static File deleteFolder(File folder, ProgressTracker tracker) throws IOException {
        tracker.setMax(getTotalSubFiles(folder.toPath()));
        tracker.setPosition(0);
        java.nio.file.Files.walkFileTree(folder.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                java.nio.file.Files.delete(file);
                tracker.increment();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                java.nio.file.Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

        return folder;
    }

    /**
     * Moves a folder on disk.
     *
     * @param sourceFolder the folder to move.
     * @param destFolder   the destination folder.
     * @param pt           progress tracker to track the progress
     *                     of the move operation.
     * @throws IOException if a file cannot be copied over.
     */
    private static void move(File sourceFolder, File destFolder, ProgressTracker pt) throws IOException {
        pt.setMax(getTotalSubFiles(sourceFolder.toPath()));
        pt.setPosition(0);
        Path targetPath = destFolder.toPath();
        Path sourcePath = sourceFolder.toPath();

        java.nio.file.Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir,
                                                     final BasicFileAttributes attrs) throws IOException {
                java.nio.file.Files.createDirectories(targetPath.resolve(sourcePath
                        .relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file,
                                             final BasicFileAttributes attrs) throws IOException {
                java.nio.file.Files.copy(file,
                        targetPath.resolve(sourcePath.relativize(file)));
                pt.increment();
                return FileVisitResult.CONTINUE;
            }
        });

        deleteFolder(sourceFolder, pt);
    }

    /**
     * Attempts to calculate the size of a file or directory.
     * <p>
     * <p>
     * Since the operation is non-atomic, the returned value may be inaccurate.
     * However, this method is quick.
     *
     * @param path the folder path.
     * @return the size of the folder or 0
     * if the folder does not exist,
     * is empty or could not be transversed.
     */
    private static long getPathSize(Path path) {
        final AtomicLong size = new AtomicLong(0);

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    LOG.error("Skipped file: " + file, exc);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    if (exc != null)
                        LOG.error("Could not transverse directory: " + dir, exc);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOG.error("Failed to calculate file/folder size. Path: " + path.toFile().getAbsolutePath(), e);
        }

        return size.get();
    }

    /**
     * Calculates the total amount of
     * files (not folders) that are nested
     * within a parent directory.
     *
     * @param path the folder path.
     * @return the total amount of nested files.
     */
    private static int getTotalSubFiles(Path path) {
        final AtomicInteger total = new AtomicInteger(0);

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    total.addAndGet(1);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    LOG.error("Skipped file: " + file, exc);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    if (exc != null)
                        LOG.error("Could not transverse directory: " + dir, exc);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOG.error("Failed to calculate file/folder size. Path: " + path.toFile().getAbsolutePath(), e);
        }

        return total.get();
    }
}
