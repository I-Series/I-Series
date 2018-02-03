/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lmelaia.iseries.launcher.fx.crash;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.fx.ControllerBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.common.system.FilePathConstants;
import org.lmelaia.iseries.launcher.ISeriesAppController;

import java.awt.*;
import java.io.IOException;

/**
 * The controller class for the crash window.
 */
public final class CrashWindowController extends ControllerBase {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();
    /**
     * The label displaying the exit codes description.
     */
    @FXML
    protected Label labelDescription;
    /**
     * The label displaying the exit code.
     */
    @FXML
    protected Label labelCode;
    /**
     * Restart button.
     */
    @FXML
    protected Button buttonRestart;
    /**
     * True if the user requested a restart.
     */
    private boolean wasRestarted = false;

    /**
     * NO-OP
     */
    @Override
    public void init() {
    }

    /**
     * @return true if the user requested a restart.
     */
    public boolean wasRestarted() {
        return wasRestarted;
    }

    /**
     * Restarts the application on request by the user.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onRestartAction(ActionEvent event) throws IOException {
        LOG.info("Restarting application.");
        this.wasRestarted = true;
        this.getWindow().close();
        ISeriesAppController.start(null);
    }

    /**
     * Closes the window causing the whole application (launcher as well)
     * to close.
     *
     * @param event
     */
    @FXML
    protected void onCloseAction(ActionEvent event) {
        this.getWindow().close();
    }

    /**
     * Opens the folder containing the logs on request by the user.
     *
     * @param event
     */
    @FXML
    protected void onOpenLogsAction(ActionEvent event) {
        try {
            Desktop.getDesktop().open(FilePathConstants.APPLICATION_LOGS_PATH);
        } catch (IOException e) {
            LOG.error("Failed to open logs", e);
        }
    }

    /**
     * Displays the given exit code.
     *
     * @param code the exit code.
     */
    void display(ExitCode code) {
        this.wasRestarted = false;
        this.labelDescription.setText(code.description);
        this.labelCode.setText(String.valueOf(code.code));
        this.buttonRestart.setDisable(!code.isRecoverable());
    }
}
