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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.lmelaia.iseries.build.BuildConfiguration;

/**
 * An enum list of the available licences.
 * 
 * @author Luke Melaia
 */
public enum Licences implements Licence{
    
    /**
     * The GNU General Public Licence Version 3.0.
     */
    GNU{
        @Override
        public String getName(){
            return "GNU General Public License 3.0";
        }
        
        @Override
        public File getFile(){
            return getLicenceFile(getName() + ".txt");
        }
    },
    
    /**
     * The Apache 2.0 licence.
     */
    APACHE{
        @Override
        public String getName(){
            return "Apache 2.0";
        }
        
        @Override
        public File getFile(){
            return getLicenceFile(getName() + ".txt");
        }
    };

    /**
     * {@inheritDoc }
     * @return 
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * {@inheritDoc }
     * @return 
     */
    @Override
    public File getFile() {
        return null;
    }
    
    /**
     * Copies the licence file to a new file.
     * 
     * @param dest the destination file.
     * @return {@code true} if the file was successfully copied, {@code false}
     * if an exception was thrown while copying.
     */
    public boolean copyOver(File dest){
        try {
            FileUtils.copyFile(this.getFile(), dest);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * @param fileName the file name of the licence.
     * @return a licence file from the licences folder with the name
     * matching the {@code fileName}
     */
    private static File getLicenceFile(String fileName){
        File licenceFile 
                = new File(BuildConfiguration.LICENCES_PATH + fileName);
        
        if(!licenceFile.exists()){
            return null;
        }
        
        return licenceFile;
    }
}
