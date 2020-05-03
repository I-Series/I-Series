package org.lmelaia.iseries.ilibrary;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lmelaia.iseries.library.LibraryEntry;
import org.lmelaia.iseries.library.LibraryException;

import static org.lmelaia.iseries.ilibrary.EntryProperties.*;

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
     * Sets this entry as either loved or not loved.
     *
     * @param isLoved {@code true} if loved.
     */
    public void setLoved(boolean isLoved) {
        add(LOVED, new JsonPrimitive(isLoved));
    }

    /**
     * Sets the synopsis of this entry.
     *
     * @param synopsis the summary of the entry.
     */
    public void setSynopsis(String synopsis) {
        add(SYNOPSIS, new JsonPrimitive(synopsis));
    }

    /**
     * Sets the text comments of this entry.
     *
     * @param comments all the comments as a single string.
     */
    public void setComments(String comments) {
        add(COMMENTS, new JsonPrimitive(comments));
    }

    /**
     * Sets the {@link IEntry.TypeValues type} of this entry.
     *
     * @param type the media type, either series, movie, or trilogy.
     */
    public void setType(TypeValues type) {
        add(TYPE, new JsonPrimitive(TypeValues.toRepresentation(type)));
    }

    /**
     * Sets the {@link RatingValues rating value} of the entry.
     *
     * @param rating the RatingValue to give this entry.
     */
    public void setRating(RatingValues rating) {
        add(RATING, new JsonPrimitive(RatingValues.toRepresentation(rating)));
    }

    /**
     * @return the display name of the entry.
     */
    public String getName() {
        return get(NAME).getAsString();
    }

    /**
     * @return {@code true} if the entry was marked
     * as loved.
     */
    public boolean isLoved() {
        if (has(LOVED)) return get(LOVED).getAsBoolean();
        else return false;
    }

    /**
     * @return the description for the entry, or an empty
     * string if it has no synopsis.
     */
    public String getSynopsis() {
        if (has(SYNOPSIS)) return get(SYNOPSIS).getAsString();
        else return "";
    }

    /**
     * @return the comments for the entry, or an empty
     * string if it has no comments.
     */
    public String getComments() {
        if (has(COMMENTS)) return get(COMMENTS).getAsString();
        else return "";
    }

    /**
     * @return the type of the entry, or {@link TypeValues#NONE}
     * if none was set/specified.
     */
    public TypeValues getType() {
        if (has(TYPE)) return TypeValues.fromRepresentation(get(TYPE).getAsString());
        else return TypeValues.NONE;
    }

    /**
     * @return the rating given to the entry, or {@link RatingValues#NA}
     * if none was set/specified.
     */
    public RatingValues getRating() {
        if (has(RATING)) return RatingValues.fromRepresentation(get(RATING).getAsString());
        else return RatingValues.NA;
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

    /**
     * Checks if this entry has information stored
     * under the given property.
     *
     * @param property the property to check for.
     * @return {@code true} if the entry has
     * information stored under the property.
     */
    private boolean has(EntryProperties property) {
        return backingEntry.getInformation().has(property.getKey());
    }

    /**
     * The complete list of possible values able to be used
     * as a type for the entry. {@link #setType(TypeValues)}.
     */
    public enum TypeValues {

        NONE, MOVIE, SERIES, TRILOGY;

        /**
         * Converts the given string representation of a TypeValue
         * to the represented TypeValue.
         *
         * @param representation the string representation of a TypeValue.
         * @return the represented TypeValue or TypeValue.NONE if not a
         * valid representation.
         */
        public static TypeValues fromRepresentation(String representation) {
            for (TypeValues val : TypeValues.values()) {
                if (representation.toLowerCase().equals(val.name().toLowerCase()))
                    return val;
            }
            return NONE;
        }

        /**
         * Converts the given TypeValue into its own unique
         * string representation, which can be used to store
         * and retrieve the value elsewhere.
         *
         * @param type the TypeValue to get the representation of.
         * @return the given TypeValues unique string representation.
         */
        public static String toRepresentation(TypeValues type) {
            return type.name().toLowerCase();
        }
    }

    /**
     * The complete list of possible values able to be used
     * as a rating for the entry. {@link #setRating(RatingValues)}.
     */
    public enum RatingValues {

        NA("N/A"), R1("1/10"), R2("2/10"), R3("3/10"), R4("4/10"),
        R5("5/10"), R6("6/10"), R7("7/10"), R8("8/10"), R9("9/10"), R10("10/10");

        /**
         * The string value that represents this enum type.
         */
        public final String val;

        /**
         * @param val The string value that represents this enum type.
         */
        RatingValues(String val) {
            this.val = val;
        }

        /**
         * Converts the given string representation of RatingValue
         * to the represented RatingValue.
         *
         * @param representation the string representation of a RatingValue.
         * @return the represented RatingValue or RatingValue.NA if not a
         * valid representation.
         */
        public static RatingValues fromRepresentation(String representation) {
            for (RatingValues val : RatingValues.values()) {
                if (representation.toLowerCase().equals(val.val.toLowerCase()))
                    return val;
            }
            return NA;
        }

        /**
         * Converts the given RatingValue into its own unique
         * string representation, which can be used to store
         * and retrieve the value elsewhere.
         *
         * @param rating the RatingValue to get the representation of.
         * @return the given RatingValues unique string representation.
         */
        public static String toRepresentation(RatingValues rating) {
            return rating.val.toLowerCase();
        }
    }
}
