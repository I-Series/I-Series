package org.lmelaia.iseries.ilibrary;

/**
 * The complete list of key that can be used
 * to information within {@link IEntry}s.
 */
enum EntryProperties {

    /**
     * The display name of the entry.
     */
    NAME,

    /**
     * The given rating of the entry.
     */
    RATING,

    /**
     * Whether or not the entry is loved.
     */
    LOVED,

    /**
     * The media type of the entry.
     */
    TYPE,

    /**
     * The synopsis (description) of the entry.
     */
    SYNOPSIS,

    /**
     * The comment(s) added to the entry.
     */
    COMMENTS;

    /**
     * @return the property name in lower case
     * (e.g. NAME -> name, A_NAME -> a_name).
     */
    protected String getKey() {
        return this.name().toLowerCase();
    }
}
