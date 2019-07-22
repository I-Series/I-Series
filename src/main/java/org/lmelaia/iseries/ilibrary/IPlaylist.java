package org.lmelaia.iseries.ilibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

/**
 * Standard Playlist class. Represents a list of
 * accepted {@link IEntry}s (or playlist) within the ILibrary.
 * <p/>
 * Use {@link IPlaylists#createAndGetNewPlaylist(String)} and
 * {@link IPlaylists#getPlaylist(String)} to create and
 * obtain references to playlists.
 * <p/>
 * Playlists implement the {@link TableEntryFilter} so they
 * can be added as EntryFilters as well.
 */
public class IPlaylist implements TableEntryFilter {

    /**
     * The json object used to store the playlist
     * on file. This json object will hold
     * on the data the playlist works with.
     */
    private final JsonObject backingData;

    /**
     * The IPlaylists instance this playlist
     * is within.
     */
    private final IPlaylists owner;

    /**
     * List of all registered change listeners.
     * Will be {@code null} if no listeners are
     * registered.
     */
    //Lazily initialized.
    private ArrayList<PlaylistChangeListener> changeListeners;

    /**
     * Constructs a new playlist with the given
     * JsonObject to use as a data holder and
     * the IPlaylists instance that created
     * this playlist.
     *
     * @param backingData the given backing json object.
     * @param owner       the IPlaylists instance that created this playlist.
     */
    protected IPlaylist(JsonObject backingData, IPlaylists owner) {
        this.owner = owner;
        this.backingData = backingData;

        if (!backingData.has("entries")) {
            backingData.add("entries", new JsonArray());
        }
    }

    // PUBLIC API

    /**
     * Adds the given IEntry to the playlist. If the IEntry is
     * already contained in the playlist, the method will simply
     * return.
     *
     * @param entry the given entry.
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the now modified
     *                                                             playlist could not be saved on disk.
     */
    public void add(IEntry entry) throws IPlaylists.PlaylistException.PlaylistWriteException {
        if (!getEntries().contains(new JsonPrimitive(entry.getUUID()))) {
            getEntries().add(entry.getUUID());
            save();
            notifyListeners();
        }
    }

    /**
     * Adds the given IEntries to the playlist. If any of the
     * IEntry is already contained in the playlist, the entry
     * will simply be skipped.
     *
     * @param entries the list of entries to add to the playlist.
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the now modified
     *                                                             playlist could not be saved on disk.
     */
    public void addAll(IEntry... entries) throws IPlaylists.PlaylistException.PlaylistWriteException {
        for (IEntry entry : entries) {
            if (!getEntries().contains(new JsonPrimitive(entry.getUUID()))) {
                getEntries().add(entry.getUUID());
            }
        }

        save();
        notifyListeners();
    }

    /**
     * Removes the given IEntry from the playlist if it
     * is already contained within the playlist.
     *
     * @param entry the given entry to remove from the playlist.
     * @return {@code true} if the playlists library contained the given entry.
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the now modified
     *                                                             playlist could not be saved on disk.
     */
    public boolean remove(IEntry entry) throws IPlaylists.PlaylistException.PlaylistWriteException {
        boolean removed = getEntries().remove(new JsonPrimitive(entry.getUUID()));

        if (removed)
            save();

        notifyListeners();
        return removed;
    }

    /**
     * Removes all the given IEntry from the playlist if it
     * is already contained within the playlist.
     *
     * @param entries the list of entries to remove from the playlist.
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the now modified
     *                                                             playlist could not be saved on disk.
     */
    public void removeAll(IEntry... entries) throws IPlaylists.PlaylistException.PlaylistWriteException {
        for (IEntry entry : entries) {
            getEntries().remove(new JsonPrimitive(entry.getUUID()));
        }

        save();
        notifyListeners();
    }

    /**
     * Clears the playlist of all its entries.
     *
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the now modified
     *                                                             playlist could not be saved on disk.
     */
    public void clear() throws IPlaylists.PlaylistException.PlaylistWriteException {
        //Effectively clears the list
        backingData.add("entries", new JsonArray());
        save();
        notifyListeners();
    }

