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

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.build.BuildOutputVerifier.ExpectedFile;
import org.lmelaia.iseries.build.launch4j.Launch4jConfiguration;
import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;
import org.lmelaia.iseries.build.launch4j.Launch4jProcessWrapper;
import org.lmelaia.iseries.build.library.Library;
import org.lmelaia.iseries.build.library.LibraryManager;
import org.lmelaia.iseries.build.licence.Licences;
import org.lmelaia.iseries.build.packaging.ZipPackager;
import org.lmelaia.iseries.build.utils.CopyFile;
import org.lmelaia.iseries.build.utils.OutputCopyFile;
import org.lmelaia.iseries.build.utils.SmartFile;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * Holds the configuration settings for the build script along with methods to
 * do a full build of the root project.
 *
 * @author Luke Melaia
 */
@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
public class BuildConfiguration {

    static {
        Thread.currentThread().setName("I-Series Builder");
        AppLogger.silentConfigure("/configuration/log4j2_configuration.xml");
    }

    /**
     * The I-Series project folder.
     */
    public static final SmartFile SPROJECT_FOLDER = SmartFile.getSmartFile(
            SmartFile.getSmartFile(System.getProperty("user.dir"))
                    .goBack().getFile().getAbsolutePath());
    /**
     * The launch4j folder which contains the launch4j binaries and executables.
     */
    public static final SmartFile SLAUNCH4J_FOLDER = SmartFile.getSmartFile(
            //CHANGE: Change this to the path of launch4j on your machine.
            "D:\\Programs\\Launch4j");

    //******************************
    //    CONFIGURATION SETTINGS
    //******************************
    
    //******************************
    //   NB FILES AND FOLDERS
    //******************************
    /**
     * The final build output folder.
     */
    public static final SmartFile SOUTPUT_FOLDER = SPROJECT_FOLDER
            .forward("buildoutput");

    static {
        //noinspection ResultOfMethodCallIgnored
        SOUTPUT_FOLDER.getFile().mkdirs();
    }


    /**
     * The launcher project directory.
     */
    public static final SmartFile SLAUNCHER_FOLDER = SPROJECT_FOLDER
            .forward("launchersource");

    //******************************
    //       BUILD PROPERTIES
    //******************************
    /**
     * The folder containing the licences files.
     */
    public static final SmartFile SLICENCES_FOLDER = SPROJECT_FOLDER
            .forward("licences");
    /**
     * The folder containing the library jar files.
     */
    public static final SmartFile SLIBRARIES_FOLDER = SPROJECT_FOLDER
            .forward("build").forward("libs").forward("libs");
    /**
     * The folder containing the licences for the libraries.
     */
    public static final SmartFile SLEGAL_FOLDER = SOUTPUT_FOLDER
            .forward("legal");
    /**
     * The distribution folder. Contains the files which will be distributed to
     * the user.
     */
    public static final SmartFile SDISTRIBUTION_FOLDER = SPROJECT_FOLDER
            .forward("distribution");

    static {
        //noinspection ResultOfMethodCallIgnored
        SDISTRIBUTION_FOLDER.getFile().mkdirs();
    }

    //******************************
    //             NAMES
    //******************************
    /**
     * The folder containing the copied libraries.
     */
    public static final SmartFile SOUT_LIBRARIES_FOLDER = SOUTPUT_FOLDER
            .forward("libs");
    private static final Logger LOG = AppLogger.getLogger();

    //******************************
    //     FILES AND FILE PATHS
    //******************************
    /**
     * The build properties file.
     */
    private static final SmartFile SBUILD_PROPERTIES_FILE
            = SPROJECT_FOLDER.forward("build.cfg");
    /**
     * Build properties object. Holds the properties.
     */
    private static final Properties BUILD_PROPERTIES = new Properties();
    /**
     * The build version property name.
     */
    private static final String BUILD_VERSION_CID = "build.version";
    /**
     * The name of the application.
     */
    private static final String APPLICATION_NAME = "I-Series";
    /**
     * The I-Series jar file.
     */
    @SuppressWarnings("unused")
    public static final SmartFile SJAR_FILE = SPROJECT_FOLDER
            .forward("build")
            .forward("libs")
            .forward(APPLICATION_NAME + ".jar");
    /**
     * The windows distribution zip file.
     */
    public static final SmartFile SWINDOWS_ZIP_FILE = SDISTRIBUTION_FOLDER
            .forward(
                    APPLICATION_NAME
                    + " v"
                    + BUILD_PROPERTIES.getProperty(BUILD_VERSION_CID)
                    + " Windows.zip");
    /**
     * The cross-platform distribution zip file.
     */
    public static final SmartFile SCROSSPLATFORM_ZIP_FILE = SDISTRIBUTION_FOLDER
            .forward(APPLICATION_NAME
                    + " v" + BUILD_PROPERTIES.getProperty(BUILD_VERSION_CID)
                    + " Cross-platform.zip");

