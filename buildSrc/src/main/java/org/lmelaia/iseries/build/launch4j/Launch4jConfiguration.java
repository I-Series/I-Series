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

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.lmelaia.iseries.build.utils.XmlDocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.lmelaia.iseries.build.utils.XmlDocumentHelper.ElementHelper;

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
     * See {@link Launch4jConfigurationBuilder#jarFileName} for documentation.
     */
    public String getJarFileName() {
        return l4jConfigurationBuilder.jarFileName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#wrap} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#iconFileName} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#stayAlive} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#errorTitle} for documentation.
     */
    public String getErrorTitle() {
        return l4jConfigurationBuilder.errorTitle;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#downloadUrl} for documentation.
     */
    public String getDownloadUrl() {
        return l4jConfigurationBuilder.downloadUrl;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#supportUrl} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#mainClass} for documentation.
     */
    public String getMainClass() {
        return l4jConfigurationBuilder.mainClass;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#classpath} for documentation.
     */
    public String[] getClasspath() {
        return l4jConfigurationBuilder.classpath;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#headerType} for documentation.
     */
    public HeaderType getHeaderType() {
        return l4jConfigurationBuilder.headerType;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#objectFiles} for documentation.
     */
    public String[] getObjectFiles() {
        return l4jConfigurationBuilder.objectFiles;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#w32Api} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#mutexName} for documentation.
     */
    public String getMutexName() {
        return l4jConfigurationBuilder.mutexName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#windowTitle} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#is64bit} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#jarFileName} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#waitForWindow} for documentation.
     */
    public boolean willWaitForWindow() {
        return l4jConfigurationBuilder.waitForWindow;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#timeout} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#fileVersion} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#copyright} for documentation.
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
     * See {@link Launch4jConfigurationBuilder#productName} for documentation.
     */
    public String getProductName() {
        return l4jConfigurationBuilder.productName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#companyName} for documentation.
     */
    public String getCompanyName() {
        return l4jConfigurationBuilder.companyName;
    }

    /**
     * See {@link Launch4jConfigurationBuilder#internalName} for documentation.
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
     * for documentation.
     */
    public String getInstanceAlreadyRunningErrorMessage() {
        return l4jConfigurationBuilder.instanceAlreadyRunningErrorMessage;
    }

    //******************
    //        XML
    //******************
    /**
     * @return a {@link DOMSource} object containing all the properties in an
     * xml format readable by launch4j.
     */
    public DOMSource getConfigurationSource() throws
            ParserConfigurationException {
        XmlDocumentHelper documentHelper 
                = XmlDocumentHelper.getInstanceWithNewDocument()
                .newRootElement("launch4jConfig").getDocumentHelper();
        
        //Content below
        appendBasic(documentHelper);

        if (usesCustomClasspath()) {
            appendClasspath(documentHelper);
        }

        appendHeader(documentHelper);

        if (isSingleInstance()) {
            appendSingleInstance(documentHelper);
        }

        appendJre(documentHelper);
        appendEnvironmentVariables(documentHelper);
        appendSplashScreen(documentHelper);
        appendVersionInfo(documentHelper);
        appendMessages(documentHelper);

        return new DOMSource(documentHelper.getXmlDocument());
    }

    /**
     * Appends the basic properties to the {@code rootElement}.
     *
     * @param rootElement
     * @param doc
     */
    private void appendBasic(XmlDocumentHelper doc) {
//        Element outputFile = doc.createElement("outfile");
//        outputFile.appendChild(doc.createTextNode(getOutputFileName()));
//        rootElement.appendChild(outputFile);
        
        doc.getRootElement().addNewElement("outfile")
                .addText(getOutputFileName());

//        Element jarFile = doc.createElement("jar");
//        jarFile.appendChild(doc.createTextNode(getJarFileName()));
//        rootElement.appendChild(jarFile);
        
        doc.getRootElement().addNewElement("jar")
                .addText(getJarFileName());

        //Element wrap = doc.createElement("dontWrapJar");
        //wrap.appendChild(doc.createTextNode(String.valueOf(!isWrapped())));
        //rootElement.appendChild(wrap);

        doc.getRootElement().addNewElement("dontWrapJar")
                .addText(!isWrapped());
        
//        Element manifest = doc.createElement("manifest");
//        manifest.appendChild(doc.createTextNode(
//                nullToEmpty(getWrapperManifest())));
//        rootElement.appendChild(manifest);

        doc.getRootElement().addNewElement("manifest")
                .addText(getWrapperManifest());
        
        //Element icon = doc.createElement("icon");
        //icon.appendChild(doc.createTextNode(nullToEmpty(getIconFileName())));
        //rootElement.appendChild(icon);

        doc.getRootElement().addNewElement("icon")
                .addText(getIconFileName());
        
        //Element changeDirectory = doc.createElement("chdir");
        //changeDirectory.appendChild(doc.createTextNode(getChangeDirectory()));
        //rootElement.appendChild(changeDirectory);

        doc.getRootElement().addNewElement("chdir")
                .addText(getChangeDirectory());
        
        //Element cmdLine = doc.createElement("cmdLine");
        //cmdLine.appendChild(doc.createTextNode(
        //        nullToEmpty(getCommandLineArguments())));
        //rootElement.appendChild(cmdLine);

        doc.getRootElement().addNewElement("cmdLine")
                .addText(getCommandLineArguments());
        
        //Element priority = doc.createElement("priority");
        //priority.appendChild(doc.createTextNode(
        //        getProcessPriority().name().toLowerCase()));
        //rootElement.appendChild(priority);

        doc.getRootElement().addNewElement("priority")
                .addText(getProcessPriority().name().toLowerCase());
        
        //Element stayAlive = doc.createElement("stayAlive");
        //stayAlive.appendChild(doc.createTextNode(
        //        String.valueOf(willStayAlive())));
        //rootElement.appendChild(stayAlive);

        doc.getRootElement().addNewElement("stayAlive")
                .addText(willStayAlive());
        
        //Element restartOnCrash = doc.createElement("restartOnCrash");
        //restartOnCrash.appendChild(doc.createTextNode(
        //        String.valueOf(willRestartAfterCrash())));
        //rootElement.appendChild(restartOnCrash);

        doc.getRootElement().addNewElement("restartOnCrash")
                .addText(willRestartAfterCrash());
        
        //Element errTitle = doc.createElement("errTitle");
        //errTitle.appendChild(doc.createTextNode(nullToEmpty(getErrorTitle())));
        //rootElement.appendChild(errTitle);

        doc.getRootElement().addNewElement("errTitle")
                .addText(getErrorTitle());
        
        //Element downloadUrl = doc.createElement("downloadUrl");
        //downloadUrl.appendChild(doc.createTextNode(getDownloadUrl()));
        //rootElement.appendChild(downloadUrl);

        doc.getRootElement().addNewElement("downloadUrl")
                .addText(getDownloadUrl());
        
        //Element supportUrl = doc.createElement("supportUrl");
        //supportUrl.appendChild(doc.createTextNode(
        //        nullToEmpty(getSupportUrl())));
        //rootElement.appendChild(supportUrl);
        
        doc.getRootElement().addNewElement("supportUrl")
                .addText(getSupportUrl());
    }

    /**
     * Appends the classpath properties to the {@code rootElement}.
     *
     * @param rootElement
     * @param doc
     */
    private void appendClasspath(XmlDocumentHelper doc) {
        //Element classPath = doc.createElement("classPath");

        XmlDocumentHelper.ElementHelper classpath 
                = doc.getRootElement().addNewElement("classPath");
        
        //Element mainClass = doc.createElement("mainClass");
        //mainClass.appendChild(doc.createTextNode(nullToEmpty(getMainClass())));
        //classPath.appendChild(mainClass);

        classpath.addNewElement("mainClass")
                .addText(getMainClass());
                
        for (String s : getClasspath()) {
            //cp = doc.createElement("cp");
            //cp.appendChild(doc.createTextNode(s));
            //classPath.appendChild(cp);
            
            classpath.addNewElement("cp")
                    .addText(s);
        }
    }

    /**
     * Appends the header properties to the {@code rootElement}.
     *
     * @param rootElement
     * @param doc
     */
    public void appendHeader(XmlDocumentHelper doc) {
        //Element headerType = doc.createElement("headerType");
        
        doc.getRootElement().addNewElement("headerType")
                .addText(getHeaderType().toString().toLowerCase());
        
        if (getObjectFiles() != null) {
            for (String objectFile : getObjectFiles()) {
                //Element obj = doc.createElement("obj");
                //obj.appendChild(doc.createTextNode(objectFile));
                //rootElement.appendChild(obj);
                
                doc.getRootElement().addNewElement("obj")
                        .addText(objectFile);
            }
        }

        if (getW32Api() != null) {
            for (String w32api : getW32Api()) {
                //Element api = doc.createElement("api");
                //api.appendChild(doc.createTextNode(w32api));
                //rootElement.appendChild(api);
                
                doc.getRootElement().addNewElement("api")
                        .addText(w32api);
            }
        }
    }

    /**
     * Appends the single instance properties to the {@code rootElement}.
     *
     * @param rootElement
     * @param doc
     */
    public void appendSingleInstance(XmlDocumentHelper doc) {
        //Element singleInstance = doc.createElement("singleInstance");

        ElementHelper singleInstance 
                = doc.getRootElement().addNewElement("singleInstance");
        
        //Element mutexName = doc.createElement("mutexName");
        //mutexName.appendChild(doc.createTextNode(getMutexName()));
        //singleInstance.appendChild(mutexName);

        singleInstance.addNewElement("mutexName")
                .addText(getMutexName());
        
        //Element windowTitle = doc.createElement("windowTitle");
        //windowTitle.appendChild(doc.createTextNode(
        //        (getWindowTitle() == null) ? "" : getWindowTitle()));
        //singleInstance.appendChild(windowTitle);

        singleInstance.addNewElement("windowTitle")
                .addText(getWindowTitle());
        
        //rootElement.appendChild(singleInstance);
    }

    public void appendJre(XmlDocumentHelper doc) {
        //Element jre = doc.createElement("jre");

        ElementHelper jre = doc.getRootElement().addNewElement("jre");
        
        //Element path = doc.createElement("path");
        //path.appendChild(doc.createTextNode(
        //        (getBundledJrePath() == null) ? "" : getBundledJrePath()));
        //jre.appendChild(path);

        jre.addNewElement("path")
                .addText(getBundledJrePath());
        
        //Element bundledJre64Bit = doc.createElement("bundledJre64Bit");
        //bundledJre64Bit.appendChild(doc.createTextNode(
        //        String.valueOf(is64Bit())));
        //jre.appendChild(bundledJre64Bit);

        jre.addNewElement("bundledJre64Bit")
                .addText(is64Bit());
        
        //Element bundledJreAsFallback
        //        = doc.createElement("bundledJreAsFallback");
        //bundledJreAsFallback.appendChild(doc.createTextNode(
        //        String.valueOf(isFallbackOption())));
        //jre.appendChild(bundledJreAsFallback);

        jre.addNewElement("bundledJreAsFallback")
                .addText(isFallbackOption());
        
        //Element minVersion = doc.createElement("minVersion");
        //minVersion.appendChild(doc.createTextNode(getMinimumJreVersion()));
        //jre.appendChild(minVersion);

        jre.addNewElement("minVersion")
                .addText(getMinimumJreVersion());
        
        //Element maxVersion = doc.createElement("maxVersion");
        //maxVersion.appendChild(doc.createTextNode(
        //        (getMaximumJreVersion() == null) ?
        //                "" : getMaximumJreVersion()
        //));
        //jre.appendChild(maxVersion);
        
        jre.addNewElement("maxVersion")
                .addText(getMaximumJreVersion());

        //Element jdkPreference = doc.createElement("jdkPreference");
        //jdkPreference.appendChild(doc.createTextNode(
        //        this.getJavaUsageOptions().getOfficalName())
        //);
        //jre.appendChild(jdkPreference);

        jre.addNewElement("jdkPreference")
                .addText(getJavaUsageOptions().getOfficalName());
        
        //Element runtimeBits = doc.createElement("runtimeBits");
        //runtimeBits.appendChild(doc.createTextNode(
        //        getJavaArchitecture().getOfficalName())
        //);
        //jre.appendChild(runtimeBits);

        jre.addNewElement("runtimeBits")
                .addText(getJavaArchitecture().getOfficalName());
        
        if (getInitialHeapSize() != 0) {
            //Element initialHeapSize = doc.createElement(
            //        (areHeapSizesPercentages()) ? "initialHeapPercent"
            //                : "initialHeapSize"
            //);
            //initialHeapSize.appendChild(doc.createTextNode(
            //        getInitialHeapSize() + ""
            //));
            //jre.appendChild(initialHeapSize);
            
            jre.addNewElement((areHeapSizesPercentages()) ? "initialHeapPercent"
                            : "initialHeapSize")
                    .addText(getInitialHeapSize());
        }
        
        if (getMaximumHeapSize() != 0) {
            //Element maxHeapSize = doc.createElement(
            //        (areHeapSizesPercentages()) ? "maxHeapPercent"
            //                : "maxHeapSize"
            //);
            //maxHeapSize.appendChild(doc.createTextNode(
            //        getMaximumHeapSize() + ""
            //));
            //jre.appendChild(maxHeapSize);
            
            jre.addNewElement((areHeapSizesPercentages()) ? "maxHeapPercent"
                            : "maxHeapSize")
                    .addText(getMaximumHeapSize());
        }
        
        if(getJvmOptions() != null)
            for(String option : getJvmOptions()){
                //Element opt = doc.createElement("opt");
                //opt.appendChild(doc.createTextNode(option));
                //jre.appendChild(opt);
                
                jre.addNewElement("opt")
                        .addText(option);
            }

        //rootElement.appendChild(jre);
    }
    
    public void appendEnvironmentVariables(XmlDocumentHelper doc) {
        if(getEnvironmentVariables() != null)
            for(String variable : getEnvironmentVariables()){
                //Element var = doc.createElement("var");
                //var.appendChild(doc.createTextNode(variable));
                //doc.appendChild(var);
                
                doc.getRootElement().addNewElement("var")
                        .addText(variable);
            }
    }
    
    public void appendSplashScreen(XmlDocumentHelper doc){
        if(!isSplashScreenEnabled()){
            return;
        }
        
        //Element splash = doc.createElement("splash");
        
        ElementHelper splash = doc.getRootElement().addNewElement("splash");
        
        //Element file = doc.createElement("file");
        //file.appendChild(doc.createTextNode(getSplashScreenFileName()));
        //splash.appendChild(file);
        
        splash.addNewElement("file")
                .addText(getSplashScreenFileName());
        
        //Element waitForWindow = doc.createElement("waitForWindow");
        //waitForWindow.appendChild(doc.createTextNode(willWaitForWindow() + ""));
        //splash.appendChild(waitForWindow);
        
        splash.addNewElement("waitForWindow")
                .addText(willWaitForWindow());
        
        //Element timeout = doc.createElement("timeout");
        //timeout.appendChild(doc.createTextNode(this.getTimeout() + ""));
        //splash.appendChild(timeout);
        
        splash.addNewElement("timeout")
                .addText(getTimeout());
        
        //Element timeoutErr = doc.createElement("timeoutErr");
        //timeoutErr.appendChild(doc.createTextNode(
        //        willSingleErrorOnTimeout() + ""));
        //splash.appendChild(timeoutErr);
        
        splash.addNewElement("timeoutErr")
                .addText(willSingleErrorOnTimeout());
        
        //doc.appendChild(splash);
    }
    
    public void appendVersionInfo(XmlDocumentHelper doc){
        if(!this.isVersionInformationAdded()){
            return;
        }
        
        //Element versionInfo = createNewElement(doc, "versionInfo", null);
        
        ElementHelper versionInfo = doc.getRootElement().addNewElement("versionInfo");
        
        //versionInfo.appendChild(createNewElement(doc, "fileVersion",
        //        this.getFileVersion()));
        
        versionInfo.addNewElement("fileVersion")
                .addText(getFileVersion());
        
        //versionInfo.appendChild(createNewElement(doc, "txtFileVersion",
        //        this.getFreeFormFileVersion()));
        
        versionInfo.addNewElement("txtFileVersion")
                .addText(getFreeFormFileVersion());
        
        //versionInfo.appendChild(createNewElement(doc, "fileDescription",
        //        this.getFileDescription()));
        
        versionInfo.addNewElement("fileDescription")
                .addText(getFileDescription());
        
        //versionInfo.appendChild(createNewElement(doc, "copyright", 
        //        this.getCopyright()));
        
        versionInfo.addNewElement("copyright")
                .addText(getCopyright());
        
        //versionInfo.appendChild(createNewElement(doc, "productVersion",
        //        this.getProductVersion()));
        
        versionInfo.addNewElement("productVersion")
                .addText(getProductVersion());
        
        //versionInfo.appendChild(createNewElement(doc, "txtProductVersion",
        //        this.getFreeFormProductVersion()));
        
        versionInfo.addNewElement("txtProductVersion")
                .addText(getFreeFormProductVersion());
        
        //versionInfo.appendChild(createNewElement(doc, "productName", 
        //        this.getProductName()));
        
        versionInfo.addNewElement("productName")
                .addText(getProductName());
        
        //versionInfo.appendChild(createNewElement(doc, "companyName",
        //        this.getCompanyName()));
        
        versionInfo.addNewElement("companyName")
                .addText(getCompanyName());
        
        //versionInfo.appendChild(createNewElement(doc, "internalName", 
        //        this.getInternalName()));
        
        versionInfo.addNewElement("internalName")
                .addText(getInternalName());
        
        //versionInfo.appendChild(createNewElement(doc, "originalFileName", 
        //        this.getOriginalFileName()));
        
        versionInfo.addNewElement("originalFileName")
                .addText(getOriginalFileName());
        
        //rootElement.appendChild(versionInfo);
    }

    public void appendMessages(XmlDocumentHelper doc){
        //Element messages = createNewElement(doc, "messages", null);
        
        ElementHelper messages = doc.getRootElement().addNewElement("messages");
        
        //messages.appendChild(createNewElement(doc, "startupErr",
        //        this.getStartupErrorMessage()));
        
        messages.addNewElement("startupErr")
                .addText(getStartupErrorMessage());
        
        //messages.appendChild(createNewElement(doc, "bundledJreErr",
        //        this.getBundledJreErrorMessage()));
        
        messages.addNewElement("bundledJreErr")
                .addText(getBundledJreErrorMessage());
        
        //messages.appendChild(createNewElement(doc, "jreVersionErr", 
        //        this.getJreVersionErrorMessage()));
        
        messages.addNewElement("jreVersionErr")
                .addText(getJreVersionErrorMessage());
        
        //messages.appendChild(createNewElement(doc, "launcherErr",
        //        this.getLauncherErrorMessage()));
        
        messages.addNewElement("launcherErr")
                .addText(getLauncherErrorMessage());
        
        //messages.appendChild(createNewElement(doc, "instanceAlreadyExistsMsg", 
        //        this.getInstanceAlreadyRunningErrorMessage()));
        
        messages.addNewElement("instanceAlreadyExistsMsg")
                .addText(getInstanceAlreadyRunningErrorMessage());
        
        //rootElement.appendChild(messages);
    }
    
    /**
     * @return {@link #getConfigurationSource() } as a string or {@code null} if
     * the {@link DOMSource} object couldn't be parsed.
     */
    public String getConfigurationSourceAsString() {
        try {
            TransformerFactory transformerFactory
                    = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = getConfigurationSource();

            StringWriter br = new StringWriter();
            StreamResult result = new StreamResult(br);
            transformer.transform(source, result);

            return br.toString();
        } catch (TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(Launch4jConfiguration.class.getName())
                    .log(Level.SEVERE, null, ex);
            //TODO: Fix this by adding propper logging.
            System.err.println("Failed to parse launch4j xml configuration"
                    + " and turn it into a string.\n" + ex);
        }
        return null;//Shouldn't get here
    }

    private static Element createNewElement(Document doc, 
            String tagName, String tagContent){
        Element el = doc.createElement(tagName);
        
        if(tagContent != null)
            el.appendChild(doc.createTextNode(tagContent));
        
        return el;
    }
}
