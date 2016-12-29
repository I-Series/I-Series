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
 * An instance of this class can be obtained from
 * {@link Launch4jConfigurationBuilder#create()}.
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
    
    /**
     * See {@link Launch4jConfigurationBuilder#outputFileName} for
     * documentation.
     */
    public String getOutputFileName() {
        return l4jConfigurationBuilder.outputFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#jarFileName} for
     * documentation.
     */
    public String getJarFileName() {
        return l4jConfigurationBuilder.jarFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#wrap} for
     * documentation.
     */
    public boolean isWrapped() {
        return l4jConfigurationBuilder.wrap;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#jarRuntimePath} for
     * documentation.
     */
    public String getJarRuntimePath() {
        return l4jConfigurationBuilder.jarRuntimePath;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#wrapperManifest} for
     * documentation.
     */
    public String getWrapperManifest() {
        return l4jConfigurationBuilder.wrapperManifest;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#iconFileName} for
     * documentation.
     */
    public String getIconFileName() {
        return l4jConfigurationBuilder.iconFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#changeDirectory} for
     * documentation.
     */
    public String getChangeDirectory() {
        return l4jConfigurationBuilder.changeDirectory;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#commandLineArguments} for
     * documentation.
     */
    public String getCommandLineArguments() {
        return l4jConfigurationBuilder.commandLineArguments;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#processPriority} for
     * documentation.
     */
    public ProcessPriority getProcessPriority() {
        return l4jConfigurationBuilder.processPriority;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#stayAlive} for
     * documentation.
     */
    public boolean willStayAlive() {
        return l4jConfigurationBuilder.stayAlive;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#restartAfterCrash} for
     * documentation.
     */
    public boolean willRestartAfterCrash() {
        return l4jConfigurationBuilder.restartAfterCrash;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#errorTitle} for
     * documentation.
     */
    public String getErrorTitle() {
        return l4jConfigurationBuilder.errorTitle;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#downloadUrl} for
     * documentation.
     */
    public String getDownloadUrl() {
        return l4jConfigurationBuilder.downloadUrl;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#supportUrl} for
     * documentation.
     */
    public String getSupportUrl() {
        return l4jConfigurationBuilder.supportUrl;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#customClasspath} for
     * documentation.
     */
    public boolean usesCustomClasspath() {
        return l4jConfigurationBuilder.customClasspath;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#mainClass} for
     * documentation.
     */
    public String getMainClass() {
        return l4jConfigurationBuilder.mainClass;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#classpath} for
     * documentation.
     */
    public String[] getClasspath() {
        return l4jConfigurationBuilder.classpath;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#headerType} for
     * documentation.
     */
    public HeaderType getHeaderType() {
        return l4jConfigurationBuilder.headerType;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#objectFiles} for
     * documentation.
     */
    public String[] getObjectFiles() {
        return l4jConfigurationBuilder.objectFiles;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#w32Api} for
     * documentation.
     */
    public String[] getW32Api() {
        return l4jConfigurationBuilder.w32Api;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#singleInstance} for
     * documentation.
     */
    public boolean isSingleInstance() {
        return l4jConfigurationBuilder.singleInstance;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#mutexName} for
     * documentation.
     */
    public String getMutexName() {
        return l4jConfigurationBuilder.mutexName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#windowTitle} for
     * documentation.
     */
    public String getWindowTitle() {
        return l4jConfigurationBuilder.windowTitle;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#bundledJrePath} for
     * documentation.
     */
    public String getBundledJrePath() {
        return l4jConfigurationBuilder.bundledJrePath;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#is64bit} for
     * documentation.
     */
    public boolean is64Bit() {
        return l4jConfigurationBuilder.is64bit;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#isFallbackOption} for
     * documentation.
     */
    public boolean isFallbackOption() {
        return l4jConfigurationBuilder.isFallbackOption;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#minimumJreVersion} for
     * documentation.
     */
    public String getMinimumJreVersion() {
        return l4jConfigurationBuilder.minimumJreVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#maximumJreVersion} for
     * documentation.
     */
    public String getMaximumJreVersion() {
        return l4jConfigurationBuilder.maximumJreVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#javaUsageOptions} for
     * documentation.
     */
    public JavaUsageOptions getJavaUsageOptions() {
        return l4jConfigurationBuilder.javaUsageOptions;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#javaArchitecture} for
     * documentation.
     */
    public JavaArchitecture getJavaArchitecture() {
        return l4jConfigurationBuilder.javaArchitecture;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#initialHeapSize} for
     * documentation.
     */
    public int getInitialHeapSize() {
        return l4jConfigurationBuilder.initialHeapSize;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#maximumHeapSize} for
     * documentation.
     */
    public int getMaximumHeapSize() {
        return l4jConfigurationBuilder.maximumHeapSize;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#isHeapInPercent} for
     * documentation.
     */
    public boolean areHeapSizesPercentages() {
        return l4jConfigurationBuilder.isHeapInPercent;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#jarFileName} for
     * documentation.
     */
    public String[] getJvmOptions() {
        return l4jConfigurationBuilder.jvmOptions;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#environmentVariables} for
     * documentation.
     */
    public String[] getEnvironmentVariables() {
        return l4jConfigurationBuilder.environmentVariables;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#isSplashScreenEnabled} for
     * documentation.
     */
    public boolean isSplashScreenEnabled() {
        return l4jConfigurationBuilder.isSplashScreenEnabled;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#splashScreenFileName} for
     * documentation.
     */
    public String getSplashScreenFileName() {
        return l4jConfigurationBuilder.splashScreenFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#waitForWindow} for
     * documentation.
     */
    public boolean willWaitForWindow() {
        return l4jConfigurationBuilder.waitForWindow;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#timeout} for
     * documentation.
     */
    public int getTimeout() {
        return l4jConfigurationBuilder.timeout;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#signalErrorOnTimeout} for
     * documentation.
     */
    public boolean willSingleErrorOnTimeout() {
        return l4jConfigurationBuilder.signalErrorOnTimeout;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#addVersionInfo} for
     * documentation.
     */
    public boolean isVersionInformationAdded() {
        return l4jConfigurationBuilder.addVersionInfo;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#fileVersion} for
     * documentation.
     */
    public String getFileVersion() {
        return l4jConfigurationBuilder.fileVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#freeFormFileVersion} for
     * documentation.
     */
    public String getFreeFormFileVersion() {
        return l4jConfigurationBuilder.freeFormFileVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#fileDescription} for
     * documentation.
     */
    public String getFileDescription() {
        return l4jConfigurationBuilder.fileDescription;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#copyright} for
     * documentation.
     */
    public String getCopyright() {
        return l4jConfigurationBuilder.copyright;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#productVersion} for
     * documentation.
     */
    public String getProductVersion() {
        return l4jConfigurationBuilder.productVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#freeFormProductVersion} for
     * documentation.
     */
    public String getFreeFormProductVersion() {
        return l4jConfigurationBuilder.freeFormProductVersion;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#productName} for
     * documentation.
     */
    public String getProductName() {
        return l4jConfigurationBuilder.productName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#companyName} for
     * documentation.
     */
    public String getCompanyName() {
        return l4jConfigurationBuilder.companyName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#internalName} for
     * documentation.
     */
    public String getInternalName() {
        return l4jConfigurationBuilder.internalName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#originalFileName} for
     * documentation.
     */
    public String getOriginalFileName() {
        return l4jConfigurationBuilder.originalFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#startupErrorMessage} for
     * documentation.
     */
    public String getStartupErrorMessage() {
        return l4jConfigurationBuilder.startupErrorMessage;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#bundledJreErrorMessage} for
     * documentation.
     */
    public String getBundledJreErrorMessage() {
        return l4jConfigurationBuilder.bundledJreErrorMessage;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#jreVersionErrorMessage} for
     * documentation.
     */
    public String getJreVersionErrorMessage() {
        return l4jConfigurationBuilder.jreVersionErrorMessage;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#launcherErrorMessage} for
     * documentation.
     */
    public String getLauncherErrorMessage() {
        return l4jConfigurationBuilder.launcherErrorMessage;
    }

    /**
     * See 
     * {@link Launch4jConfigurationBuilder#instanceAlreadyRunningErrorMessage}
     * for
     * documentation.
     */
    public String getInstanceAlreadyRunningErrorMessage() {
        return l4jConfigurationBuilder.instanceAlreadyRunningErrorMessage;
    }
}