    /**
     * The name of the application with a '.exe' extension. Used as the launch4j
     * executable file name.
     */
    private static final String EXECUTABLE_NAME = APPLICATION_NAME + ".exe";

    /**
     * A list of files which need to be copied over to the output folder
     * ({@link #SOUTPUT_FOLDER}).
     */
    private static final CopyFile[] FILES_TO_COPY = {
        //Jar file
        new OutputCopyFile(SPROJECT_FOLDER.forward("build").forward("libs")
                        .forward("I-Series.jar").getFile(), "I-Series-Base.jar"),
        //I-Series licence
        new OutputCopyFile(Licences.GNU.getFile(),
        APPLICATION_NAME + " Licence.txt"),

        new OutputCopyFile(SLAUNCHER_FOLDER.forward("build").forward("libs")
                .forward("launchersource.jar").getFile(), "I-Series.jar")
    };
    /**
     * A list of the libraries for the root project.
     */
    private static final Library[] LIBRARIES = {
        new Library("Gson", "gson-2.8.0", Licences.APACHE),
        new Library("Log4j", new String[]{"log4j-api-2.8.2", "log4j-core-2.8.2"}, Licences.APACHE),
            new Library("Guava", "guava-19.0", Licences.APACHE),
            new Library("CommonSource", "commonsource", Licences.GNU)
    };


    //******************************
    //             LISTS
    //******************************
    /**
     * A list of the libraries for the launcher project.
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static final Library[] LAUNCHER_LIBRARIES = {

    };
    /**
     * A list of directories required to exist before beginning a full build.
     */
    private static final File[] REQUIRED_DIRECTORIES = {
        SOUTPUT_FOLDER.getFile(), SLEGAL_FOLDER.getFile(),
        SOUT_LIBRARIES_FOLDER.getFile(), SDISTRIBUTION_FOLDER.getFile()
    };
    /**
     * List of files expected to be in the output directory.
     */
    private static final ExpectedFile[] OUTPUT_DIR_FILES = {
        new ExpectedFile("legal"),
        new ExpectedFile("libs"),
        new ExpectedFile(APPLICATION_NAME + " Licence.txt"),
        new ExpectedFile(APPLICATION_NAME + ".exe"),
        new ExpectedFile(APPLICATION_NAME + ".jar"),
        new ExpectedFile(APPLICATION_NAME + "-Base.jar")
    };
    /**
     * List of files expected to be in the distribution directory.
     */
    private static final ExpectedFile[] DIST_DIR_FILES = {
        new ExpectedFile(SWINDOWS_ZIP_FILE.getFileName()),
        new ExpectedFile(SCROSSPLATFORM_ZIP_FILE.getFileName())
    };
    /**
     * The verifier for the output folder.
     */
    private static final BuildOutputVerifier BUILD_DIR_VERIFIER
            = new BuildOutputVerifier(SOUTPUT_FOLDER.getFile(),
                    OUTPUT_DIR_FILES)
            .setLogExtraFiles(true);
    /**
     * The verifier for the output folder.
     */
    private static final BuildOutputVerifier DIST_DIR_VERIFIER
            = new BuildOutputVerifier(SDISTRIBUTION_FOLDER.getFile(),
                    DIST_DIR_FILES)
            .setLogExtraFiles(true);
    /**
     * The configuration settings to build the I-Series executable.
     */
    private static final Launch4jConfiguration EXEC_CONFIGURATION
            = new Launch4jConfigurationBuilder()
                    .setJarRuntimePath("I-Series.jar")
                    .setWrap(false)
                    .setOutputFile(
                            SOUTPUT_FOLDER.forward(EXECUTABLE_NAME).getPath())
                    .setMinimumJreVersion("1.8.0_111")
                    .create();

