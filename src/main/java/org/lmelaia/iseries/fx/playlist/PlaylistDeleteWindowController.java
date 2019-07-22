package org.lmelaia.iseries.fx.playlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.fx.main.MainWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.ilibrary.IPlaylist;
import org.lmelaia.iseries.ilibrary.IPlaylists;

/**
 * Playlist delete confirmation dialog controller.
 */
public class PlaylistDeleteWindowController extends FXController {

    @FXML
    private Label labelPlaylist;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnCancel;

    /**
     * The playlist to delete if the user
     * confirms.
     */
    private IPlaylist playlist;

    // ****
    // Init
    // ****

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.btnDelete.setOnAction(this::onDelete);
        this.btnCancel.setOnAction(this::onCancel);

        this.getWindow().focusedProperty().addListener((observable) -> {
            if (!this.getWindow().isFocused()) {
                this.getWindow().hide();
            }
        });
    }

    // *************
    // Action Events
    // *************

    /**
     * Called when the delete button is pressed.
     * Deletes the given playlist ({@link #playlist}).
     *
     * @param event action event.
     */
    private void onDelete(ActionEvent event) {
        try {
            playlist.delete();
        } catch (IPlaylists.PlaylistException.PlaylistWriteException e) {
            AlertUtil.showErrorDialog("Failed to delete playlist.", e.getClass().getName());
            return;
        }

        App.getInstance().getWindowsManager().getWindow(MainWindow.class)
                .getController().getNavigator().selectDefaultNavigation();
        this.getWindow().close();
    }

    /**
     * Called when the cancel button is pressed.
     * Closes the window without deleting the playlist.
     *
     * @param event action event.
     */
    private void onCancel(ActionEvent event) {
        this.getWindow().close();
    }

    // *************
    // Protected API
    // *************

    /**
     * Sets the playlist to delete if the user confirms.
     *
     * @param playlist the playlist.
     */
    protected void setPlaylist(IPlaylist playlist) {
        this.playlist = playlist;
        this.labelPlaylist.setText(playlist.getName());
    }
}
