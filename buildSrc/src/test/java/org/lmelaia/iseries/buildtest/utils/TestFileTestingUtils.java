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
import static org.lmelaia.iseries.buildtest.utils.FileTestingUtils.TestFile;

/**
 * Tests the {@link FileTestingUtils} class.
 *
 * @author Luke Melaia
 */
public class TestFileTestingUtils {

    private static FileTestingUtils fileUtils;

    private static FileTestingUtils fileUtilsNew;

    private static String file1 = "TestFileOne.test",
            file1Content = "Some content...",
            file2 = "TestFileTwo.test",
            file3 = "TestFileThree.test",
            file3Content = "Some more content...",
            path = System.getProperty("user.dir") + "\\tests\\";

    @BeforeClass
    public static void setup() {
        fileUtils = new FileTestingUtils(path, new String[][]{
            new String[]{file1, file1Content},
            new String[]{file2},
            new String[]{file3, file3Content}
        });

        fileUtilsNew = new FileTestingUtils(
                new TestFile("TestFileA.test").setContent("A bit of content"),
                new TestFile("TestFileB.test").setContent("Some content here"),
                new TestFile("TestFileC.test")
                .setContent("Some more content")
                .setID("tfc"),
                new TestFile("TestFileD.test").setID("tfd")
        );
    }

    @Test
    public void testSetup() throws IOException {
        fileUtils.setup();
        
        if (!new File(path + file1).exists()) {
            fail("The file " + path + file1 + " wasn't created");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(
                new File(path + file1)))) {
            if (!br.readLine().equals(file1Content)) {
                fail("The file was not written to");
            }
        }

        if (!new File(path + file2).exists()) {
            fail("The file " + path + file1 + " wasn't created");
        }

        if (!new File(path + file3).exists()) {
            fail("The file " + path + file3 + " wasn't created");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(
                new File(path + file3)))) {
            if (!br.readLine().equals(file3Content)) {
                fail("The file was not written to");
            }
        }
        
        if(!fileUtils.getFileByID(file2).exists()){
            fail("Couldn't get file by ID using String Array constructor");
        }
        
        fileUtilsNew.setup();
        
        if(!fileUtilsNew.getFileByID("TestFileA.test").exists()){
            fail("The file TestFileA.test wasn't created");
        }
        
        if(!fileUtilsNew.getFileByID("TestFileB.test").exists()){
            fail("The file TestFileB.test wasn't created");
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                fileUtilsNew.getFileByID("TestFileB.test")))) {
            if (!br.readLine().equals("Some content here")) {
                fail("The file TestFileB.test was not written to");
            }
        }
        
        if(!fileUtilsNew.getFileByID("tfc").exists()){
            fail("The file TestFileC.test wasn't created or getting a file by"
                    + " doesn't work");
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                fileUtilsNew.getFileByID("tfc")))) {
            if (!br.readLine().equals("Some more content")) {
                fail("The file TestFileC.test was not written to");
            }
        }
    }

    @Test
    public void testTeardown() {
        boolean a = fileUtils.teardown();
        boolean b = fileUtilsNew.teardown();
        //@REM: Here because of bug
        System.out.println("new File(path + file1).exists()" + new File(path + file1).exists());
        if (new File(path + file1).exists()) {
            fail("The file " + path + file1 + " wasn't deleted");
        }
        //@REM: Here because of bug
        System.out.println("new File(path + file2).exists()" + new File(path + file2).exists());
        if (new File(path + file2).exists()) {
            fail("The file " + path + file2 + " wasn't deleted");
        }
        //@REM: Here because of bug
        System.out.println("new File(path + file3).exists()" + new File(path + file3).exists());
        if (new File(path + file3).exists()) {
            fail("The file " + path + file3 + " wasn't deleted");
        }
        
        System.out.println("A: " + a + "\nB: " + b);
    }
}
