package org.lmelaia.iseries.fx.unindex;

import javafx.scene.image.Image;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.ilibrary.IEntry;

/**
 * Unindex window prompt to request confirmation from the
 * user before unindexing the entry.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/unindex_window.fxml",
        cssFileName = "windows/css/unindex_window.css",
        controllerClass = UnindexWindowController.class
)
public class UnindexWindow extends FXWindow<UnindexWindowController> {

    /**
     * Presents (shows) the window to the user with the given {@link IEntry}.
     * This entry will then be unindex or not depending on the users choice.
     * <p>
     * Calling this method won't display the window if the user has requested
     * that entries be unindex without confirmation. If so, the entry will
     * be unindex when this method is called.
     *
     * @param entry the entry we're requesting to be unindex.
     */
    public static void present(IEntry entry) {
        UnindexWindow window = App.getInstance().getWindowsManager().getWindow(UnindexWindow.class);
        window.controller.setEntry(entry);

        if (Settings.ALWAYS_UNINDEX.getValueAsBoolean()) {
            window.controller.onUnindex(null);
        } else {
            window.show();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Unindex Entry?");
        this.getIcons().add(new Image("/images/iseries-32.png"));
        this.setResizable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {

    }
}
