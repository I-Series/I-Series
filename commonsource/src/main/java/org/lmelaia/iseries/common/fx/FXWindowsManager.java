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

import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all declared {@link FXWindow}s.
 * <p>
 * <p>
 * Provides a way to initialize
 * a window by annotating it with
 * {@link RegisterFXWindow}, along
 * with a few other utilities
 * to manipulate and use the declared
 * windows.
 * </p>
 */
public class FXWindowsManager extends Application {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Package path to .fxml window files in the common jar.
     */
    private static final String COMMON_WINDOWS = "org.lmelaia.iseries.common.fx";

    /**
     * FX window manager instance.
     */
    private static FXWindowsManager INSTANCE;

    /**
     * The name of the fx thread.
     */
    private static String name;

    /**
     * Package path to where
     * the windows are declared.
     */
    private static String windowsPath;

    /**
     * The application instance the fx thread is going
     * to be run on.
     */
    private static AppBase app;

    /**
     * Lock object for the guard block in
     * {@link #startFX(String, String[], String, AppBase, boolean)}.
     */
    private static final Object guardLock = new Object();

    /**
     * Becomes true once the fx thread has completed
     * initialization.
     */
    private static volatile boolean started = false;

    /**
     * List of all declared windows.
     */
    private final List<FXWindow> windows = new ArrayList<>();

    /**
     * Starts the FX application thread in a nonblocking way
     * and initializes all declared windows.
     *
     * @param threadName  the name of the FX thread.
     * @param args        the arguments passed to the program.
     * @param windowsPath the package path to where the
     *                    windows are declared.
     * @param app         the application instance the FX thread
     *                    will be run on.
     * @param block       if {@code true}, this method will block
     *                    until the initialization of the fx thread
     *                    has completed.
     */
    @SuppressWarnings("SameParameterValue")
    public static void startFX(String threadName, String[] args, String windowsPath, AppBase app, boolean block) {
        if (INSTANCE != null)
            throw new IllegalStateException("FX thread already started");

        LOG.trace("Starting FX thread...");
        name = threadName;
        FXWindowsManager.windowsPath = windowsPath;
        FXWindowsManager.app = app;

        new Thread("FX starter") {
            @Override
            public void run() {
                Application.launch(FXWindowsManager.class, args);
            }
        }.start();

        Platform.setImplicitExit(false);

        if (block) {
            synchronized (guardLock) {
                while (!started) {
                    try {
                        guardLock.wait();
                    } catch (InterruptedException e) {
                        LOG.warn("FX Window manager guard lock was interrupted", e);
                    }
                }
            }
        }
    }

    /**
     * @return the FX window manager instance if
     * initialized by {@link #startFX(String, String[], String, AppBase, boolean)}.
     * @throws IllegalStateException if the instance
     *                               has not been initialized by
     *                               {@link #startFX(String, String[], String, AppBase, boolean)}.
     */
    @SuppressWarnings("unused")
    public static FXWindowsManager getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Instance not initialized");

