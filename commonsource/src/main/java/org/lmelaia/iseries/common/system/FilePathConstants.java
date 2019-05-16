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

package org.lmelaia.iseries.common.system;

import java.io.File;

/**
 * Static class holding references to files/folders
 * used throughout the application.
 */
public class FilePathConstants {

    /**
     * The path to the application folder.
     */
    @SuppressWarnings("WeakerAccess")
    public static final File APPLICATION_PATH = new File(System.getProperty("user.dir"));

    /**
     * The path to the folder containing application and launcher logs.
     */
    public static final File APPLICATION_LOGS_PATH = new File(APPLICATION_PATH.getAbsolutePath() + "/logs/");

    /**
     * The path to the I-Series license.txt file.
     */
    public static final File APPLICATION_LICENSE_PATH = new File(APPLICATION_PATH + "/I-Series Licence.txt");

    /**
     * The path to the I-Series Acknowledgements.txt file.
     */
    public static final File APPLICATION_ACKNOWLEDGEMENTS_PATH
            = new File(APPLICATION_PATH + "/I-Series Acknowledgements.txt");

    /**
     * Private constructor.
     */
    private FilePathConstants() {
        throw new RuntimeException(FilePathConstants.class.getCanonicalName() + " cannot be instantiated.");
    }

}
