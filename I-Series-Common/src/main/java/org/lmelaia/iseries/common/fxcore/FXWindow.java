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

package org.lmelaia.iseries.common.fxcore;

import javafx.stage.Stage;

/**
 * Base class for all FX windows managed by the {@link FXWindowManager}.
 */
public abstract class FXWindow extends Stage {

    /**
     * Default constructor.
     *
     * @param title window title.
     */
    public FXWindow(String title) {
        super();
        this.setTitle(title);
    }

    /**
     * The first of the two initialization stages.
     */
    void initialize() {

        onInitialization();
    }

    /**
     * The second of the two initialization stages.
     */
    void postInitialize() {

        onPostInitialization();
    }

    /**
     * Called when the window is first initialized.
     * <p>
     * <p>
     * All onInitialization implementations
     * are called before any onPostInitialization
     * implementations.
     * </p>
     * <p>
     * <p>
     * Use this method to initialize the window,
     * components and structure the window.
     * </p>
     */
    public abstract void onInitialization();

    /**
     * Called after all declared windows have been
     * initialized.
     * <p>
     * <p>
     * All onPostInitialization implementations
     * are called only after every declared window
     * has been initialized.
     * </p>
     * <p>
     * <p>
     * Use this method to interact with
     * other declared windows, set up
     * displaying of the window and continue
     * operating the program.
     * </p>
     */
    public abstract void onPostInitialization();
}
