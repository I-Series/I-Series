using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace I_Series {
    
    /// <summary>
    /// A partial class of RuntimeSettings that
    /// allows us to access the private constructor
    /// of RuntimeSettings. 
    /// </summary>
    partial class RuntimeSettings {
        
        /// <summary>
        /// The build used to create a RuntimeSettings object
        /// instance.
        /// </summary>
        public class RuntimeSettingsBuilder {

            /// <summary>
            /// List of additional arguments.
            /// </summary>
            private readonly List<string> _arguments = new List<string>();

            /// <summary>
            /// The relative path to the x32bit JRE.
            /// </summary>
            private string _jre32Path;

            /// <summary>
            /// The relative path to the x64bit JRE.
            /// </summary>
            private string _jre64Path;

            /// <summary>
            /// The relative path to the runtime working directory.
            /// </summary>
            private string _workingDirectory;

            /// <summary>
            /// The relative path to I-Series launcher java jar application.
            /// </summary>
            private string _launcherJar;

            /// <summary>
            /// Adds an argument to the list of additional
            /// arguments used to run the I-Series application.
            /// </summary>
            /// <param name="argument">the argument to add.</param>
            /// <returns>This builder object.</returns>
            public RuntimeSettingsBuilder AddAdditionalArgument(string argument) {
                _arguments.Add(argument);
                return this;
            }

            /// <summary>
            /// Sets the relative file path to the x32bit java jre
            /// environment directory.
            /// </summary>
            /// <param name="path">the relative file path of the x32bit jre.</param>
            /// <returns></returns>
            public RuntimeSettingsBuilder SetJre32Path(string path) {
                _jre32Path = path;
                return this;
            }
            
            /// <summary>
            /// Sets the relative file path to the x64bit java jre
            /// environment directory.
            /// </summary>
            /// <param name="path">the relative file path of the x64bit jre.</param>
            /// <returns></returns>
            public RuntimeSettingsBuilder SetJre64Path(string path) {
                _jre64Path = path;
                return this;
            }
            
            /// <summary>
            /// Sets the relative file path to the working directory
            /// where the I-Series application will be run from.
            /// </summary>
            /// <param name="path">the relative file path of the
            /// working directory.</param>
            /// <returns></returns>
            public RuntimeSettingsBuilder SetWorkingDirectory(string path) {
                _workingDirectory = path;
                return this;
            }
            
            /// <summary>
            /// Sets the relative file path to the I-Series launcher java (jar)
            /// application.
            /// </summary>
            /// <param name="path">the relative file path of the I-Series launcher jar.</param>
            /// <returns></returns>
            public RuntimeSettingsBuilder SetLauncherJar(string path) {
                _launcherJar = path;
                return this;
            }

            /// <summary>
            /// Builds a new RuntimeSettings object instance using the
            /// values specified using the various setter methods.
            /// </summary>
            /// <returns>the newly created RuntimeSettings object.</returns>
            public RuntimeSettings Build() {
                return new RuntimeSettings(
                    new ReadOnlyCollection<string>(_arguments),
                    _jre32Path, _jre64Path, _workingDirectory, _launcherJar
                );
            }
        }
    }
}