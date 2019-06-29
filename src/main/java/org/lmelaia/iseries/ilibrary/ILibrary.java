package org.lmelaia.iseries.ilibrary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.lmelaia.iseries.library.Library;
import org.lmelaia.iseries.library.LibraryEntry;
import org.lmelaia.iseries.library.LibraryException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A wrapper library for a {@link Library} that makes the original
 * implementation more suited to the I-Series application. This includes:
 * adding an {@link LibraryEntry} with named getters & setters that
 * relate to the application, rather than a {@link com.google.gson.JsonObject};
 * As well as providing a hook for {@link TableView}s to see & respond to
 * changes in the Library.
 */
public class ILibrary {

    /**
     * Map that keeps track of the created ITableEntries.
     */
    private final TableItemHandler tableHandler;

    /**
     * The Library this class warps.
     */
    private final Library backingLibrary;

    /**
     * The TableView linked to this library that
     * will display this libraries content and
     * be notified of changes.
     */
    private TableView<ITableEntry> linkedTable;

    /**
     * Creates a new ILibrary object wrapping a
     * Library object with ITableEntries already
     * initialized.
     *
     * @param backingLibrary the Library to wrap.
     */
    public ILibrary(Library backingLibrary) {
        this.tableHandler = new TableItemHandler();
        this.backingLibrary = backingLibrary;
        populateILibrary();
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Adds or updates an entry in the index and stores
     * it on file.
     *
     * @param entry the entry to add/update.
     * @throws LibraryException.EntryModificationException if the entry could not
     *                                                     be stores on file.
     */
    public void add(IEntry entry) throws LibraryException.EntryModificationException {
        entry.setOwner(this);
        backingLibrary.add(entry.getBackingEntry());
        addTableEntry(entry);
    }

    /**
     * Returns an entry from the index
     * matching the given UUID.
     *
     * @param uuid the unique ID.
     * @return the stored entry or {@code null}
     * if no entry with a matching UUID
     * exists.
     */
    public IEntry get(String uuid) {
        LibraryEntry entry = backingLibrary.get(uuid);

        if (entry == null)
            return null;

        IEntry iEntry = new IEntry(entry, tableHandler.get(uuid));
        iEntry.setOwner(this);
        return iEntry;
    }

    /**
     * Removes an entry from the index and deletes
     * all files related the the entry from disk.
     *
     * @param entry the entry to delete.
     * @throws LibraryException.EntryModificationException if the entry
     *                                                     could not be removed from the index
     *                                                     or it's files could not be deleted.
     */
    public IEntry delete(IEntry entry) throws LibraryException.EntryModificationException {
        if (entry != null) {
            backingLibrary.delete(entry.getBackingEntry());
            tableHandler.remove(entry.getUUID());
        }

        return entry;
    }

    /**
     * Removes an entry from the index but
     * does NOT delete any files created
     * by the entry.
     *
     * @param entry the entry to unindex.
     * @throws LibraryException.EntryModificationException if the entry
     *                                                     could not be removed from the index.
     */
    public IEntry unindex(IEntry entry) throws LibraryException.EntryModificationException {
        if (entry != null) {
            backingLibrary.unindex(entry.getBackingEntry());
            tableHandler.remove(entry.getUUID());
        }

        return entry;
    }

    /**
     * Sets the given TableViews {@link TableView#setItems(ObservableList)}
     * to the backing ObservableList linked to this library. Once linked
     * it will display the library contents & reflect any changes to it.
     *
     * @param table the table to link.
     */
    public void linkTable(TableView<ITableEntry> table) {
        this.linkedTable = table;
        table.setItems(tableHandler.display);
    }

    public void setSearchFilter(TableEntryFilter filter) {
        tableHandler.setSearchFilter(filter);
    }

    public void clearSearchFilter() {
        tableHandler.clearSearchFilter();
    }


    // *********
    // INTERNALS
    // *********

    /**
     * Creates ITableEntries for LibraryEntries
     * created from file.
     */
    private void populateILibrary() {
        for (LibraryEntry entry : backingLibrary.getAll())
            addTableEntry(new IEntry(entry));
    }

    /**
     * Adds the given IEntry's ITableEntry to the
     * backing ObservableList.
     *
     * @param entry the given IEntry.
     */
    private void addTableEntry(IEntry entry) {
        tableHandler.put(entry.getUUID(), entry.getTableEntry());
    }

    // **************
    // TABLE HANDLING
    // **************

    /**
     * Handles the items in the linked table
     * in relation to the entries stored
     * in the library.
     */
    private class TableItemHandler {

        /**
         * Map of table entries to their uuid (a table entry takes
         * the uuid of the entry it represents).
         */
        private final HashMap<UUID, ITableEntry> source = new HashMap<>();

        /**
         * List of table entries to display in the table.
         */
        private final ObservableList<ITableEntry> display = FXCollections.observableArrayList();

        private TableEntryFilter searchFilter;

        public void setSearchFilter(TableEntryFilter filter) {
            this.searchFilter = filter;
            filterDisplay();
            forceRefresh(null);
        }

        public void clearSearchFilter() {
            searchFilter = null;
            filterDisplay();
            forceRefresh(null);
        }

        private void filterDisplay() {
            display.clear();

            for (Map.Entry<UUID, ITableEntry> entry : source.entrySet()) {
                if (canAdd(entry.getValue()))
                    display.add(entry.getValue());
            }
        }

        private boolean canAdd(ITableEntry entry) {
            return (searchFilter == null) || searchFilter.accept(entry);
        }

        /**
         * Adds a table entry under the given uuid.
         *
         * @param uuid  the given uuid.
         * @param entry the given entry.
         */
        public void put(String uuid, ITableEntry entry) {
            UUID uuid1 = UUID.fromString(uuid);
            display.remove(source.remove(uuid1));
            source.put(uuid1, entry);
            if (canAdd(entry))
                display.add(entry);

            forceRefresh(entry);
        }

        /**
         * @param uuid the given uuid.
         * @return the table entry under the given uuid.
         */
        public ITableEntry get(String uuid) {
            return source.get(UUID.fromString(uuid));
        }

        /**
         * Removes the table entry under the given uuid
         * from the list.
         *
         * @param uuid the given uuid.
         */
        public void remove(String uuid) {
            display.remove(source.remove(UUID.fromString(uuid)));
            forceRefresh(null);
        }

        /**
         * Makes sure the the linked table refreshes
         * to see any changes.
         *
         * @param entry the entry in the table to select,
         *              or {@code null} to unselect any entry.
         */
        private void forceRefresh(ITableEntry entry) {
            if (linkedTable == null) return;

            if (entry == null)
                linkedTable.getSelectionModel().clearSelection();
            else linkedTable.getSelectionModel().select(entry);

            linkedTable.refresh();
            linkedTable.sort();
        }
    }
}
