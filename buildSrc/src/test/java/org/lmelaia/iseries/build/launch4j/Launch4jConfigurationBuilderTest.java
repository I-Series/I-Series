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
import static org.junit.Assert.*;
import org.junit.BeforeClass;

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
            .setOutputFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyExe.exe")
            .setJarFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyJar.jar")
            .setMinimumJreVersion("1.8.0")
            .setWrap(true);

    /**
     * The test configuration builder object. <b>This object is reset before
     * each test.</b>
     */
    private Launch4jConfigurationBuilder testObject = DEFAULT_VALID_TEMPLATE;

    public Launch4jConfigurationBuilderTest() {
        
    }

    @BeforeClass
    public static void testConfiguration() {
        try {
            DEFAULT_VALID_TEMPLATE.create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Default template is invalid\n" + ex);
        }
    }

    @Before
    public void setUp() {
        //Reset the object before each test.
        testObject = DEFAULT_VALID_TEMPLATE;
    }

    @Test
    public void testValidateOutputFile() {
        //Test for null exe
        try {
            testObject.setOutputFile(null).create();
            fail("Configuration allows null output file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for empty exe
        try {
            testObject.setOutputFile("").create();
            fail("Configuration allows empty output file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for non-exe exe
        try {
            testObject.setOutputFile("something/something.lol").create();
            fail("Configuration allows non-exe output file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for valid exe.
        try {
            testObject.setOutputFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyExe.exe").create();
        } catch (InvalidLaunch4jConfigurationException | NullPointerException ex) {
            fail("Test doesn't allow valid exe\n" + ex);
        }
    }

    @Test
    public void testValidateJarFile() {
        testObject.setWrap(true);
        
        //Test for null jar
        try {
            testObject.setJarFile(null).create();
            fail("Configuration allows null jar file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for empty jar
        try {
            testObject.setJarFile("").create();
            fail("Configuration allows empty jar file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for non-jar jar
        try {
            testObject.setJarFile("something/something.lol").create();
            fail("Configuration allows non-jar jar file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        //Test for nonexistant jar.
        try {
            testObject.setJarFile(System.getProperty("user.dir")
                    + "\\tests\\Nojar.jar").create();
            fail("Test allows nonexistant jar");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        //Test for valid jar.
        try {
            testObject.setJarFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyJar.jar").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid jar\n" + ex);
        }
    }

    @Test
    public void testValidateJarRuntimePath() {
        testObject.setWrap(false);
        
        try{
            testObject.setJarRuntimePath(null).create();
            fail("Test allows null runtime path when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setJarRuntimePath("").create();
            fail("Test allows empty runtime path when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setJarRuntimePath("/").create();
            fail("Test allows runtime path beginning with '/' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setJarRuntimePath("\\").create();
            fail("Test allows runtime path beginning with '\\' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setJarRuntimePath("somepath:").create();
            fail("Test allows runtime path without ':' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
    }
    
    @Test
    public void testValidateManifestFile(){
        try{
            testObject.setWrapperManifest("something.not").create();
            fail("Test allows manifest without propper extension");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setWrapperManifest("someplace/someotherplace.manifest").create();
            fail("Test allows nonexistant manifest");
        } catch (InvalidLaunch4jConfigurationException ex){
        }
        
        try{
            testObject.setWrapperManifest(System.getProperty("user.dir")
                    + "\\tests\\EmptyManifest.manifest").create();
        } catch (InvalidLaunch4jConfigurationException ex){
            fail("Test doesn't allow valid manifest file\n" + ex);
        }
    }
}
