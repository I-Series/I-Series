package org.lmelaia.iseries.ilibrary;

/**
 * A wrapper for {@link IEntry}s that is
 * put into a {@link javafx.scene.control.TableView}s
 * {@link javafx.collections.ObservableList}.
 */
public class ITableEntry {

    /**
     * The actual entry this TableEntry
     * represents & the object this wrapper
     * gets its values from.
     */
    private final IEntry entry;

    /**
     * Constructor.
     *
     * @param entry The actual entry this TableEntry
     *              represents & the object this wrapper
     *              gets its values from.
     */
    ITableEntry(IEntry entry) {
        this.entry = entry;
    }

    // *******
    // GETTERS
    // *******

    /**
     * @return The display name of the entry.
     */
    public String getName() {
        return this.entry.getName();
    }

    /**
     * @return the UUID of the entry.
     */
    public String getUUID() {
        return this.entry.getUUID();
    }

    // ***********
    // END GETTERS
    // ***********

    /**
     * Sets the display name of the entry.
     *
     * @param name the display name.
     */
    void setName(String name) {
        this.entry.setName(name);
    }

    /**
     * @return the actual entry this wrapper
     * class represents.
     */
    public IEntry getEntry() {
        return this.entry;
    }
}
