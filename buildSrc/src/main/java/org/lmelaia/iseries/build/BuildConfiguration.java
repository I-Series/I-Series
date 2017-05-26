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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.build.launch4j.Launch4jConfiguration;
import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;
import org.lmelaia.iseries.build.launch4j.Launch4jProcessWrapper;
import org.lmelaia.iseries.build.library.Library;
import org.lmelaia.iseries.build.library.LibraryManager;
import org.lmelaia.iseries.build.licence.Licences;
import org.lmelaia.iseries.build.packaging.ZipPackager;
import org.lmelaia.iseries.build.utils.CopyFile;
import org.lmelaia.iseries.build.utils.OutputCopyFile;

/**
 * Holds the configuration settings for the build script along with methods to
 * do a full build of the root project.
 *
 * @author Luke Melaia
 */
public class BuildConfiguration {

    private static final Logger LOG = BuildLogger.getLogger();

    //******************************
    //    CONFIGURATION SETTINGS
    //******************************
    
    //******************************
    //   NB FILES AND FILE PATHS
    //******************************
    
    /**
     * The string representation of the path to the I-Series project folder.
     */
    public static final String PROJECT_PATH
            = "C:/Users/Luke/Projects/Java/I-Series/";
    //Change this to the path on your machine.

    /**
     * The I-Series project folder.
     */
    public static final File PROJECT_FOLDER = new File(PROJECT_PATH);

    //******************************
    //       BUILD PROPERTIES
    //******************************
    
    /**
     * Path to the build properties file.
     */
    private static final String BUILD_PROPERTIES_PATH
            = PROJECT_FOLDER + "/build.cfg";

    /**
     * The build properties file.
     */
    private static final File BUILD_PROPERTIES_FILE
            = new File(BUILD_PROPERTIES_PATH);

    /**
     * Build properties object. Holds the properties.
     */
    private static final Properties BUILD_PROPERTIES = new Properties();

    /**
     * The build version property name.
     */
    private static final String BUILD_VERSION_CID = "build.version";

    /**
     * Reads the properties from file and puts them in the properties object.
     */
    static {
        readBuildProperties();
    }

    //******************************
    //             NAMES
    //******************************
    
    /**
     * The name of the application.
     */
    private static final String APPLICATION_NAME = "I-Series";

    /**
     * The name of the application with a '.exe' extension. Used as the launch4j
     * executable file name.
     */
    private static final String EXECUTABLE_NAME = APPLICATION_NAME + ".exe";

    //******************************
    //     FILES AND FILE PATHS
    //******************************
    
    /**
     * The string representation of the path to the I-Series jar file.
     */
    public static final String JAR_PATH
            = PROJECT_PATH
            + "build/libs/I-Series.jar";

    /**
     * The I-Series jar file.
     */
    public static final File JAR_FILE = new File(JAR_PATH);

    /**
     * The string representation of the path to where the build output will be
     * written.
     */
    public static final String OUTPUT_PATH = PROJECT_PATH + "buildoutput/";

    /**
     * The final build output folder.
     */
    public static final File OUTPUT_FOLDER = new File(OUTPUT_PATH);

    /**
     * The string representation of the path to the launch4j application folder.
     */
    //Change this to the path of launch4j on your machine.
    public static final String LAUNCH4J_PATH
            = "C:/Program Files (x86)/Launch4j";

    /**
     * The launch4j folder which contains the launch4j binaries and executables.
     */
    public static final File LAUNCH4J_FOLDER = new File(LAUNCH4J_PATH);

    /**
     * The path to the folder containing the licence for the project and
     * libraries.
     */
    public static final String LICENCES_PATH
            = PROJECT_PATH + "licences/";

    /**
     * The folder containing the licences files.
     */
    public static final File LICENCES_FOLDER = new File(LICENCES_PATH);

    /**
     * The path to the folder containing the library jar files.
     */
    public static final String LIBRARIES_PATH
            = PROJECT_PATH + "build/libs/libs/";

    /**
     * The folder containing the library jar files.
     */
    public static final File LIBRARIES_FOLDER = new File(LIBRARIES_PATH);

