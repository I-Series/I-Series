package org.lmelaia.iseries.library;

/**
 * Provides a way to sort library entries
 * on the file system.
 */
public interface EntrySorter {

    /**
     * Called when determining what path to
     * use when storing a library entry on file.
     * <p>
     * <p>
     * The value returned should provide a
     * relative path for the given entry. eg.
     * <code>
     * "folder/entry name" or "entry name"
     * </code>. {@code Null} if the entry
     * cannot be sorted. <b>The return value should NOT
     * begin with a slash</b>
     * <p>
     * <p>
     * The entry will be stored within the
     * library under the given path.
     *
     * @param entry the entry to be sorted.
     * @return the relative path
     * the entry will be sorted in.
     */
    String getRelativeFilePath(LibraryEntry entry);
}
