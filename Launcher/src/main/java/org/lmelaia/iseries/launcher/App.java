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

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;
import org.lmelaia.iseries.common.fxcore.FXWindowManager;
import org.lmelaia.iseries.common.intercommunication.*;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.common.system.ShutdownListener;

import java.io.IOException;
import java.util.Arrays;

/**
 * Main launcher application instance.
 */
public class App extends AppBase implements ShutdownListener {

    static App INSTANCE;

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Client communication instance.
     */
    private Client client;

    /**
     * Server communication instance.
     */
    private Server server;

    /**
     * Default constructor.
     * <p>
     * Sets up the uncaught exception
     * handler and shutdown listener.
     */
    App() {
        this.manageThread(Thread.currentThread());
        this.addShutdownListener(this);
    }

    /**
     * Starts the launcher application process.
     *
     * @param args arguments given to the application.
     * @throws IOException
     */
    public void start(String[] args) throws IOException {
        LOG.info("New launcher instance starting.");
        LOG.info("New launcher being run with args: " + Arrays.toString(args));

        FXWindowManager.startFX("Launcher", args,
                "org.lmelaia.iseries.launcher.fx.", this);
        initCommunication(args);
    }

    /**
     * Initializes communication with the main application
     * process and sets up the server to receive requests.
     *
     * @param args the arguments given to the application.
     * @throws IOException
     */
    private void initCommunication(String[] args) throws IOException {
        LOG.info("Attempting to get port number");

        int port = FileComUtil.getLastKnownPort();

        if (port == -1) {
            LOG.info("No port number could be obtained. Starting new instance.");
            startInstance(args);
        } else {
            LOG.info("Found server port number: " + port);
            LOG.info("Attempting connection...");

            client = new Client(500);
            if (!client.pingServer(port)) {
                client.close();
                LOG.info("Server not responding, assuming no server exists. Starting new instance.");
                startInstance(args);
            } else {
                LOG.info("Arguments sent: " + Arrays.toString(args));
                Message m = client.sendToServer(new Message(MessageType.ARGS, port, Arrays.toString(args)));
                if (m.getMsgType().equals(MessageType.RECEIVED)) {
                    LOG.info("Message received");
                }
            }
        }
    }

    /**
     * Starts the main application process.
     */
    private void startInstance(String[] args) {
        try {
            AppProcess process = new AppProcess();
            LOG.info("Host server started");
            server = new Server();
            server.start("Launcher");
            LOG.info("I-Series instance starting");
            process.start("I-Series-Base.jar", server.getPort());
        } catch (Exception e) {
            LOG.error("Exception on startInstance", e);
        }
    }

    /**
     * Called when the application is requested to close.
     *
     * @param code the code given as the reason for the application to close.
     */
    @Override
    public void onShutdown(ExitCode code) {
        if (code.error) {
            LOG.fatal("Either the launcher or main application crashed.");
        }
    }

    /**
     * @return the launcher application instance.
     */
    public static App getInstance() {
        return INSTANCE;
    }
}
