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
package org.lmelaia.iseries.build;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.lmelaia.iseries.build.launch4j.Launch4jConfiguration;
import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;
import org.lmelaia.iseries.build.launch4j.Launch4jProcessWrapper;
import static org.apache.commons.io.FileUtils.*;
import org.lmelaia.iseries.build.licence.Licences;
import org.lmelaia.iseries.build.utils.CopyFile;
import org.lmelaia.iseries.build.utils.OutputCopyFile;

/**
 * Holds the configuration settings for the build script along with methods to
 * do a full build of the root project.
 *
 * @author Luke Melaia
 */
public class BuildConfiguration {

    //******************************
    //    CONFIGURATION SETTINGS
    //******************************
    /**
     * The string representation of the path to the I-Series project folder.
     */
    public static final String PROJECT_PATH
            = "C:/Programming/Languages/Java/Projects/I-Series/";

    /**
     * The string representation of the path to the I-Series jar file.
     */
    public static final String JAR_PATH
            = PROJECT_PATH
            + "build/libs/I-Series.jar";

    /**
     * The string representation of the path to where the build output will be
     * written.
     */
    public static final String OUTPUT_PATH = PROJECT_PATH + "output/";

    /**
     * The string representation of the path to the launch4j application folder.
     */
    //Change this to the path of launch4j on your machine.
    public static final String LAUNCH4J_PATH
            = "C:/Program Files (x86)/Launch4j";

    /**
     * The path to the folder containing the licence for the project and
     * libraries.
     */
    public static final String LICENCES_PATH
            = PROJECT_PATH + "licences/";

    /**
     * The name of the application.
     */
    private static final String APPLICATION_NAME = "I-Series";

    /**
     * The name of the application with a '.exe' extension. Used as the launch4j
     * executable file name.
     */
    private static final String EXECUTABLE_NAME = APPLICATION_NAME + ".exe";

    //Needs to be here or the FILES_TO_COPY array definition throws an
    //exception.
    static {
        try {
            FileUtils.forceMkdir(new File(OUTPUT_PATH));
        } catch (IOException ex) {
            System.err.println("Failed to create output directory: \n" + ex);
        }
    }
    
    /**
     * The configuration settings to build the I-Series executable.
     */
    private static Launch4jConfiguration executableConfiguration
            = new Launch4jConfigurationBuilder()
            .setJarFile(JAR_PATH)
            .setOutputFile(OUTPUT_PATH + EXECUTABLE_NAME)
            .setMinimumJreVersion("1.8.0_111")
            .create();

    /**
     * A list of files which need to be copied over to the output folder
     * ({@link #OUTPUT_PATH}).
     */
    private static final CopyFile[] FILES_TO_COPY = {
        //Jar file
        new OutputCopyFile(new File(PROJECT_PATH + "build/libs/I-Series.jar")),
        //I-Series licence
        new OutputCopyFile(Licences.GNU.getFile(),
                APPLICATION_NAME + " Licence.txt")
    };

    //*******************
    //      METHODS
    //*******************
    /**
     * This method is considered an alternative to
     * {@link #main(java.lang.String[])}, and is called ONLY when the user whats
     * to do a full build of the root project.
     *
     * <p>
     * It is guaranteed that a build of the root project and all subprojects is
     * done before this method is called.
     *
     * <p>
     * This method begins a full build of the root project. I.E. creates the
     * executable files, installers and so on.
     *
     * <p>
     * See the task fullBuild in the root projects build.gradle file for more
     * information.
     */
    public static void fullBuild() throws Exception {
        cleanDirectory(new File(OUTPUT_PATH));
        buildISeriesExecutable();
        copyFilesOver();
    }

    /**
     * The main method according to build source.
     *
     * <p>
     * This method is called from the final task when running the root project.
     * As such, it's useless for building executables and what not as it's never
     * called when simply building the root project, and it's called every time
     * when running the root project.
     *
     * <p>
     * {@link #fullBuild()} can be considered an alternative to this method.
     *
     * @param args
     *
     * @deprecated Use {@link #fullBuild() }. See the task fullBuild in the root
     * projects build.gradle file for more information.
     */
    @Deprecated
    public static void main(String[] args) {
        //NO-OP
    }

    /**
     * Builds the executable file for the I-Series jar file.
     */
    private static void buildISeriesExecutable() {
        System.out.println("Creating I-Series executable");
        createExecutable(executableConfiguration);
    }
    
    /**
     * Copies over a list of files ({@link #FILES_TO_COPY}) to the output
     * folder ({@link #OUTPUT_PATH}). These files include things such
     * as the jar file, licences and so on.
     */
    private static void copyFilesOver(){
        for(CopyFile file : FILES_TO_COPY){
            System.out.println("Copying over file: " + file.toString());
            try {
                file.copy();
            } catch (IOException ex) {
                System.err.println("Failed to copy file: " + file.toString()
                        + "\n" + ex);
            }
        }
    }

    /**
     * Creates an executable file with launch4j.
     *
     * @param configuration the configuration settings for the executable file.
     */
    private static void createExecutable(Launch4jConfiguration configuration) {
        Launch4jProcessWrapper launch4jProcess = new Launch4jProcessWrapper(
                new File(LAUNCH4J_PATH), configuration);
        StringBuilder output = new StringBuilder();

        System.out.println("Starting launch4j process");

        try {
            launch4jProcess.startProcess(output);
        } catch (IOException ex) {
            System.err.println("The launch4j process failed: " + ex);
            System.err.println(
                    Arrays.toString(ex.getStackTrace()).replaceAll(",", ",\n"));
        }

        System.out.println("Launch4j output: " + (output.toString().equals("")
                ? "No output" : output.toString()));
    }

    /**
     * Copies over a file to the output directory (Specified by
     * {@link #OUTPUT_PATH}). The new file will have the same name as the old.
     *
     * @param source the source file to copy.
     * @throws IOException if an IOException occurs during copying or if the
     * source file is a directory.
     */
    public static void copyFileToOutput(File source) throws IOException {
        copyFileToOutput(source, null);
    }

    /**
     * Copies over a file to the output directory (Specified by
     * {@link #OUTPUT_PATH}) under a new name.
     *
     * <p>
     * If the new file name is {@code null} or empty, the new file name will be
     * the same as the old. The new file can be placed in 1 or more sub
     * directories by putting a directory nameS followed by either {@code "\"}
     * or {@code "/"} before the file name (e.g. {@code "dir/filename.txt"} or
     * {@code "dir1/dir2/filename.txt"}).
     *
     * @param source the source file to copy.
     * @param newFileName the new name of the copied file.
     * @throws IOException if an IOException occurs during copying or if the
     * source file is a directory.
     */
    public static void copyFileToOutput(File source, String newFileName)
            throws IOException {
        File dest;

        if (Objects.requireNonNull(source).isDirectory()) {
            throw new IOException("Source file cannot be a directory");
        }

        if (newFileName == null || newFileName.equals("")) {
            dest = new File(OUTPUT_PATH + source.getName());
        } else {
            newFileName = newFileName.replace("\\", "/");
            dest = new File(OUTPUT_PATH + newFileName);
        }

        FileUtils.copyFile(source, dest);
    }
}