        return INSTANCE;
    }

    /**
     * Initializes all declared windows and
     * further sets up the FX thread.
     *
     * @param primaryStage the primary stage given. <b>Unused.</b>
     */
    @Override
    public void start(Stage primaryStage) {
        if (INSTANCE != null)
            throw new IllegalStateException("FX thread already started");

        INSTANCE = this;

        try {
            Thread.currentThread().setName(name + " FX Thread");
            app.manageThread(Thread.currentThread());
            LOG.trace("FX thread started: " + Thread.currentThread().toString());

            initWindows();
            postInitWindows();
        } catch (Exception e) {
            LOG.fatal("Uncaught exception propagated to FX start()", e);
            app.exit(ExitCode.UNEXPECTED_ERROR);
        }

        started = true;
        synchronized (guardLock) {
            guardLock.notifyAll();
        }
    }

    /**
     * Gets the window instance by class from
     * the list of registered windows.
     *
     * @param windowClass the class of the window instance/
     * @param <T>         the window type.
     * @return the window instance or {@code null} if the
     * window was not registered.
     */
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public <T extends FXWindow> T getWindow(Class<T> windowClass) {
        for (FXWindow window : windows) {
            if (window.getClass().equals(windowClass)) {
                return (T) window;
            }
        }

        return null;
    }

    /**
     * Displays a registered window.
     *
     * @param windowClass the class of the window (eg. Window.class)
     * @return {@code true} if the window was displayed.
     */
    public boolean showWindow(Class<? extends FXWindow> windowClass) {
        FXWindow window = getWindow(windowClass);

        if (window == null)
            return false;

        if (window.isShowing())
            return false;

        Platform.runLater(window::show);
        return window.isShowing();
    }

    /**
     * Displays a registered window with the {@code showAndWait()}
     * method.
     *
     * @param windowClass the class of the window (eg. Window.class)
     * @return {@code true} if the window was displayed.
     */
    public boolean showWindowAndWait(Class<? extends FXWindow> windowClass) {
        FXWindow window = getWindow(windowClass);

        if (window == null)
            return false;

        if (window.isShowing())
            return false;

        Platform.runLater(window::showAndWait);
        return window.isShowing();
    }

    /**
     * Displays a registered window with the {@code showAndWait()}
     * method with an option to set the owner.
     *
     * @param windowClass the class of the window (eg. Window.class)
     * @param owner       the parent window.
     * @return {@code true} if the window was displayed.
     */
    public boolean showWindowAndWait(Class<? extends FXWindow> windowClass, Window owner) {
        FXWindow window = getWindow(windowClass);

        if (window == null)
            return false;

        if (window.isShowing())
            return false;

        if (window.getOwner() == null && owner != null)
            window.initOwner(owner);

        Platform.runLater(window::showAndWait);
        return window.isShowing();
    }

    /**
     * Constructs a new instance of each
     * declared window and calls the
     * {@link FXWindow#initialize(String, String, FXController)} method
     * on it.
     */
    private void initWindows() {
        try {
            for (Class<? extends FXWindow> c : getWindowClasses()) {
                LOG.debug("Initializing window: " + c.getCanonicalName());

                String fxml = getFxmlFileName(c);
                String css = getCssFileName(c);

                Class<? extends FXController> controllerClass = getControllerClass(c);

                FXController controller = null;

                if (fxml != null) {
                    if (controllerClass == null) {
                        throw new IllegalStateException(
                                "No controller class specified for window: " + c.getCanonicalName());
                    }

                    try {
                        controller = controllerClass.newInstance();
                    } catch (InstantiationException e) {
                        throw new IllegalStateException("Controller: " + controllerClass.getCanonicalName()
                                + " does not have a default constructor");
                    }
                }

                FXWindow window = c.newInstance();
                windows.add(window);
                window.initialize(fxml, css, controller);
            }
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            //Should not happen as each class is read from
            //the list of classes in the package.
            throw new IllegalStateException(e);
        }

    }

    /**
     * Calls the {@link FXWindow#postInitialize()}
     * method of each declared window.
     */
    private void postInitWindows() {
        for (FXWindow window : windows)
            window.postInitialize();
    }

    /**
     * @return a list of all found, declared window classes.
     * @throws IOException if the attempt to read class path resources
     *                     (jar files or directories) failed.
     */
    private List<Class<? extends FXWindow>> getWindowClasses() throws IOException {
        List<Class<? extends FXWindow>> classList = new ArrayList<>();
        classList.addAll(getWindowClasses(windowsPath));
        classList.addAll(getWindowClasses(COMMON_WINDOWS));
        return classList;
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    private List<Class<? extends FXWindow>> getWindowClasses(String path) throws IOException {
        List<Class<? extends FXWindow>> classList = new ArrayList<>();

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getAllClasses()) {
            if (info.getName().startsWith(path)) {
                final Class<?> clazz = info.load();
                try {
                    try {
                        if (clazz.newInstance() instanceof FXWindow) {
                            if (clazz.getDeclaredAnnotation(RegisterFXWindow.class) != null) {
                                classList.add((Class<? extends FXWindow>) clazz);
                                LOG.debug("Identified possible window class: " + clazz.getCanonicalName());
                            }
                        } else {
                            LOG.debug("Skipping window class: " + clazz.getCanonicalName());
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        LOG.debug("Failed to process a possible window class: " + clazz.getCanonicalName());
                    }
                } catch (NullPointerException e) {
                    LOG.error("Failed to process possible window class: " + clazz.getCanonicalName());
                }

            }
        }

        return classList;
    }

    /**
     * @param windowClass the window class to retrieve the value from.
     * @return the fxml file name value from the window class annotation.
     */
    private String getFxmlFileName(Class<? extends FXWindow> windowClass) {
        if (windowClass.getDeclaredAnnotation(RegisterFXWindow.class) == null) {
            throw new IllegalArgumentException("Class: " + windowClass.getCanonicalName()
                    + " is not annotated with: " + RegisterFXWindow.class.getCanonicalName());
        }

        String s = windowClass.getDeclaredAnnotation(RegisterFXWindow.class).fxmlFileName();

        return (s.equals("")) ? null : s;
    }

    /**
     * @param windowClass the window class to retrieve the value from.
     * @return the css file name value from the window class annotation.
     */
    private String getCssFileName(Class<? extends FXWindow> windowClass) {
        if (windowClass.getDeclaredAnnotation(RegisterFXWindow.class) == null) {
            throw new IllegalArgumentException("Class: " + windowClass.getCanonicalName()
                    + " is not annotated with: " + RegisterFXWindow.class.getCanonicalName());
        }

        String s = windowClass.getDeclaredAnnotation(RegisterFXWindow.class).cssFileName();

        return (s.equals("")) ? null : s;
    }

    /**
     * @param windowClass the window class to retrieve the value from.
     * @return the controller class from the window class annotation.
     */
    private Class<? extends FXController> getControllerClass(Class<? extends FXWindow> windowClass) {
        if (windowClass.getDeclaredAnnotation(RegisterFXWindow.class) == null) {
            throw new IllegalArgumentException("Class: " + windowClass.getCanonicalName()
                    + " is not annotated with: " + RegisterFXWindow.class.getCanonicalName());
        }

        Class<? extends FXController> c
                = windowClass.getDeclaredAnnotation(RegisterFXWindow.class).controllerClass();

        return (c == FXController.class) ? null : c;
    }
}
