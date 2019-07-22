package org.lmelaia.iseries.library;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for any library entry.
 * <p>
 * Represents an entry stored on file within a library.
 * <p>
 * <p>
 * Each entry is given a universally unique identifier (uuid/id) as a string,
 * by which it is identified on file and within the library. This id can be anything and
 * it's uniqueness is not enforced.
 * <p>
 * <p>
 * Each entry is stored on file under a unique folder
 * within a library. This folder is named using the
 * data contained within the entry. <b>File names
 * are updated when the data within the entry changes
 * and the entry is committed. The sub-files
 * contained within this entry will be updated as
 * well.</b> The entry data is stored on file in
 * json format in a file named {@code metadata.json}.
 * <p>
 * <p>
 * Each entry can store files on disk under
 * the folder dedicated to it, referenced by
 * named keys.
 * <p>
 * <p>
 * Each entry is capable of storing data
 * in the form of json.
 */
public class LibraryEntryBase {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The json object holding the data for this entry.
     */
    private JsonObject jsonData;

    /**
     * The library this entry was added to. Null if this entry
     * has yet to be added.
     */
    private Library owner;

    /**
     * The path to this entry <b>on file</b> or
     * {@code null} if this entry does not exist
     * on file.
     */
    private File path;

    /**
     * Constructs a new library entry
     * with the given json data.
     *
     * @param jsonData the data the
     *                 entry should contain
     */
    protected LibraryEntryBase(JsonObject jsonData) {
        this.jsonData = jsonData;
    }

    /**
     * Constructs a new library entry
     * with the the given uuid and type.
     *
     * @param uuid the unique id of the entry.
     * @param type the specific type of entry (the extending class).
     */
    protected LibraryEntryBase(String uuid, String type) {
        this(getBackingJsonDataObject(uuid, type));
    }

    /**
     * @return the universally unique ID of this entry.
     */
    public String getUUID() {
        return jsonData.get("UUID").getAsString();
    }

    /**
     * @return a json object which can be written
     * to and read from. This object is saved to file.
     */
    public JsonObject getInformation() {
        return jsonData.get("Information").getAsJsonObject();
    }

    /**
     * Copies a file over to the folder dedicated
     * to this entry. This file is indexed
     * and can be retrieved by a UID (Unique ID).
     * <p>
     * <p>
     * If a file is already indexed under the given
     * UID, the UID will now map to the new file
     * under the new name and the previous
     * file will be deleted.
     *
     * @param UID      The unique ID for this file.
     * @param source   the file to copy, or {@code null} to create a new empty file.
     * @param fileName The path and file name
     *                 (eg. "folder/file.txt" or "file.txt)
     * @throws LibraryException.FileModificationException  if the file cannot be copied or modified.
     * @throws LibraryException.EntryModificationException if the entry cannot be changed to index
     *                                                     the file.
     * @throws FileNotFoundException                       if the source file does
     *                                                     not exist.
     */
    @SuppressWarnings("WeakerAccess")
    public void addFile(String UID, File source, String fileName) throws FileNotFoundException,
            LibraryException.FileModificationException, LibraryException.EntryModificationException {
        Objects.requireNonNull(UID);
        Objects.requireNonNull(fileName);
        fileName = fileName.replace("\\", "/");

        if (source == null) {
            getFiles().add(UID, new JsonPrimitive(fileName));
            this.update();
            return;
        }

        if (!source.exists())
            throw new FileNotFoundException(
                    "Source file: " + source.getAbsolutePath() + " does not exist");
        if (source.isDirectory())
            throw new IllegalArgumentException("Source file is directory");

        File dest = makeAbsolute(fileName);
        File original = getFiles().get(UID) == null ? null : makeAbsolute(getFiles().get(UID).getAsString());

        if (original != null)
            removeFile(original);

        copyFile(source, dest);
        getFiles().add(UID, new JsonPrimitive(fileName));

        this.update();
    }

