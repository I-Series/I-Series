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

import java.io.IOException;

/**
 * Starting point of the i-series application.
 *
 * @author Luke
 */
public class Main {

    /**
     * Sets up the application class
     * and calls the start method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("I-Series Main");
        App.INSTANCE = new App();
        App.INSTANCE.start(args);
    }

}
