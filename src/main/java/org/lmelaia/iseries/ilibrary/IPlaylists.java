package org.lmelaia.iseries.ilibrary;

import com.google.gson.*;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.library.LibraryException;
import org.lmelaia.iseries.library.NamedLibraryEntry;
import org.lmelaia.iseries.util.StringUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container, file handler and accessor for {@link IPlaylist}s
 * within an {@link ILibrary}. The IPlaylists class is used to
 * obtain references to existing playlists, create new playlists,
 * register callbacks to be notified when the list of playlists
 * changes, and, handle the backing files.
 * <p/>
 * Every {@link ILibrary} has a single {@link IPlaylists} object
 * used to store playlists.
 * <p/>
 * From an API standpoint, this class is mostly used to get
 * existing playlists and create new playlists. Any other actions
 * should be done on the playlist object itself.
 * <p/>
 * A reference to this class must be obtained from the {@link ILibrary}
 * using {@link ILibrary#playlists()}.
 */
public class IPlaylists {

    /**
     * Gson instance used to convert json objects
     * to a json string and back.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Logger instance for this class.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The key (uid) used to store the playlists entry with the
     * {@link ILibrary}.
     */
    protected static final String UID = "Playlists";

    /**
     * Map of all loaded playlists (loaded through {@link #getPlaylist(String)}
     * or {@link #createAndGetNewPlaylist(String)}) mapped to their unique
     * name.
     */
    private final Map<String, IPlaylist> loadedPlaylists = new HashMap<>();

    /**
     * List of all registered change listeners notified when the list
     * of playlists changes.
     */
    private final List<PlaylistsChangedListener> changeListeners = new ArrayList<>();

    /**
     * Json list of all playlist names saved to index file.
     */
    private final JsonArray playlists;

    /**
     * The backing library entry used to implement
     * the playlist system within the
     * {@link org.lmelaia.iseries.library.Library}.
     */
    private final NamedLibraryEntry backingEntry;

    /**
     * Constructs or loads the playlist system
     * from the given entry within the Library.
     *
     * @param backingEntry the entry the playlists
     *                     are saved to.
     */
    protected IPlaylists(NamedLibraryEntry backingEntry) {
        this.backingEntry = backingEntry;

        JsonElement ePlaylists = backingEntry.getInformation().get("playlists");

        if (ePlaylists == null) {
            LOG.info("Playlists entry missing playlists array. creating...");
            playlists = new JsonArray();
            backingEntry.getInformation().add("playlists", playlists);

            try {
                backingEntry.update();
            } catch (LibraryException.EntryModificationException e) {
                //I don't think this should be too big a problem.
                //If any playlist is created/modified it should try save everything again.
                LOG.error("Failed to save playlists after modification...", e);
            }
        } else {
            playlists = ePlaylists.getAsJsonArray();
        }
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * @return a new String array containing the names (UIDs)
     * of all the playlists.
     */
    public String[] getPlaylistNames() {
        String[] names = new String[playlists.size()];

        for (int i = 0; i < playlists.size(); i++) {
            names[i] = playlists.get(i).getAsString();
        }

        return names;
    }

    /**
     * Obtains an existing playlist within the playlists list
     * with the same name (UID) as the one provided either from the
     * cache or from file if the playlist hasn't been
     * cached yet by calling with method. Once a playlist
     * has been read from file, it is automatically cached
     * as well.
     *
     * @param name the name (i.e. UID) of the playlist.
     * @return the requested playlist, or {@code null} if no playlist
     * with the given name could be found.
     * @throws PlaylistException.PlaylistNotFoundException if the playlist file on disk could not
     *                                                     be found. This does not indicate the playlist does NOT exist, only that the file reference
     *                                                     could not be found.
     * @throws PlaylistException.PlaylistReadException     if the playlist file on disk could not
     *                                                     be read.
     */
    public IPlaylist getPlaylist(String name) throws PlaylistException.PlaylistNotFoundException,
            PlaylistException.PlaylistReadException {
        //If cached, load from cache.
        if (loadedPlaylists.containsKey(name))
            return loadedPlaylists.get(name);

        //Otherwise read from disk.
        for (JsonElement element : playlists) {
            if (element.getAsString().equals(name)) {
                IPlaylist playlist = readPlaylist(element.getAsString());
                //And cache
                loadedPlaylists.put(element.getAsString(), playlist);
                return playlist;
            }
        }

        //Could not be found
        return null;
    }

    /**
     * Creates a new playlist with the given name (UID) as
     * the one provided. The newly created playlist will then
     * be written to file and cached.
     *
     * @param name the name (UID) of the new playlist.
     * @return the created playlist.
     * @throws PlaylistException.PlaylistAlreadyExistsException if a playlist
     *                                                          with the same name (UID) already exists with within the list.
     * @throws PlaylistException.PlaylistWriteException         if the newly created
     *                                                          playlist could not be written to file (either the index or the actual
     *                                                          playlist file).
     */
    public IPlaylist createAndGetNewPlaylist(String name) throws PlaylistException.PlaylistAlreadyExistsException,
            PlaylistException.PlaylistWriteException {
        //Make sure the playlist name is unique.
        for (JsonElement element : playlists) {
            if (element.getAsString().toLowerCase().equals(name.toLowerCase()))
                throw new PlaylistException.PlaylistAlreadyExistsException();
        }

        //Create the playlist
        JsonObject playlistData = new JsonObject();
        playlistData.add("name", new JsonPrimitive(name));

        IPlaylist newPlaylist = new IPlaylist(playlistData, this);
        //Cache the playlist & write it to file
        playlists.add(new JsonPrimitive(name));
        loadedPlaylists.put(name, newPlaylist);
        writePlaylist(name);

        //Structure the playlist
        backingEntry.getInformation().add("playlists", playlists);

        //Ensure the index is up to date.
        try {
            backingEntry.update();
        } catch (LibraryException.EntryModificationException e) {
            LOG.error("Failed to save playlists index", e);
            //It may still be possible for the playlist to be created & loaded
            //however it may not be remembered after a restart.
            //This shouldn't be an issue though.
            throw new PlaylistException.PlaylistWriteException("Failed to save playlist index", e);
        }

        notifyListeners();
        return newPlaylist;
    }

    /**
     * Adds a change listener to list of registered change listeners.
     * <p/>
     * A change listener will only be notified when the actual list
     * of playlists changes, not when a playlists data is modified.
     * That is, a listener will only be notified when: the number
     * of playlists changes, a playlist is added/removed, or a playlist
     * is renamed.
     *
     * @param listener the listener to add/register.
     */
    public void addChangeListener(PlaylistsChangedListener listener) {
        this.changeListeners.add(listener);
    }

    /**
     * Removes an already registered change listener from the list
     * of registered listeners.
     *
     * @param listener the listener to remove.
     * @return {@code true} if this list of change listeners
     * contained the specified listener.
     */
    public boolean removeChangeListener(PlaylistsChangedListener listener) {
        return this.changeListeners.remove(listener);
    }

    // *************
    // PROTECTED API
    // *************

    /**
     * Renames the given playlists name (UID) to the specified name, provided
     * it is unique among all playlists. This will rename the playlist
     * file as well. The playlist must be in the playlists library
     * to be renamed.
     *
     * @param playlist the playlist to rename.
     * @param newName  the new name (UID) for the playlist.
     * @throws PlaylistException.PlaylistAlreadyExistsException if a playlist
     *                                                          with the same name (UID) already exists in the playlist library.
     * @throws PlaylistException.PlaylistWriteException         if the playlist index
     *                                                          or the playlist file could not be written to.
     */
    protected void rename(IPlaylist playlist, String newName) throws PlaylistException.PlaylistWriteException,
            PlaylistException.PlaylistAlreadyExistsException {
        if (playlists.contains(new JsonPrimitive(newName)))
            throw new PlaylistException.PlaylistAlreadyExistsException();

        //Remove old.
        loadedPlaylists.remove(playlist.getName());
        backingEntry.removeFile(playlist.getName());

        //Ensure we keep insertion order
        int index = -1;
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getAsString().equals(playlist.getName()))
                index = i;
        }

        if (index == -1) {
            playlists.remove(new JsonPrimitive(playlist.getName()));
            playlists.add(new JsonPrimitive(newName));
        } else {
            playlists.set(index, new JsonPrimitive(newName));
        }

        //Rename
        playlist.setName(newName);
        loadedPlaylists.put(newName, playlist);

        //Save
        try {
            backingEntry.update();
        } catch (LibraryException.EntryModificationException e) {
            LOG.error("Failed to save playlists index", e);
            throw new PlaylistException.PlaylistWriteException("Failed to save playlist index", e);
        }

        //Write playlist file.
        writePlaylist(newName);

        notifyListeners();
    }

    /**
     * Method used by {@link IPlaylist}s to save themselves.
     * <p/>
     * Writes the given playlist to file.
     *
     * @param playlist the given playlist.
     * @throws PlaylistException.PlaylistWriteException if the playlist
     *                                                  could not be written to file.
     */
    protected void savePlaylist(IPlaylist playlist) throws PlaylistException.PlaylistWriteException {
        writePlaylist(playlist.getName());
    }

    /**
     * Method used by {@link IPlaylist}s to delete themselves.
     * <p/>
     * Deletes the given playlist from the playlist library
     * and from disk.
     *
     * @param playlist the given playlist to delete.
     * @throws PlaylistException.PlaylistWriteException if the playlists
     *                                                  index could not be written to file.
     */
    protected void deletePlaylist(IPlaylist playlist) throws PlaylistException.PlaylistWriteException {
        playlists.remove(new JsonPrimitive(playlist.getName()));
        loadedPlaylists.remove(playlist.getName());
        backingEntry.removeFile(playlist.getName());

        //Save
        try {
            backingEntry.update();
        } catch (LibraryException.EntryModificationException e) {
            LOG.error("Failed to save playlists index", e);
            throw new PlaylistException.PlaylistWriteException("Failed to save playlist index", e);
        }

        notifyListeners();
    }

    // *********
    // INTERNALS
    // *********

    /**
     * Writes the given playlist in the cache to file, effectively saving
     * it to disk.
     *
     * @param name the name (uid) of the playlist.
     * @throws PlaylistException.PlaylistWriteException if the playlist could
     *                                                  not be written to file.
     */
    private void writePlaylist(String name) throws PlaylistException.PlaylistWriteException {
        IPlaylist playlist = loadedPlaylists.get(name);

        //Don't save a non-cached playlist
        if (playlist == null)
            return;

        String dataStr = GSON.toJson(playlist.getBackingData());
        File playlistFile = backingEntry.getFile(name);

        try {
            if (playlistFile == null) {
                try {
                    StringBuilder fileName = new StringBuilder(StringUtil.toAlphanumeric(name));

                    //Ensure the file we create doesn't already exist by appending an
                    //underscore to the filename.
                    while (
                            new File(backingEntry.getPath() + "/" + fileName + ".json")
                                    .exists()
                    ) {
                        fileName.append("_");
                    }

                    backingEntry.addFile(name, null, fileName + ".json");
                } catch (LibraryException.FileModificationException |
                        LibraryException.EntryModificationException e) {
                    LOG.error("Failed to save playlist file: " + name, e);
                    throw new PlaylistException.PlaylistWriteException(
                            "Failed to write playlist file: " + name, e
                    );
                }
            }

            FileWriter writer = new FileWriter(backingEntry.getFile(name));

            writer.write(dataStr);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the given playlist by name (UID) from file.
     *
     * @param name the name (UID) of the playlist.
     * @return the requested playlist read from file.
     * @throws PlaylistException.PlaylistNotFoundException if the playlist
     *                                                     doesn't exist on file.
     * @throws PlaylistException.PlaylistReadException     if the playlist
     *                                                     couldn't be read from file.
     */
    private IPlaylist readPlaylist(String name) throws PlaylistException.PlaylistReadException,
            PlaylistException.PlaylistReadException.PlaylistNotFoundException {
        File playlistFile = backingEntry.getFile(name);

        if (!playlistFile.exists()) {
            LOG.error("Missing playlist file: " + name);
            throw new PlaylistException.PlaylistNotFoundException("Playlist: " + name + " could not be found on disk.");
        }

        try {
            StringBuilder dataStr = new StringBuilder();
            FileReader reader = new FileReader(playlistFile);

            int letter;
            while ((letter = reader.read()) != -1) {
                dataStr.append((char) letter);
            }

            reader.close();

            return new IPlaylist(GSON.fromJson(dataStr.toString(), JsonObject.class), this);
            //Broad catch all to make sure we handle all possible causes of failure.
        } catch (Exception e) {
            LOG.error("Failed to read playlist: " + playlistFile, e);
            throw new PlaylistException.PlaylistReadException(
                    "Could not read playlist file: " + playlistFile, e
            );
        }
    }

    /**
     * Notifies all registered change listeners that
     * the playlists have changed and passes them
     * a this objects reference and the list of
     * updated playlists.
     */
    private void notifyListeners() {
        for (PlaylistsChangedListener listener : changeListeners)
            listener.onPlaylistsChanged(getPlaylistNames(), this);
    }

    // ****************
    // INTERNAL CLASSES
    // ****************

    /**
     * Interface used to subscribe to changes in the
     * playlist library.
     * <p/>
     * Subscribed change listeners
     * will only be notified when the number of playlists
     * changes or a playlist name changes. Changes to the
     * playlists themselves will not notify the change
     * listeners.
     */
    public interface PlaylistsChangedListener {
        void onPlaylistsChanged(String[] playlistNames, IPlaylists playlists);
    }

    /**
     * Base exception type that all playlist exceptions
     * inherit from.
     */
    public static class PlaylistException extends Exception {

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         */
        private PlaylistException() {
            super();
        }

        /**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        private PlaylistException(String message) {
            super(message);
        }

        /**
         * Constructs a new exception with the specified detail message and
         * cause.  <p>Note that the detail message associated with
         * {@code cause} is <i>not</i> automatically incorporated in
         * this exception's detail message.
         *
         * @param message the detail message (which is saved for later retrieval
         *                by the {@link #getMessage()} method).
         * @param cause   the cause (which is saved for later retrieval by the
         *                {@link #getCause()} method).  (A <tt>null</tt> value is
         *                permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         */
        private PlaylistException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new exception with the specified cause and a detail
         * message of <tt>(cause==null ? null : cause.toString())</tt> (which
         * typically contains the class and detail message of <tt>cause</tt>).
         * This constructor is useful for exceptions that are little more than
         * wrappers for other throwables (for example, {@link
         * java.security.PrivilegedActionException}).
         *
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A <tt>null</tt> value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         */
        private PlaylistException(Throwable cause) {
            super(cause);
        }

        /**
         * Constructs a new exception with the specified detail message,
         * cause, suppression enabled or disabled, and writable stack
         * trace enabled or disabled.
         *
         * @param message            the detail message.
         * @param cause              the cause.  (A {@code null} value is permitted,
         *                           and indicates that the cause is nonexistent or unknown.)
         * @param enableSuppression  whether or not suppression is enabled
         *                           or disabled
         * @param writableStackTrace whether or not the stack trace should
         *                           be writable
         */
        private PlaylistException(String message, Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        // *******************
        // SPECIFIC EXCEPTIONS
        // *******************

        /**
         * Thrown to indicate that the requested playlist could not be found
         * on disk. This exception does not mean that the requested playlist
         * does not exist.
         */
        public static class PlaylistNotFoundException extends PlaylistException {

            /**
             * Constructs a new exception with the specified detail message.  The
             * cause is not initialized, and may subsequently be initialized by
             * a call to {@link #initCause}.
             *
             * @param message the detail message. The detail message is saved for
             *                later retrieval by the {@link #getMessage()} method.
             */
            public PlaylistNotFoundException(String message) {
                super(message);
            }
        }

        /**
         * Thrown when a playlist file on disk could not be read. Either
         * from failing to read the file or if the file is corrupted/in
         * invalid format.
         */
        public static class PlaylistReadException extends PlaylistException {

            /**
             * Constructs a new exception with the specified detail message and
             * cause.  <p>Note that the detail message associated with
             * {@code cause} is <i>not</i> automatically incorporated in
             * this exception's detail message.
             *
             * @param message the detail message (which is saved for later retrieval
             *                by the {@link #getMessage()} method).
             * @param cause   the cause (which is saved for later retrieval by the
             *                {@link #getCause()} method).  (A <tt>null</tt> value is
             *                permitted, and indicates that the cause is nonexistent or
             *                unknown.)
             */
            public PlaylistReadException(String message, Exception cause) {
                super(message, cause);
            }
        }

        /**
         * Thrown when a playlist file on disk could not be written to.
         */
        public static class PlaylistWriteException extends PlaylistException {

            /**
             * Constructs a new exception with the specified detail message and
             * cause.  <p>Note that the detail message associated with
             * {@code cause} is <i>not</i> automatically incorporated in
             * this exception's detail message.
             *
             * @param message the detail message (which is saved for later retrieval
             *                by the {@link #getMessage()} method).
             * @param cause   the cause (which is saved for later retrieval by the
             *                {@link #getCause()} method).  (A <tt>null</tt> value is
             *                permitted, and indicates that the cause is nonexistent or
             *                unknown.)
             */
            public PlaylistWriteException(String message, Exception cause) {
                super(message, cause);
            }
        }

        /**
         * Thrown when attempting to create or rename a playlist with/to the same
         * name as an already existing playlist.
         */
        public static class PlaylistAlreadyExistsException extends PlaylistException {

            /**
             * Constructs a new exception with {@code null} as its detail message.
             * The cause is not initialized, and may subsequently be initialized by a
             * call to {@link #initCause}.
             */
            public PlaylistAlreadyExistsException() {
                super();
            }
        }
    }
}
