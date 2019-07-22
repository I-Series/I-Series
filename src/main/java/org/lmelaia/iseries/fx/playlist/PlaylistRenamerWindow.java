package org.lmelaia.iseries.fx.playlist;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.ilibrary.IPlaylist;

/**
 * Playlist renamer window.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/playlist_rename_window.fxml",
        cssFileName = "windows/css/playlist_rename_window.css",
        controllerClass = PlaylistRenamerWindowController.class
)
public class PlaylistRenamerWindow extends FXWindow<PlaylistRenamerWindowController> {

    /**
     * Presents the renamer window to the user
     * with the playlist they want to rename.
     * The window controller will then handle the rest.
     *
     * @param playlist the playlist to rename.
     */
    public static void present(IPlaylist playlist) {
        if (playlist == null)
            return;

        PlaylistRenamerWindow window = App.getInstance().getWindowsManager().getWindow(PlaylistRenamerWindow.class);
        window.show();
        window.controller.setPlaylist(playlist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Playlist Renamer");
        this.getIcons().add(new Image("/images/iseries-32.png"));
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {
        //NO-OP
    }
}
