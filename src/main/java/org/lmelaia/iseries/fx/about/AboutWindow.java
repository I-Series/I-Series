package org.lmelaia.iseries.fx.about;

import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;

/**
 * The about window class.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/about_window.fxml",
        cssFileName = "windows/about_window.css",
        controllerClass = AboutWindowController.class
)
public class AboutWindow extends FXWindow<AboutWindowController> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("About I-Series");
        this.setResizable(false);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Requests the close button component has
     * focus on window showing.
     */
    @Override
    protected void onPostInitialization() {
        this.setOnShowing(e -> {
            controller.btnClose.requestFocus();
        });
    }
}
