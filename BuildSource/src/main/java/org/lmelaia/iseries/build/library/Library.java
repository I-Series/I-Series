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
package org.lmelaia.iseries.build.library;

import java.util.Objects;
import org.lmelaia.iseries.build.licence.Licence;
import org.lmelaia.iseries.build.utils.CopyFile;

/**
 * Represents a library used by the root project.
 * 
 * @author Luke Melaia
 */
public class Library {
    
    /**
     * The actual name of the library. Excluding version numbers.
     */
    private final String name;
    
    /**
     * The file name of the licence. Excluding the {@code .jar} extension.
     */
    private final String fileName;
    
    /**
     * The licence the library is licensed under.
     */
    private final Licence licence;
    
    /**
     * The library jar file that will be copied over.
     */
    private CopyFile libraryFile;
    
    /**
     * The licence file that will be copied over.
     */
    private CopyFile licenceFile;
    
    /**
     * Creates a new instance of library which represents a library used
     * by the root project.
     * 
     * @param name The actual name of the library. Excluding version numbers.
     * @param fileName The file name of the licence. Excluding the {@code .jar}
     *                 extension.
     * @param licence The licence the library is licensed under.
     */
    public Library(String name, String fileName, Licence licence){
        this.name = Objects.requireNonNull(name);
        this.fileName = Objects.requireNonNull(fileName + ".jar");
        this.licence = Objects.requireNonNull(licence);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the licence
     */
    public Licence getLicence() {
        return licence;
    }

    /**
     * @return the libraryFile
     */
    protected CopyFile getLibraryFile() {
        return libraryFile;
    }

    /**
     * @param libraryFile the libraryFile to set
     */
    protected void setLibraryFile(CopyFile libraryFile) {
        this.libraryFile = Objects.requireNonNull(libraryFile);
    }

    /**
     * @return the licenceFile
     */
    protected CopyFile getLicenceFile() {
        return licenceFile;
    }

    /**
     * @param licenceFile the licenceFile to set
     */
    protected void setLicenceFile(CopyFile licenceFile) {
        this.licenceFile = Objects.requireNonNull(licenceFile);
    }
}
