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
package org.lmelaia.iseries.build.izpack;

import java.io.File;

/**
 * A wrapper for the installers info panel.
 * 
 * @author Luke Melaia
 */
public class InfoPanel {
    
    private final File infoFile;
    
    /**
     * 
     * @param infoFile the file which contains the info text displayed by the
     * installer.
     */
    public InfoPanel(File infoFile){
        this.infoFile = infoFile;
    }

    public File getInfoFile() {
        return infoFile;
    }
    
}
