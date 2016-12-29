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
package org.lmelaia.iseries.build.launch4j;

import org.lmelaia.iseries.build.launch4j.Launch4jConfigurationBuilder;

/**
 * A set of configuration settings for launch4j.
 *
 * <p>
 * An instance of this class can be obtained from null {@link Launch4jConfigurationBuilder#create()
 * }.
 * </p>
 *
 * <p>
 * This class provides a way to retrieve the settings passed to a
 * {@link Launch4jConfigurationBuilder} object and retrieve an xml document with
 * the settings in a format readable by Launch4j.</p>
 *
 * @author Luke Melaia
 */
public class Launch4jConfiguration {

    private Launch4jConfigurationBuilder l4jConfigurationBuilder;

    protected Launch4jConfiguration(
            Launch4jConfigurationBuilder l4jConfigurationBuilder) {
        this.l4jConfigurationBuilder = l4jConfigurationBuilder;
    }

    //********************
    //       GETTERS
    //********************
    
    public String getOutputFileName() {
        return l4jConfigurationBuilder.outputFileName;
    }

    public String getJarFileName() {
        return l4jConfigurationBuilder.jarFileName;
    }

    public boolean isWrapped() {
        return l4jConfigurationBuilder.wrap;
    }

    public String getJarRuntimePath() {
        return l4jConfigurationBuilder.jarRuntimePath;
    }

    public String getWrapperManifest() {
        return l4jConfigurationBuilder.wrapperManifest;
    }
    
    public String getIconFileName() {
        return l4jConfigurationBuilder.iconFileName;
    }
    
    public String getChangeDirectory() {
        return l4jConfigurationBuilder.changeDirectory;
    }
    
    public String getCommandLineArguments() {
        return l4jConfigurationBuilder.commandLineArguments;
    }
    
    public ProcessPriority getProcessPriority() {
        return l4jConfigurationBuilder.processPriority;
    }
    
    public boolean willStayAlive() {
        return l4jConfigurationBuilder.stayAlive;
    }
    
    public boolean willRestartAfterCrash() {
        return l4jConfigurationBuilder.restartAfterCrash;
    }
    
    public String getErrorTitle() {
        return l4jConfigurationBuilder.errorTitle;
    }
    
    public String getDownloadUrl() {
        return l4jConfigurationBuilder.downloadUrl;
    }
    
    public String getSupportUrl() {
        return l4jConfigurationBuilder.supportUrl;
    }
    
    public boolean usesCustomClasspath() {
        return l4jConfigurationBuilder.customClasspath;
    }
    
    public String getMainClass() {
        return l4jConfigurationBuilder.mainClass;
    }
    
    public String[] getClasspath() {
        return l4jConfigurationBuilder.classpath;
    }
    
    public HeaderType getHeaderType() {
        return l4jConfigurationBuilder.headerType;
    }
    
    public String[] getObjectFiles() {
        return l4jConfigurationBuilder.objectFiles;
    }
    
    public String[] getW32Api(){
        return l4jConfigurationBuilder.w32Api;
    }
    
    public boolean isSingleInstance() {
        return l4jConfigurationBuilder.singleInstance;
    }
    
    public String getMutexName() {
        return l4jConfigurationBuilder.mutexName;
    }
    
    public String getWindowTitle() {
        return l4jConfigurationBuilder.windowTitle;
    }
    
    public String getBundledJrePath() {
        return l4jConfigurationBuilder.bundledJrePath;
    }
    
    public boolean is64Bit() {
        return l4jConfigurationBuilder.is64bit;
    }
    
    public boolean isFallbackOption() {
        return l4jConfigurationBuilder.isFallbackOption;
    }
    
    public String getMinimumJreVersion() {
        return l4jConfigurationBuilder.minimumJreVersion;
    }
    
    public String getMaximumJreVersion() {
        return l4jConfigurationBuilder.maximumJreVersion;
    }
    
    public JavaUsageOptions getJavaUsageOptions() {
        return l4jConfigurationBuilder.javaUsageOptions;
    }
    
    public JavaArchitecture getJavaArchitecture() {
        return l4jConfigurationBuilder.javaArchitecture;
    }
    
    public int getInitialHeapSize() {
        return l4jConfigurationBuilder.initialHeapSize;
    }
    
    public int getMaximumHeapSize() {
        return l4jConfigurationBuilder.maximumHeapSize;
    }
    
    public boolean areHeapSizesInPercentage() {
        return l4jConfigurationBuilder.isHeapInPercent;
    }
    
    public String[] getJvmOptions() {
        return l4jConfigurationBuilder.jvmOptions;
    }
    
    public String[] getEnvironmentVariables() {
        return l4jConfigurationBuilder.environmentVariables;
    }
    
    public boolean isSplashScreenEnabled() {
        return l4jConfigurationBuilder.isSplashScreenEnabled;
    }
    
    public String getSplashScreenFileName() {
        return l4jConfigurationBuilder.splashScreenFileName;
    }
    
    public boolean willWaitForWindow() {
        return l4jConfigurationBuilder.waitForWindow;
    }
    
    public int getTimeout() {
        return l4jConfigurationBuilder.timeout;
    }
    
    public boolean willSingleErrorOnTimeout() {
        return l4jConfigurationBuilder.signalErrorOnTimeout;
    }
    
    public boolean isVersionInformationAdded() {
        return l4jConfigurationBuilder.addVersionInfo;
    }
    
    public String getFileVersion() {
        return l4jConfigurationBuilder.fileVersion;
    }
    
    public String getFreeFormFileVersion() {
        return l4jConfigurationBuilder.freeFormFileVersion;
    }
    
    public String getFileDescription() {
        return l4jConfigurationBuilder.fileDescription;
    }
    
    public String getCopyright() {
        return l4jConfigurationBuilder.copyright;
    }
    
    public String getProductVersion() {
        return l4jConfigurationBuilder.productVersion;
    }
    
    public String getFreeFormProductVersion() {
        return l4jConfigurationBuilder.freeFormProductVersion;
    }
    
    public String getProductName() {
        return l4jConfigurationBuilder.productName;
    }
    
    public String getCompanyName() {
        return l4jConfigurationBuilder.companyName;
    }
    
    public String getInternalName() {
        return l4jConfigurationBuilder.internalName;
    }
    
    public String getOriginalFileName() {
        return l4jConfigurationBuilder.originalFileName;
    }
    
    public String getStartupErrorMessage() {
        return l4jConfigurationBuilder.startupErrorMessage;
    }
    
    public String getBundledJreErrorMessage() {
        return l4jConfigurationBuilder.bundledJreErrorMessage;
    }
    
    public String getJreVersionErrorMessage() {
        return l4jConfigurationBuilder.jreVersionErrorMessage;
    }
    
    public String getLauncherErrorMessage() {
        return l4jConfigurationBuilder.launcherErrorMessage;
    }
    
    public String getInstanceAlreadyRunningErrorMessage() {
        return l4jConfigurationBuilder.instanceAlreadyRunningErrorMessage;
    }
}
