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
import java.util.Objects;

/**
 *
 * @author Luke Melaia
 */
public class PackFile {
    
    private final File source;
    
    //Optional Advanced
    
    private String targetFileRename;
    
    private String targetDirectory = "$INSTALL_PATH/";
    
    private OverwriteType overwrite = OverwriteType.UPDATE;
    
    private OperatingSystem operatingSystem = OperatingSystem.ALL;
    
    private String name = "All";
    
    private String version = "All";
    
    private String architecture = "All";
    
    public PackFile(File source){
        this.source = Objects.requireNonNull(source);
    }

    public File getSource() {
        return source;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = validateString(targetDirectory);
    }

    public OverwriteType getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(OverwriteType overwrite) {
        this.overwrite = Objects.requireNonNull(overwrite);
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = Objects.requireNonNull(operatingSystem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateString(name);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = validateString(version);
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = validateString(architecture);
    }
    
    public String getTargetFileRename() {
        return targetFileRename;
    }

    public void setTargetFileRename(String targetFileRename) {
        this.targetFileRename = validateString(targetFileRename);
    }
    
    private String validateString(String s){
        if(s == null || s.equals("")){
            throw new IllegalArgumentException(
                    "String must not be null or empty");
        }
        
        return s;
    }

    public enum OverwriteType{
        UPDATE,
        OVERWRITE,
        ASK_TRUE,
        ASK_FALSE;
    }
}
