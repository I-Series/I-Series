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

/**
 * Tests the {@link FileTestingUtils} class.
 * 
 * @author Luke Melaia
 */
public class TestFileTestingUtils {
    
    private static FileTestingUtils fileUtils;
    
    private static String file1 = "TestFileOne.test",
            file1Content = "Some content...",
            file2 = "TestFileTwo.test",
            path = System.getProperty("user.dir") + "\\tests\\";
    
    @BeforeClass
    public static void setup(){
        fileUtils = new FileTestingUtils(path, new String[][]{
            new String[]{file1, file1Content},
            new String[]{file2}});
    }
    
    @Test
    public void testSetup() throws IOException{
        fileUtils.setup();
        
        if(!new File(path + file1).exists()){
            fail("The file " + path + file1 + " wasn't created");
        }
        
        try(BufferedReader br = new BufferedReader(new FileReader(
                new File(path + file1)))) {
            if(!br.readLine().equals(file1Content)){
                fail("The file was not written to");
            }
        }
        
        if(!new File(path + file2).exists()){
            fail("The file " + path + file1 + " wasn't created");
        }
    }
    
    @Test
    public void testTeardown(){
        fileUtils.teardown();
        //@REM: Here because of bug
        System.out.println("new File(path + file1).exists()" + new File(path + file1).exists());
        if(new File(path + file1).exists()){
            fail("The file " + path + file1 + " wasn't deleted");
        }
        //@REM: Here because of bug
        System.out.println("new File(path + file1).exists()" + new File(path + file2).exists());
        if(new File(path + file2).exists()){
            fail("The file " + path + file1 + " wasn't deleted"); 
        }
    }
}
