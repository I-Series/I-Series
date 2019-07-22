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

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.components.TextProgressBar;
import org.lmelaia.iseries.fx.library.LibraryWindow;
import org.lmelaia.iseries.fx.main.MainWindow;
import org.lmelaia.iseries.ilibrary.ILibrary;
import org.lmelaia.iseries.ilibrary.LibraryInitializedListener;
import org.lmelaia.iseries.ilibrary.NamedEntrySorter;
import org.lmelaia.iseries.library.Library;
import org.lmelaia.iseries.library.LibraryException;
import org.lmelaia.iseries.library.ProgressTracker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
     * List of listeners waiting to be notified as to when
     * the Library & ILibrary are initialized/loaded.
     */
    private final List<LibraryInitializedListener> libraryInitializedListeners = new ArrayList<>();

    /**
     * The timer responsible for running the {@link App.PingClientTask}.
     */
    private final Timer pingClientTask = new Timer("Ping client", true);

    /**
     * The global library instance.
     */
    private Library library;

    /**
     * The global ILibrary instance. Used
     * instead of {@link #library}.
     */
    private ILibrary iLibrary;

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
        super();
        this.library = new Library();

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
     * @return the global {@link ILibrary}
     * instance.
     */
    public ILibrary getILibrary() {
        return iLibrary;
    }

    /**
     * Adds a listener that will be notified when the Library
     * & ILibrary are initialized/loaded.
     *
     * @param listener the listener to register.
     */
    public void addLibraryInitListener(LibraryInitializedListener listener) {
        this.libraryInitializedListeners.add(listener);
    }

    /**
     * Removes a registered LibraryInitListener.
     *
     * @param listener the registered listener to unregister.
     */
    public void removeLibraryInitListener(LibraryInitializedListener listener) {
        this.libraryInitializedListeners.remove(listener);
    }

    /**
     * @return the global window manager.
     */
    public FXWindowsManager getWindowsManager() {
        return FXWindowsManager.getInstance();
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
     * launchers and update this applications
     * argument handler instance.
     */
    private void registerArgumentReceiver() {
        getMessenger().addMessageListener(message -> {
            if (message.get("name").getAsString().equals("argument_change")) {
                ArrayList<String> arguments = new ArrayList<>();
                message.get("arguments").getAsJsonArray().forEach(
                        element -> arguments.add(element.getAsString()));
                update(arguments.toArray(new String[0]));
            }
        });
    }

    /**
     * Task responsible for loading the Library/ILibrary
     * and updating the main windows progress bar to
     * reflect the progress of the library loading.
     */
    private final Task loadLibrary = new Task() {

        /**
         * Tracks the progress of the Library load and updates
         * the main window progress to reflect the load.
         */
        ProgressTracker tracker = new ProgressTracker() {
            @Override
            public void onProgressChange(double percentage, int pos, int max) {
                updateProgress(pos, max);
                updateMessage("Loading Library... " + pos + " of " + max);
            }

            @Override
            public void onCompletion() {
                updateProgress(1, 1);
                updateMessage("Library Loaded");
            }
        };

        /**
         * Loads the library from file and notifies
         * registered listeners.
         *
         * @return {@code null}
         */
        @Override
        protected Object call() throws Exception {
            //Actual Initialization.
            try {
                App.this.library.load(
                        new File(Settings.LIBRARY_PATH.getValue()), NamedEntrySorter.NAMED_ENTRY_SORTER,
                        tracker
                );
            } catch (LibraryException.LibraryFetchException e) {
                //TODO: Add dialogs to deal with this problem.
                throw e;
            } catch (LibraryException.LibraryCreationException e) {
                //TODO: Add dialogs to deal with this problem.
                throw e;
            }

            App.this.iLibrary = new ILibrary(library);

            for (LibraryInitializedListener listener : libraryInitializedListeners)
                listener.onInitialized(iLibrary);

            return null;
        }
    };

    /**
     * Loads the Library from file in a separate task and
     * initializes the ILibrary.
     */
    private void initLibrary() {
        //Get path from user if no path exists.
        String libraryLocation = Settings.LIBRARY_PATH.getValue();
        while (libraryLocation.equals("")) {
            Platform.runLater(() -> LibraryWindow.present(true));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TextProgressBar progressBar
                = getWindowsManager().getWindow(MainWindow.class).getController().getProgress().getProgressBar();

        progressBar.progressProperty().bind(loadLibrary.progressProperty());
        progressBar.textProperty().bind(loadLibrary.messageProperty());

        new Thread(loadLibrary).start();
    }

    /**
     * Initializes and starts the application.
     */
    @Override
    protected void start() {
        postInitIPC();
        registerArgumentReceiver();

        FXWindowsManager.getInstance().showWindow(MainWindow.class);

        initLibrary();
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