    //******************************
    //        CONFIGURATION
    //******************************
    /**
     * Contains a list of the libraries for the application and handles the
     * copying of them and their licences.
     */
    private static final LibraryManager LIBRARY_MANAGER = new LibraryManager(
            SPROJECT_FOLDER.forward("build").forward("libs")
                    .forward("libs").getFile(),
            SOUTPUT_FOLDER.forward("libs").getFile(),
            SOUTPUT_FOLDER.forward("legal").getFile());

    /**
     * Contains a list of the libraries for the launcher application and handles the
     * copying of them and their licences.
     */
    private static final LibraryManager LAUNCHER_LIBRARY_MANAGER = new LibraryManager(
            SLAUNCHER_FOLDER.forward("build").forward("libs")
                    .forward("libs").getFile(),
            SOUTPUT_FOLDER.forward("libs").getFile(),
            SOUTPUT_FOLDER.forward("legal").getFile()
    );

    //Reads the properties from file and puts them in the properties object.
    static {
        readBuildProperties();
    }

    static {
        for (File f : REQUIRED_DIRECTORIES) {
            forceCleanMake(f);
        }
    }

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
     * Adds the list of libraries ({@link #LIBRARIES) to the library manager.
     */
    @SuppressWarnings("unused")
    private static void addLauncherLibrariesToList() {
        for (Library library : LAUNCHER_LIBRARIES) {
            try {
                LAUNCHER_LIBRARY_MANAGER.addLibrary(library);
            } catch (FileNotFoundException ex) {
                LOG.error("Failed to add library: " + library.getName(), ex);
            }
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
    @SuppressWarnings("UnusedReturnValue")
    private static boolean forceCleanMake(File f) {
        try {
            if (f.exists()) {
                FileUtils.cleanDirectory(f);
            } else {
                FileUtils.forceMkdir(new File(f + "/"));
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
     * Uses the library manager to copy the libraries and library licences.
     */
    private static void copyLauncherLibraries() {
        LOG.debug("Copying launcher libraries and library licences");
        try {
            LAUNCHER_LIBRARY_MANAGER.copyOver();
        } catch (IOException ex) {
            LOG.error("Failed to copy over launcher libraries", ex);
        }
    }

    /**
     * Copies over a list of files ({@link #FILES_TO_COPY}) to the output folder
     * ({@link #SOUTPUT_FOLDER}). These files include things such as the jar file,
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
    @SuppressWarnings("SameParameterValue")
    private static void createExecutable(Launch4jConfiguration configuration) {
        Launch4jProcessWrapper launch4jProcess = new Launch4jProcessWrapper(
                SLAUNCH4J_FOLDER.getFile(), configuration);
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
                    ? "No output" : output.toString()));
        }
    }

    /**
     * Compares the library(.jar) files in the buildOutput/libs folder to the
     * folders containing the libraries for the application and launcher. This ensures
     * no required library is missing from the buildOutput.
     */
    private static void compareLibraries(){
        File buildOutputLibs = SOUTPUT_FOLDER.forward("libs").getFile();
        compare(SLIBRARIES_FOLDER.getFile(), buildOutputLibs);
        compare(SLAUNCHER_FOLDER.forward("build").forward("libs").forward("libs").getFile(), buildOutputLibs);
    }

    /**
     * Compares the files in the primary folder to the files in the secondary folder.
     * If a file in the primary is not contained in the secondary, a warning is written
     * to the console.
     *
     * @param primary the folder to compare to.
     * @param secondary the folder being compared.
     */
    @SuppressWarnings("ConstantConditions")
    private static void compare(File primary, File secondary){
        for(File f : primary.listFiles()){
            if(f.isDirectory())
                continue;

            if(!contains(secondary, f.getName()))
                LOG.warn("File: " + f.getName() + " from folder: " + primary.getAbsolutePath()
                        + " is not contained in the folder: " + secondary);
        }
    }

    /**
     * Checks if the given folder contains a file with the name of {@code fileName}.
     *
     * @param folder the folder to check.
     * @param fileName the name of the file.
     * @return {@code true} if there is a file with name of {@code fileName} in the folder,
     * {@code false} otherwise.
     */
    @SuppressWarnings("ConstantConditions")
    private static boolean contains(File folder, String fileName){
        ArrayList<String> fileNames = new ArrayList<>();

        for (File f : folder.listFiles()) {
            fileNames.add(f.getName());
        }

        return fileNames.contains(fileName);
    }

    /**
     * Zips the projects build output into a windows and a cross-platform zip
     * file.
     *
     * @throws IOException if the project cannot be zipped due to an IO error.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void zipProject() throws IOException {
        //Create a copy of the output folder without
        //windows executables for packaging
        //into the cross-platform zip.
        File nwof = new File(SOUTPUT_FOLDER.getPath().substring(
                0, SOUTPUT_FOLDER.getPath().length() - 1)
                + "nw");
        FileUtils.copyDirectory(SOUTPUT_FOLDER.getFile(), nwof);
        new File(nwof.getCanonicalPath() + "/" + APPLICATION_NAME + ".exe")
                .delete();

        //Zip files
        ZipPackager windows = new ZipPackager(SWINDOWS_ZIP_FILE.getFile(),
                SOUTPUT_FOLDER.getFile());
        windows.create();

        ZipPackager crossPlatform = new ZipPackager(
                SCROSSPLATFORM_ZIP_FILE.getFile(), nwof);
        crossPlatform.create();

        //Delete output copy
        FileUtils.deleteDirectory(nwof);
    }

    /**
     * Reads the build properties from file and writes them to the properties
     * object.
     */
    private static void readBuildProperties() {
        LOG.debug("Reading build config from file: "
                + SBUILD_PROPERTIES_FILE.getPath());
        
        try (FileInputStream inputStream
                = new FileInputStream(SBUILD_PROPERTIES_FILE.getFile())) {
            BUILD_PROPERTIES.load(inputStream);
        } catch (IOException iox) {
            LOG.error("Failed to load build config", iox);
        }
    }

    /**
     * Writes the build properties from object to file.
     */
    private static void saveProperties() {
        LOG.debug("Saving build config to file: "
                + SBUILD_PROPERTIES_FILE.getPath());
        
        try (FileWriter writer = new FileWriter(
                SBUILD_PROPERTIES_FILE.getFile())) {
            BUILD_PROPERTIES.store(writer,
                    "Contains the configuration settings"
                    + " for the build (e.g. version number)");
        } catch (IOException ex) {
            LOG.error("Failed to save build config", ex);
        }
    }

    /**
     * Copies over a file to the output directory (Specified by
     * {@link #SOUTPUT_FOLDER}). The new file will have the same name as the old.
     *
     * @param source the source file to copy.
     * @throws IOException if an IOException occurs during copying or if the
     * source file is a directory.
     */
    @SuppressWarnings("unused")
    public static void copyFileToOutput(File source) throws IOException {
        copyFileToOutput(source, null);
    }

    /**
     * Copies over a file to the output directory (Specified by
     * {@link #SOUTPUT_FOLDER}) under a new name.
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
    @SuppressWarnings("SameParameterValue")
    public static void copyFileToOutput(File source, String newFileName)
            throws IOException {
        File dest;

        if (Objects.requireNonNull(source).isDirectory()) {
            throw new IOException("Source file cannot be a directory");
        }

        if (newFileName == null || newFileName.equals("")) {
            dest = SOUTPUT_FOLDER.forward(source.getName()).getFile();
        } else {
            newFileName = newFileName.replace("\\", "/");
            dest = SOUTPUT_FOLDER.forward(newFileName).getFile();
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
     * @throws java.lang.Exception if for any reason the full build fails.
     */
    @SuppressWarnings("EmptyCatchBlock")
    public static void fullBuild() throws Exception {
        copyFilesOver();
        addLibrariesToList();
        copyLibraries();
        copyLauncherLibraries();
        buildISeriesExecutable();
        //Give windows explorer time to refresh
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        zipProject();
        saveProperties();
        BUILD_DIR_VERIFIER.verify();
        DIST_DIR_VERIFIER.verify();
        compareLibraries();
        LOG.info("Full build completed without fatal errors");
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("Beginning full build...");

        try {
            fullBuild();
        } catch (Exception ex) {
            LOG.error("Full build failed", ex);
        }
    }
}
