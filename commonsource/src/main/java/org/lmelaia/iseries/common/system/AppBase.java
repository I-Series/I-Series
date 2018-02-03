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
import org.lmelaia.iseries.common.net.xcom.Client;
import org.lmelaia.iseries.common.net.xcom.Server;

import java.io.IOException;
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
     * Client instance.
     */
    private Client client = null;

    /**
     * Server instance.
     */
    private Server server = null;

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
     * @param code the exit code.
     */
    public void exit(ExitCode code) {
        LOG.info("Application close requested.");

        if (!code.normal) {
            LOG.warn("Shutting down due to abnormal termination");
        }

        boolean abort = false;

        for (ShutdownListener listener : shutdownListeners) {
            if (!listener.onShutdown(code)) {
                abort = true;
                break;
            }
        }

        if (!abort) {
            LOG.info("Shutdown complete: " + code.toString());
            System.exit(code.code);
        } else {
            LOG.trace("Shutdown aborted");
        }
    }

    /**
     * @return the server instance.
     */
    @SuppressWarnings("WeakerAccess")
    public Server getServer() {
        if (server == null) {
            try {
                server = new Server();
            } catch (IOException e) {
                throw new RuntimeException("Could not get server instance", e);
            }
        }

        return server;
    }

    /**
     * @return the client instance.
     */
    @SuppressWarnings("WeakerAccess")
    public Client getClient() {
        if (client == null) {
            try {
                client = new Client();
            } catch (IOException e) {
                throw new RuntimeException("Could not get client instance", e);
            }
        }

        return client;
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
}