    /**
     * Renames an indexed file.
     *
     * @param UID      The unique ID of the file.
     * @param filename The new path and file name
     *                 (eg. "folder/file.txt" or "file.txt)
     * @throws LibraryException.EntryModificationException If the file cannot be renamed.
     * @throws LibraryException.FileUIDNotFoundException   if no file exists matching the given UID.
     * @throws FileNotFoundException                       if the file matching the UID
     *                                                     could not be found on disk.
     */
    public void renameFile(String UID, String filename) throws LibraryException.EntryModificationException,
            LibraryException.FileUIDNotFoundException, FileNotFoundException {
        File f = getFile(UID);

        if (f == null)
            throw new LibraryException.FileUIDNotFoundException("The file UID: [" + UID + "] does not exist.", null);

        if (!f.exists())
            throw new FileNotFoundException(
                    "The indexed file: " + f.getAbsolutePath() + " cannot be found.");

        f.renameTo(makeAbsolute(filename));
        f.delete();

        getFiles().add(UID, new JsonPrimitive(filename));
        this.update();
    }

    /**
     * Replaces an indexed file with a new file.
     *
     * @param UID  The unique ID of the file.
     * @param file The file to replace the old one with.
     * @throws FileNotFoundException                                                 if the source file cannot be found.
     * @throws LibraryException.FileModificationException                            if the file cannot be copied over.
     * @throws LibraryException.EntryModificationException                           if the index cannot be edited to reflect
     *                                                                               the changes.
     * @throws org.lmelaia.iseries.library.LibraryException.FileUIDNotFoundException if no file is mapped to the UID.
     */
    public void updateFile(String UID, File file) throws FileNotFoundException,
            LibraryException.FileUIDNotFoundException, LibraryException.FileModificationException,
            LibraryException.EntryModificationException {
        Objects.requireNonNull(UID);
        Objects.requireNonNull(file);

        if (getFiles().get(UID) == null)
            throw new LibraryException.FileUIDNotFoundException("The file UID: [" + UID + "] does not exist.", null);

        String filename = getFiles().get(UID).getAsString();
        addFile(UID, file, filename);
    }

    /**
     * Gets and returns an indexed file.
     *
     * @param UID The unique ID of the the file.
     * @return The indexed file.
     */
    @SuppressWarnings("WeakerAccess")
    public File getFile(String UID) {
        if (getFiles().get(UID) == null)
            return null;

        return makeAbsolute(getFiles().get(UID).getAsString());
    }

    /**
     * Deletes and unindexes the file
     * under the given UID
     *
     * @param UID The unique ID the of the file.
     */
    public void removeFile(String UID) {
        File ftd = makeAbsolute(getFiles().get(UID).getAsString());
        removeFile(ftd);
        getFiles().remove(UID);
    }

    /**
     * Removes a file and UID from the index
     * but does NOT delete the file.
     *
     * @param ID The unique ID of the file.
     */
    public void unindexFile(String ID) {
        getFiles().remove(ID);
    }

    /**
     * Adds this entry to a library.
     *
     * @param library The library this entry will be added to.
     * @param tracker Tracks the progress of the add operation.
     * @throws LibraryException.EntryModificationException if the entry cannot be written to file.
     */
    public void addTo(Library library, ProgressTracker tracker) throws LibraryException.EntryModificationException {
        Objects.requireNonNull(library).add(this, tracker);
    }

    /**
     * Updates this entry on file.
     * <b>
     * This entry must exist within a library
     * before this method can be called.
     * </b>
     *
     * @param tracker Tracks the progress of the update
     *                operation.
     * @throws IllegalStateException                       If this entry has
     *                                                     not been added to a library.
     * @throws LibraryException.EntryModificationException if the entry cannot be edited on file.
     */
    @SuppressWarnings("WeakerAccess")
    public void update(ProgressTracker tracker) throws LibraryException.EntryModificationException {
        if (this.owner == null)
            throw new IllegalStateException("Entry: " + getUUID() + " has no owner");

        this.owner.add(this, tracker);
    }

    /**
     * Sames as {@link #update(ProgressTracker)}
     *
     * @throws IllegalStateException                       If this entry has
     *                                                     not been added to a library.
     * @throws LibraryException.EntryModificationException if the entry cannot be edited on file.
     */
    @SuppressWarnings("WeakerAccess")
    public void update() throws LibraryException.EntryModificationException {
        update(ProgressTracker.getUnboundTracker());
    }

