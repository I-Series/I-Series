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

import java.util.Arrays;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;

/**
 * Starting point of the i-series application.
 * 
 * @author Luke
 */
public class Main {

    /**
     * Pre-start initialization.
     */
    static{
        Thread.currentThread().setName("I-Series Main");
        AppLogger.silentConfigure("/configuration/log4j.xml");
    }

    private static final Logger LOG = AppLogger.getLogger();
    
    /**
     * Entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("Starting with arguments: " + Arrays.toString(args));
    }
}
