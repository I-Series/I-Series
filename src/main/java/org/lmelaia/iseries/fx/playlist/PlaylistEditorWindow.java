package org.lmelaia.iseries.fx.playlist;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.ilibrary.IPlaylist;

/**
 * Playlist entry editor window.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/playlist_editor_window.fxml",
        cssFileName = "windows/css/playlist_editor_window.css",
        controllerClass = PlaylistEditorWindowController.class
)
public class PlaylistEditorWindow extends FXWindow<PlaylistEditorWindowController> {

    /**
     * Presents the playlist editor to the user
     * with the given playlist to edit. The window
     * controller will then edit and save the playlist
     * as directed.
     *
     * @param playlist the playlist the user wants
     *                 to edit.
     */
    public static void present(IPlaylist playlist) {
        if (playlist == null)
            return;

        PlaylistEditorWindow window = App.getInstance().getWindowsManager().getWindow(PlaylistEditorWindow.class);
        window.show();
        //Must be done last to prevent the application from freezing.
        window.controller.populate(playlist);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Playlist Editor");
        this.getIcons().add(new Image("/images/iseries-32.png"));
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
