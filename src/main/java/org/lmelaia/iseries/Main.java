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
package org.lmelaia.iseries;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;
import org.lmelaia.iseries.common.intercommunication.Client;
import org.lmelaia.iseries.common.intercommunication.Message;
import org.lmelaia.iseries.common.intercommunication.MessageType;
import org.lmelaia.iseries.common.intercommunication.Server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Starting point of the i-series application.
 *
 * @author Luke
 */
public class Main {

    /**
     * Sets the current threads name and initializes
     * the logging framework.
     */
    //This method must exist at the top of the file
    //in order for the logging instance to be
    //initialized by the time it's required for use.
    static{
        Thread.currentThread().setName("I-Series Main");
        AppLogger.silentConfigure("/configuration/log4j.xml");
    }

    /**
     * Main class instance
     */
    public static final Main INSTANCE = new Main();

    /**
     * Time in milliseconds between each successive
     * ping client task execution.
     */
    private static final int PINGER_WAIT_PERIOD = 1000 * 60;//1 minute.

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The timer responsible for running the {@link PingClientTask}.
     */
    private final Timer pingClientTask = new Timer("Ping client", true);

    /**
     * The implementations instance.
     */
    private final Implementations implementations = new Implementations();

    /**
     * Server instance responsible
     * for receiving and responding
     * to messages from new clients.
     */
    private Server server;

    /**
     * Client instance responsible
     * for communicating with the
     * host client.
     */
    private Client client;

    /**
     * The port on which the host's
     * server was started on.
     */
    private int hostPort;

    //Private constructor
    /*Initializes the client and server*/
    private Main() {
        try {
            client = new Client();
            server = new Server();
        } catch (IOException e) {
            LOG.fatal("Failed to start client or server.", e);
        }
    }

    /**
     * Entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        LOG.info("New ISeries instance starting");
        LOG.info("Starting with arguments: " + Arrays.toString(args));
        INSTANCE.start(args);
    }

    /**
     * Starts the application.
     *
     * @param args given program arguments.
     */
    private void start(String[] args) {
        hostPort = Integer.parseInt(args[0]);

        server.setOnMessageReceived(implementations);
        server.start("Launchers");
        server.writePortNumber();

        LOG.info("Client pinger started");
        pingClientTask.schedule(new PingClientTask(), 0, PINGER_WAIT_PERIOD);

        new Thread(() -> {
            while (true) ;
        }).start();//TODO: Remove
    }

    /**
     * Implements the various interfaces the {@link Main} class requires.
     */
    private class Implementations implements Server.MessageReceivedListener {

        /**
         * Called when a new client instance sends a request
         * to the server.
         *
         * @param m the request message.
         * @return a response message.
         */
        @Override
        public Message onMessageReceived(Message m) {
            LOG.info("New message from client: " + m.toString());
            return new Message(MessageType.RECEIVED, m.getPort(), "");
        }
    }

    /**
     * Task responsible for pinging the host to
     * check if it's still online and operating.
     */
    private class PingClientTask extends TimerTask {

        /**
         * Periodically pings the host to check
         * if it's still online and operating.
         */
        @Override
        public void run() {
            try {
                if (client.pingServer(hostPort)) {
                    LOG.debug("Client online.");
                } else {
                    LOG.debug("Client offline.");
                    //TODO: Kill app somehow or something.
                }
            } catch (IOException e) {
                LOG.error("Failed to ping client.", e);
            }
        }
    }
}
