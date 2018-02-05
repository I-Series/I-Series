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

package org.lmelaia.iseries.common.fx;

/**
 * Base class for fx window controllers.
 */
public abstract class FXController {

    /**
     * Window instance this controller belongs to.
     */
    private FXWindow window;

    /**
     * Default public constructor.
     */
    public FXController() {
    }

    /**
     * Called on initialization of window controllers.
     */
    @SuppressWarnings("EmptyMethod")
    public abstract void init();

    /**
     * @return the window instance this controller belongs to.
     */
    @SuppressWarnings("WeakerAccess")
    public FXWindow getWindow() {
        return this.window;
    }

    /**
     * Sets the window instance this controller belongs to.
     *
     * @param window the window instance.
     */
    void setWindow(FXWindow window) {
        this.window = window;
    }
}
