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

import java.io.File;
import java.util.Objects;

/**
 * Represents the list of libraries required by root project and libraries
 * licences.
 * 
 * @author Luke Melaia
 */
@SuppressWarnings("unused")
public class LibraryPackage {
    
    /**
     * A list of the library files (.jar)s
     */
    private final File[] libraries;
    
    /**
     * A list of the library licence files.
     */
    private final File[] licences;
    
    /**
     * Constructs a new library package.
     * 
     * @param libraries the list of libraries used by the root project.
     * @param licences the list of licences the libraries are licensed under.
     */
    LibraryPackage(File[] libraries, File[] licences){
        this.libraries = Objects.requireNonNull(libraries);
        this.licences = Objects.requireNonNull(licences);
    }

    /**
     * @return the library files
     */
    public File[] getLibraries() {
        return libraries;
    }

    /**
     * @return the licence files
     */
    public File[] getLicences() {
        return licences;
    }
}
