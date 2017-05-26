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
package org.lmelaia.iseries.buildtest.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.lmelaia.iseries.buildtest.utils.FileTestingUtils.TestFile;
import static org.lmelaia.iseries.buildtest.utils.FileTestingUtils.PATH_BUILD_TEST_FILES;

/**
 * Tests the {@link FileTestingUtils} class.
 *
 * @author Luke Melaia
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFileTestingUtils {

    private static FileTestingUtils fileUtilsA;
    private final File A1 = new File(PATH_BUILD_TEST_FILES + "TestFileA1");
    private final File A2 = new File(PATH_BUILD_TEST_FILES + "TestFileA2");
    private final File A3 = new File(PATH_BUILD_TEST_FILES + "TestFileA3");

    private static FileTestingUtils fileUtilsB;
    private final File B1 = new File(PATH_BUILD_TEST_FILES + "TestFileB1");
    private final File B2 = new File(PATH_BUILD_TEST_FILES + "TestFileB2");
    private final File B3 = new File(PATH_BUILD_TEST_FILES + "TestFileB3");

    @BeforeClass
    public static void setup() {
        fileUtilsA = new FileTestingUtils(
                new String[]{"TestFileA1"},
                new String[]{"TestFileA2", "Some content"},
                new String[]{"TestFileA3", "Some more content", "tfa3"}
        );

        fileUtilsB = new FileTestingUtils(
                new TestFile("TestFileB1"),
                new TestFile("TestFileB2").setContent("A tiny bit of content"),
                new TestFile("TestFileB3").setContent("content").setID("tfb3")
        );
    }

    @Test
    public void A_testCreation() throws IOException {
        fileUtilsA.setup();
        fileUtilsB.setup();

        if (!A1.exists()) 
            fail(A1 + " doesn't exist");
        if (!A2.exists()) 
            fail(A2 + " doesn't exist");
        if (!A3.exists()) 
            fail(A3 + " doesn't exist");
        
        if (!B1.exists()) 
            fail(B1 + " doesn't exist");
        if (!B2.exists()) 
            fail(B2 + " doesn't exist");
        if (!B3.exists()) 
            fail(B3 + " doesn't exist");
    }

    @Test
    public void B_testIDSystem() {
        if(fileUtilsA.getFileByID("a nonexistant id") != null){
            fail("A non-existant ID doesn't return null");
        }
        
        if(fileUtilsA.getFileByID("TestFileA1") == null){
            fail("File name isn't ID by default");
        }
        
        if(fileUtilsA.getFileByID("tfa3") == null){
            fail("Setting an id doesn't work");
        }
        
        if(fileUtilsA.getFileByID("TFA3") == null){
            fail("ID doesn't ignore lowercase");
        }
        
        if(fileUtilsB.getFileByID("a nonexistant id") != null){
            fail("A non-existant ID doesn't return null");
        }
        
        if(fileUtilsB.getFileByID("TestFileB1") == null){
            fail("File name isn't ID by default");
        }
        
        if(fileUtilsB.getFileByID("tfb3") == null){
            fail("Setting an id doesn't work");
        }
        
        if(fileUtilsB.getFileByID("TFB3") == null){
            fail("ID doesn't ignore lowercase");
        }
    }

    @Test
    public void C_testContentWriting() {
        try (BufferedReader br = new BufferedReader(new FileReader(A2))) {
            if(br.readLine() == null){
                fail(A2 + " wasn't written to");
            }
        } catch (Exception e) {
            fail("Exception thrown during reading\n" + e);
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(B2))) {
            if(br.readLine() == null){
                fail(B2 + " wasn't written to");
            }
        } catch (Exception e) {
            fail("Exception thrown during reading\n" + e);
        }
    }

    @Test
    public void D_testDeletion() {
        boolean a = fileUtilsA.teardown();
        boolean b = fileUtilsB.teardown();
        
        if(!a){
            fail("All files in fileUtilsA weren't deleted");
        }
        
        if(!b){
            fail("All files in fileUtilsB weren't deleted");
        }
        
        if(A1.exists())
            fail(A1 + " wasn't deleted");
        if(A2.exists())
            fail(A2 + " wasn't deleted");
        if(A3.exists())
            fail(A3 + " wasn't deleted");
        
        if(B1.exists())
            fail(B1 + " wasn't deleted");
        if(B2.exists())
            fail(B2 + " wasn't deleted");
        if(B3.exists())
            fail(B3 + " wasn't deleted");
    }
}
