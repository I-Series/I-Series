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
import org.lmelaia.iseries.common.fxcore.FXWindowManager;
import org.lmelaia.iseries.common.intercommunication.*;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.IOException;
import java.util.Arrays;

/**
 * Main launcher application instance.
 */
public class App extends AppBase {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();
    static App INSTANCE;
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
    }

    /**
     * @return the launcher application instance.
     */
    public static App getInstance() {
        return INSTANCE;
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
        int port = initCommunication();

        if (port == -1)
            startInstance(args);
        else {
            LOG.info("Arguments sent: " + Arrays.toString(args));
            Message m = client.sendToServer(new Message(MessageType.ARGS, port, Arrays.toString(args)));
            if (m.getMsgType().equals(MessageType.RECEIVED)) {
                LOG.info("Message received");
            }
        }

        while (true) ;
    }

    /**
     * Initializes communication with the main application
     * process and sets up the server to receive requests.
     *
     * @throws IOException
     */
    private int initCommunication() throws IOException {
        LOG.info("Attempting to get port number");

        int port = FileComUtil.getLastKnownPort();

        if (port == -1) {
            LOG.info("No port number could be obtained. Starting new instance.");
            server = new Server();
            server.start("Launcher");
            return -1;
        } else {
            LOG.info("Found server port number: " + port);
            LOG.info("Attempting connection...");

            client = new Client(500);
            if (!client.pingServer(port)) {
                client.close();
                LOG.info("Server not responding, assuming no server exists. Starting new instance.");
                server = new Server();
                server.start("Launcher");
                return -1;
            } else {
                return port;
            }
        }
    }

    /**
     * Starts the main application process.
     */
    private void startInstance(String[] args) throws IOException {
        AppController.start(args);
    }

    public int getServerPort() {
        return this.server.getPort();
    }
}
