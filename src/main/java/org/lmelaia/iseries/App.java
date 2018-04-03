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
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.main.MainWindow;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class for the application.
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
    public static App getInstance() {
        return INSTANCE;
    }

    /**
     * Writes the receiver port of the this instances
     * messenger object and starts the ping client
     * task.
     */
    private void postInitIPC() {
        hostPort = Integer.parseInt(getArgumentHandler().getNamedArgument(DefinedArguments.PORT.key));
        getMessenger().writeToPortFile();

        pingClientTask.schedule(new App.PingClientTask(), 0,
                Settings.LAUNCHER_PING_FREQUENCY.getValueAsInt());
    }

    /**
     * Registers a message listener
     * to receive new arguments from connecting
     * clients and update this applications
     * argument handler instance.
     */
    private void registerArgumentReceiver() {
        getMessenger().addMessageListener(message -> {
            if (message.get("name").getAsString().equals("argument_change")) {
                ArrayList<String> arguments = new ArrayList<>();
                message.get("arguments").getAsJsonArray().forEach(
                        element -> arguments.add(element.getAsString()));
                update(arguments.toArray(new String[arguments.size()]));
            }
        });
    }

    /**
     * Initializes and starts the application.
     */
    @Override
    protected void start() throws Exception {
        postInitIPC();
        registerArgumentReceiver();
        FXWindowsManager.getInstance().showWindow(MainWindow.class);
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
        return "I-Series";
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected String getFXWindowsClassPath() {
        return "org.lmelaia.iseries.fx";
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
            if (!getMessenger().ping(hostPort)) {
                LOG.fatal("Client not responding.");
                App.INSTANCE.exit(ExitCode.UNRESPONSIVE_LAUNCHER);
            }
        }
    }
}
