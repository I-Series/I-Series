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

/**
 * List of properties for a window.
 */
public class FXWindowProperties {

    /**
     * The window title.
     */
    private final String name;

    /**
     * Window.fxml file or null if the window does
     * not have one.
     */
    private String fxml = null;

    /**
     * Window controller. May be null
     * if no window.fxml is used.
     */
    private ControllerBase controller = null;

    /**
     * The path to the window css file.
     */
    private String cssFile = null;

    /**
     * The width of the window.
     */
    private int width = 500;

    /**
     * The height of the window.
     */
    private int height = 500;

    /**
     * Default constructor.
     *
     * @param name the title of the window.
     */
    public FXWindowProperties(String name) {
        this.name = name;
    }

    /**
     * @return the title of the window.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the window.fxml file if one was
     * specified.
     */
    public String getFxml() {
        return fxml;
    }

    /**
     * Sets the window.fxml file.
     *
     * @param fxml the path to the window.fxml file.
     * @return {@code this}
     */
    public FXWindowProperties setFxml(String fxml) {
        this.fxml = fxml;
        return this;
    }

    /**
     * @return the width of the window.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the window.
     *
     * @param width the width of the window.
     * @return {@code this}.
     */
    public FXWindowProperties setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * @return the height of the window.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the window.
     *
     * @param height the height of the window.
     * @return {@code this}.
     */
    public FXWindowProperties setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * @return a new clone of this class which
     * has no references, so it cannot be changed by another class.
     */
    FXWindowProperties getUnboundInstance() {
        return new FXWindowProperties(this.name)
                .setFxml(this.fxml).setWidth(this.width)
                .setHeight(this.height).setController(this.controller)
                .setCssFile(this.getCssFile());
    }

    /**
     * @return the window controller object.
     */
    public ControllerBase getController() {
        return controller;
    }

    /**
     * Sets the window controller.
     *
     * @param controller the window controller.
     * @return {@code this}.
     */
    public FXWindowProperties setController(ControllerBase controller) {
        this.controller = controller;
        return this;
    }

    /**
     * @return the path to the window css file.
     */
    public String getCssFile() {
        return cssFile;
    }

    /**
     * @param cssFile the path to the css file.
     * @return {@code this}.
     */
    public FXWindowProperties setCssFile(String cssFile) {
        this.cssFile = cssFile;
        return this;
    }
}
