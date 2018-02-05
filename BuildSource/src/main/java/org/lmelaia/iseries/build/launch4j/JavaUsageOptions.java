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
 * Which java runtime to use, the JDK runtime or the public java runtime
 * environment.
 *
 * @author Luke Melaia
 */
@SuppressWarnings("unused")
public enum JavaUsageOptions {

    /**
     * Only the public java runtime environment will be used.
     */
    ONLY_JRE("jreOnly"),
    /**
     * The public java runtime environment will be used unless the JDK runtime
     * is newer.
     */
    JRE_OVER_JDK("preferJre"),
    /**
     * The JDK runtime will be used unless the public java runtime environment
     * is newer.
     */
    JDK_OVER_JRE("preferJdk"),
    /**
     * Only the JDK java runtime environment will be used.
     */
    ONLY_JDK("jdkOnly");

    private final String l4jOfficialName;

    JavaUsageOptions(String l4jOfficialName) {
        this.l4jOfficialName = l4jOfficialName;
    }
    
    /**
     * @return the name of the property used in the launch4j configuration file. 
     */
    public String getOfficialName(){
        return this.l4jOfficialName;
    }
    
    
}
