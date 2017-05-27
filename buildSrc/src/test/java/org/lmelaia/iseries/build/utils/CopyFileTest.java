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
package org.lmelaia.iseries.build.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.BeforeClass;
import org.lmelaia.iseries.build.BuildConfiguration;
import org.lmelaia.iseries.buildtest.utils.FileTestingUtils;
import static org.lmelaia.iseries.buildtest.utils.FileTestingUtils.TestFile;

/**
 * Tests the copy file mechanism.
 * 
 * @author Luke Melaia
 */
public class CopyFileTest {
    
    private static final FileTestingUtils FILE_TESTING_UTILS
            = new FileTestingUtils(
                    new TestFile("File1.txt").setContent("Some content"),
                    new TestFile("File2.txt").setContent("Some more content"));
    
    private static final File DEST_FOLDER = BuildConfiguration.SPROJECT_FOLDER
                    .forward("copyFileTestFolder").getFile();
    
    public CopyFileTest() {
    }
    
    @BeforeClass
    public static void setup() throws IOException{
        DEST_FOLDER.mkdirs();
        FILE_TESTING_UTILS.setup();
    }
    
    @AfterClass
    public static void teardown() throws IOException{
        FILE_TESTING_UTILS.teardown();
        FileUtils.deleteDirectory(DEST_FOLDER);
    }

    @Test
    public void testCopyFileCreation() {
        CopyFile testA = new CopyFile(
                FILE_TESTING_UTILS.getFileByID("File1.txt"), DEST_FOLDER);
        
        CopyFile testB = new CopyFile(
                FILE_TESTING_UTILS.getFileByID("File2.txt"), DEST_FOLDER,
                "someNewName");
    }
    
    @Test
    public void testCopyFileCopying() throws IOException{
        CopyFile testA = new CopyFile(
                FILE_TESTING_UTILS.getFileByID("File1.txt"), DEST_FOLDER);
        
        CopyFile testB = new CopyFile(
                FILE_TESTING_UTILS.getFileByID("File2.txt"), DEST_FOLDER,
                "someNewName.txt");
        
        File testACopy = testA.copy();
        File testBCopy = testB.copy();
        
        if(!testACopy.exists())
            fail("TestA was not copied");
        
        if(!testBCopy.exists())
            fail("TestB was not copied");
        
        if(testACopy.length() == 0)
            fail("TestA is empty");
        
        if(testBCopy.length() == 0)
            fail("TestB is empty");
        
        if(DEST_FOLDER.listFiles().length == 0){
            fail("Destination folder contains no files");
        }
    }
}