    /**
     * Deletes this entry from file.
     *
     * @param tracker Tracks the progress of the
     *                delete operation.
     * @throws IllegalStateException                       if the entry has
     *                                                     not been added to a library.
     * @throws LibraryException.EntryModificationException if the entry files cannot be deleted.
     */
    @SuppressWarnings("WeakerAccess")
    public void delete(ProgressTracker tracker) throws LibraryException.EntryModificationException {
        if (this.owner == null)
            throw new IllegalStateException("Entry: " + getUUID() + " has no owner");

        this.owner.delete(this, tracker);
    }

    /**
     * Same as {@link #delete(ProgressTracker)}
     *
     * @throws IllegalStateException                       if the entry has
     *                                                     not been added to a library.
     * @throws LibraryException.EntryModificationException if the entry files cannot be deleted.
     */
    public void delete() throws LibraryException.EntryModificationException {
        delete(ProgressTracker.getUnboundTracker());
    }

    /**
     * Removes this entry from the libraries index.
     *
     * @throws IllegalStateException                       if the entry has
     *                                                     not been added to a library.
     * @throws LibraryException.EntryModificationException if the library index cannot be modified.
     */
    public void unindex() throws LibraryException.EntryModificationException {
        if (this.owner == null)
            throw new IllegalStateException("Entry: " + getUUID() + " has no owner");

        this.owner.unindex(this);
    }

    /**
     * @return The path for this entry on file.
     */
    public File getPath() {
        return this.path;
    }

    /**
     * @return The json object holding the data
     * for this entry.
     */
    @SuppressWarnings("WeakerAccess")
    protected JsonObject getMetadata() {
        return jsonData;
    }

    /**
     * Sets the json object that holds the data for this entry.
     *
     * @param jsonData The json object.
     */
    protected void setMetadata(JsonObject jsonData) {
        this.jsonData = jsonData;
    }

    /**
     * @return The library that contains
     * this entry or {@code null} if this
     * entry has not been added to a library.
     */
    protected Library getOwner() {
        return this.owner;
    }

    /**
     * Sets the owner library reference.
     *
     * @param library The library.
     */
    void setOwner(Library library) {
        this.owner = library;
    }

    /**
     * Sets the path to this entry on file.
     *
     * @param path The path to this entry on file.
     */
    void setPath(File path) {
        this.path = path;
    }

    /**
     * Internal file remove method.
     * <p>
     * Deletes a file on disk and the parent
     * directories if they are empty.
     *
     * @param f the file to remove.
     */
    @SuppressWarnings("ConstantConditions")
    private void removeFile(File f) {
        File FTDParent = f.getParentFile();
        f.delete();
        do {
            if (FTDParent.listFiles().length == 0)
                FTDParent.delete();
        } while ((FTDParent = FTDParent.getParentFile()) != null);
    }

    /**
     * @return the json object that holds the index
     * of all the files in the entry.
     */
    private JsonObject getFiles() {
        return jsonData.get("Files").getAsJsonObject();
    }

    /**
     * Takes a relative path and makes it absolute
     * by appending the relative path to the end of
     * the path of the entry on file.
     *
     * @param fileName the relative path/relative file name.
     * @return the absolute path.
     */
    private File makeAbsolute(String fileName) {
        if (getPath() == null)
            throw new IllegalStateException("Library Entry: " + getUUID() + " has not yet been indexed");

        return new File(getPath().getAbsolutePath() + "/" + fileName);
    }

    /**
     * Copies a file.
     *
     * @param source the file to copy.
     * @param dest   the file location to copy to.
     * @throws LibraryException.FileModificationException if the file cannot be copied.
     */
    private static void copyFile(File source, File dest) throws LibraryException.FileModificationException {
        dest.getParentFile().mkdirs();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new LibraryException.FileModificationException("Could not copy file", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                LOG.error("Failed to close streams after copying file", e);
            }
        }
    }

    /**
     * @return A newly generated {@link UUID}
     * as an uppercase string.
     */
    private static String getNewUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * Returns a new json object that can be used as a {@link #jsonData}
     * object.
     *
     * @param uuid the unique id of the entry.
     * @param type the type of entry.
     * @return the requested json object.
     */
    private static JsonObject getBackingJsonDataObject(String uuid, String type) {
        JsonObject ret = new JsonObject();
        ret.add("UUID", new JsonPrimitive(uuid));
        ret.add("Information", new JsonObject());
        ret.add("Files", new JsonObject());
        ret.add("type", new JsonPrimitive(type));
        return ret;
    }
}
