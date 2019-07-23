package org.lmelaia.iseries.library;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.library.LibraryException.EntryFetchException;
import org.lmelaia.iseries.library.LibraryException.EntryModificationException;
import org.lmelaia.iseries.library.LibraryException.LibraryCreationException;
import org.lmelaia.iseries.library.LibraryException.LibraryFetchException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Provides a library capable of:
 * <p>
 * <ul>
 * <li>Storing json data on file within
 * a unique, named folder as a {@link LibraryEntry} object</li>
 * <li>CRUD and unindex functionality.</li>
 * <li>Updating data and files during CRUD operations.</li>
 * <li>Storing files specific to each entry.</li>
 * <li>Providing information about the library (eg. size).</li>
 * </ul>
 * <p>
 * This class stores a set of LibraryEntries on file in the
 * form of json. Each entry is capable of storing files
 * under folders and sub-folders under a key in the form
 * of a String.
 * <p>
 * <p>
 * The library will keep file names
 * up-to-date with the data contained within
 * the entry during add/update operations.
 */
public class Library {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Maps each library entry to it's UUID.
     */
    private final Map<String, LibraryEntryBase> mapping = new HashMap<>();

    /**
     * A list of entries that were not found on disk but are listed in the index.
     * <p>
     * <p>
     * This list is cleared and the entries removed from the index after reboot.
     */
    private final List<String> missingEntries = new ArrayList<>();

    /**
     * A list of entries that that could not be loaded due to corruption.
     * <p>
     * <p>
     * This list is cleared and the entries removed from the index after reboot.
     */
    private final List<String> corruptedEntries = new ArrayList<>();

    /**
     * Handles the library files on disk.
     */
    private LibraryFileManager fileManager;

    /**
     * Constructs a new library.
     */
    public Library() {
    }

    /**
     * Loads the library from file. This will create a new
     * library file structure if one does not exist.
     *
     * @param libraryPath the path of the library on disk.
     * @param sorter      the sorter responsible for providing
     *                    file names for entries.
     * @throws LibraryFetchException    if the library could not be
     *                                  read from file or constructed in memory.
     * @throws LibraryCreationException if the library is non-existent
     *                                  and could not be created.
     */
    public void load(File libraryPath, EntrySorter sorter, ProgressTracker progressTracker)
            throws LibraryCreationException, LibraryFetchException {
        Objects.requireNonNull(libraryPath);
        Objects.requireNonNull(sorter);
        LOG.info("Loading library...");
        this.fileManager = new LibraryFileManager(libraryPath, sorter, this, progressTracker);
    }

    /**
     * Adds or updates an entry in the index and stores
     * it on file.
     *
     * @param entry   the entry to add/update.
     * @param tracker tracks the progress of the add/update
     *                operation
     * @throws EntryModificationException if the entry could not
     *                                    be stores on file.
     */
    @SuppressWarnings("WeakerAccess")
    public void add(LibraryEntryBase entry, ProgressTracker tracker) throws EntryModificationException {
        checkFileManager();
        Objects.requireNonNull(entry);
        try {
            fileManager.add(entry, tracker);
            mapping.put(entry.getUUID(), entry);
            entry.setOwner(this);
        } catch (IOException e) {
            throw new EntryModificationException("Could not add entry: " + entry.getUUID(), e);
        }
    }

    /**
     * Adds or updates an entry in the index and stores
     * it on file.
     *
     * @param entry the entry to add/update.
     * @throws EntryModificationException if the entry could not
     *                                    be stores on file.
     */
    public void add(LibraryEntryBase entry) throws EntryModificationException {
        add(entry, ProgressTracker.getUnboundTracker());
    }

    /**
     * Returns an entry from the index
     * matching the given UUID.
     *
     * @param UUID the unique ID.
     * @return the stored entry or {@code null}
     * if no entry with a matching UUID
     * exists.
     */
    public LibraryEntryBase get(String UUID) {
        checkFileManager();
        return mapping.get(UUID);
    }

    /**
     * Updates an entry in the index from
     * the entries metadata on file.
     *
     * @param UUID the UUID of the entry.
     * @throws EntryFetchException if the entry metadata
     *                             file could not be read.
     */
    public void reload(String UUID) throws EntryFetchException {
        checkFileManager();
        try {
            this.mapping.put(UUID, fileManager.get(UUID));
        } catch (IOException e) {
            throw new EntryFetchException("Could not read entry: " + UUID, e);
        }
    }

