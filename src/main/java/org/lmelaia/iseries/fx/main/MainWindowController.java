package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.ExitCode;

/**
 * The controller class for the main window.
 */
public class MainWindowController extends FXController {

    /**
     * NO-OP
     */
    @Override
    public void init() {

    }

    /**
     * Causes the application to crash
     * with an exit code of {@link ExitCode#TEST_EXIT}.
     *
     * @param event event object.
     */
    @SuppressWarnings("unused")
    @FXML
    protected void onCrashAction(ActionEvent event) {
        App.getInstance().exit(ExitCode.TEST_EXIT);
    }
}
