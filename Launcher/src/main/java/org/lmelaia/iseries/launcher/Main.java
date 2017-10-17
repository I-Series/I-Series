/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.launcher;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;
import org.lmelaia.iseries.common.intercommunication.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Starting class for the launcher.
 *
 * @author Luke
 */
public class Main {

    /**
     * Pre-start initialization.
     */
    static{
        Thread.currentThread().setName("Launcher Main");
        AppLogger.silentConfigure("/configuration/log4j2_configuration.xml");
    }

    /**
     * Logging framework instance.
     */
    static Logger LOG = AppLogger.getLogger();

    //End of logging configuration

    /**
     * Client communication instance.
     */
    public static Client client;

    /**
     * Server communication instance.
     */
    public static Server server;

    /**
     * Starts the launcher.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        LOG.info("New launcher instance starting.");
        LOG.info("New launcher being run with args: " + Arrays.toString(args));

        LOG.info("Attempting to get port number");

        int port = FileComUtil.getLastKnownPort();

        if (port == -1) {
            LOG.info("No port number could be obtained. Starting new instance.");
            start();
        } else {
            LOG.info("Found server port number: " + port);
            LOG.info("Attempting connection...");

            client = new Client();
            if (!client.pingServer(port)) {
                client.close();
                LOG.info("Server not responding, assuming no server exists. Starting new instance.");
                start();
            } else {
                LOG.info("Arguments sent: " + Arrays.toString(args));
                Message m = client.sendToServer(new Message(MessageType.ARGS, port, Arrays.toString(args)));
                if (m.getMsgType().equals(MessageType.RECEIVED)) {
                    LOG.info("Message received");
                }
            }
        }

        LOG.info("End");
        System.exit(0);
    }

    /**
     * Starts the main application process.s
     */
    private static void start() {
        try {
            AppProcess process = new AppProcess();
            LOG.info("Host server started");
            server = new Server();
            server.start("Launchers");
            LOG.info("I-Series instance starting");
            process.start("I-Series-Base.jar", server.getPort());
        } catch (Exception e) {
            LOG.error("Exception on start", e);
        }
    }

}