    /**
     * @return an array of all the entries
     * within the the index.
     */
    public LibraryEntryBase[] getAll() {
        checkFileManager();
        return mapping.values().toArray(new LibraryEntryBase[0]);
    }

    /**
     * Removes an entry from the index but
     * does NOT delete any files created
     * by the entry.
     *
     * @param entry the entry to unindex.
     * @throws EntryModificationException if the entry
     *                                    could not be removed from the index.
     */
    @SuppressWarnings("WeakerAccess")
    public void unindex(LibraryEntryBase entry) throws EntryModificationException {
        checkFileManager();
        try {
            fileManager.unindex(entry);
            mapping.remove(entry.getUUID());
            entry.setOwner(null);
        } catch (IOException e) {
            throw new EntryModificationException("Failed to unindex entry: " + entry.getUUID(), e);
        }
    }

    /**
     * Removes an entry from the index and deletes
     * all files related the the entry from disk.
     *
     * @param entry   the entry to delete.
     * @param tracker tracks the progress of the
     *                delete operation.
     * @throws EntryModificationException if the entry
     *                                    could not be removed from the index
     *                                    or it's files could not be deleted.
     */
    @SuppressWarnings("WeakerAccess")
    public void delete(LibraryEntryBase entry, ProgressTracker tracker) throws EntryModificationException {
        checkFileManager();
        try {
            fileManager.delete(entry, tracker);
            mapping.remove(entry.getUUID());
            entry.setOwner(null);
            tracker.complete();
        } catch (IOException e) {
            throw new EntryModificationException("Failed to delete entry: " + entry.getUUID(), e);
        }
    }

    /**
     * Removes an entry from the index and deletes
     * all files related the the entry from disk.
     *
     * @param entry the entry to delete.
     * @throws EntryModificationException if the entry
     *                                    could not be removed from the index
     *                                    or it's files could not be deleted.
     */
    public void delete(LibraryEntryBase entry) throws EntryModificationException {
        checkFileManager();
        delete(entry, ProgressTracker.getUnboundTracker());
    }

    /**
     * @return {@code true} if entries on disk
     * were not found but are listed in the index.
     * <p>
     * <p>
     * These entries are removed from the index.
     */
    public boolean hasMissingEntries() {
        checkFileManager();
        return missingEntries.size() != 0;
    }

    /**
     * @return an array of entry file locations listed
     * in the index but could not be found.
     * <p>
     * <p>
     * These entries are removed from the index.
     */
    public String[] getMissingEntries() {
        checkFileManager();
        return missingEntries.toArray(new String[0]);
    }

    /**
     * @return {@code true} if entries on disk
     * were found to be corrupted.
     * <p>
     * <p>
     * These entries are removed from the index.
     */
    public boolean hasCorruptedEntries() {
        checkFileManager();
        return corruptedEntries.size() != 0;
    }

    /**
     * @return an array of entry file locations that
     * hold corrupted entries.
     * <p>
     * <p>
     * These entries are removed from the index.
     */
    public String[] getCorruptedEntries() {
        checkFileManager();
        return corruptedEntries.toArray(new String[0]);
    }

    /**
     * @return the number of entries contained
     * in the index.
     */
    public int getNumberOfEntries() {
        checkFileManager();
        return mapping.size();
    }

    public String getPath() {
        return fileManager.getPath();
    }

    /**
     * @return the size of the library on file in bytes.
     */
    public long getLibrarySize() {
        checkFileManager();
        return fileManager.getLibrarySize();
    }

    /**
     * @return the map used as an index.
     */
    Map<String, LibraryEntryBase> getMapping() {
        return mapping;
    }

    /**
     * Adds an entry file location to the
     * list of missing entries.
     *
     * @param path the path to the entry folder.
     */
    void addMissingEntry(String path) {
        missingEntries.add(path);
    }

    /**
     * Adds an entry file location to the
     * list of corrupted entries.
     *
     * @param path the path to the entry folder.
     */
    void addCorruptedEntry(String path) {
        corruptedEntries.add(path);
    }

    /**
     * Ensures the file manager is initialized.
     *
     * @throws IllegalStateException if the file manager is not initialized.
     */
    private void checkFileManager() {
        if (fileManager == null)
            throw new IllegalStateException("Library not yet loaded");
    }
}
