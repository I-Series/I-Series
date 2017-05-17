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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.lmelaia.iseries.buildtest.utils.FileTestingUtils;

/**
 * Tests the launch4j configuration builder class.
 *
 * @author Luke Melaia
 */
public class Launch4jConfigurationBuilderTest {

    /**
     * The test configuration builder object. <b>This object is reset before
     * each test.</b>
     */
    private Launch4jConfigurationBuilder testObject = getNewTestObject();

    private static FileTestingUtils fileUtils;

    @BeforeClass
    public static void initialize() {
        try {
            fileUtils = new FileTestingUtils(new String[][]{
                new String[]{"EmptyExe.exe"},
                new String[]{"EmptyIcon.ico"},
                new String[]{"EmptyJar.jar"},
                new String[]{"EmptyManifest.manifest"},
                new String[]{"EmptySplash.bmp"},});
            fileUtils.setup();
        } catch (Exception e) {
            fail("Couldn't create or setup file utils object\n" + e);
        }

        try {
            getNewTestObject().create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test object is invalid\n" + ex);
        }
    }

    @AfterClass
    public static void cleanup() {
        fileUtils.teardown();
    }

    public static Launch4jConfigurationBuilder getNewTestObject() {
        return new Launch4jConfigurationBuilder()
                .setOutputFile(System.getProperty("user.dir")
                        + "\\tests\\EmptyExe.exe")
                .setJarFile(System.getProperty("user.dir")
                        + "\\tests\\EmptyJar.jar")
                .setMinimumJreVersion("1.8.0");
    }

