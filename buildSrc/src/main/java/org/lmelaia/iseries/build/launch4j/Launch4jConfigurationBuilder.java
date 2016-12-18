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

import java.io.File;

/**
 * This class provides a way to set the configuration options for launch4j
 * through a builder pattern. Once the options have been specified, call the
 * {@link #create() } method to get a {@link Launch4jConfiguration} object which
 * can then be used to create an executable.
 *
 * <p>
 * The options in this class may be accessed through the
 * {@link Launch4jConfiguration} class. An instance of which can be obtained
 * through the {@link #create() } method.
 * </p>
 *
 * <p>
 * <b><u>Important:</u></b><br>
 * The setter methods in this class will not throw any exceptions - all the
 * options in this class are validated by the {@link #create()
 * } method, which will throw an {@link InvalidLaunch4jConfigurationException}
 * if any option is invalid.
 * </p>
 *
 * <p>
 * <b>Even though all the options in this class are validated as much as
 * possible, it is still possible for options to be invalid.</b>
 * </p>
 *
 * @see Launch4jConfiguration
 * @see Launch4j
 * @see InvalidLaunch4jConfigurationException
 * @author Luke Melaia
 */
public class Launch4jConfigurationBuilder {

    //Basic
    /**
     * The file path to the executable launch4j will create.
     *
     * <p>
     * <b>This cannot be {@code null} or empty.</b>
     * The executable doesn't need to exist, as launch4j will create it if it
     * doesn't.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default and needs to specified.
     * </p>
     */
    protected String outputFileName;

    /**
     * The path to the java archive file launch4j will use to create the
     * executable.
     *
     * <p>
     * <b>This cannot be {@code null} or empty and the path must point to a
     * valid, existing java archive.</b>
     * </p>
     *
     * <p>
     * This need not be set if {@link #wrap} is set to {@code false}, but
     * {@link #jarRuntimePath} must then be set.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String jarFileName;

    /**
     * When set to {@code true}, launch4j will create an executable wrapper for
     * the specified java archive ({@link #jarFileName}). The
     * {@link #jarFileName} file name must be specified when this option is set
     * to {@code true}.
     *
     * <p>
     * When set to {@code false}, launch4j will <b>not</b> wrap the java archive
     * inside the executable, but launch the jar when the executable is
     * executed. The {@link #jarRuntimePath} must be specified when this option
     * is set to {@code false}.
     * </p>
     *
     * <p>
     * This option is set to {@code true} by default.
     * </p>
     */
    protected boolean wrap = true;

    /**
     * The runtime path of the jar relative to the executable.
     *
     * <p>
     * <b>This cannot be {@code null} or empty.</b> This need not be set if
     * {@link #wrap} is set to {@code true}, but {@link #jarFileName} must then
     * be set.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String jarRuntimePath;

    /**
     * The path to the manifest file(.manifest) for User Account Control.
     *
     * <p>
     * This <b>may</b> be {@code null}, but <b>must point to a valid, existing
     * manifest file (.manifest) if it has been set.</b>
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String wrapperManifest;

    /**
     * The path to the icon file(.ico) which will be used as the executables
     * icon.
     *
     * <p>
     * This <b>may</b> be {@code null}, but <b>must point to a valid, existing
     * icon file (.ico) if set.</b>
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String iconFileName;

    /**
     * Changes the current directory to a location relative to the executable.
     * An empty/{@code null} String has no effect. {@code "."} changes the
     * directory to the executables location.
     *
     * <p>
     * {@code "."} By default.
     * </p>
     */
    protected String changeDirectory = ".";

    /**
     * The constant command line arguments passed to the application.
     *
     * <p>
     * This <b>may</b> be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String commandLineArguments;

    /**
     * The priority of the running process.
     *
     * <p>
     * This option is set to {@link ProcessPriority#NORMAL} by default.
     * </p>
     */
    protected ProcessPriority processPriority = ProcessPriority.NORMAL;

    /**
     * Should the process stay alive after launching a graphical user interface
     * application.
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean stayAlive = false;

    /**
     * Should the process restart after a crash.
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean restartAfterCrash = false;

    /**
     * The title of the error message box.
     *
     * <p>
     * This is normally set to the applications name, but <b>may</b> be
     * {@code null}.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String errorTitle;

    /**
     * The java download URL. This URL will be opened in the users browser if
     * java is not installed on their machine.
     *
     * <p>
     * This may <b>not</b> be {@code null}.
     * </p>
     *
     * <p>
     * This option is set to {@code "http://java.com/download"} by default.
     * </p>
     */
    protected String downloadUrl = "http://java.com/download";

    /**
     * The support url for your application.
     *
     * <p>
     * This option <b>may</b> be {@code null} and is set to {@code null} by
     * default.
     * </p>
     */
    protected String supportUrl;

