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

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Luke
 */
public class Main {

    static{
        AppLogger.silentConfigure("/configuration/log4j2_configuration.xml");
    }

    static Logger LOG = AppLogger.getLogger();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("Starting Launcher with arguments: " + Arrays.toString(args));
        JOptionPane.showMessageDialog(null, "Launcher executing.");
        System.out.println(Arrays.toString(new File(System.getProperty("user.dir")).listFiles()));
        System.out.println("Hello World! Launcher run successfully.");
    }

}
