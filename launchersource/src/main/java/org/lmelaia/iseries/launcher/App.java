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

package org.lmelaia.iseries.launcher;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.common.util.JsonObjectBuilder;

import java.io.IOException;

/**
 * Main launcher application instance.
 */
public class App extends AppBase {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Global instance of this class.
     */
    private static App INSTANCE;

    /**
     * Default constructor.
     * <p>
     * Sets up the uncaught exception
     * handler and shutdown listener.
     */
    App() {
        this.manageThread(Thread.currentThread());
        INSTANCE = this;
    }

    /**
     * @return the launcher application instance.
     */
    public static App getInstance() {
        return INSTANCE;
    }

    /**
     * @return the last port number written to file
     *         if an application instance is running
     *         on it.
     */
    private int getPortIfRunning() {
        int givenPort = getMessenger().readFromPortFile();

        if (givenPort == -1) {
            LOG.info("No last port number could be found");
            return -1;
        }

        if (getMessenger().ping(givenPort)) {
            LOG.info("Found running port: " + givenPort);
            return givenPort;
        } else {
            LOG.info("No instance running on last known port");
        }

        return -1;
    }

    /**
     * Starts the main application process.
     */
    private void startInstance() throws IOException {
        ISeriesAppController.start(getArgumentHandler().reconstruct());
    }

    /**
     * Starts the launcher application process.
     *
     * @throws IOException if communication
     * could not be initialized.
     */
    @Override
    protected void start() throws Exception {
        int port = getPortIfRunning();

        if (port == -1)
            startInstance();
        else {
            LOG.info("Sending new argument change request...");

            JsonObject reply = getMessenger().send(
                    new JsonObjectBuilder()
                            .addName("argument_change")
                            .add("arguments", getArgumentHandler().reconstruct())
                            .get(),
                    port);

            if (reply != null) {
                LOG.info("Message received");
                exit(ExitCode.NORMAL);
            } else {
                LOG.fatal("Failed to send argument change request");
                exit(ExitCode.IPC_FAILURE);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected int getMessengerTimeout() {
        return 200;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected boolean initializeFX() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected String getFXThreadName() {
        return "Launcher";
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected String getFXWindowsClassPath() {
        return "org.lmelaia.iseries.launcher.fx.";
    }
}