    //Classpath
    /**
     * When this option is enabled(set to {@code true}), launch4j will use the
     * custom classpath provided instead of the default one.
     *
     * <p>
     * <b><u>Important:</u></b>
     * <br>
     * This class will <b>not</b> automatically update the {@link #mainClass} or
     * {@link #classpath} from a jar file, <b>nor</b> are the options validated.
     * </p>
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     *
     * @see #mainClass
     * @see #classpath
     */
    protected boolean customClasspath = false;

    /**
     * The path to the main class inside the java archive. The directories
     * should be separated by periods(.) - not slashes(/).
     *
     * <p>
     * This option will not take effect unless {@link #customClasspath} is set
     * to {@code true}. This option may <b>not</b> be {@code null} if
     * {@link #customClasspath} is set to {@code true}.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     *
     * @see #customClasspath
     */
    protected String mainClass;

    /**
     * A list of the applications dependencies. Each dependency should be
     * declared in a new index inside the array.
     *
     * <p>
     * This option will not take effect unless {@link #customClasspath} is set
     * to {@code true}. This option <b>may</b> be {@code null} regardless of the
     * value contained in {@link #customClasspath}.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     *
     * @see #customClasspath
     */
    protected String[] classpath;

    //Header
    /**
     * Defines the application as a GUI or console application.
     *
     * <p>
     * This option is set to {@link HeaderType#GUI} by default.
     * </p>
     */
    protected HeaderType headerType = HeaderType.GUI;

    /**
     * The c/c++ object files used by launch4j.
     *
     * <p>
     * This option <b>may</b> be {@code null} and is set to {@code null} by
     * default.
     * </p>
     */
    protected String[] objectFiles;

    /**
     * The API files used by launch4j.
     *
     * <p>
     * This option <b>may</b> be {@code null} and is set to {@code null} by
     * default.
     * </p>
     */
    protected String[] w32Api;

    //Single instance
    /**
     * Allows only one instance of the application to be running when
     * enabled(set to {@code true}). The {@link #mutexName} option must be
     * specified if this option is enabled.
     *
     * <p>
     * The options {@link #mutexName} and {@link #windowTitle} will not take
     * effect unless this option is enabled.
     * </p>
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean singleInstance = false;

    /**
     * The name which will uniquely identify your application.
     *
     * <p>
     * This option will <b>not</b> take effect if {@link #singleInstance} is
     * <b>not</b> enabled. This option may <b>not</b> be {@code null} if
     * {@link #singleInstance} is enabled.
     * </p>
     *
     * <p>
     * This option is set to {@code null} by default.
     * </p>
     */
    protected String mutexName;

    /**
     * The title of the window that will inform the user that another instance
     * of the application is already running.
     *
     * <p>
     * This option <b>may</b> be {@code null} and is {@code null} by default. It
     * will also not take effect unless {@link #singleInstance} is enabled.
     * </p>
     */
    protected String windowTitle;

    //JRE
    /**
     * The path to the bundled java runtime environment. This path may be
     * relative or absolute.
     *
     * <p>
     * This option <b>may</b> be {@code null} and is {@code null} by default.
     * </p>
     */
    protected String bundledJrePath;

    /**
     * Is the bundled java runtime environment 64bit.
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean is64bit = false;

    /**
     * Is the bundled java runtime environment the fallback option. When
     * enabled(set to {@code true}), the bundled java runtime environment will
     * only be used if the min/max search fails. The bundled java runtime
     * environment is used first by default.
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean isFallbackOption = false;

    /**
     * The minimum java runtime environment version required to run the
     * application.
     *
     * <p>
     * This <b>may only</b> be {@code null} if a {@link #bundledJrePath} has
     * been specified. This option is {@code null} by default.
     * </p>
     */
    protected String minimumJreVersion;

    /**
     * The maximum java runtime environment version that can run the
     * application.
     *
     * <p>
     * This option may be {@code null} and is {@code null} by default.
     * </p>
     */
    protected String maximumJreVersion;

    /**
     * This option specifies which java runtime environment to use(jre/jdk).
     *
     * <p>
     * This option is set to {@link JavaUsageOptions#JRE_OVER_JDK} by default.
     * </p>
     */
    protected JavaUsageOptions javaUsageOptions = JavaUsageOptions.JRE_OVER_JDK;

    /**
     * This option specifies which architecture of java to use.
     *
     * <p>
     * This option is set to {@link JavaArchitecture#_64BIT_THEN_32BIT} by
     * default.
     * </p>
     */
    protected JavaArchitecture javaArchitecture
            = JavaArchitecture._64BIT_THEN_32BIT;

    /**
     * The number of megabytes to allocate to the heap on startup(initially).
     *
     * <p>
     * This option can be {@code null} and is {@code null} by default.
     * </p>
     */
    protected int initialHeapSize;

    /**
     * The maximum number of megabytes the heap can allocate.
     *
     * <p>
     * This option can be {@code null} and is {@code null} by default.
     * </p>
     */
    protected int maximumHeapSize;

