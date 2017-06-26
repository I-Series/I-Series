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
import org.lmelaia.iseries.build.BuildConfiguration;

/**
 * Represents a file that needs to be copied over to the output folder, normally
 * a build file such as a jar or licence file.
 * 
 * @author Luke Melaia
 */
public class OutputCopyFile extends CopyFile{

    /**
     * Constructs a new copy file which, when copied, will be copied
     * to the output folder ({@link BuildConfiguration#SOUTPUT_FOLDER}).
     * 
     * @param source the source file to copy over.
     * @param newFileName optional new name of the copied file.
     */
    public OutputCopyFile(File source, String newFileName) {
        super(source, BuildConfiguration.SOUTPUT_FOLDER.getFile(), newFileName);
    }
    
    /**
     * Constructs a new copy file which, when copied, will be copied
     * to the output folder ({@link BuildConfiguration#SOUTPUT_FOLDER}).
     * 
     * @param source the source file to copy over.
     */
    @SuppressWarnings("unused")
    public OutputCopyFile(File source){
        this(source, null);
    }

}
