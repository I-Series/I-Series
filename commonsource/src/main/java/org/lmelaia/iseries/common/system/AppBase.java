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

package org.lmelaia.iseries.common.system;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.net.ipc.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base class for application instances (i.e.
 * launcher and main application).
 */
public abstract class AppBase {

    /*
     * Configures the logger before it's initialized
     * by a constructor call.
     */
    static {
        AppLogger.silentConfigure("/configuration/log4j2_configuration.xml");
    }

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * List of shutdown shutdownListeners.
     */
    private final List<ShutdownListener> shutdownListeners = new ArrayList<>();

    /**
     * The argument handler object which will receive the arguments
     * passed to this application.
     */
    private final ArgumentHandler argumentHandler = new ArgumentHandler();

    /**
     * This application instances messenger object used
     * for communicating with other running
     * application instances.
     */
    private Messenger messenger = null;

    /**
     * Constructs, initialises and initiates
     * the start procedure of an app class
     * extending this class.
     *
     * @param appClass the subclass to start.
     * @param args     the command line arguments given
     *                 when starting the application.
     */
    public static void startApp(AppBase appClass, String[] args) {
        Objects.requireNonNull(appClass, "App class cannot be null")
                .internalStart(Objects.requireNonNull(args, "Given arguments cannot be null"));
    }

    /**
     * Adds an uncaught exception handler
     * to the provided thread.
     * <p>
     * The handler calls appropriate
     * shutdown methods and handles
     * the exception by crashing.
     *
     * @param t the thread to manage.
     */
    public void manageThread(Thread t) {
        t.setUncaughtExceptionHandler(getNewHandler());
    }

    /**
     * Adds a shutdown listener to the list, which will
     * be called when the application is called to exit,
     * either by crash or normal termination.
     *
     * @param listener the listener to add.
     */
    public void addShutdownListener(ShutdownListener listener) {
        shutdownListeners.add(listener);
    }

    /**
     * Removes the shutdown listener from the list.
     *
     * @param listener the listener to remove.
     */
    @SuppressWarnings("unused")
    public void removeShutdownListener(ShutdownListener listener) {
        shutdownListeners.remove(listener);
    }

    /**
     * Closes all application instances and supporting
     * applications (i.e. launcher), along with notifying
     * all registered shutdown shutdownListeners.
     *
     * This is done on a separate thread
     * to ensure that, killing the thread
     * that called this method doesn't halt
     * the shutdown process.
     *
     * This method blocks until all shutdown
     * listeners have been called.
     *
     * @param code the exit code.
     */
    public void exit(ExitCode code) {
        LOG.info("Application close requested from thread: " + Thread.currentThread().getName());

        if (!code.normal) {
            LOG.warn("Shutting down due to abnormal termination");
        }

        ShutdownThread shutdownThread = new ShutdownThread(code);
        shutdownThread.start();

        try {
            shutdownThread.join();
        } catch (InterruptedException e) {
            LOG.debug("Interrupt", e);
        }
    }

    /**
     * @return the argument handler object
     * containing the arguments passed to
     * this application.
     */
    @SuppressWarnings("WeakerAccess")
    public ArgumentHandler getArgumentHandler() {
        return this.argumentHandler;
    }

    /**
     * @return the {@link Messenger} object for this application instance.
     */
    public Messenger getMessenger() {
        if (messenger == null)
            messenger = new Messenger(getMessengerTimeout());

        return messenger;
    }

    /**
     * @param args command line arguments.
     * @see ArgumentHandler#update(String[]).
     */
    protected void update(String[] args) {
        argumentHandler.update(args);
    }

    /**
     * Internal method to start an app class.
     *
     * @param args the command line arguments.
     */
    private void internalStart(String args[]) {
        argumentHandler.update(args);
        attemptFXInitialization();

        try {
            start();
        } catch (Exception e) {
            LOG.fatal("Start method of app class["
                    + this.getClass().getCanonicalName() + "] threw an error.", e);
            exit(ExitCode.UNHANDLED_EXCEPTION);
        }
    }

    /**
     * Starts the fx thread if {@link #initializeFX()}
     * returns true.
     */
    private void attemptFXInitialization() {
        if (!initializeFX()) {
            return;
        }

        FXWindowsManager.startFX(
                getFXThreadName(), getArgumentHandler().reconstruct(),
                getFXWindowsClassPath(), this, true);
    }

    /**
     * @return a new uncaught exception handler which
     * crashes the application properly when an exception goes uncaught.
     */
    private Thread.UncaughtExceptionHandler getNewHandler() {
        return (t, e) -> {
            LOG.fatal("Uncaught exception found.");
            LOG.info(String.format("Details: [thread=%s, exception=%s]", t.getName(), e.toString()), e);
            exit(ExitCode.UNHANDLED_EXCEPTION);
        };
    }

    /**
     * Called when a main method tries to start
     * the application. This is used in place of
     * a main(String[] args) method.
     *
     * @throws Exception if any exception
     * is thrown during the start procedure.
     */
    protected abstract void start() throws Exception;

    /**
     * Should return the timeout for receiving replies
     * from other messenger instances and pinging them.
     *
     * @return the timeout in milliseconds.
     */
    protected abstract int getMessengerTimeout();

    /**
     * @return the name of the fx thread.
     */
    protected String getFXThreadName() {
        return null;
    }

    /**
     * @return the package path to where
     * the window classes are located. eg.
     * com.name.app.windows
     */
    protected String getFXWindowsClassPath() {
        return null;
    }

    /**
     * Override this method to return {@code true}
     * to have the fx thread initialized before
     * application start.
     *
     * @return {@code true} when the fx thread
     * is desired to start before application
     * start.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected abstract boolean initializeFX();

    /**
     * Thread responsible for calling the registered
     * shutdown hooks and closing the application
     * if an abort isn't requested.
     */
    class ShutdownThread extends Thread {

        /**
         * Exit code given.
         */
        private ExitCode exitCode = null;

        /**
         * Creates a new shutdown thread.
         *
         * @param code exit code to give
         *             registered shutdown
         *             listeners.
         */
        ShutdownThread(ExitCode code) {
            super("Shutdown thread");
            this.exitCode = Objects.requireNonNull(code, "Exit code cannot be null");
        }

        /**
         * Starts the shutdown process.
         * <p>
         * This consists of calling all registered
         * shutdown listeners, and, if none
         * requested an abort, closes the
         * application through the {@link System#exit(int)}
         * method.
         */
        @Override
        public void run() {
            if (exitCode == null) {
                throw new IllegalArgumentException("ExitCode cannot be null");
            }

            boolean aborted = false;

            for (ShutdownListener listener : shutdownListeners) {
                if (!listener.onShutdown(exitCode)) {
                    aborted = true;
                }
            }

            if (aborted) {
                LOG.trace("Shutdown aborted");
            } else {
                LOG.info("Shutdown complete: " + exitCode.toString());
                System.exit(exitCode.code);
            }
        }
    }
}
