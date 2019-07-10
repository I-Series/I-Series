package org.lmelaia.iseries.fx.library;

import javafx.scene.image.Image;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;

/**
 * Window class for the Library Window.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/library_window.fxml",
        cssFileName = "windows/css/library_window.css",
        controllerClass = LibraryWindowController.class
)
public class LibraryWindow extends FXWindow<LibraryWindowController> {

    /**
     * Displays this window (with accurate
     * data & mode).
     *
     * @param required {@code true} if the user input
     *                 is required (e.g. on first launch).
     */
    public static void present(boolean required) {
        LibraryWindow window = App.getInstance().getWindowsManager().getWindow(LibraryWindow.class);

        if (window.isShowing())
            return;

        window.getController().populateFields();
        window.getController().setRequired(required);
        window.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setResizable(false);
        this.setTitle("Select Library");
        this.getIcons().add(new Image("/images/iseries-32.png"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {
        //NO-OP
    }
}
