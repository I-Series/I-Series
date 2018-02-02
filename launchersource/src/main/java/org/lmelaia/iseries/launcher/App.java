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
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.net.xcom.FileComUtil;
import org.lmelaia.iseries.common.net.xcom.Message;
import org.lmelaia.iseries.common.net.xcom.MessageType;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

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
     * Initializes xcom with the main application
     * process and sets up the server to receive requests.
     *
     * @throws IOException
     */
    private int initCommunication() throws IOException {
        LOG.info("Attempting to get port number");

        int port = FileComUtil.getLastKnownPort();

        if (port == -1) {
            LOG.info("No port number could be obtained. Starting new instance.");
            getServer().start("Launcher");
            return -1;
        } else {
            LOG.info("Found server port number: " + port);
            LOG.info("Attempting connection...");

            if (!getClient().pingServer(port, 200)) {
                getClient().close();
                LOG.info("Server not responding, assuming no server exists. Starting new instance.");
                getServer().start("Launcher");
                return -1;
            } else {
                return port;
            }
        }
    }

    /**
     * Starts the main application process.
     */
    private void startInstance() throws IOException {
        ISeriesAppController.start(getArgumentHandler().reconstruct());
    }

    /**
     * @return the port number the server is running on.
     */
    public int getServerPort() {
        return this.getServer().getPort();
    }

    /**
     * Starts the launcher application process.
     *
     * @throws IOException
     */
    @Override
    public void start() throws Exception {
        FXWindowsManager.startFX("Launcher", new String[0],
                "org.lmelaia.iseries.launcher.fx.", this);

        int port = initCommunication();

        if (port == -1)
            startInstance();
        else {
            LOG.info("Arguments sent: " + Arrays.toString(getArgumentHandler().reconstruct()));
            Message m = getClient().sendToServer(new Message(MessageType.ARGS, port, Arrays.toString(
                    getArgumentHandler().reconstruct()
            )));
            if (m.getMsgType().equals(MessageType.RECEIVED)) {
                LOG.info("Message received");
                exit(ExitCode.NORMAL);
            }
        }
    }
}
