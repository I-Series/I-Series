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
package org.lmelaia.iseries.build;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class to verify that the build task outputs the correct content.
 *
 * <li>
 * Checks the output directory file content against a list of expected files.
 * </li>
 * <li>
 * Logs file content, files not found, unexpected files, and empty files and
 * directories.
 * </li>
 * 
 * <p>
 * An object of this class is meant to scan a directory where build output is
 * written to and check that all expected files exist. The purpose of this
 * class is to catch build errors that go unreported by looking for files that
 * fail to get written. This helps quickly verify if the build was a success
 * or not.
 * </p>
 *
 * @author Luke
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BuildOutputVerifier {

    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The output directory to verify.
     */
    private final File outputDirectory;

    /**
     * The list of files which should be in this directory.
     */
    private final java.util.List<ExpectedFile> expectedFiles;

    /**
     * @see #setLogFilesNotFound(boolean)
     */
    private boolean logFilesNotFound = true;

    /**
     * @see #setLogExtraFiles(boolean)
     */
    private boolean logExtraFiles = false;

    /**
     * @see #setLogEmptyFiles(boolean)
     */
    private boolean logEmptyFiles = true;

    /**
     * A list of the file names found in the output directory.
     */
    private java.util.List<String> contentNames;

    /**
     * Constructs a new build output verifier.
     *
     * @param outputDirectory the directory to verify.
     * @param expectedFiles the list of files which should be in this directory.
     * This cannot be null and no entry in the list can be null.
     * @throws IllegalArgumentException a {@link FileNotFoundException} wrapped
     * in an {@link IllegalArgumentException} if the output directory doesn't
     * exist.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public BuildOutputVerifier(File outputDirectory,
                               ExpectedFile... expectedFiles)
            throws IllegalArgumentException {
        outputDirectory = Objects.requireNonNull(outputDirectory);

        for (ExpectedFile ef : Objects.requireNonNull(expectedFiles)) {
            if (ef == null) {
                throw new NullPointerException("Expected files array contains"
                        + " a null entry.");
            }
        }

        this.expectedFiles = Arrays.asList(expectedFiles);

        if (!outputDirectory.exists()) {
            try{
                outputDirectory.createNewFile();
            } catch (IOException ex){
                throw new IllegalArgumentException(new FileNotFoundException(
                    "Output directory does not exist"));
            }
        }

        this.outputDirectory = outputDirectory;
    }

    /**
     * If true, a file in the list of files to expect not found in the output
     * directory, will be logged as a warning.
     *
     * Default: {@code true}
     *
     * @param log logs files not found if true.
     * @return this object for convenience.
     */
    public BuildOutputVerifier setLogFilesNotFound(boolean log) {
        this.logFilesNotFound = log;
        return this;
    }

    /**
     * If true, a file in the output directory not contained in the list of
     * files to expect, will be logged as a warning.
     *
     * Default: {@code false}
     *
     * @param log logs extra files found if true.
     * @return this object for convenience.
     */
    public BuildOutputVerifier setLogExtraFiles(boolean log) {
        this.logExtraFiles = log;
        return this;
    }

    /**
     * If true, a file in the output directory with no content, will be logged
     * as a warning.
     *
     * Default: {@code true}
     *
     * @param log logs empty files found if true.
     * @return this object for convenience.
     */
    public BuildOutputVerifier setLogEmptyFiles(boolean log) {
        this.logEmptyFiles = log;
        return this;
    }

    /**
     * Begins verifying the file content.
     */
    @SuppressWarnings("ConstantConditions")
    public void verify() {
        LOG.info("Verifying output directory: " + outputDirectory);

        contentNames = new ArrayList<>();

        for (File f : outputDirectory.listFiles()) {
            if (logEmptyFiles && f.isFile()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    if (br.readLine() == null) {
                        LOG.warn("File: " + f + " is empty");
                    }
                } catch (IOException ex) {
                    LOG.error("Couldn't read file: " + f, ex);
                }
            }

            contentNames.add(f.getName());
        }

        if (logFilesNotFound) {
            expectedFiles.stream().filter((ef)
                    -> (!contentNames.contains(ef.name))).forEachOrdered((ef)
                    -> LOG.warn("File: (" + ef.name + ") is not contained in the"
                            + " output directory: " + outputDirectory.getName()));
        }

        if (logExtraFiles) {
            contentNames.stream().filter((f)
                    -> (!expectedFiles.contains(
                            new ExpectedFile(f)))).forEachOrdered((f) -> LOG.warn("File: (" + f + ") is not contained"
                                + " in the list"
                                + " of expected files, but it is in the output"
                                + " directory: " + outputDirectory.getName()));
        }

    }

    /**
     * Represents a file that should be in the output directory.
     */
    public static class ExpectedFile {

        /**
         * The name of this file/directory.
         */
        private final String name;

        /**
         * Constructs a new expected file.
         *
         * @param name the name of the file.
         */
        public ExpectedFile(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public boolean equals(Object e) {
            return e instanceof ExpectedFile && name.equals(((ExpectedFile) e).name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
