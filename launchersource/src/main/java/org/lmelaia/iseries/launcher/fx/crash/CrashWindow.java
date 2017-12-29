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

import javafx.application.Platform;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.FXWindowProperties;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.common.util.ThreadUtil;
import org.lmelaia.iseries.launcher.App;

/**
 * The window class for the crash window.
 */
@RegisterFXWindow
public class CrashWindow extends FXWindow {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Default constructor.
     */
    public CrashWindow() {
        super(
                new FXWindowProperties("Crash Prompt")
                        .setFxml("windows/crash_window.fxml")
                        .setController(new CrashWindowController())
                        .setWidth(480).setHeight(178)
        );
        this.setResizable(false);
    }

    /**
     * NO-OP
     */
    @Override
    public void onInitialization() {
        //NO-OP
    }

    /**
     * Sets the window up to show when the
     * application is requested to shutdown
     * due to an error.
     */
    @Override
    public void onPostInitialization() {
        App.getInstance().addShutdownListener(code -> {
            if (code.error)
                Platform.runLater(() -> {
                    this.show(code);
                });

            ThreadUtil.silentSleep(100);
            while (this.isShowing()) {
                ThreadUtil.silentSleep(500);
            }

            return !getController().wasRestarted();
        });
    }

    /**
     * Shows the window displaying the given exit code.
     *
     * @param code the exit code to display.
     */
    public void show(ExitCode code) {
        getController().display(code);
        this.show();
    }

    /**
     * @return this windows controller.
     */
    public CrashWindowController getController() {
        return (CrashWindowController) this.controller;
    }
}