    /**
     * Are the heap sizes in percent of available memory.
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean isHeapInPercent = false;

    /**
     * The java virtual machine options. Each option should be specified in a
     * new index of the array.
     *
     * <p>
     * This option can be {@code null} and is {@code null} by default.
     * </p>
     */
    protected String[] jvmOptions;

    //Environment variables
    /**
     * The environment variables. Each variable should be declared in a new
     * index in the array.
     *
     * <p>
     * This option can be {@code null} and is {@code null} by default.
     * </p>
     */
    protected String[] environmentVariables;

    //Splash screen
    /**
     * When enabled(set to {@code true}), the application will display a splash
     * screen before it starts.
     *
     * <p>
     * The following options will not take effect unless this option is enabled:
     * {@link #splashScreenFileName}, {@link #waitForWindow}, {@link #timeout}
     * and {@link #signalErrorOnTimeout}.
     * </p>
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean isSplashScreenEnabled = false;

    /**
     * The path to the splash screen in BMP format(.bmp).
     *
     * <p>
     * This option may <b>not</b> be {@code null} if
     * {@link #isSplashScreenEnabled} is enabled.
     * </p>
     *
     * <p>
     * This option is {@code null} by default.
     * </p>
     */
    protected String splashScreenFileName;

    /**
     * When enabled(set to {@code true}), the splash screen will close when the
     * application displays a window.
     *
     * <p>
     * This option is {@code true} by default.
     * </p>
     */
    protected boolean waitForWindow = true;

    /**
     * The number of seconds before the splash screen is required to close.
     *
     * <p>
     * This option is set to {@code 60} by default.
     * </p>
     */
    protected int timeout = 60;

    /**
     * When enabled(set to {@code true}), an error will be signaled when the
     * splash screen times out(timeout specified by {@link #timeout}). When
     * disabled, the splash will close quietly.
     *
     * <p>
     * This option is {@code true} by default.
     * </p>
     */
    protected boolean signalErrorOnTimeout = true;

    //Version information
    /**
     * When enabled(set to {@code true}), launch4j will add the version
     * information to the executable.
     *
     * <p>
     * The following options specify the version information and will not take
     * effect unless this option is enabled: {@link #fileVersion},
     * {@link #freeFormFileVersion},
     * {@link #fileDescription}, {@link #copyright}, {@link #productVersion},
     * {@link #freeFormProductVersion}, {@link #productName},
     * {@link #companyName}, {@link #internalName} and
     * {@link #originalFileName}.
     * </p>
     *
     * <p>
     * This option is set to {@code false} by default.
     * </p>
     */
    protected boolean addVersionInfo = false;

    /**
     * The file version number in the format: x.x.x.x.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String fileVersion;

    /**
     * The free form file version(e.g. 1.20RC1).
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String freeFormFileVersion;

    /**
     * The file description presented to the user.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String fileDescription;

    /**
     * The copyright notice presented to the user.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String copyright;

    /**
     * The product version number in the format: x.x.x.x
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String productVersion;

    /**
     * The free form product version(e.g. 1.20RC1).
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String freeFormProductVersion;

    /**
     * The name of the product.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String productName;

    /**
     * The name of the company that produced this application.
     *
     * <p>
     * This option <b>can</b> be {@code null} and is set to {@code null} by
     * default.
     * </p>
     */
    protected String companyName;

    /**
     * The internal name without extention, original file name or module name
     * for example.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String internalName;

    /**
     * The original file name without the path. This is used to determine
     * whether the file has been renamed by the user. This option must end with
     * the .exe extention.
     *
     * <p>
     * This option cannot be {@code null} and is set to {@code null} by default.
     * </p>
     */
    protected String originalFileName;

    //Error messages.
    /**
     * The error message presented to the user when the application fails to
     * start.
     *
     * <p>
     * This option is set to
     * {@code "An error occurred while starting the application."} by default.
     * This option can be {@code null}, but it is recommended that it not
     * {@code null}.
     * </p>
     */
    protected String startupErrorMessage
            = "An error occurred while starting the application.";

    /**
     * The error message presented to the user when the application cannot find
     * the bundled jre or it is corrupt. This message will only be presented to
     * the user if your application uses a bundled jre.
     *
     * <p>
     * This option is set to {@code "This application was configured to use a
     * bundled Java Runtime Environment but the runtime is
     * missing or corrupted."} by default. This option can be {@code null}, but
     * it is recommended that it is not {@code null}.
     * </p>
     */
    protected String bundledJreErrorMessage
            = "This application was configured to use a bundled Java Runtime "
            + "Environment but the runtime is missing or corrupted.";

    /**
     * The error message presented to the user when a jre cannot be found or
     * doesn't match the version requirements set in {@link #minimumJreVersion}
     * and {@link #maximumJreVersion}. The version number will be appended to
     * the end of this message. The user will also be redirected to the java
     * download page after this message has been acknowledged.
     *
     * <p>
     * This option is set to {@code "This application requires
     * a Java Runtime Environment"} by default. This option can be {@code null},
     * but it is recommended that it is not set to {@code null}.
     * </p>
     */
    protected String jreVersionErrorMessage
            = "This application requires a Java Runtime Environment";

