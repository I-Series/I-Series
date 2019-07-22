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
 * Controller class for the playlist renamer window.
 */
public class PlaylistRenamerWindowController extends FXController {

    @FXML
    private TextField textFieldPlaylistName;

    @FXML
    private Button btnRename;

    @FXML
    private Button btnCancel;

    /**
     * Current working playlist. The one
     * to rename.
     */
    private IPlaylist workingPlaylist;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.getWindow().setOnShowing((event -> this.textFieldPlaylistName.requestFocus()));

        this.btnCancel.setOnAction(this::onCancel);
        this.btnRename.setOnAction(this::onRename);
    }

    /**
     * Called when the cancel button is pressed.
     * Closes the window without saving any changes.
     *
     * @param event action event.
     */
    private void onCancel(ActionEvent event) {
        this.getWindow().close();
    }

    /**
     * Called when the rename button is pressed.
     * Renames the playlist if no error occurs
     * and closes the window.
     *
     * @param event action event.
     */
    private void onRename(ActionEvent event) {
        try {
            workingPlaylist.rename(textFieldPlaylistName.getText());
        } catch (IPlaylists.PlaylistException.PlaylistAlreadyExistsException e) {
            AlertUtil.showInfoDialog(
                    "Playlist name in use.",
                    "The playlist name is already in use by another playlist."
            );
            return;
        } catch (IPlaylists.PlaylistException.PlaylistWriteException e) {
            AlertUtil.showErrorDialog("Failed to rename playlist.", e.getClass().getName());
            return;
        }

        App.getInstance().getWindowsManager().getWindow(MainWindow.class)
                .getController().getNavigator().selectPlaylist(workingPlaylist.getName());

        this.getWindow().close();
    }

    /**
     * Sets the working playlist and modifies
     * the window to display the playlist name.
     *
     * @param playlist the playlist.
     */
    protected void setPlaylist(IPlaylist playlist) {
        this.workingPlaylist = playlist;
        this.textFieldPlaylistName.setText(playlist.getName());
        this.textFieldPlaylistName.selectAll();
        this.textFieldPlaylistName.setPromptText(playlist.getName());
    }
}
