using System.Collections.ObjectModel;

namespace I_Series {
    
    /// <summary>
    /// A bunch of constant runtime settings that
    /// allow configuring the application. Settings
    /// include things such as file paths and
    /// runtime arguments. Create with a
    /// RuntimeSettingsBuilder.
    /// </summary>
    public partial class RuntimeSettings {
        
        /// <summary>
        /// Used by the RuntimeSettingsBuilder to
        /// create a new RuntimeSettings Object.
        /// </summary>
        private RuntimeSettings(ReadOnlyCollection<string> additionalArguments,
                string jre32Path, string jre64Path, string workingDirectory, string launcherJar) {
            AdditionalArguments = additionalArguments;
            Jre32Path = jre32Path;
            Jre64Path = jre64Path;
            WorkingDirectory = workingDirectory;
            LauncherJar = launcherJar;
        }
        
        /// <summary>
        /// <returns>a list of all additional command line arguments to run the
        /// program with.</returns>
        /// </summary>
        public ReadOnlyCollection<string> AdditionalArguments { get; }
        
        /// <summary>
        /// <returns>the relative path to the 32bit jre runtime.</returns>
        /// </summary>
        public string Jre32Path { get; }
        
        /// <summary>
        /// <returns>the relative path to the 64bit jre runtime.</returns>
        /// </summary>
        public string Jre64Path { get; }
        
        /// <summary>
        /// <returns>
        /// the relative file path of the working directory
        /// where the main I-Series java application will be run from.
        /// </returns>
        /// </summary>
        public string WorkingDirectory { get; }
        
        /// <summary>
        /// <returns></returns>
        /// </summary>
        public string LauncherJar { get; }
        
    }
}