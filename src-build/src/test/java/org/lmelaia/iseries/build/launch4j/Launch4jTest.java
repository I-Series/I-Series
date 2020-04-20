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
package org.lmelaia.iseries.build.launch4j;

import org.junit.Ignore;
import org.junit.Test;
import org.lmelaia.iseries.build.BuildConfiguration;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;
import static org.lmelaia.iseries.build.BuildConfiguration.SPROJECT_FOLDER;

/**
 * Tests creating an executable with launch4j.
 *
 * @author Luke Melaia
 */
public class Launch4jTest {

    /**
     * The path to the launch4j application folder.
     */
    private static final File LAUNCH4J_PATH
            = BuildConfiguration.SLAUNCH4J_FOLDER.getFile();

    /**
     * The output executable file created with launch4j.
     * <br>
     * <b>This file is never deleted by the application.</b>
     */
    private static final File EXECUTABLE_FILE = SPROJECT_FOLDER
            .forward("buildSrc").forward("output.exe").getFile();

    /**
     * Basic configuration for creating a standard executable.
     */
    private final Launch4jConfiguration l4jConfig
            = new Launch4jConfigurationBuilder()
            .setOutputFile(EXECUTABLE_FILE.getAbsolutePath())
            .setJarFile(
                    //This is the jar file created from the application source.
                    SPROJECT_FOLDER.forward("build").forward("libs")
                            .forward("I-Series.jar").getPath()
            )
            .setMinimumJreVersion("1.8.0")
            .create();

    /**
     * Attempts to create an executable with launch4j.
     *
     * <p>
     * This method uses launch4j to test for failure. If launch4j fails to
     * create the executable, it's assumed that there is an error in the code.
     * <br>
     * If launch4j returns an exit code of {@code 0}, it's assumed the file was
     * created successfully. Checking for an output executable is buggy and
     * prone to error so this method won't check for an output file.
     * </p>
     * 
     * <p>
     * The launch4j output is printed to the console if launch4j fails to create
     * the executable, which will aid in debugging.
     * </p> 
     *
     * @throws IOException ...
     */
    @Ignore
    @Test
    public void testExecutableCreation() throws IOException {
        Launch4jProcessWrapper l4jProcessWrapper
                = new Launch4jProcessWrapper(LAUNCH4J_PATH, l4jConfig);
        StringBuilder output = new StringBuilder();

        int l4jExitCode = l4jProcessWrapper.startProcess(output);

        //Launch4j failed to create the executable
        if (l4jExitCode != 0) {
            fail("Launch4j failed with exit code: " + l4jExitCode);
        }
        
        if(!output.toString().equals("launch4j: Compiling resources")){
            fail("The launch4j output was expected to "
                    + "be: \"launch4j: Compiling resources\", but was: "
                    + "\"" + output + "\"");
        }
    }
}
