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
import java.util.Objects;
import org.apache.commons.io.FileUtils;

/**
 * Represents a file that needs to be copied over
 * from one folder to another.
 * 
 * @author Luke Melaia
 */
public class CopyFile {
    
    /**
     * The file to copy over.
     */
    private File source;
    
    /**
     * The folder to copy the file to.
     */
    private File destFolder;
    
    /**
     * Optional new name of the copied file.
     */
    private String newFileName;
    
    /**
     * Constructs a new copy file instance.
     * 
     * @param source the source file to copy over.
     * @param destFolder the folder to copy the file to.
     * @param newFileName optional new name of the copied file.
     */
    public CopyFile(File source, File destFolder, String newFileName){
        if(newFileName == null || newFileName.equals("")){
            newFileName = null;
        }
        
        this.source = Objects.requireNonNull(source);
        this.destFolder = Objects.requireNonNull(destFolder);
        this.newFileName = newFileName;
    }
    
    /**
     * Constructs a new copy file instance. The new file will
     * have the same name as the old.
     * 
     * @param source the source file to copy over.
     * @param destFolder the folder to copy the file to.
     */
    public CopyFile(File source, File destFolder){
        this(source, destFolder, null);
    }
    
    /**
     * Copies the file to the new destination.
     * 
     * @throws IOException if an error occurs while copying the file.
     * @return the new file.
     */
    public File copy() throws IOException{
        if(!source.exists()){
            throw new IllegalArgumentException("Source file must exist");
        }
        
        if(!destFolder.exists()){
            try{
                FileUtils.forceMkdir(destFolder);
            } catch (IOException ex){
                throw new IllegalArgumentException(
                        "Destination folder must exist and couldn't be created"
                );
            }
        }
        
        if(!destFolder.isDirectory()){
            throw new IllegalArgumentException(
                    "Destination folder must be a folder");
        }
        
        File dest;

        if (newFileName == null) {
            dest = new File(destFolder.getAbsolutePath() + "/" + source.getName());
        } else {
            newFileName = newFileName.replace("\\", "/");
            dest = new File(destFolder.getAbsolutePath() + "/" + newFileName);
        }

        FileUtils.copyFile(source, dest);
        return dest;
    }
    
    /**
     * {@inheritDoc }
     * 
     * @return 
     */
    @Override
    public String toString(){
        return String.format("%s[source=%s, destination=%s, newName=%s]",
                this.getClass().getName(), source.toString(),
                destFolder.toString(), newFileName);
    }
}
