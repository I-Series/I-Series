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

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Used to create and monitor an I-Series process.
 *
 * @author Luke
 */
public class AppProcess {

    static final Logger LOG = AppLogger.getLogger();

    /**
     * The created process object running the i-series jar.
     */
    private Process appProcess;

    /**
     * Name of the i-series jar file.
     */
    private String jarName;

    /**
     * The port number passed to the process as an argument.
     */
    private int port;

    /**
     * Starts the i-series process.
     *
     * @param jarName i-series jar file name.
     * @param portNumber the port number to pass in as a parameter.
     * @throws IOException if the process cannot be created.
     */
    public void start(String jarName, int portNumber) throws IOException {
        this.jarName = Objects.requireNonNull(jarName);
        this.port = portNumber;
        _start();
    }

    /**
     * Internal start method.
     *
     * @throws IOException
     */
    private void _start() throws IOException {
        String command = "\"" + getJavaHome() + "\"" //Java executable
                + " -jar " //Java command
                + "\"" + System.getProperty("user.dir")
                + File.separator // Jar file
                + jarName + "\""
                + " " + port //Port as argument
        ;

        LOG.info("Running I-Series with arguments: " + command);

        appProcess = Runtime.getRuntime().exec(command);
        LOG.info("Process started");

        Thread t = new Thread(() -> {
            String line = null;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(appProcess.getInputStream()));

            try {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                LOG.warn("Failed to get output from process", e);
            }

        }, "Process output printer");
        t.setDaemon(true);
        t.start();

        try {
            int code = appProcess.waitFor();
            LOG.info("Process finished with exit code: " + code);
        } catch (InterruptedException e) {
            LOG.warn("waitFor() failed", e);
        }
    }

    /**
     * @return the absolute path to the jvm executable obtained
     * from the java environment variable.
     */
    private String getJavaHome(){
        if(System.getProperty("java.home") == null) {
            JOptionPane.showMessageDialog(null, "Couldn't get java home.");
            System.exit(1);
        }
        String sep = File.separator;
        return System.getProperty("java.home") + sep + "bin" + sep + "java.exe";
    }
}