    /**
     * The path to the legal folder inside the output folder. This folder
     * contains the licences for the libraries.
     */
    public static final String LEGAL_PATH = OUTPUT_PATH + "legal/";

    /**
     * The folder containing the licences for the libraries.
     */
    public static final File LEGAL_FOLDER = new File(LEGAL_PATH);

    /**
     * The path to the distribution folder.
     */
    public static final String DISTRIBUTION_PATH
            = PROJECT_PATH + "distribution/";

    /**
     * The distribution folder. Contains the files which will be distributed to
     * the user.
     */
    public static final File DISTRIBUTION_FOLDER = new File(DISTRIBUTION_PATH);

    /**
     * The path to the folder containing the copied libraries.
     */
    public static final String OUT_LIBRARIES_PATH = OUTPUT_PATH + "libs/";

    /**
     * The folder containing the copied libraries.
     */
    public static final File OUT_LIBRARIES_FOLDER
            = new File(OUT_LIBRARIES_PATH);

    /**
     * The path to the windows distribution zip file
     */
    public static final String WINDOWS_ZIP_PATH = DISTRIBUTION_PATH
            + APPLICATION_NAME
            + " v" + BUILD_PROPERTIES.getProperty(BUILD_VERSION_CID)
            + " Windows.zip";

    /**
     * The windows distribution file.
     */
    public static final File WINDOWS_ZIP_FILE = new File(WINDOWS_ZIP_PATH);

    /**
     * The path to the cross-platform distribution zip file.
     */
    public static final String CROSSPLATFORM_ZIP_PATH = DISTRIBUTION_PATH
            + APPLICATION_NAME
            + " v" + BUILD_PROPERTIES.getProperty(BUILD_VERSION_CID)
            + " Cross-platform.zip";

    /**
     * The cross-platform distribution zip file.
     */
    public static final File CROSSPLATFORM_ZIP_FILE
            = new File(CROSSPLATFORM_ZIP_PATH);
    //******************************
    //        CONFIGURATION
    //******************************
    
    /**
     * The configuration settings to build the I-Series executable.
     */
    private static final Launch4jConfiguration EXEC_CONFIGURATION
            = new Launch4jConfigurationBuilder()
                    .setJarRuntimePath("I-Series.jar")
                    .setWrap(false)
                    .setOutputFile(OUTPUT_PATH + EXECUTABLE_NAME)
                    .setMinimumJreVersion("1.8.0_111")
                    .create();

    /**
     * Contains a list of the libraries for the application and handles the
     * copies of them and their licences.
     */
    private static final LibraryManager LIBRARY_MANAGER = new LibraryManager(
            new File(PROJECT_PATH + "build/libs/libs"),
            new File(OUTPUT_PATH + "libs/"),
            new File(OUTPUT_PATH + "legal/"));

    //******************************
    //             LISTS
    //******************************
    
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

    /**
     * A list of the libraries for the root project.
     */
    private static final Library[] LIBRARIES = {
        new Library("Gson", "gson-2.8.0", Licences.APACHE)
    };

    /**
     * A list of directories required to exist before beginning a full build.
     */
    private static final File[] REQUIRED_DIRECTORIES = {
        OUTPUT_FOLDER, LEGAL_FOLDER, OUT_LIBRARIES_FOLDER, DISTRIBUTION_FOLDER
    };

    //*******************
    //      METHODS
    //*******************
    
    /**
     * Builds the executable file for the I-Series jar file.
     */
    private static void buildISeriesExecutable() {
        LOG.debug("Creating executable");
        createExecutable(EXEC_CONFIGURATION);
    }

    /**
     * Adds the list of libraries ({@link #LIBRARIES) to the library manager.
     */
    private static void addLibrariesToList() {
        for (Library library : LIBRARIES) {
            try {
                LIBRARY_MANAGER.addLibrary(library);
            } catch (FileNotFoundException ex) {
                LOG.error("Failed to add library: " + library.getName(), ex);
            }
        }
    }