    /**
     * The error message presented to the user when the jre is corrupted or the
     * registry refers to a nonexistent jre installation.
     *
     * <p>
     * This option is set to {@code "The registry refers to a nonexistent
     * Java Runtime Environment installation or the runtime is corrupted."} by
     * default. This option can be {@code null}, but it is recommended that it
     * is not set to {@code null}.
     * </p>
     */
    protected String launcherErrorMessage
            = "The registry refers to a nonexistent Java Runtime Environment "
            + "installation or the runtime is corrupted.";

    /**
     * The error message presented to the user when they try execute the
     * application while another instance is already running.
     *
     * <p>
     * This option is set to {@code "An application instance is already
     * running."} by default. This option can be {@code null}, but it is
     * recommended that it is not {@code null}.
     * </p>
     */
    protected String instanceAlreadyRunningErrorMessage
            = "An application instance is already running.";

    public Launch4jConfigurationBuilder() {
    }

    //##########################
    //           BASIC
    //##########################
    /**
     * See {@link #outputFileName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setOutputFile(String name) {
        this.outputFileName = name;
        return this;
    }

    /**
     * See {@link #jarFileName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setJarFile(String name) {
        this.jarFileName = name;
        return this;
    }

    /**
     * See {@link #wrap} for documentation.
     *
     * @param wrap
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setWrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * See {@link #jarRuntimePath} for documentation.
     *
     * @param runtimePath
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setJarRuntimePath(String runtimePath) {
        this.jarRuntimePath = runtimePath;
        return this;
    }

    /**
     * See {@link #wrapperManifest} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setWrapperManifest(String name) {
        this.wrapperManifest = name;
        return this;
    }

    /**
     * See {@link #outputFileName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setIconFile(String name) {
        this.iconFileName = name;
        return this;
    }

    /**
     * See {@link #changeDirectory} for documentation.
     *
     * @param changeDir
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setChangeDirectory(String changeDir) {
        this.changeDirectory = changeDir;
        return this;
    }

    /**
     * See {@link #commandLineArguments} for documentation.
     *
     * @param arguments
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setCommandLineArguments(String arguments) {
        this.commandLineArguments = arguments;
        return this;
    }

    /**
     * See {@link #processPriority} for documentation.
     *
     * @param priority
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setProcessPriority(ProcessPriority priority) {
        this.processPriority = priority;
        return this;
    }

    /**
     * See {@link #stayAlive} for documentation.
     *
     * @param stayAlive
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setStayAlive(boolean stayAlive) {
        this.stayAlive = stayAlive;
        return this;
    }

    /**
     * See {@link #restartAfterCrash} for documentation.
     *
     * @param restart
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setRestartAfterCrash(boolean restart) {
        this.restartAfterCrash = restart;
        return this;
    }

    /**
     * See {@link #errorTitle} for documentation.
     *
     * @param title
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setErrorTitle(String title) {
        this.errorTitle = title;
        return this;
    }

    /**
     * See {@link #downloadUrl} for documentation.
     *
     * @param url
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setDownloadUrl(String url) {
        this.downloadUrl = url;
        return this;
    }

    /**
     * See {@link #supportUrl} for documentation.
     *
     * @param url
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setSupportURL(String url) {
        this.supportUrl = url;
        return this;
    }

    //##########################
    //        CLASSPATH
    //##########################
    /**
     * See {@link #customClasspath} for documentation.
     *
     * @param customClasspath
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            customClasspath(boolean customClasspath) {
        this.customClasspath = customClasspath;
        return this;
    }

    /**
     * See {@link #mainClass} for documentation.
     *
     * @param mainClass
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setMainClass(String mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    /**
     * See {@link #classpath} for documentation.
     *
     * @param classpath
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setClasspath(String... classpath) {
        this.classpath = classpath;
        return this;
    }

    //##########################
    //           HEADER
    //##########################
    /**
     * See {@link #headerType} for documentation.
     *
     * @param type
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setHeaderType(HeaderType type) {
        this.headerType = type;
        return this;
    }

    /**
     * See {@link #objectFiles} for documentation.
     *
     * @param objectFiles
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setObjectFiles(String... objectFiles) {
        this.objectFiles = objectFiles;
        return this;
    }

    /**
     * See {@link #w32Api} for documentation.
     *
     * @param w32Api
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setW32Api(String... w32Api) {
        this.w32Api = w32Api;
        return this;
    }

    //##########################
    //      SINGLE INSTANCE
    //##########################
    /**
     * See {@link #singleInstance} for documentation.
     *
     * @param singleInstance
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            allowOneInstanceOnly(boolean singleInstance) {
        this.singleInstance = singleInstance;
        return this;
    }

    /**
     * See {@link #mutexName} for documentation.
     *
     * @param mutexName
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setMutexName(String mutexName) {
        this.mutexName = mutexName;
        return this;
    }

    /**
     * See {@link #windowTitle} for documentation.
     *
     * @param title
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setWindowTitle(String title) {
        this.windowTitle = title;
        return this;
    }

    //##########################
    //            JRE
    //##########################
    /**
     * See {@link #bundledJrePath} for documentation.
     *
     * @param path
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setBundledJrePath(String path) {
        this.bundledJrePath = path;
        return this;
    }

    /**
     * See {@link #is64bit} for documentation.
     *
     * @param is64bit
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder set64Bit(boolean is64bit) {
        this.is64bit = is64bit;
        return this;
    }

    /**
     * See {@link #isFallbackOption} for documentation.
     *
     * @param isFallbackOption
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setFallback(boolean isFallbackOption) {
        this.isFallbackOption = isFallbackOption;
        return this;
    }

    /**
     * See {@link #minimumJreVersion} for documentation.
     *
     * @param jreVersion
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setMinimumJreVersion(String jreVersion) {
        this.minimumJreVersion = jreVersion;
        return this;
    }

    /**
     * See {@link #maximumJreVersion} for documentation.
     *
     * @param jreVersion
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setMaximumJreVersion(String jreVersion) {
        this.maximumJreVersion = jreVersion;
        return this;
    }

    /**
     * See {@link #isHeapInPercent} for documentation.
     *
     * @param isInPercent
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setHeapToPercent(boolean isInPercent) {
        this.isHeapInPercent = isInPercent;
        return this;
    }

    /**
     * See {@link #javaUsageOptions} for documentation.
     *
     * @param juo
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setJavaUsageOptions(JavaUsageOptions juo) {
        this.javaUsageOptions = juo;
        return this;
    }

    /**
     * See {@link #javaArchitecture} for documentation.
     *
     * @param ja
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setJavaArchitecture(JavaArchitecture ja) {
        this.javaArchitecture = ja;
        return this;
    }

    /**
     * See {@link #initialHeapSize} for documentation.
     *
     * @param heapSize
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setInitialHeapSize(int heapSize) {
        this.initialHeapSize = heapSize;
        return this;
    }

    /**
     * See {@link #maximumHeapSize} for documentation.
     *
     * @param heapSize
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setMaximumHeapSize(int heapSize) {
        this.maximumHeapSize = heapSize;
        return this;
    }

    /**
     * See {@link #jvmOptions} for documentation.
     *
     * @param jvmOptions
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setJvmOptions(String... jvmOptions) {
        this.jvmOptions = jvmOptions;
        return this;
    }

    //##########################
    //   ENVIRONMENT VARIABLES
    //##########################
    /**
     * See {@link #environmentVariables} for documentation.
     *
     * @param vars
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setEnvironmentVariables(String... vars) {
        this.environmentVariables = vars;
        return this;
    }

    //##########################
    //      SPLASH SCREEN
    //##########################
    /**
     * See {@link #isSplashScreenEnabled} for documentation.
     *
     * @param enable
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder enableSplashScreen(boolean enable) {
        this.isSplashScreenEnabled = enable;
        return this;
    }

    /**
     * See {@link #splashScreenFileName} for documentation.
     *
     * @param splashFile
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setSplashFile(String splashFile) {
        this.splashScreenFileName = splashFile;
        return this;
    }

    /**
     * See {@link #waitForWindow} for documentation.
     *
     * @param waitForWindow
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setWaitForWindow(boolean waitForWindow) {
        this.waitForWindow = waitForWindow;
        return this;
    }

    /**
     * See {@link #timeout} for documentation.
     *
     * @param timeout
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * See {@link #signalErrorOnTimeout} for documentation.
     *
     * @param signalError
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            signalErrorOnTimeout(boolean signalError) {
        this.signalErrorOnTimeout = signalError;
        return this;
    }

    //##########################
    //      VERSION INFO
    //##########################
    /**
     * See {@link #addVersionInfo} for documentation.
     *
     * @param info
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder addVersionInfo(boolean info) {
        this.addVersionInfo = info;
        return this;
    }

    /**
     * See {@link #fileVersion} for documentation.
     *
     * @param version
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setFileVersion(String version) {
        this.fileVersion = version;
        return this;
    }

    /**
     * See {@link #freeFormFileVersion} for documentation.
     *
     * @param version
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setFreeFormFileVersion(String version) {
        this.freeFormFileVersion = version;
        return this;
    }

    /**
     * See {@link #fileDescription} for documentation.
     *
     * @param description
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setFileDescription(String description) {
        this.fileDescription = description;
        return this;
    }

    /**
     * See {@link #copyright} for documentation.
     *
     * @param copyright
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    /**
     * See {@link #productVersion} for documentation.
     *
     * @param version
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setProductVersion(String version) {
        this.productVersion = version;
        return this;
    }

    /**
     * See {@link #freeFormProductVersion} for documentation.
     *
     * @param version
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setFreeFormProductVersion(String version) {
        this.freeFormProductVersion = version;
        return this;
    }

    /**
     * See {@link #productName} for documentation.
     *
     * @param productName
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    /**
     * See {@link #companyName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setCompanyName(String name) {
        this.companyName = name;
        return this;
    }

    /**
     * See {@link #internalName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setInternalName(String name) {
        this.internalName = name;
        return this;
    }

    /**
     * See {@link #originalFileName} for documentation.
     *
     * @param name
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setOriginalFileName(String name) {
        this.originalFileName = name;
        return this;
    }

    //##########################
    //        MESSAGES
    //##########################
    /**
     * See {@link #startupErrorMessage} for documentation.
     *
     * @param message
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder setStartupErrorMessage(String message) {
        this.startupErrorMessage = message;
        return this;
    }

    /**
     * See {@link #bundledJreErrorMessage} for documentation.
     *
     * @param message
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setBundledJreErrorMessage(String message) {
        this.bundledJreErrorMessage = message;
        return this;
    }

    /**
     * See {@link #jreVersionErrorMessage} for documentation.
     *
     * @param message
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setJreVersionErrorMessage(String message) {
        this.jreVersionErrorMessage = message;
        return this;
    }

    /**
     * See {@link #launcherErrorMessage} for documentation.
     *
     * @param message
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setLauncherErrorMessage(String message) {
        this.launcherErrorMessage = message;
        return this;
    }

    /**
     * See {@link #instanceAlreadyRunningErrorMessage} for documentation.
     *
     * @param message
     * @return this for convenience.
     */
    public Launch4jConfigurationBuilder
            setInstanceAlreadyRunningErrorMessage(String message) {
        this.instanceAlreadyRunningErrorMessage = message;
        return this;
    }

