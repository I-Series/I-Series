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

import java.util.Arrays;
import org.lmelaia.iseries.build.launch4j.Launch4jConfiguration;
import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;

/**
 * Holds the configuration settings for the build script.
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
    public static final String LAUNCH4J_PATH = "C:\\Program Files (x86)\\Launch4j";
    
    /**
     * The file name (not path) of the executable. This must end in '.exe'.
     */
    private static final String APPLICATION_NAME = "I-Series.exe";

    private static final Launch4jConfiguration CONFIGURATION
            = new Launch4jConfigurationBuilder()
            .setJarFile(JAR_PATH)
            .setOutputFile(OUTPUT_PATH + APPLICATION_NAME)
            .setMinimumJreVersion("1.8.0_65")
            .create();
    
    
    //*******************
    //      METHODS
    //*******************
    
    /**
     * <b>
     * This method is called BEFORE the root projects run task is executed.
     * </b>
     * 
     * <p>
     * This allows any action to be completed before running
     * the root project.
     */
    public static void run(){
        System.out.println("BuildConfiguration.run()");
        //TODO: logic before running root project.
    }
    
    /**
     * <b>
     * This method is called AFTER the root projects build task is executed.
     * </b>
     * 
     * <p>
     * This allows any action to be completed after building the root project,
     * such as: building the executable and installer files.
     */
    public static void build(){
        System.out.println("BuildConfiguration.build()");
        //TODO: logic after building root project.
    }
    
    /**
     * Dummy main method for buildsrc:build.gradle.<p>
     * <b>This method should NOT perform any operations!</b>
     * 
     * @param args 
     */
    public static void main(String[] args){
        //NO-OP
    }
}