    @Before
    public void setUp() {
        //Reset the object before each test.
        testObject = getNewTestObject();
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

        try {
            testObject.setJarRuntimePath(null).create();
            fail("Test allows null runtime path when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJarRuntimePath("").create();
            fail("Test allows empty runtime path when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJarRuntimePath("/").create();
            fail("Test allows runtime path beginning with '/' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJarRuntimePath("\\").create();
            fail("Test allows runtime path beginning with '\\' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJarRuntimePath("somepath:").create();
            fail("Test allows runtime path without ':' when wrap is false");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
    }

    @Test
    public void testValidateManifestFile() {
        try {
            testObject.setWrapperManifest("something.not").create();
            fail("Test allows manifest without propper extension");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setWrapperManifest("someplace/someotherplace.manifest").create();
            fail("Test allows nonexistant manifest");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setWrapperManifest(System.getProperty("user.dir")
                    + "\\tests\\EmptyManifest.manifest").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid manifest file\n" + ex);
        }
    }

    @Test
    public void testValidateIconFile() {
        try {
            testObject.setIconFile("wrong.filetype").create();
            fail("Test allows incorrect file type");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setIconFile("nonexistant_file.ico").create();
            fail("Test allows nonexistant file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setIconFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyIcon.ico").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid icon file");
        }
    }

    @Test
    public void testValidateUrls() {
        try {
            testObject.setDownloadUrl(null).create();
            fail("Test allows null download url");
        } catch (InvalidLaunch4jConfigurationException e) {
        }

        try {
            testObject.setDownloadUrl("").create();
            fail("Test allows empty download url");
        } catch (InvalidLaunch4jConfigurationException e) {
        }
    }

    @Test
    public void testValidateClasspath() {
        testObject.useCustomClasspath(true);

        try {
            testObject.setMainClass(null).create();
            fail("Test allows null main class when using custom classpath");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMainClass("").create();
            fail("Test allows empty main class when using custom classpath");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMainClass("somme.main.Class").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid main class when using custom classpath");
        }
    }

    @Test
    public void testValidateSingleInstance() {
        testObject.allowOneInstanceOnly(true);

        try {
            testObject.setMutexName(null).create();
            fail("Test allows null mutex name when using single instance");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMutexName("").create();
            fail("Test allows empty mutex name when using single instance");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        try {
            testObject.setMutexName("someMutexName").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid mutex name when using single instance");
        }
    }

    @Test
    public void testValidateJreVersions() {
        //Bundled jre path
        try {
            testObject.setBundledJrePath("some/path").setMinimumJreVersion(null).create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow null minimum jre version with a bundled path");
        }
        testObject.setBundledJrePath(null);

        //Minimum jre version
        try {
            testObject.setMinimumJreVersion(null).create();
            fail("Test allows null minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("").create();
            fail("Test allows empty minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0..").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0.0.").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0.0_").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0.0_1.").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0.0.0").create();
            fail("Test allows invalid minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMinimumJreVersion("1.0.0_65").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid minimum jre version");
        }

        try {
            testObject.setMinimumJreVersion("1.0.0").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid minimum jre version");
        }

        //Maximum jre path.
        try {
            testObject.setMaximumJreVersion("1").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0..").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0.0.").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0.0_").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0.0_1.").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.0.0.0").create();
            fail("Test allows invalid maximum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumJreVersion("1.2.0_65").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid maximum jre version");
        }

        try {
            testObject.setMaximumJreVersion("1.2.0").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid maximum jre version");
        }

        try {
            testObject.setMinimumJreVersion("2.0.0")
                    .setMaximumJreVersion("1.0.0").create();
            fail("Test allows lager minimum jre version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
    }

    @Test
    public void testValidateJavaUsageOptions() {
        try {
            testObject.setJavaUsageOptions(null).create();
            fail("Test allows null java usage options");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
    }

    @Test
    public void testValidateJavaArchitecture() {
        try {
            testObject.setJavaUsageOptions(null).create();
            fail("Test allows null java architecture");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
    }

    @Test
    public void testValidateHeaps() {
        try {
            testObject.setInitialHeapSize(1).setMaximumHeapSize(0).create();
            fail("Test allows no maximum heap size when initial is set");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        testObject.setInitialHeapSize(0);

        try {
            testObject.setInitialHeapSize(0).setHeapInPercent(true).create();
            fail("Test allows <1% heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setInitialHeapSize(101).create();
            fail("Test allows >100% heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumHeapSize(0).setHeapInPercent(true).create();
            fail("Test allows <1% maximum heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumHeapSize(101).setHeapInPercent(true).create();
            fail("Test allows >100% heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        testObject.setHeapInPercent(false);

        try {
            testObject.setInitialHeapSize(-1).create();
            fail("Test allows <0 heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setMaximumHeapSize(-1).create();
            fail("Test allows <0 max heap size heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setInitialHeapSize(100).setMaximumHeapSize(50).create();
            fail("Test allows bigger initial heap size");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setInitialHeapSize(50).setMaximumHeapSize(100).create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid heap configuration");
        }
    }

    @Test
    public void testValidateJvmOptions() {
        try {
            testObject.setJvmOptions("ww", "ww", "ww", null).create();
            fail("Test allows null jvm option");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJvmOptions("XX", "XX", "").create();
            fail("Test allows empty jvm option");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setJvmOptions("XX", "XX", "XX").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid jvm options");
        }
    }

    @Test
    public void testValidateEnvironmentVariables() {
        try {
            testObject.setEnvironmentVariables("").create();
            fail("Test allows empty environment variable");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setEnvironmentVariables(null, null).create();
            fail("Test allows null environment variable");
        } catch (InvalidLaunch4jConfigurationException | NullPointerException ex) {
        }

        try {
            testObject.setEnvironmentVariables("sasd=").create();
            fail("Test allows invalid environment variable");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setEnvironmentVariables("=sdahss").create();
            fail("Test allows invalid environment variable");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setEnvironmentVariables("sasd=asdasd").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid environment variable");
        }
    }

    @Test
    public void testValidateSplashScreen() {
        testObject.enableSplashScreen(true);

        try {
            testObject.setSplashFile("").create();
            fail("Test allows empty splash file when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setSplashFile(null).create();
            fail("Test allows null splash file when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setSplashFile("").create();
            fail("Test allows empty splash file when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setSplashFile("something.notBmp").create();
            fail("Test allows non-bmp splash file");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }

        try {
            testObject.setSplashFile("doesntExist.bmp").create();
            fail("Test allows empty splash file when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setSplashFile(System.getProperty("user.dir")
                    + "\\tests\\EmptySplash.bmp").setTimeout(0).create();
            fail("Test allows timout < 1 when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setSplashFile(System.getProperty("user.dir")
                    + "\\tests\\EmptySplash.bmp").setTimeout(9001).create();
            fail("Test allows timout > 900 when enabled");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        testObject.setTimeout(60);

        try {
            testObject.setSplashFile(System.getProperty("user.dir")
                    + "\\tests\\EmptySplash.bmp").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid splash\n" + ex);
        }
    }
    
    @Test
    public void validateVersionInfo(){
        testObject.addVersionInfo(true);
        testObject.setProductVersion("0.0.0.0");
        testObject.setFileVersion("0.0.0.0");
        
        try {
            testObject.setOriginalFileName("something.exe")
                    .setFileVersion("0.0.0.0")
                    .setFreeFormFileVersion(" something ")
                    .setFileDescription(" something ")
                    .setCopyright(" something ")
                    .setFreeFormProductVersion("0.0.0.0")
                    .setProductName(" something ")
                    .setInternalName(" something ").create();
        } catch (InvalidLaunch4jConfigurationException ex) {
            fail("Test doesn't allow valid file extension\n" + ex);
        }
        
        try {
            testObject.setFileVersion(null).create();
            fail("Test allows null file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("").create();
            fail("Test allows empty file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("0.0.0").create();
            fail("Test allows invalid file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("0.0.0.0.0").create();
            fail("Test allows invalid file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("0.").create();
            fail("Test allows invalid file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("0").create();
            fail("Test allows invalid file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileVersion("0.0.0..").create();
            fail("Test allows invalid file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFreeFormFileVersion(null).create();
            fail("Test allows null free form file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFreeFormFileVersion("").create();
            fail("Test allows empty free form file version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileDescription(null).create();
            fail("Test allows null file description");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFileDescription("").create();
            fail("Test allows empty file description");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setCopyright(null).create();
            fail("Test allows null copyright");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setCopyright("").create();
            fail("Test allows empty copyright");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion(null).create();
            fail("Test allows null product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("").create();
            fail("Test allows empty product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("0.0.0").create();
            fail("Test allows invalid product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("0.0.0.0.0").create();
            fail("Test allows invalid product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("0.").create();
            fail("Test allows invalid product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("0").create();
            fail("Test allows invalid product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductVersion("0.0.0..").create();
            fail("Test allows invalid product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFreeFormProductVersion(null).create();
            fail("Test allows null free form product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setFreeFormProductVersion("").create();
            fail("Test allows empty free form product version");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductName(null).create();
            fail("Test allows null product name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setProductName("").create();
            fail("Test allows empty product name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setInternalName(null).create();
            fail("Test allows null internal name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setInternalName("").create();
            fail("Test allows empty internal name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setOriginalFileName(null).create();
            fail("Test allows null original file name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setOriginalFileName("").create();
            fail("Test allows empty original file name");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
        
        try {
            testObject.setOriginalFileName("something.notExe").create();
            fail("Test allows non-exe original file name file extension");
        } catch (InvalidLaunch4jConfigurationException ex) {
        }
    }
    
    @Test
    public void testCreateCopyFunction(){
        Launch4jConfiguration configuration = testObject.create();
        
        testObject.setErrorTitle("Some error title");
        
        if(configuration.getErrorTitle() != null 
                && configuration.getErrorTitle().equals(testObject.errorTitle)){
            fail(
                    "Modifying configuration builder "
                            + "effects configuration object"
            );
        }
    }
}
