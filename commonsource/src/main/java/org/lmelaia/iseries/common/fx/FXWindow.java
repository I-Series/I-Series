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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.IOException;

/**
 * Base class for all FX windows managed by the {@link FXWindowsManager}.
 */
public abstract class FXWindow extends Stage {

    /**
     * Logging object.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Root node for the scene.
     */
    @SuppressWarnings("WeakerAccess")
    protected Parent root;

    /**
     * Window scene object.
     */
    @SuppressWarnings("WeakerAccess")
    protected Scene scene;

    /**
     * This windows controller. Might be null.
     */
    protected ControllerBase controller = null;

    /**
     * Sets the root object and scene for the window.
     * <p>
     * If the window has a window.fxml file set, the scene
     * will be loaded from the window.fxml file,
     * otherwise the root object is set to {@link StackPane}.
     */
    @SuppressWarnings("ConstantConditions")
    private void setScene(String fxmlFileName, String cssFileName,
                          ControllerBase controller) {
        if (fxmlFileName != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFileName));
                loader.setController(controller);
                this.controller = controller;

                this.root = loader.load();

                if (this.controller == null)
                    this.controller = loader.getController();


                this.scene = new Scene(root);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load window.fxml file", e);
            }
        } else {
            this.root = new StackPane();
            this.scene = new Scene(root);
        }

        if (cssFileName != null)
            try {
                this.scene.getStylesheets().add(
                        getClass().getClassLoader().getResource(
                                cssFileName).toExternalForm());
            } catch (NullPointerException ex) {
                LOG.error("Could not set css file.", ex);
            }


        this.setScene(scene);
    }

    /**
     * Initializes the window controller.
     */
    private void initController() {
        this.controller.setWindow(this);
        this.controller.init();
    }

    /**
     * The first of the two initialization stages.
     */
    void initialize(String fxmlFileName, String cssFileName,
                    ControllerBase controllerClass) {
        setScene(fxmlFileName, cssFileName, controllerClass);

        if (controllerClass != null)
            initController();

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
    @SuppressWarnings("EmptyMethod")
    protected abstract void onInitialization();

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
    protected abstract void onPostInitialization();

    /**
     * @return the controller instance linked to this
     * window.
     */
    @SuppressWarnings("unused")
    public ControllerBase getController() {
        return this.controller;
    }
}
