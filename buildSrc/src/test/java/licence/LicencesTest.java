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
package licence;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Tests that each licence in the licences enum points to a valid file.
 * 
 * @author Luke Melaia
 */
public class LicencesTest {
    
    public LicencesTest() {
    }

    /**
     * Tests each licence for a valid name (not null or empty).
     */
    @Test
    public void testLicenceNames(){
        for(Licence licence : Licences.values()){
            if(licence.getName()== null || licence.getName().equals(""))
                fail("The Licence: " + "\"" + licence.toString() + "\" doesn't "
                        + "have a valid name");
        }
    }
    
    /**
     * Tests each licence for an existing licence file. If a licence doesn't
     * return a valid file, this method will fail.
     */
    @Test
    public void testLicenceFiles() {
        for(Licence licence : Licences.values()){
            if(licence.getFile() == null || !licence.getFile().exists())
                fail("The Licence: " + "\"" + licence.toString()+ "\" doesn't "
                        + "have a valid licence file");
        }
    }
    
}
