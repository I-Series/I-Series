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
package org.lmelaia.iseries.buildtest.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility object for tests that require files.
 * <br><br>
 * This class provides utilities to create a list of test files and delete them.
 * Content can be written to the files as well.
 *
 * @author Luke Melaia
 */
public class FileTestingUtils {

    /**
     * Default path.
     */
    public static final String PATH_BUILD_TEST_FILES
            = System.getProperty("user.dir") + "\\tests\\";

    /**
     * The files.
     */
    private File files[] = null;

    /**
     * The content of each file.
     */
    private String[] contents;

    /**
     * See {@link #FileUtils(java.lang.String[][])) for full documentation.
     *
     * @param path the home directory of the test files.
     * @param files
     */
    public FileTestingUtils(String path, String[][] files) {
        new File(path).mkdirs();
        this.files = getFiles(Objects.requireNonNull(files), path);
        this.contents = getContent(files);
    }

    /**
     * Creates a new FileUtils object with a default home directory of {@link
     * #PATH_BUILD_TEST_FILES}, and a list of files and there content.
     *
     * @param files an array of arrays, each containing a file name as their
     * first element, and there content as the second element. The second
     * element can be null if the file doesn't need content.
     */
    public FileTestingUtils(String[][] files) {
        this(PATH_BUILD_TEST_FILES, files);
    }

    /**
     * Creates and writes to all the files.
     *
     * @throws IOException if the file cannot be created or written to.
     */
    public void setup() throws IOException {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String content = contents[i];

            file.createNewFile();

            if (content != null) {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(file))) {
                    writer.write(content);
                }
            }
        }
    }

    /**
     * Deletes all the files that were created by this class. Any other files
     * will not be deleted.
     *
     * @return true if every file was deleted.
     */
    public boolean teardown() {
        boolean success = true;

        for (File file : files) {
            if (!file.delete()) {
                success = false;
            }
        }

        return success;
    }

    /**
     * Gets the content out of the multidimensional array.
     *
     * @param names
     * @return
     */
    private String[] getContent(String[][] names) {
        List<String> ret = new ArrayList<>();

        for (String[] element : names) {
            if (element[1] != null && element[1].equals("")) {
                element[1] = null;
            }
            ret.add(element[1]);
        }

        return ret.toArray(new String[ret.size()]);
    }

    /**
     * Gets the file names out the multidimensional array and turns them into
     * valid file objects.
     *
     * @param names
     * @param path the home directory of the files.
     * @return
     */
    private File[] getFiles(String[][] names, String path) {
        List<File> ret = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            String[] element = names[i];
            if(element.length == 1){
                element = new String[]{element[0], null};
            }
            
            if (element.length != 2) {
                throw new IllegalArgumentException("Each array must have "
                        + "one or two elements.");
            }

            if (element[0] == null || element[0].isEmpty()) {
                throw new IllegalArgumentException(
                        "The first element in each array must "
                        + "not be null or empty.");
            }

            names[i] = element;
            ret.add(new File(path + element[0]));
        }

        return ret.toArray(new File[ret.size()]);
    }
}
