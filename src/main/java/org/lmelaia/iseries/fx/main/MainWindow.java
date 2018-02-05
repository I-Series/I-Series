package org.lmelaia.iseries.fx.main;

import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.common.system.ExitCode;

/**
 * The main display window for I-Series.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/main_window.fxml",
        controllerClass = MainWindowController.class
)
public class MainWindow extends FXWindow {

    /**
     * Constructs a new window instance.
     */
    public MainWindow() {
        this.setTitle("I-Series");
    }

    /**
     * Set's the application up to close when this window closes.
     */
    @Override
    protected void onInitialization() {
        this.setOnCloseRequest(e -> App.getInstance().exit(ExitCode.NORMAL));
    }

    /**
     * NO-OP
     */
    @Override
    protected void onPostInitialization() {

    }
}
