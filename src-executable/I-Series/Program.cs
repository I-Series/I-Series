using System;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;

namespace I_Series {
    
    /// <summary>
    /// The applications entry point. Contains runtime configuration
    /// and starting procedure.
    /// </summary>
    internal class Program {
        
        /// <summary>
        /// The current working directory the executable was launched from.
        /// </summary>
        private readonly string _workingDirectory = Directory.GetCurrentDirectory();
        
        /// <summary>
        /// The runtime configuration settings.
        /// </summary>
        private readonly RuntimeSettings _runtimeSettings = new RuntimeSettings.RuntimeSettingsBuilder()
            .SetJre32Path(@"\runtime\x32\")
            .SetJre64Path(@"\runtime\x64\")
            .SetWorkingDirectory(@"\bin\")
            .AddAdditionalArgument("-Dprism.vsync=false")
            .SetLauncherJar(@"\bin\I-Series-Launcher.jar")
            .Build();

        /// <summary>
        /// The command line arguments the executable was launched with.
        /// </summary>
        private readonly List<Object> _cmdArguments;

        /// <summary>
        /// boolean flag that lets us know the
        /// cpu architecture we're running on.
        /// </summary>
        private readonly bool _is64BitEnv;

        /// <summary>
        /// The JRE helper object for the x32bit jre runtime.
        /// </summary>
        private readonly Jre _jreX32;
        
        /// <summary>
        /// The JRE helper object for the x64bit jre runtime.
        /// </summary>
        private readonly Jre _jreX64;
 
        /// <summary>
        /// The determined jre runtime architecture (x32/x64) to use.
        /// </summary>
        private RuntimeChipset _runtimeChipset;
        
        /// <summary>
        /// Represents a cpu chipset (x32/x64 bit) or
        /// an undetermined chipset.
        /// </summary>
        private enum RuntimeChipset {
            X32, X64, Undetermined
        }
        
        public Program(string[] args) {
            _cmdArguments = new List<object>();
            foreach (var arg in args) {
                _cmdArguments.Add(arg);
            }
            
            _is64BitEnv = Environment.Is64BitOperatingSystem;
            _jreX32 = new Jre($"{_workingDirectory}{_runtimeSettings.Jre32Path}");
            _jreX64 = new Jre($"{_workingDirectory}{_runtimeSettings.Jre64Path}");
        }

        /// <summary>
        /// Starts the I-Series launch procedure.
        /// </summary>
        private void Start() {
            Console.Out.WriteLine("Starting from directory: {0}", _workingDirectory);
            Console.Out.WriteLine("Cpu architecture: {0}", _is64BitEnv ? "x64" : "x32");
            
            if (_cmdArguments.Count == 0) Console.Out.WriteLine("Command line arguments: None");
            else {
                Console.Out.Write("Command line arguments: ");
                _cmdArguments.ForEach(i => Console.Write("{0} | ", i));
                Console.Out.Write("\n");
            }

            ConfigureRuntime();
        }

        /// <summary>
        /// Determines which runtime chipset (x32/x64) to use
        /// based on availability, cpu architecture, and user
        /// choice.
        ///
        /// Warns the user and terminates the program if no
        /// suitable JRE runtime can be chosen.
        /// </summary>
        private void ConfigureRuntime() {
            //Get
            bool hasJreX32, hasJreX64;
            Console.Out.WriteLine("Has x32 JRE: {0}", (hasJreX32 = _jreX32.Check()).ToString());
            Console.Out.WriteLine("Has x64 JRE: {0}", (hasJreX64 = _jreX64.Check()).ToString());

            //Dialog Boxes for errors.
            if (!(hasJreX32 || hasJreX64))
                //Fail
                MessageBox.Show("No JRE runtime found. Please reinstall I-Series.");

            if (_is64BitEnv && !hasJreX64) {
                //Warn
                MessageBox.Show("Warning: using x32bit runtime on x64bit environment.");
            }
            
            if (!_is64BitEnv && !hasJreX32) {
                //Fail
                MessageBox.Show("No x32bit runtime found. Please reinstall I-Series for x32bit Windows.");
            }

            //Determine runtime mode (x32, x64)
            if (hasJreX32 && hasJreX64) {
                _runtimeChipset = GetChipsetFromArgs();
                if(_runtimeChipset == RuntimeChipset.Undetermined)
                    _runtimeChipset = _is64BitEnv ? RuntimeChipset.X64 : RuntimeChipset.X32;
            } else if (hasJreX32) _runtimeChipset = RuntimeChipset.X32;
            else if (hasJreX64) _runtimeChipset = RuntimeChipset.X64;
            
            Console.Out.WriteLine("Determined chipset: {0}",
                _runtimeChipset == RuntimeChipset.X32 ? "x32bit" : "x64bit"
            );
        }

        /// <summary>
        /// Checks what runtime (x32 or x64) the user
        /// has chosen to use, if any, using the command
        /// line.
        /// </summary>
        /// <returns>X32 or X64 depending on what the user
        /// chose, Undetermined if the user didn't make a choice
        /// </returns>
        private RuntimeChipset GetChipsetFromArgs() {
            foreach (var arg in _cmdArguments) {
                if (arg.ToString().StartsWith("--architecture")) {
                    string arch = arg.ToString().Replace("--architecture", "");
                    arch = arch.Replace("=", "");
                    arch = arch.Replace(" ", "");

                    switch (arch) {
                        case "x64":
                        case "X64":
                        case "64":
                            return RuntimeChipset.X64;
                        case "x32":
                        case "X32":
                        case "32":
                            return RuntimeChipset.X32;
                    }
                }
            }

            return RuntimeChipset.Undetermined;
        }

        /// <summary>
        /// Main entry point for the application.
        /// Starts the I-Series launch procedure.
        /// </summary>
        /// <param name="args">the command line arguments.
        /// </param>
        private static void Main(string[] args) {
            new Program(args).Start();
        }
    }
}