    //##########################
    //      END OF SETTERS
    //##########################
    /**
     * Validates all the options provided to this class and creates a
     * {@link Launch4jConfiguration} object with the options provided to this
     * class. The Launch4jConfiguration object can then be used to create an
     * executable with launch4j with the {@link Launch4j} class.
     *
     * @return a {@link Launch4jConfiguration} object with the settings from
     * this class.
     * @throws InvalidLaunch4jConfigurationException if any option provided to
     * this class is invalid.
     */
    public Launch4jConfiguration create()
            throws InvalidLaunch4jConfigurationException {
        validate();
        return new Launch4jConfiguration(this);
    }

    //##########################
    //        VALIDATION
    //##########################
    /**
     * Validates all the options in this class.
     */
    private void validate() {
        validateBasic();
        validateClasspath();
        validateSingleInstance();
        validateJre();
        validateEnvironmentVariables();
        validateSplashScreen();
        validateVersionInformation();
    }

    private void validateBasic() {
        validateOutputFile();
        validateJarFile();
        validateJarRuntimePath();
        validateManifestFile();
        validateIconFile();
        validateChangeDirectory();
        validateCommandLineArguments();
        validateErrorTitle();
        validateUrls();
    }

    private void validateOutputFile() {
        checkString(outputFileName, "ouput file name");
    }

