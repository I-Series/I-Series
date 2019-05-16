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
package org.lmelaia.iseries.build.licence;

import org.lmelaia.iseries.build.BuildConfiguration;

import java.io.File;

/**
 * An enum list of the available licences.
 * 
 * @author Luke Melaia
 */
public enum Licences implements Licence{
    
    /**
     * The GNU General Public Licence Version 3.0.
     */
    GNU("GNU General Public License 3.0"),
    
    /**
     * The Apache 2.0 licence.
     */
    APACHE("Apache 2.0"),

    /**
     * The text file containing the list
     * of credits/acknowledgements and
     * the licences that come with them.
     * <br/>
     * This is not a license but it's
     * treated as one for simplicity.
     */
    ACKNOWLEDGEMENTS("Acknowledgements");

    /**
     * The name of the licence.
     * 
     * <p>
     * Normally, the name of the licence file is the same as the
     * name of the licence. This allows the file name and licence
     * name to defined in a single string. If the file name differs,
     * the method {@link #getFile() } will need to be overridden.
     * 
     * <p>
     * <b>When using the default functionality, the licence file
     * should have a file extension of {@code .txt}.</b>
     * 
     */
    private final String name;
    
    /**
     * @param name the name of the licence and, by convention, the
     * name of the licence file.
     */
    Licences(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc }
     * @return 
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc }
     * @return 
     */
    @Override
    public File getFile() {
        return getLicenceFile(getName() + ".txt");
    }

    /**
     * @param fileName the file name of the licence.
     * @return a licence file from the licences folder with the name
     * matching the {@code fileName} or {@code null} if the file
     * couldn't be found.
     */
    private static File getLicenceFile(String fileName){
        File licenceFile 
                = BuildConfiguration.SLICENCES_FOLDER.forward(fileName)
                        .getFile();
        
        if(!licenceFile.exists()){
            return null;
        }
        
        return licenceFile;
    }
}
