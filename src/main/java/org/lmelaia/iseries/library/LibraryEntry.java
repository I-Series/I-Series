package org.lmelaia.iseries.library;

import com.google.gson.JsonObject;

import java.util.UUID;

/**
 * A basic library entry.
 *
 * Each new LibraryEntry is given a new random {@link UUID}
 * as its uuid. This makes the preferable entry type
 * to use.
 */
public class LibraryEntry extends LibraryEntryBase {

    /**
     * String to used identify if the json data on file
     * matches this object.
     */
    //This should never be changed.
    protected static final String TYPE = "BASIC_ENTRY";

    /**
     * Constructs a new library entry
     * with a new {@link UUID}.
     */
    public LibraryEntry() {
        super(getNewUUID(), TYPE);
    }

    /**
     * Constructs a new library entry
     * with the given json data.
     *
     * @param jsonData the data the
     *                 entry should contain
     */
    protected LibraryEntry(JsonObject jsonData) {
        super(jsonData);
    }

    /**
     * @return A newly generated random {@link UUID}
     * as an uppercase string.
     */
    private static String getNewUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }


}