    private void validateJarFile() {
        if (!wrap) {
            jarFileName = null;
            return;
        }

        checkString(jarFileName, "jar file name");

        if (!jarFileName.toLowerCase().endsWith(".jar")) {
            throw new InvalidLaunch4jConfigurationException(
                    "Jar file name must point to a jar file");
        }

        if (!new File(jarFileName).exists()) {
            throw new InvalidLaunch4jConfigurationException(
                    String.format("Jar file %s doesn't exist", jarFileName));
        }
    }

    private void validateJarRuntimePath() {
        if (wrap) {
            jarRuntimePath = null;
            return;
        }

        checkString(jarRuntimePath, "jar runtime path");

        if (jarRuntimePath.startsWith("/")
                || jarRuntimePath.startsWith("\\")
                || jarRuntimePath.indexOf(':') != -1) {
            throw new InvalidLaunch4jConfigurationException(
                    "The jar runtime path must be relative to the executable");
        }
    }

    private void validateManifestFile() {
        if (wrapperManifest != null) {
            if (wrapperManifest.isEmpty()) {
                wrapperManifest = null;
            } else {
                if (!wrapperManifest.toLowerCase().endsWith(".manifest")) {
                    throw new InvalidLaunch4jConfigurationException(
                            "Wrapper manifest must point to a .manifest file");
                }

                if (!new File(wrapperManifest).exists()) {
                    throw new InvalidLaunch4jConfigurationException(
                            String.format("Manifest file %s doesn't exist",
                                    wrapperManifest));
                }
            }
        }
    }

