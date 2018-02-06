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
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.launcher.App;

/**
 * The window class for the crash window.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/crash_window.fxml",
        controllerClass = CrashWindowController.class
)
public class CrashWindow extends FXWindow {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Lock object used when waiting
     * on this window to close.
     */
    private final Object lock = new Object();

    /**
     * Default constructor.
     */
    public CrashWindow() {
        this.setTitle("Crash Prompt");
        this.setResizable(false);
    }

    /**
     * Notifies objects waiting on {@link #lock}
     * that the window has closed.
     */
    @Override
    protected void onInitialization() {
        this.setOnHiding((e) -> {
            synchronized (lock) {
                lock.notifyAll();
            }
        });
    }

    /**
     * Sets the window up to show when the
     * application is requested to shutdown
     * due to an error.
     */
    @Override
    protected void onPostInitialization() {
        App.getInstance().addShutdownListener(code -> {
            if (code.error) {
                Platform.runLater(() -> this.show(code));

                synchronized (lock) {
                    do {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            LOG.info("Interrupted", e);
                        }
                    }
                    while (this.isShowing());
                }
            }

            return !getController().wasRestarted();
        });
    }

    /**
     * Shows the window displaying the given exit code.
     *
     * @param code the exit code to display.
     */
    private void show(ExitCode code) {
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
