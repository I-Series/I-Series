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
import org.lmelaia.iseries.common.net.xcom.MessageType;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

import javax.swing.*;
import java.io.IOException;
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
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Global instance of this class.
     */
    private static App INSTANCE;

    /**
     * The timer responsible for running the {@link App.PingClientTask}.
     */
    private final Timer pingClientTask = new Timer("Ping client", true);

    /**
     * The port on which the launchers
     * server was started on.
     */
    private int hostPort;

    /**
     * List of predefined named arguments.
     */
    public enum DefinedArguments {

        /**
         * The port argument.
         */
        PORT("port"),

        /**
         * Test argument.
         */
        @SuppressWarnings("unused")
        TEST("test");

        /**
         * The name of the argument.
         */
        public final String key;

        /**
         * Constructor.
         *
         * @param key the name of the argument.
         */
        DefinedArguments(String key) {
            this.key = key;
        }
    }

    /**
     * Constructor.
     */
    App() {
        this.manageThread(Thread.currentThread());
        INSTANCE = this;
    }

    /**
     * @return the instance of this application.
     */
    @SuppressWarnings("unused")
    public static App getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes communication with the launcher.
     *
     * @param portArg the port number given through the
     *                application arguments.
     */
    private void initLauncherCom(String portArg) {
        hostPort = Integer.parseInt(portArg);

        getServer().start("I-Series");
        getServer().writePortNumber();

        pingClientTask.schedule(new App.PingClientTask(), 0,
                Settings.LAUNCHER_PING_FREQUENCY.getValueAsInt());
    }

    /**
     * Opens a normal JFrame for debugging.
     */
    private void openDebugWindow() {
        JFrame frame = new JFrame("Debug window");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(100, 100);
        frame.setVisible(true);
    }

    /**
     * Registers a message listener
     * to receive new arguments from connecting
     * clients and update this applications
     * argument handler instance.
     */
    private void registerArgumentReceiver() {
        getServer().addMessageListener(m -> {
            if (m.getMsgType().equals(MessageType.ARGS)) {
                update(m.getArgs().substring(1, m.getArgs().length() - 1).split(", "));
            }
        });
    }

    /**
     * Initializes and starts the application.
     */
    @Override
    protected void start() throws Exception {
        initLauncherCom(getArgumentHandler().getNamedArgument(DefinedArguments.PORT.key));
        registerArgumentReceiver();
        openDebugWindow();
    }

    @Override
    protected boolean initializeFX() {
        return false;
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
                if (!getClient().pingServer(hostPort, 2000)) {
                    LOG.fatal("Client not responding.");
                    App.INSTANCE.exit(ExitCode.UNRESPONSIVE_LAUNCHER);
                }
            } catch (IOException e) {
                App.INSTANCE.exit(ExitCode.LAUNCHER_COM_FAILED);
            }
        }
    }
}
