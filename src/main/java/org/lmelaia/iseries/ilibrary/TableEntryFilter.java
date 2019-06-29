package org.lmelaia.iseries.ilibrary;

/**
 * Allows filtering ITableEntries within
 * a linked TableView.
 */
public interface TableEntryFilter {

    /**
     * Called by the ILibrary to check if the given
     * ITableEntry should be kept in the table view.
     *
     * @param entry the ITableEntry to check.
     * @return {@code true} if the given ITableEntry can
     * be kept in the table, {@code false} if it should be
     * removed. Returning {@code true} doesn't guarantee
     * the entry will stay, however, returning {@code false}
     * does guarantee the entry will be removed.
     */
    boolean accept(ITableEntry entry);

}