    private void validateIconFile() {
        if (iconFileName != null) {
            if (iconFileName.isEmpty()) {
                //An empty icon file name might as well be null
                iconFileName = null;
            } else {
                if (!iconFileName.toLowerCase().endsWith(".ico")) {
                    throw new InvalidLaunch4jConfigurationException(
                            "Icon file name must point to a .ico file");
                }

                if (!new File(iconFileName).exists()) {
                    throw new InvalidLaunch4jConfigurationException(
                            String.format("Icon file %s doesn't exist",
                                    iconFileName));
                }
            }
        }
    }

    private void validateChangeDirectory() {
        if (changeDirectory != null && changeDirectory.isEmpty()) {
            //An empty change directory might as well be null
            changeDirectory = null;
        }
    }

    private void validateCommandLineArguments() {
        if (commandLineArguments != null && commandLineArguments.isEmpty()) {
            //Empty command line arguments might as well be null
            commandLineArguments = null;
        }
    }

    private void validateErrorTitle() {
        if (errorTitle != null && errorTitle.isEmpty()) {
            //An empty error title might as well be null
            errorTitle = null;
        }
    }

    private void validateUrls() {
        //Download url
        checkString(downloadUrl, "download URL");

        //Support url
        if (supportUrl != null && supportUrl.isEmpty()) {
            //An empty download url might as well be null
            supportUrl = null;
        }
    }

    private void validateClasspath() {
        if (!customClasspath) {
            this.mainClass = null;
            this.classpath = null;
        } else {
            if (mainClass == null || mainClass.isEmpty()) {
                throw new InvalidLaunch4jConfigurationException(
                        "Main class cannot be null or empty. "
                        + "Disable custom classpath to use the default "
                        + "main class.");
            }

            if (classpath != null && classpath.length == 0) {
                classpath = null;
            }
        }
    }

    private void validateSingleInstance() {
        if (!singleInstance) {
            mutexName = null;
            windowTitle = null;
        } else {
            checkString(mutexName, "muxtex name");

            if (windowTitle != null && windowTitle.isEmpty()) {
                windowTitle = null;
            }
        }
    }

    private void validateJre() {
        validateJreVersions();
        validateHeaps();
        validateJvmOptions();
    }

    private void validateJreVersions() {
        //The regex pattern launch4j uses to validate the jre versions.
        final String LAUNCH4J_VERSION_REGEX = "(\\d\\.){2}\\d(_\\d+)?";

        //Minimum jre version
        if ((bundledJrePath == null || bundledJrePath.isEmpty())
                && (minimumJreVersion == null || minimumJreVersion.isEmpty())) {
            throw new InvalidLaunch4jConfigurationException(
                    "The minimum jre version or bundled "
                    + "jre path must be specified");
        }

        if (minimumJreVersion != null) {
            if (!minimumJreVersion.matches(LAUNCH4J_VERSION_REGEX)) {
                throw new InvalidLaunch4jConfigurationException(
                        "Minimum JRE version should be "
                        + "in the format x.x.x[_xx]");
            }
        }

        //Maximum jre version
        if (maximumJreVersion
                != null && maximumJreVersion.isEmpty()) {
            maximumJreVersion = null;
        } else {
            if (!maximumJreVersion.matches(LAUNCH4J_VERSION_REGEX)) {
                throw new InvalidLaunch4jConfigurationException(
                        "Maximum JRE version should be "
                        + "in the format x.x.x[_xx]");
            }
        }

        if (minimumJreVersion == null && maximumJreVersion != null) {
            throw new InvalidLaunch4jConfigurationException(
                    "Please specify a minimum jre version if "
                    + "you specify a maximum jre version");
        }

        //Version size check
        double min = Double.parseDouble(minimumJreVersion.replace(".", "")
                .replace("_", "."));

        double max = Double.parseDouble(maximumJreVersion.replace(".", "")
                .replace("_", "."));

        if (min >= max) {
            throw new InvalidLaunch4jConfigurationException(
                    "The maximum jre version must be greater than the minimum");
        }
    }

    private void validateHeaps() {
        //Heaps.
        if (initialHeapSize != 0 && maximumHeapSize == 0) {
            throw new InvalidLaunch4jConfigurationException(
                    "If you specify a value for the initial heap size, "
                    + "please also specify a value for the "
                    + "maximum heap size");
        }

        if (isHeapInPercent) {
            //Initial
            if (initialHeapSize < 1) {
                throw new InvalidLaunch4jConfigurationException(
                        "Initial heap size must be between 1 and 100%");
            }

            if (initialHeapSize > 100) {
                throw new InvalidLaunch4jConfigurationException(
                        "Initial heap size must be between 1 and 100%");
            }

            //Maximum
            if (maximumHeapSize < 1) {
                throw new InvalidLaunch4jConfigurationException(
                        "Maximum heap size must be between 1 and 100%");
            }

            if (maximumHeapSize > 100) {
                throw new InvalidLaunch4jConfigurationException(
                        "Maximum heap size must be between 1 and 100%");
            }
        } else {
            //Initial
            if (initialHeapSize < 0) {
                throw new InvalidLaunch4jConfigurationException(
                        "Initial heap size must be above 0MB");
            }

            //Maximum
            if (maximumHeapSize < 0) {
                throw new InvalidLaunch4jConfigurationException(
                        "Maximum heap size must be above 0MB");
            }
        }

        //Both heaps
        if (initialHeapSize > maximumHeapSize) {
            throw new InvalidLaunch4jConfigurationException(
                    "Maximum heap size must be above or equal to "
                    + "initial heap size");
        }
    }

