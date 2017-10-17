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
import java.util.Arrays;
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
     * A list of the test files.
     */
    private List<TestFile> files = new ArrayList<>();

    /**
     * See {@link #FileTestingUtils(java.lang.String[][])) for full documentation.
     *
     * @param path the home directory of the test files.
     * @param files
     */
    public FileTestingUtils(String path, String[]... files) {
        new File(path).mkdirs();
        this.files.clear();

        for (String[] fileProps : files) {
            TestFile testFile = new TestFile(fileProps[0]).initReference(path);
            switch (fileProps.length) {
                case 2:
                    testFile.setContent(fileProps[1]);
                    break;
                case 3:
                    testFile.setContent(fileProps[1]).setID(fileProps[2]);
                    break;
            }
            this.files.add(testFile);
        }
    }

    /**
     * Creates a new FileTestingUtils object with a default home directory of
     * {@link #PATH_BUILD_TEST_FILES}, and a list of files and there content.
     *
     * @param files An array where each element is another array which specifies
     * the the properties of a test file. Within each sub-array should be 1 to 3
     * Strings - the first is the name of the file (only the name and extension,
     * not the path) which is required, the second is the content which will be
     * written to the file (this isn't required and can be null), the final
     * String is the files ID - this is used to get a reference to the file
     * easily, this isn't required and cannot be null. If an ID is not
     * specified, the ID is set to the files name (i.e. the first element).
     */
    public FileTestingUtils(String[]... files) {
        this(PATH_BUILD_TEST_FILES, files);
    }

    /**
     * See {@link #FileTestingUtils(java.lang.String[][])} for full
     * documentation.
     *
     * @param path the home directory of the test files
     * @param testFiles a list of test files.
     */
    public FileTestingUtils(String path, TestFile... testFiles) {
        new File(path).mkdirs();

        files.clear();
        files.addAll(Arrays.asList(testFiles));
        files.forEach(file -> file.initReference(path));
    }

    /**
     * Creates a new FileTestingUtils object with a list of test files which can
     * be created and deleted with a method call.
     *
     * @param testFiles a list of test files
     */
    public FileTestingUtils(TestFile... testFiles) {
        this(PATH_BUILD_TEST_FILES, testFiles);
    }

    /**
     * Creates and writes to all the files.
     *
     * @throws IOException if the file cannot be created or written to.
     */
    public void setup() throws IOException {
        for (TestFile testFile : files) {
            File ref = testFile.reference;
            String content = testFile.content;

            if (!ref.createNewFile()) {
                continue;
            }

            if (content == null) {
                continue;
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(ref))) {
                writer.write(content);
                writer.close();
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

        for (TestFile tFile : files) {
            if (!tFile.reference.delete()) {
                success = false;
            }
        }

        return success;
    }

    /**
     * Returns a file object with
     *
     * @param id the ID of a test file.
     * @return the file reference of a test file with the ID passed as an
     * argument or {@code null} if no test file exists with the ID passed in.
     */
    public File getFileByID(String id) {
        for (TestFile file : files) {
            if (file.id.toLowerCase().equals(id.toLowerCase())) {
                return file.reference;
            }
        }

        return null;
    }

    /**
     * An instance of this class would represent a single file used by a unit
     * test(s) or class of unit tests.
     *
     * <p>
     * The file will be created when it's needed and deleted once the unit tests
     * which require it have been run.</p>
     */
    public static class TestFile {

        /**
         * The name of the file. This is just the file name and its extension,
         * the path is not included in the name.
         */
        private final String name;

        /**
         * The content of the file. Can be null
         */
        private String content;

        /**
         * The files ID. Can be null
         */
        private String id;

        /**
         * The actual file this object points to.
         */
        private File reference;

        /**
         * Constructs a new test file instance with the specified name.
         *
         * @param name the name of the file not including the path to it (e.g.
         * TestFile.txt)
         */
        public TestFile(String name) {
            this.id = Objects.requireNonNull(name);
            this.name = id;
        }

        /**
         * Sets the content of the file. This isn't required.
         *
         * @param content the content of the file.
         * @return a reference to this object.
         */
        public TestFile setContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets the ID of the file. This isn't required.
         *
         * <p>
         * <b>
         * If an ID is not specified, the ID of the file will become its name
         * (including the extension but not including the path).
         * </b></p>
         *
         * @param id the files ID.
         * @return a reference to this object.
         */
        public TestFile setID(String id) {
            this.id = Objects.requireNonNull(id);
            return this;
        }

        /**
         * Initializes the file reference in this class using the path provided.
         *
         * @param path the path to the folder which will contain the test files.
         * @return a reference to this object.
         */
        private TestFile initReference(String path) {
            this.reference = new File(Objects.requireNonNull(path) + name);
            return this;
        }
    }
}
