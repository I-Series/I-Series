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

package org.lmelaia.iseries;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;
import org.lmelaia.iseries.common.intercommunication.Client;
import org.lmelaia.iseries.common.intercommunication.Message;
import org.lmelaia.iseries.common.intercommunication.MessageType;
import org.lmelaia.iseries.common.intercommunication.Server;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.ExitCode;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Application class responsible for controlling
 * the lifecycle of the application.
 * <p>
 * 7,869
 * 37
 */
public class App extends AppBase {

    /**
     * Instance of this application.
     */
    static App INSTANCE;

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The timer responsible for running the {@link App.PingClientTask}.
     */
    private final Timer pingClientTask = new Timer("Ping client", true);

    /**
     * The implementations instance.
     */
    private final App.Implementations implementations = new App.Implementations();

    /**
     * Server instance responsible
     * for receiving and responding
     * to messages from new launchers.
     */
    private Server server;

    /**
     * Client instance responsible
     * for communicating with the
     * host launcher.
     */
    private Client client;

    /**
     * The port on which the launchers
     * server was started on.
     */
    private int hostPort;

    /**
     * Constructor.
     */
    App() {
        this.manageThread(Thread.currentThread());
        try {
            client = new Client();
            server = new Server();
        } catch (IOException e) {
            LOG.fatal("Failed to start client or server.", e);
        }
    }

    /**
     * @return the instance of this application.
     */
    public static App getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes and starts the application.
     *
     * @param args given program arguments.
     */
    void start(String[] args) {
        LOG.info("New ISeries instance starting");
        LOG.info("Starting with arguments: " + Arrays.toString(args));

        initLauncherCom(args[0]);
        openDebugWindow();
    }

    /**
     * Initializes communication with the launcher.
     *
     * @param portArg
     */
    private void initLauncherCom(String portArg) {
        hostPort = Integer.parseInt(portArg);

        server.setOnMessageReceived(implementations);
        server.start("I-Series");
        server.writePortNumber();

        LOG.info("Client pinger started");
        pingClientTask.schedule(new App.PingClientTask(), 0,
                Settings.LAUNCHER_PING_FREQUENCY.getValueAsInt());
    }

    /**
     * Opens a normal JFrame for debugging.
     */
    private void openDebugWindow() {
        JFrame frame = new JFrame("Debug window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100, 100);
        frame.setVisible(true);
    }

    /**
     * Implements the various interfaces the {@link Main} class requires.
     */
    private class Implementations implements Server.MessageReceivedListener {

        /**
         * Called when a new launcher instance sends a request
         * to the main application instance.
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
     * Task responsible for pinging the launcher to
     * check if it's still online and operating.
     */
    private class PingClientTask extends TimerTask {

        /**
         * Periodically pings the launcher to check
         * if it's still online and operating.
         */
        @Override
        public void run() {
            try {
                if (client.pingServer(hostPort)) {
                } else {
                    LOG.fatal("Client not responding.");
                    App.INSTANCE.exit(ExitCode.UNRESPONSIVE_LAUNCHER);
                }
            } catch (IOException e) {
                App.INSTANCE.exit(ExitCode.LAUNCHER_COM_FAILED);
            }
        }
    }
}