    private void validateJvmOptions() {
        if (jvmOptions == null || jvmOptions.length == 0) {
            jvmOptions = null;
            return;
        }

        for (String argument : jvmOptions) {
            if (argument == null || argument.isEmpty()) {
                throw new InvalidLaunch4jConfigurationException(
                        "One or more of the jvm options are invalid");
            }
        }
    }

    private void validateEnvironmentVariables() {
        if (environmentVariables == null) {
            return;
        }

        if (environmentVariables.length == 0) {
            environmentVariables = null;
        }

        for (String variable : environmentVariables) {
            if (!variable.matches(".+=.+")) {
                throw new InvalidLaunch4jConfigurationException(
                        "All environment variables should be in the format "
                        + "varname=[value][%varref%]");
            }
        }
    }

    private void validateSplashScreen() {
        if (!isSplashScreenEnabled) {
            splashScreenFileName = null;
            waitForWindow = true;
            timeout = 60;
            signalErrorOnTimeout = true;
        } else {
            //Splash file
            checkString(splashScreenFileName, "spash screen file name");

            if (!splashScreenFileName.toLowerCase().endsWith(".bmp")) {
                throw new InvalidLaunch4jConfigurationException(
                        "The splash screen should be in BMP format");
            }

            if (!new File(splashScreenFileName).exists()) {
                throw new InvalidLaunch4jConfigurationException(
                        String.format(
                                "The splash file %s doesn't exist",
                                splashScreenFileName));
            }

            if (timeout < 1 || timeout > 900) {
                throw new InvalidLaunch4jConfigurationException(
                        "Timeout must be between 1 and 900");
            }
        }
    }

    /**
     * The regex pattern used to determine if a file version is in the correct
     * format.
     */
    private static final String VERSION_REGEX
            = "[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+";

    private void validateVersionInformation() {
        if (!addVersionInfo) {
            this.fileVersion = null;
            this.freeFormFileVersion = null;
            this.fileDescription = null;
            this.copyright = null;
            this.productVersion = null;
            this.freeFormProductVersion = null;
            this.productName = null;
            this.companyName = null;
            this.internalName = null;
            this.originalFileName = null;
            return;
        }
        validateFileVersion();
        validateFreeFormFileVersion();
        validateFileDescription();
        validateCopyright();
        validateProductVersion();
        validateFreeFormProductVersion();
        validateProductName();
        validateInternalName();
        validateOriginalFileName();
    }

    private void validateFileVersion() {
        checkString(fileVersion, "file version");

        if (!fileVersion.matches(VERSION_REGEX)) {
            throw new InvalidLaunch4jConfigurationException(
                    "The file version must be in the format x.x.x.x");
        }
    }

    private void validateFreeFormFileVersion() {
        checkString(freeFormFileVersion, "free form file version");
    }

    private void validateFileDescription() {
        checkString(fileDescription, "file description");
    }

    private void validateCopyright() {
        checkString(copyright, "copyright");
    }

    private void validateProductVersion() {
        checkString(productVersion, "product version");

        if (!productVersion.matches(VERSION_REGEX)) {
            throw new InvalidLaunch4jConfigurationException(
                    "The product version must be in the format x.x.x.x");
        }
    }

    private void validateFreeFormProductVersion() {
        checkString(freeFormProductVersion, "free form product version");
    }

    private void validateProductName() {
        checkString(productName, "product name");
    }

    //Company name doesn't need validation
    private void validateInternalName() {
        checkString(internalName, "internal name");
    }

    private void validateOriginalFileName() {
        checkString(originalFileName, "original file name");

        if (!originalFileName.toLowerCase().endsWith(".exe")) {
            throw new InvalidLaunch4jConfigurationException(
                    "The original file name must end with the .exe extention");
        }
    }

    //Messages don't need validation
    /**
     * Checks if a {@link String} is {@code null}, or empty by calling the
     * {@link String#isEmpty() } method on it. If the String is {@code null} or
     * empty, an {@link InvalidLaunch4jConfigurationException} will be thrown
     * indicating the String is {@code null} or empty. The {@code name}
     * parameter is used for the variables name.
     *
     * @param strVar the String variable to check.
     * @param name the name of the variable.
     */
    private static void checkString(String strVar, String name) {
        if (strVar == null || strVar.isEmpty()) {
            throw new InvalidLaunch4jConfigurationException(
                    String.format("The %s cannot be null or empty", name));
        }
    }
}