    /**
     * Creates the directories required to do a full build.
     */
    private static void createRequiredDirectories() {
        for (File f : REQUIRED_DIRECTORIES) {
            forceCleanMake(f);
        }
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories if the directory is nonexistent, else the directory is
     * cleaned.
     *
     * @param f the directory
     * @return {@code true} if and only if the directory was created or cleaned,
     * {@code false} otherwise.
     */
    private static boolean forceCleanMake(File f) {
        try {
            if (f.exists()) {
                FileUtils.cleanDirectory(f);
            } else {
                FileUtils.forceMkdir(f);
            }
        } catch (IOException ex) {
            LOG.warn("Failed to force a clean make of the directory: " + f,
                    ex);
            return false;
        }

        return true;
    }

    /**
     * Uses the library manager to copy the libraries and library licences.
     */
    private static void copyLibraries() {
        LOG.debug("Copying libraries and library licences");
        try {
            LIBRARY_MANAGER.copyOver();
        } catch (IOException ex) {
            LOG.error("Failed to copy over libraries", ex);
        }
    }

    /**
     * Copies over a list of files ({@link #FILES_TO_COPY}) to the output folder
     * ({@link #OUTPUT_PATH}). These files include things such as the jar file,
     * licences and so on.
     */
    private static void copyFilesOver() {
        for (CopyFile file : FILES_TO_COPY) {
            LOG.debug("Copying file: " + file + " to output directory.");
            try {
                file.copy();
            } catch (IOException ex) {
                LOG.error("Failed to copy file", ex);
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

        LOG.debug("Starting launch4j process");

        try {
            launch4jProcess.startProcess(output);
        } catch (IOException ex) {
            LOG.error("Launch4j process failed", ex);
        }

        if (!output.toString().equals("launch4j: Compiling resources")) {
            LOG.error("Launch4j produced unexpected output: " 
                    + (output.toString().equals("") 
                            ? "No output" : output.toString()) );
        }
    }

    /**
     * Zips the projects build output into a windows and a cross-platform zip
     * file.
     *
     * @throws IOException
     */
    private static void zipProject() throws IOException {
        //Create a copy of the output folder without
        //windows executables for packaging
        //into the cross-platform zip.
        File nwof = new File(OUTPUT_PATH.substring(0, OUTPUT_PATH.length() - 1)
                + "nw");
        FileUtils.copyDirectory(OUTPUT_FOLDER, nwof);
        new File(nwof.getCanonicalPath() + "/" + APPLICATION_NAME + ".exe")
                .delete();

        //Zip files
        ZipPackager windows = new ZipPackager(WINDOWS_ZIP_FILE, OUTPUT_FOLDER);
        windows.create();

        ZipPackager crossPlatform = new ZipPackager(
                CROSSPLATFORM_ZIP_FILE, nwof);
        crossPlatform.create();

        //Delete output copy
        FileUtils.deleteDirectory(nwof);
    }

    /**
     * Reads the build properties from file and writes them to the properties
     * object.
     */
    private static void readBuildProperties() {
        LOG.debug("Reading build config from file: " + BUILD_PROPERTIES_PATH);
        try (FileInputStream inputStream
                = new FileInputStream(BUILD_PROPERTIES_FILE)) {
            BUILD_PROPERTIES.load(inputStream);
        } catch (IOException iox) {
            LOG.error("Failed to load build config", iox);
        }
    }

    /**
     * Writes the build properties from object to file.
     *
     * @throws IOException
     */
    private static void saveProperties() {
        LOG.debug("Saving build config to file: " + BUILD_PROPERTIES_PATH);
        try (FileWriter writer = new FileWriter(BUILD_PROPERTIES_FILE)) {
            BUILD_PROPERTIES.store(writer,
                    "Contains the configuration settings"
                    + " for the build (e.g. version number)");
        } catch (IOException ex) {
            LOG.error("Failed to save build config", ex);
        }
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
     *
     * @throws java.lang.Exception
     */
    public static void fullBuild() throws Exception {
        createRequiredDirectories();
        copyFilesOver();
        addLibrariesToList();
        copyLibraries();
        buildISeriesExecutable();
        //Give windows explorer time to refresh
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        zipProject();
        saveProperties();
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
        LOG.info("Beginning full build...");
        
        try {
            fullBuild();
        } catch (Exception ex) {
            LOG.error("Full build failed", ex);
        }
    }
}
