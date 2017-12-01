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

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for application instances (i.e.
 * launcher and main application).
 */
public class AppBase {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Configures the logger before it's initialized
     * by a constructor call.
     */
    static {
        AppLogger.silentConfigure("/configuration/log4j2_configuration.xml");
    }

    /**
     * List of shutdown listeners.
     */
    private final List<ShutdownListener> listeners = new ArrayList<>();

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
        listeners.add(listener);
    }

    /**
     * Removes the shutdown listener from the list.
     *
     * @param listener the listener to remove.
     */
    public void removeShutdownListener(ShutdownListener listener) {
        listeners.remove(listener);
    }

    /**
     * Closes all application instances and supporting
     * applications (i.e. launcher), along with notifying
     * all registered shutdown listeners.
     *
     * @param code the exit code.
     */
    public void exit(ExitCode code) {
        LOG.info("Application close requested.");

        if (!code.normal) {
            LOG.warn("Shutting down due to abnormal termination");
        }

        LOG.info("Running exit callbacks...");

        boolean aborted = false;

        for (ShutdownListener listener : listeners) {
            if (!listener.onShutdown(code)) {
                LOG.info("Aborting shutdown from: " + listener.toString());
                aborted = true;
                break;
            }
        }

        if (!aborted) {
            LOG.info("Shutdown complete: " + code.toString());
            System.exit(code.code);
        } else {
            LOG.info("Shutdown aborted");
        }
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
