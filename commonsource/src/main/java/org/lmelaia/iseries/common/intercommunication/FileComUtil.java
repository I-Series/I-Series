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

package org.lmelaia.iseries.common.intercommunication;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.*;

/**
 * Provides a way for the main application
 * and launcher to pass a port number
 * between them through a file.
 */
public class FileComUtil {

    /**
     * This file will contain the last known port the
     * application was opened on or an application
     * closed flag.
     */
    private static final File PORT_FILE = new File("savedata/port.int");

    //Logging instance
    static Logger LOG = AppLogger.getLogger();

    /**
     * Attempt to create file.
     */
    static {
        try {
            PORT_FILE.createNewFile();
        } catch (IOException e) {
            LOG.warn("Failed to create port file.", e);
        }
    }

    /**
     * Writes the given port number to the port file.
     *
     * @param number port number.
     * @return false if the process failed.
     * <b>True does not always mean the process succeeded<b/>
     */
    public static boolean writePortNumber(int number) {
        try {
            FileWriter writer = new FileWriter(PORT_FILE);
            writer.write(String.valueOf(number));
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Attempts to get the last port number written to the
     * port file.
     *
     * @return the last written port number or {@code -1} if
     * a port number couldn't be found.
     */
    public static int getLastKnownPort() {
        int port = -1;
        String content = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PORT_FILE));
            content = reader.readLine();
            port = Integer.parseInt(content);//Tries to turn string into port number.
            //If it fails the port number will be unchanged (threw an error).
            reader.close();
        } catch (FileNotFoundException e) {
            LOG.error("Port file not found when trying to read.", e);
            //NO-OP
        } catch (IOException e) {
            LOG.error("Couldn't read port file.", e);
            //NO-OP
        } catch (NumberFormatException e) {
            LOG.warn("Port file content not in expected format (content = " + content + ")", e);
            //NO-OP
        }

        if (port == -1)
            LOG.warn("No last known port. New instance will probably be started.");

        return port;
    }

}
