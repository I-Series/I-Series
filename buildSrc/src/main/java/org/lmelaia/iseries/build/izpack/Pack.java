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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Luke Melaia
 */
public class Pack {

    private final String name;

    private final PackFile[] packFiles;

    private boolean required;

    private boolean preSelected;

    private String description;

    //Optional Advanced
    
    private File packScreenshot;

    private OperatingSystem operatingSystem;

    private String osName = "All";

    private String osVersion = "All";

    private String osArchitecture = "All";

    private List<String> dependsOn;

    public Pack(String name, PackFile... packFiles) {
        this.name = validateString(name);
        this.packFiles = Objects.requireNonNull(packFiles);
    }

    public String getName() {
        return name;
    }

    public PackFile[] getPackFiles() {
        return packFiles;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isPreSelected() {
        return preSelected;
    }

    public void setPreSelected(boolean preSelected) {
        this.preSelected = preSelected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateString(description);
    }

    public File getPackScreenshot() {
        return packScreenshot;
    }

    public void setPackScreenshot(File packScreenshot) {
        this.packScreenshot = Objects.requireNonNull(packScreenshot);
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = Objects.requireNonNull(operatingSystem);
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = validateString(osName);
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = validateString(osVersion);
    }

    public String getOsArchitecture() {
        return osArchitecture;
    }

    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = validateString(osArchitecture);
    }

    public String[] getDependencies() {
        return (dependsOn != null)
                ? dependsOn.toArray(new String[dependsOn.size()]) : null;
    }

    public void addDependency(String dependency) {
        if(dependsOn == null)
            dependsOn = new ArrayList<>();
        
        dependsOn.add(dependency);
    }
    
    public boolean removeDependency(String dependency){
        if(dependsOn == null)
            return false;
        
        return dependsOn.remove(dependency);
    }

    private String validateString(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException(
                    "String must not be null or empty");
        }

        return s;
    }
}
