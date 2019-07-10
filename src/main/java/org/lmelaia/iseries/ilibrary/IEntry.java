package org.lmelaia.iseries.ilibrary;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lmelaia.iseries.library.LibraryEntry;
import org.lmelaia.iseries.library.LibraryException;

import static org.lmelaia.iseries.ilibrary.EntryProperties.NAME;

/**
 * A {@link LibraryEntry} wrapper that both provides a way
 * to easily display the entries in a TableView and makes
 * getting/setting field easier.
 */
public class IEntry {

    /**
     * The backing LibraryEntry this class wraps.
     */
    private final LibraryEntry backingEntry;

    /**
     * The library this entry is added to
     * (if at all)
     */
    private ILibrary owner;

    /**
     * The ITableEntry object used to display this
     * entry in a TableView.
     */
    private ITableEntry tableEntry;

    /**
     * Creates a new (empty) IEntry.
     */
    public IEntry() {
        this.backingEntry = new LibraryEntry();
    }

    /**
     * Internal constructor used to wrap LibraryEntry
     * objects.
     *
     * @param entry the LibraryEntry object to wrap.
     */
    IEntry(LibraryEntry entry) {
        this.backingEntry = entry;
    }

    /**
     * Internal constructor used to wrap LibraryEntry
     * objects with an already constructed ITableEntry
     * object.
     *
     * @param entry      the LibraryEntry object to wrap.
     * @param tableEntry the associated ITableEntry object.
     */
    IEntry(LibraryEntry entry, ITableEntry tableEntry) {
        this(entry);
        this.tableEntry = tableEntry;
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Sets the display name of the entry.
     *
     * @param name the display name to set.
     */
    public void setName(String name) {
        add(NAME, new JsonPrimitive(name));
    }

    /**
     * @return the display of the entry.
     */
    public String getName() {
        return get(NAME).getAsString();
    }

    /**
     * @return the unique ID of this specific entry object.
     */
    public String getUUID() {
        return backingEntry.getUUID();
    }

    /**
     * Ensures changes to this object are seen by
     * the application and saved to file.
     *
     * @throws LibraryException.EntryModificationException if the entry cannot
     *                                                     be edited.
     * @throws IllegalStateException                       if the entry doesn't belong to any Library.
     */
    public void update() throws LibraryException.EntryModificationException {
        if (this.owner == null) {
            throw new IllegalStateException("IEntry: " + getUUID() + " has no owner.");
        }

        this.owner.add(this);
    }

    /**
     * @return The name and uuid of this entry.
     */
    public String toString() {
        return this.getName() + " (" + this.getUUID() + ")";
    }

    // *********
    // ILibrary
    // *********

    /**
     * @return The backing LibraryEntry object this class wraps.
     */
    protected LibraryEntry getBackingEntry() {
        return this.backingEntry;
    }

    /**
     * Creates or updates the ITableEntry representing
     * this object in a TableView.
     *
     * @return the created/updated TableEntry.
     */
    protected ITableEntry getTableEntry() {
        if (this.tableEntry == null) {
            this.tableEntry = new ITableEntry(this);
        } else {
            updateTableEntry();
        }

        return this.tableEntry;
    }

    /**
     * Sets the reference to the library "owning"
     * this object.
     *
     * @param owner the library who owns this entry.
     */
    protected void setOwner(ILibrary owner) {
        this.owner = owner;
    }

    // **********
    // Internals
    // **********

    /**
     * Updates the table entry that represents
     * this object with the current information
     * contained in this object.
     */
    private void updateTableEntry() {
        this.tableEntry.setName(this.getName());
    }

    /**
     * Adds a json primitive under the given property name
     * to the backing entry.
     *
     * @param property  the propery name.
     * @param primitive the value as a json primitive.
     */
    private void add(EntryProperties property, JsonPrimitive primitive) {
        this.backingEntry.getInformation().add(property.getKey(), primitive);
    }

    /**
     * @param property the given property.
     * @return the json primitive under the given property
     * name from the backing entry.
     */
    private JsonElement get(EntryProperties property) {
        return backingEntry.getInformation().get(property.getKey());
    }
}
