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

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the launch4j configuration builder class.
 *
 * @author Luke Melaia
 */
public class Launch4jConfigurationBuilderTest {

    /**
     * A valid configuration builder used as a reset template.
     */
    private static final Launch4jConfigurationBuilder DEFAULT_VALID_TEMPLATE
            = new Launch4jConfigurationBuilder()
            .setOutputFile("C:/Users/Melaia/Desktop/Output.exe")
            .setJarFile(System.getProperty("user.dir")
                    + "\\build\\libs\\I-Series.jar")
            .setMinimumJreVersion("1.8.0");

    /**
     * The test configuration builder object. <b>This object is reset before
     * each test.</b>
     */
    private Launch4jConfigurationBuilder testObject = DEFAULT_VALID_TEMPLATE;

    public Launch4jConfigurationBuilderTest() {
    }

    @Before
    public void setUp() {
        //Reset the object before each test.
        testObject = DEFAULT_VALID_TEMPLATE;
    }

    @Test
    public void testOutputFileName() {

    }

    @Test
    public void testJarFileName() {

    }

    @Test
    public void testWrap() {

    }

    @Test
    public void testJarRuntumePath() {

    }

    @Test
    public void testWrapperManifest() {

    }

    @Test
    public void testIconFileName() {

    }

    @Test
    public void testChangeDirectory() {

    }

    @Test
    public void testCommandLineArguments() {

    }

    @Test
    public void testProcessPriority() {

    }

    @Test
    public void testDownloadUrl() {

    }

    @Test
    public void testCustomClasspath() {

    }

    @Test
    public void testMainClass() {

    }

    @Test
    public void testClasspath() {

    }

    @Test
    public void testHeaderType() {

    }

    @Test
    public void testSingleInstance() {

    }

    @Test
    public void testMutexName() {

    }

    @Test
    public void testBundledJrePath() {

    }

    @Test
    public void testMinimumJreVersion() {

    }

    @Test
    public void testJavaUsageOptions() {

    }

    @Test
    public void testJavaArchitecture() {

    }

    @Test
    public void testInitialHeapSize() {

    }

    @Test
    public void testMaximumHeapSize() {

    }

    @Test
    public void testIsHeapInPercent() {

    }

    @Test
    public void testJvmOptions() {

    }

    @Test
    public void testEnvironmentVariables() {

    }

    @Test
    public void testIsSplashScreenEnabled() {

    }

    @Test
    public void testSplashScreenFileName() {

    }

    @Test
    public void testTimeout() {

    }

    @Test
    public void testAddVersionInfo() {

    }

    @Test
    public void testFileVersion() {

    }

    @Test
    public void testFreeFormFileVersion() {

    }

    @Test
    public void testFileDescription() {

    }

    @Test
    public void testCopyright() {

    }

    @Test
    public void testProductVersion() {

    }

    @Test
    public void testFreeFormProductVersion() {

    }

    @Test
    public void testProductName() {

    }

    @Test
    public void testCompanyName() {

    }

    @Test
    public void testInternalName() {

    }

    @Test
    public void testOriginalFileName() {

    }
}