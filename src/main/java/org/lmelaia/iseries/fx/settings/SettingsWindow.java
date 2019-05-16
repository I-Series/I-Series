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
package org.lmelaia.iseries.fx.settings;

import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;

/**
 * Settings window class.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/settings_window.fxml",
        cssFileName = "windows/settings_window.css",
        controllerClass = SettingsWindowController.class
)
public class SettingsWindow extends FXWindow<SettingsWindowController> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialization() {
        this.setTitle("I-Series Settings");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostInitialization() {

    }
}
