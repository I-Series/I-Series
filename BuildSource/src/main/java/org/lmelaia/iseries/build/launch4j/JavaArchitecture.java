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

/**
 * The java runtime environment architecture launch4j will look for.
 *
 * @author Luke Melaia
 */
@SuppressWarnings("unused")
public enum JavaArchitecture {

    /**
     * Only 64bit architecture will be used.
     */
    ONLY_64BIT("64"),
    /**
     * 64Bit architecture will be used first, if available, otherwise 32bit will
     * be used.
     */
    _64BIT_THEN_32BIT("64/32"),
    /**
     * 32Bit architecture will be used first, if available, otherwise 64bit will
     * be used.
     */
    _32BIT_THEN_64BIT("32/64"),
    /**
     * Only 32bit architecture will be used.
     */
    ONLY_32BIT("32");

    private final String officialName;
    
    JavaArchitecture(String officalName){
        this.officialName = officalName;
    }
    
    /**
     * @return the name of the property used in the launch4j configuration file. 
     */
    public String getOfficalName() {
        return this.officialName;
    }
}