    /**
     * Checks if this playlist contains
     * the given IEntry.
     *
     * @param entry the given IEntry.
     * @return {@code true} if and only if
     * this playlist contains the given IEntry.
     */
    public boolean has(IEntry entry) {
        return getEntries().contains(new JsonPrimitive(entry.getUUID()));
    }

    /**
     * @return the unique name (UID) of the playlist.
     */
    public String getName() {
        return backingData.get("name").getAsString();
    }

    /**
     * Renames this playlist to the name specified
     * provided no other playlist in the playlist
     * library has the same name.
     *
     * @param newName the new name and UID of the playlist.
     * @throws IPlaylists.PlaylistException.PlaylistWriteException         if the
     *                                                                     playlist on disk could not be written to.
     * @throws IPlaylists.PlaylistException.PlaylistAlreadyExistsException if the
     *                                                                     new name provided is already in use by another playlist.
     */
    public void rename(String newName) throws IPlaylists.PlaylistException.PlaylistWriteException,
            IPlaylists.PlaylistException.PlaylistAlreadyExistsException {
        owner.rename(this, newName);
        //This does not notify any listeners as the IPlaylists class handles that.
    }

    /**
     * Deletes this playlist from the playlist library.
     *
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the playlist
     *                                                             could not be written to disk.
     */
    public void delete() throws IPlaylists.PlaylistException.PlaylistWriteException {
        owner.deletePlaylist(this);
        //This does not notify any listeners as the IPlaylists class handles that.
    }

    /**
     * Registers a change listener that will be notified
     * whenever the content of the playlist is changed.
     *
     * @param listener the listener to register.
     */
    public void addChangeListener(PlaylistChangeListener listener) {
        //We lazy init the listeners to save on memory.
        if (this.changeListeners == null)
            //We're only expecting one change listener to ever be registered
            //at once.
            this.changeListeners = new ArrayList<>(2);

        this.changeListeners.add(listener);
    }

    /**
     * Removes an already registered change listener.
     *
     * @param listener the change listener to remove.
     * @return {@code true} if the change listener
     * was removed (contained).
     */
    public boolean removeChangeListener(PlaylistChangeListener listener) {
        //No listeners can be registered
        if (changeListeners == null)
            return false;

        boolean removed = changeListeners.remove(listener);

        //Remove the array if it's not in use.
        if (changeListeners.size() == 0)
            this.changeListeners = null;

        return removed;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p/>
     * <p>
     * Allows only entries that are contained within
     * this playlist.
     *
     * @param entry the ITableEntry to check.
     * @return {@code true} if and only if
     * this playlist contains the given
     * entry.
     */
    @Override
    public boolean accept(ITableEntry entry) {
        return has(entry.getEntry());
    }

    // Playlist Library

    /**
     * @return the json object containing
     * the playlist data.
     */
    protected JsonObject getBackingData() {
        return this.backingData;
    }

    /**
     * Sets the new name of this playlist. Used
     * when renaming playlists.
     *
     * @param name the new name of this playlist.
     */
    protected void setName(String name) {
        this.backingData.add("name", new JsonPrimitive(name));
    }

    // Internal logic

    /**
     * Saves this playlist to file.
     *
     * @throws IPlaylists.PlaylistException.PlaylistWriteException if the
     *                                                             playlist could not be written to file.
     */
    private void save() throws IPlaylists.PlaylistException.PlaylistWriteException {
        this.owner.savePlaylist(this);
    }

    /**
     * @return the entries json array.
     */
    private JsonArray getEntries() {
        return this.backingData.getAsJsonArray("entries");
    }

    /**
     * Notifies any registers listeners
     * that a changes has happened.
     */
    private void notifyListeners() {
        if (this.changeListeners == null)
            return;

        for (PlaylistChangeListener listener : changeListeners) {
            listener.onPlaylistChanged(this);
        }
    }

    // Helping classes

    /**
     * Used to subscribe to changes to the
     * structure of a playlist (e.i. when
     * the entries in it are changed).
     */
    public interface PlaylistChangeListener {

        /**
         * Called when an entry is added to
         * or removed from the playlist this
         * listener is registered to.
         *
         * @param playlist the playlist that was changed.
         */
        void onPlaylistChanged(IPlaylist playlist);
    }
}
