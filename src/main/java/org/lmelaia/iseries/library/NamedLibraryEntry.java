package org.lmelaia.iseries.library;

import com.google.gson.JsonObject;

/**
 * A special type of {@link LibraryEntry} that allows
 * a custom UUID (i.e. a name) to be used instead of
 * a randomly chosen one. This is used to create special
 * entries where the name is always known, such as entries
 * hard coded into I-Series (e.g. playlists).
 * <p>
 * This type of entry can be dangerous. It relies on the
 * user making sure the uuid is actually unique whereas
 * {@link LibraryEntry} (the preferred implementation)
 * ensures uniqueness with {@link java.util.UUID}'s.
 * <p>
 * This, as well the chance of NamedLibraryEntries having vastly
 * different data structures from normal LibraryEntries, they're
 * considered different objects (a NamedLibraryEntry cannot be cast to
 * a LibraryEntry) and should be treated as such.
 */
public class NamedLibraryEntry extends LibraryEntryBase {

    /**
     * String to used identify if the json data on file
     * matches this object.
     */
    //This should never be changed.
    protected static final String TYPE = "NAMED_ENTRY";

    /**
     * Constructs a new Library Entry with a
     * given String as the entries uuid.
     *
     * @param uuid the String to use as the entries uuid.
     */
    public NamedLibraryEntry(String uuid) {
        super(uuid, TYPE);
    }

    /**
     * Constructs a new library entry
     * with the given json data.
     *
     * @param jsonData the data the
     *                 entry should contain
     */
    protected NamedLibraryEntry(JsonObject jsonData) {
        super(jsonData);
    }
}
