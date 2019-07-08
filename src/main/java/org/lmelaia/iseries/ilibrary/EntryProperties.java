package org.lmelaia.iseries.ilibrary;

/**
 * List of json keys used to store information
 * in {@link org.lmelaia.iseries.library.LibraryEntry}s.
 */
enum EntryProperties {

    /**
     * The display name of the entry.
     */
    NAME;

    /**
     * @return the property name in lower case
     * (e.g. NAME -> name, A_NAME -> a_name).
     */
    protected String getKey() {
        return this.name().toLowerCase();
    }
}
