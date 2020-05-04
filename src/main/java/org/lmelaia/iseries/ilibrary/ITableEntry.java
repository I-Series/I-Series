package org.lmelaia.iseries.ilibrary;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.lmelaia.iseries.fx.entry.EntryWindowController;

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

    /**
     * @return the type of the entry, or an
     * empty string if no type was specified.
     */
    public String getType() {
        return switch (entry.getType()) {
            case SERIES -> "Series";
            case MOVIE -> "Movie";
            case TRILOGY -> "Trilogy";
            default -> "";
        };
    }

    /**
     * @return the rating of the entry in string form,
     * or an empty string if no rating was specified.
     */
    public String getRating() {
        if (entry.getRating() == null)
            return "";

        String value = entry.getRating().val;

        if (value.equals(IEntry.RatingValues.NA.val))
            return "";
        else return value;
    }

    /**
     * @return a (possibly shortened) string of the
     * synopsis/description of the entry.
     */
    public String getSynopsis() {
        return shortenIfNeeded(entry.getSynopsis(), 45);
    }

    /**
     * @return a (possibly shortened) string of the comments for the entry.
     */
    public String getComments() {
        return shortenIfNeeded(entry.getComments(), 45);
    }

    /**
     * @return a heart outline image graphic if the entry is not
     * loved, and a shaded heart image graphic if the entry is
     * loved.
     */
    public ImageView getLovedGraphic() {
        return entry.isLoved()
                ? new ImageView(new Image(EntryWindowController.class.getResourceAsStream(
                "/images/closed_heart_16.png")))
                : new ImageView(new Image(EntryWindowController.class.getResourceAsStream(
                "/images/open_heart_16.png")));
    }

    /**
     * Shortens the given string to the given character
     * limit, including the trailing 3 ellipsis, if the
     * string is longer than the character limit.
     *
     * @param str       the string to shorten.
     * @param charLimit the character limit to shorten the string to.
     * @return the shortened string, or the original string if it's
     * already short enough.
     */
    private static String shortenIfNeeded(String str, int charLimit) {
        if (str.length() > charLimit) {
            str = str.substring(0, charLimit - 3);
            str += "...";
        }

        return str;
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
