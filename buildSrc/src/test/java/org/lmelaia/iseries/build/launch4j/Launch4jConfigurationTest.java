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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lmelaia.iseries.buildtest.utils.FileUtils;

/**
 *
 * @author Luke Melaia
 */
public class Launch4jConfigurationTest {

    public Launch4jConfigurationTest() {
    }

    private Launch4jConfiguration testObject
            = new Launch4jConfigurationBuilder()
            .setOutputFile("someout.exe")
            .setJarFile(System.getProperty("user.dir")
                    + "\\tests\\EmptyJar.jar")
            .setMinimumJreVersion("1.8.0")
            .useCustomClasspath(true)
            .setMainClass("mainclassdsdss")
            .setClasspath("AAA", "BBB")
            .create();

    private static FileUtils fileUtils;

    @BeforeClass
    public static void initialize() {
        try {
            fileUtils = new FileUtils(new String[][]{
                new String[]{"EmptyJar.jar"}});
            fileUtils.setup();
        } catch (Exception e) {
            fail("Couldn't create or setup file utils object\n" + e);
        }
    }

    @AfterClass
    public static void cleanup() {
        if (!fileUtils.teardown()) {
            System.err.println("Couldn't clean up test files for class: "
                    + Launch4jConfigurationTest.class);
        }
    }

    @Test
    public void getXMLTest() throws ParserConfigurationException {
        System.out.println(testObject.getConfigurationSource());
    }

    @Test
    public void getXMLAsStringTest() throws ParserConfigurationException,
            TransformerException {
        System.out.println(testObject.getConfigurationSourceAsString());
    }

}
