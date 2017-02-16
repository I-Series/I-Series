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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.lmelaia.iseries.build.launch4j.Launch4jConfiguration;
import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;
import org.lmelaia.iseries.build.launch4j.Launch4jProcessWrapper;
import static org.apache.commons.io.FileUtils.*;
import org.lmelaia.iseries.build.library.Library;
import org.lmelaia.iseries.build.library.LibraryManager;
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
    
    //******************************
    //     FILES AND FILE PATHS
    //******************************
    
    /**
     * The string representation of the path to the I-Series project folder.
     */
    public static final String PROJECT_PATH
            = "C:/Programming/Languages/Java/Projects/I-Series/";

    /**
     * The I-Series project folder.
     */
    public static final File PROJECT_FOLDER = new File(PROJECT_PATH);
    
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
    public static final String OUTPUT_PATH = PROJECT_PATH + "output/";
    
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
     * The path to the folder containing the copied libraries.
     */
    public static final String OUT_LIBRARIES_PATH = OUTPUT_PATH + "libs/";
    
    /**
     * The folder containing the copied libraries.
     */
    public static final File OUT_LIBRARIES_FOLDER 
            = new File(OUT_LIBRARIES_PATH);
    
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
    //        CONFIGURATION
    //******************************
    
    /**
     * The configuration settings to build the I-Series executable.
     */
    private static Launch4jConfiguration executableConfiguration
            = new Launch4jConfigurationBuilder()
            .setJarRuntimePath("I-Series.jar")
            .setWrap(false)
            .setOutputFile(OUTPUT_PATH + EXECUTABLE_NAME)
            .setMinimumJreVersion("1.8.0_111")
            .create();

    /**
     * Contains a list of the libraries for the application and handles
     * the copies of them and their licences.
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
        OUTPUT_FOLDER, LEGAL_FOLDER, OUT_LIBRARIES_FOLDER
    };
    
    //*******************
    //      METHODS
    //*******************
    
    /**
     * Builds the executable file for the I-Series jar file.
     */
    private static void buildISeriesExecutable() {
        System.out.println("Creating I-Series executable");
        createExecutable(executableConfiguration);
    }
    
    /**
     * Adds the list of libraries ({@link #LIBRARIES) to the library manager.
     */
    private static void addLibrariesToList(){
        for(Library library : LIBRARIES){
            try{
                LIBRARY_MANAGER.addLibrary(library);
            } catch (FileNotFoundException ex) {
                System.err.println("Failed to add library: "
                        + library.getName() + "\n" + ex);
            }
        }
    }
    
    /**
     * Creates the directories required to do a full build.
     */
    private static void createRequiredDirectories(){
        for(File f : REQUIRED_DIRECTORIES){
            if(!forceCleanMake(f)){
                System.err.println("Failed to create/clean directory: " + f);
            }
        }
    }
    
    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories if the directory is nonexistent, else the directory
     * is cleaned.
     * 
     * @param f the directory
     * @return {@code true} if and only if the directory was created or cleaned,
     * {@code false} otherwise.
     */
    private static boolean forceCleanMake(File f){
        try{
            if(f.exists())
                FileUtils.cleanDirectory(f);
             else 
                FileUtils.forceMkdir(f);
        } catch (IOException ex){
            return false;
        }
        
        return true;
    }
    
    /**
     * Uses the library manager to copy the libraries and library licences.
     */
    private static void copyLibraries() {
        System.out.println("Copying libraries and library licences");
        try {
            LIBRARY_MANAGER.copyOver();
        } catch (IOException ex) {
            System.err.println("Failed to copy over libraries: \n" + ex);
        }
    }

    /**
     * Copies over a list of files ({@link #FILES_TO_COPY}) to the output folder
     * ({@link #OUTPUT_PATH}). These files include things such as the jar file,
     * licences and so on.
     */
    private static void copyFilesOver() {
        for (CopyFile file : FILES_TO_COPY) {
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

        if(!output.toString().equals("launch4j: Compiling resources")){
            System.err.println("Launch4j might have failed "
                    + "to produce an executable");
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
        createRequiredDirectories();
        copyFilesOver();
        addLibrariesToList();
        copyLibraries();
        buildISeriesExecutable();
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
}
