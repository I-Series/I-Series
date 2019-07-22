package org.lmelaia.iseries.fx.playlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.fx.main.MainWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.ilibrary.IPlaylist;
import org.lmelaia.iseries.ilibrary.IPlaylists;

/**
 * New playlist creator window controller.
 */
public class PlaylistCreatorWindowController extends FXController {

    @FXML
    private TextField textFieldPlaylistName;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnPopulate;

    @FXML
    private Button btnCancel;

    // ****
    // Init
    // ****

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.getWindow().setOnShowing((event -> {
            this.textFieldPlaylistName.requestFocus();
            this.textFieldPlaylistName.setText("");
        }));

        this.btnCancel.setOnAction(this::onCancel);
        this.btnCreate.setOnAction(this::onCreate);
        this.btnPopulate.setOnAction(this::onCreateAndPopulate);
    }

    // *************
    // Action Events
    // *************

    /**
     * Called when the cancel button is pressed.
     * Closes the window.
     *
     * @param event action event.
     */
    private void onCancel(ActionEvent event) {
        this.getWindow().close();
    }

    /**
     * Called when the "Create and populate" button is pressed.
     * Creates the requested playlist and opens
     * the playlist editor window on the new
     * playlist.
     *
     * @param event action event.
     */
    private void onCreateAndPopulate(ActionEvent event) {
        IPlaylist playlist = createPlaylist();

        if (playlist == null)
            return;

        this.getWindow().close();
        PlaylistEditorWindow.present(playlist);
    }

    /**
     * Called when the Create button is pressed.
     * Creates the requested playlist and selects
     * it in the main window.
     *
     * @param event action event.
     */
    private void onCreate(ActionEvent event) {
        if (createPlaylist() == null)
            return;

        this.getWindow().close();
    }

    // *******
    // Helpers
    // *******

    /**
     * Creates a new playlist from the users input
     * with in the Library.
     *
     * @return the new playlist. {@code null} if a new playlist
     * could not be created.
     */
    private IPlaylist createPlaylist() {
        if (!checkInput())
            return null;

        try {
            IPlaylist playlist = App.getInstance().getILibrary().playlists()
                    .createAndGetNewPlaylist(textFieldPlaylistName.getText());
            App.getInstance().getWindowsManager().getWindow(MainWindow.class)
                    .getController().getNavigator().selectPlaylist(playlist.getName());
            return playlist;
        } catch (IPlaylists.PlaylistException.PlaylistAlreadyExistsException e) {
            //Playlist name in use
            AlertUtil.showInfoDialog(
                    "Playlist name in use.",
                    "The playlist name is already in use by another playlist."
            );
        } catch (IPlaylists.PlaylistException e) {
            //Could not create playlist.
            AlertUtil.showErrorDialog("Failed to create playlist", e.getClass().getName());
        }

        return null;
    }

    /**
     * Ensures the users input is valid.
     *
     * @return {@code true} if the input from
     * the user is valid to use, {@code false}
     * otherwise.
     */
    private boolean checkInput() {
        if (textFieldPlaylistName.getText() == null || textFieldPlaylistName.getText().equals("")) {
            //No input
            AlertUtil.showInfoDialog("Invalid Playlist Name.", "Playlist name cannot be empty.");
            return false;
        }

        return true;
    }
}
