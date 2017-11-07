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
import org.lmelaia.iseries.common.AppLogger;
import org.lmelaia.iseries.common.fxcore.FXWindow;
import org.lmelaia.iseries.common.fxcore.LoadFXWindow;
import org.lmelaia.iseries.common.util.ThreadUtil;
import org.lmelaia.iseries.launcher.App;

/**
 * Created by Luke on 11/3/2017.
 */
@LoadFXWindow
public class CrashWindow extends FXWindow {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    public CrashWindow() {
        super("Crash Prompt");
    }

    @Override
    public void onInitialization() {

    }

    @Override
    public void onPostInitialization() {
        App.getInstance().addShutdownListener(code -> {
            if (code.error)
                Platform.runLater(() -> {
                    LOG.info("Showing crash window");
                    this.show();
                });

            ThreadUtil.silentSleep(100);
            while (this.isShowing()) {
                ThreadUtil.silentSleep(500);
            }
        });
    }
}
