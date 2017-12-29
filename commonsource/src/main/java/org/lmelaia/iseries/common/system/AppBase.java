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
import org.lmelaia.iseries.common.intercommunication.Client;
import org.lmelaia.iseries.common.intercommunication.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for application instances (i.e.
 * launcher and main application).
 */
public class AppBase {

    /**
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
     * Client instance.
     */
    private Client client = null;

    /**
     * Server instance.
     */
    private Server server = null;

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

        boolean aborted = false;

        for (ShutdownListener listener : shutdownListeners) {
            if (!listener.onShutdown(code)) {
                aborted = true;
                break;
            }
        }

        if (!aborted) {
            LOG.info("Shutdown complete: " + code.toString());
            System.exit(code.code);
        } else {
            LOG.trace("Shutdown aborted");
        }
    }

    /**
     * @return the server instance.
     */
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

}
