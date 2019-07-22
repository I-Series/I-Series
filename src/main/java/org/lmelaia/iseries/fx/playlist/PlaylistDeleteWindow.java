package org.lmelaia.iseries.fx.playlist;

import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.ilibrary.IPlaylist;

/**
 * Playlist delete confirmation dialog.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/playlist_delete_window.fxml",
        cssFileName = "windows/css/playlist_delete_window.css",
        controllerClass = PlaylistDeleteWindowController.class
)
public class PlaylistDeleteWindow extends FXWindow<PlaylistDeleteWindowController> {

    /**
     * Presents the dialog to the user which will then
     * delete the playlist if the user confirms.
     *
     * @param playlist the playlist to confirm deletion of.
     */
    public static void present(IPlaylist playlist) {
        if (playlist == null)
            return;

        PlaylistDeleteWindow window = App.getInstance().getWindowsManager().getWindow(PlaylistDeleteWindow.class);
        window.show();
        window.controller.setPlaylist(playlist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Delete Playlist?");
        this.getIcons().add(new Image("/images/iseries-32.png"));
        this.setResizable(false);
        this.initStyle(StageStyle.UTILITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {

    }

}
