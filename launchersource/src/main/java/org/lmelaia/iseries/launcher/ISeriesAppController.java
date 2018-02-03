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
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.common.system.ShutdownListener;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handles the starting of the main application,
 * it's output and termination.
 * <p>
 * <p><b>
 * NB: The termination of the main application
 * causes the launcher to begin termination
 * as well, with the exit code of the main
 * application.
 * <p>
 * <p>
 * Use {@link org.lmelaia.iseries.common.system.AppBase#addShutdownListener(ShutdownListener)}
 * to register a listener for the shutdown of the main application as well the launcher.
 * </p>
 * </b></p>
 */
public class ISeriesAppController {

    /**
     * Logging object.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The main applications jar filename.
     */
    private static final String JAR_FILENAME
            = "I-Series-Base.jar";

    /**
     * Thread responsible for capturing the output
     * of the main application and printing it
     * to the console for development
     * purposes.
     */
    private static ProcessOutputCaptureThread captureThread;

    /**
     * Thread responsible for handling the termination
     * of the main application.
     */
    private static ProcessTerminationHandler waitThread;

    /**
     * Process object linked to the main application.
     */
    private static Process applicationProcess;

    /**
     * Private constructor.
     */
    private ISeriesAppController() {
    }

    /**
     * Starts the main application process and related threads.
     *
     * @param args arguments to pass to the main application
     *             (port is passed automatically).
     * @throws IOException
     */
    public static void start(String[] args) throws IOException {
        if (isRunning())
            throw new IllegalStateException("Process is already running");

        String cmd = formatCommandLineArgs(args);

        LOG.info("Running new I-Series instance with command: " + cmd);
        applicationProcess = Runtime.getRuntime().exec(cmd);
        startNewCaptureThread();
        LOG.trace("Process started");
        startTerminationHandlerThread();
    }

    /**
     * @return if the main application process is running.
     */
    public static boolean isRunning() {
        if (applicationProcess == null)
            return false;

        return applicationProcess.isAlive();
    }

    /**
     * Stops the related threads and calls for termination.
     *
     * @param code the exit code to terminate with.
     */
    private static void stop(int code) {
        captureThread.stop();
        App.getInstance().exit(ExitCode.getFromCode(code).setRecoverable(true));
    }

    /**
     * Produces the command used to start the main
     * application with the given arguments.
     *
     * @param args the given arguments.
     * @return the command produced.
     */
    private static String formatCommandLineArgs(String[] args) {
        String command = "\"" + getJavaExecutablePath() + "\""
                + " -jar "
                + "\"" + System.getProperty("user.dir")
                + File.separator
                + JAR_FILENAME + "\" --port=" + App.getInstance().getServerPort();

        if (args != null)
            for (String arg : args) {
                command += " \"" + arg + "\"";
            }

        return command;
    }

    /**
     * @return the path to the java executable.
     */
    private static String getJavaExecutablePath() {
        if (System.getProperty("java.home") == null) {
            JOptionPane.showMessageDialog(null, "Couldn't get java home.");
            System.exit(1);
        }
        String sep = File.separator;
        return System.getProperty("java.home") + sep + "bin" + sep + "java.exe";
    }

    /**
     * Creates a new instances of the capture thread
     * and starts it.
     */
    private static void startNewCaptureThread() {
        if (captureThread != null)
            if (captureThread.isAlive())
                captureThread.stop();

        captureThread = new ProcessOutputCaptureThread();
        captureThread.start();
    }

    /**
     * Creates a new instances of the termination thread
     * and starts it.
     */
    private static void startTerminationHandlerThread() {
        if (waitThread != null)
            if (waitThread.isAlive())
                waitThread.stop();

        waitThread = new ProcessTerminationHandler();
        waitThread.start();
    }

    /**
     * Thread responsible for capturing the main application
     * output and printing it to the console for development
     * purposes.
     */
    private static class ProcessOutputCaptureThread extends Thread {

        /**
         * Sets the name of the thread and ensures it is a deamon
         * thread.
         */
        public ProcessOutputCaptureThread() {
            this.setName("Process output capture thread");
            this.setDaemon(true);
        }

        /**
         * Captures and prints the output.
         */
        @Override
        public void run() {
            String line;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(applicationProcess.getInputStream()));

            try {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                LOG.warn("Failed to get output from process", e);
            }
        }
    }

    /**
     * Thread responsible for handling the termination
     * of the main application.
     */
    private static class ProcessTerminationHandler extends Thread {

        /**
         * Sets the name of the thread and ensures it is a deamon
         * thread.
         */
        public ProcessTerminationHandler() {
            this.setName("Process wait thread");
            this.setDaemon(true);
        }

        /**
         * Waits for the application to terminate
         * and calls for termination of the launcher
         * with the same exit code.
         */
        @Override
        public void run() {
            try {
                int code = applicationProcess.waitFor();
                LOG.info("Process finished with exit code: " + code);
                ISeriesAppController.stop(code);
            } catch (InterruptedException e) {
                LOG.warn("waitFor() failed", e);
            }
        }
    }
}
