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
public class Shortcut {
    
    private final String name;
    
    private final File targetFile;
    
    private boolean placeInProgramGroup;
    
    private boolean placeInDesktop;
    
    private boolean placeInStartup;
    
    private boolean placeInApplications;
    
    private boolean placeInStartMenu;
    
    //Optional Advanced
    
    private String description;
    
    private String commandLineArguments;
    
    private String workingDirectory;
    
    private String iconFile;
    
    private int iconIndex;
    
    private InitialState initialState = InitialState.NO_SHOW;

    public Shortcut(String name, File targetFile){
        if(Objects.requireNonNull(name).equals("")){
            throw new IllegalArgumentException("name cannot be empty");
        }
        
        this.name = name;
        this.targetFile = Objects.requireNonNull(targetFile);
    }
    
    public String getName() {
        return name;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public boolean isPlaceInProgramGroup() {
        return placeInProgramGroup;
    }

    public void setPlaceInProgramGroup(boolean placeInProgramGroup) {
        this.placeInProgramGroup = placeInProgramGroup;
    }

    public boolean isPlaceInDesktop() {
        return placeInDesktop;
    }

    public void setPlaceInDesktop(boolean placeInDesktop) {
        this.placeInDesktop = placeInDesktop;
    }

    public boolean isPlaceInStartup() {
        return placeInStartup;
    }

    public void setPlaceInStartup(boolean placeInStartup) {
        this.placeInStartup = placeInStartup;
    }

    public boolean isPlaceInApplications() {
        return placeInApplications;
    }

    public void setPlaceInApplications(boolean placeInApplications) {
        this.placeInApplications = placeInApplications;
    }

    public boolean isPlaceInStartMenu() {
        return placeInStartMenu;
    }

    public void setPlaceInStartMenu(boolean placeInStartMenu) {
        this.placeInStartMenu = placeInStartMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateString(description);
    }

    public String getCommandLineArguments() {
        return commandLineArguments;
    }

    public void setCommandLineArguments(String commandLineArguments) {
        this.commandLineArguments = validateString(commandLineArguments);
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = validateString(workingDirectory);
    }

    public String getIconFile() {
        return iconFile;
    }

    public void setIconFile(String iconFile) {
        this.iconFile = validateString(iconFile);
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public void setIconIndex(int iconIndex) {
        if(iconIndex < 0){
            throw new IllegalArgumentException("iconIndex cannot be below 0");
        }
        
        this.iconIndex = iconIndex;
    }

    public InitialState getInitialState() {
        return initialState;
    }

    public void setInitialState(InitialState initialState) {
        this.initialState = Objects.requireNonNull(initialState);
    }
    
    private String validateString(String s){
        if(s == null || s.equals("")){
            throw new IllegalArgumentException(
                    "String must not be null or empty");
        }
        
        return s;
    }
    
    public enum InitialState{
        NO_SHOW,
        NORMAL,
        MAXAMIZED,
        MINIMIZED
    }
}
