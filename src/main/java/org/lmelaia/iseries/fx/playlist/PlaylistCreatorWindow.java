package org.lmelaia.iseries.fx.playlist;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;

/**
 * New playlist creator window.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/playlist_creator_window.fxml",
        cssFileName = "windows/css/playlist_creator_window.css",
        controllerClass = PlaylistCreatorWindowController.class
)
public class PlaylistCreatorWindow extends FXWindow<PlaylistCreatorWindowController> {

    /**
     * Presents the playlist creator window to the user.
     * <p/>
     * The window will then handle adding the playlist the
     * user wants to create.
     */
    public static void present() {
        PlaylistCreatorWindow window = App.getInstance().getWindowsManager().getWindow(PlaylistCreatorWindow.class);
        window.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Playlist Creator");
        this.getIcons().add(new Image("/images/iseries-32.png"));
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {
    }
}
