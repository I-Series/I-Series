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
package org.lmelaia.iseries.fx.main.util;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A set of utilities for javafx controls.
 */
public final class ControlUtil {

    //Static class.
    private ControlUtil() {
    }

    /**
     * Sets the background colour of a node using a css colour value
     * (e.g. "red", "rbg(0,0,0)").
     *
     * @param node  the node to set the background colour of.
     * @param color the new background color.
     * @param <T>   The node class type.
     * @return the node.
     */
    public static <T extends Node> T setBackgroundColor(T node, String color) {
        node.setStyle("-fx-background-color: " + color);
        return node;
    }

    /**
     * Creates an ImageView with the given resource path.
     *
     * @param path the resource path.
     * @return the created ImageView.
     */
    public static ImageView getImageView(String path) {
        return new javafx.scene.image.ImageView(new Image(
                ControlUtil.class.getResourceAsStream(path))
        );
    }
}
