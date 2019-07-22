package org.lmelaia.iseries.fx.delete;

import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.ilibrary.IEntry;

/**
 * Delete window prompt to request confirmation from the
 * user before deleting the entry.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/delete_window.fxml",
        cssFileName = "windows/css/delete_window.css",
        controllerClass = DeleteWindowController.class
)
public class DeleteWindow extends FXWindow<DeleteWindowController> {

    /**
     * Presents the window to the user with the given {@link IEntry}.
     * This entry will then be delete/unindex depending on the users choice.
     *
     * @param entry the entry we're requesting to be deleted.
     */
    public static void present(IEntry entry) {
        DeleteWindow window = App.getInstance().getWindowsManager().getWindow(DeleteWindow.class);
        window.controller.setEntry(entry);
        window.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("Delete Entry?");
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
