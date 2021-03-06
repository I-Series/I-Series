﻿using System;
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
        /// A list of arguments meant for this specific application, and which should
        /// not be passed onto the actual I-Series process.
        /// </summary>
        private static readonly object[] FilteredArguments = { "--architecture", "--shell"};

        /// <summary>
        /// An object representation of every file that should exist within the
        /// current working directory (excluding jre), which can be used to
        /// easily verify that each file does exist.
        /// </summary>
        private static readonly VeriStructure CoreFiles = new VeriStructure(Directory.GetCurrentDirectory())
            .Add(new VeriStructure("bin")
                //Bin
                .Add("I-Series-App.jar")
                .Add("I-Series-Launcher.jar")
                .Add("I-Series-Updater.jar")
            ).Add(new VeriStructure("legal")
                //Legal
                .Add("Gson Licence.txt")
                .Add("Guava Licence.txt")
                .Add("I-Series-Updater Licence.txt")
                .Add("Log4j Licence.txt")
                .Add("src-common Licence.txt")
            ).Add(new VeriStructure("libs")
                //Libs
                .Add("checker-qual-2.11.1.jar")
                .Add("error_prone_annotations-2.3.4.jar")
                .Add("failureaccess-1.0.1.jar")
                .Add("gson-2.8.0.jar")
                .Add("guava-29.0-jre.jar")
                .Add("j2objc-annotations-1.3.jar")
                .Add("jsr305-3.0.2.jar")
                .Add("listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar")
                .Add("log4j-api-2.8.2.jar")
                .Add("log4j-core-2.8.2.jar")
                .Add("src-common.jar")
            )
            //Root
            .Add("I-Series Acknowledgements.txt").Add("I-Series Licence.txt");
        
        /// <summary>
        /// The runtime configuration settings.
        /// </summary>
        private static readonly RuntimeSettings RuntimeSettings = new RuntimeSettings.RuntimeSettingsBuilder()
            .SetJre32Path(@"\runtime\x32\")
            .SetJre64Path(@"\runtime\x64\")
            .SetWorkingDirectory(@"\bin\")
            .AddAdditionalArgument("-Dprism.vsync=false")
            .SetLauncherJar(@"\bin\I-Series-Launcher.jar")
            .Build();
        
        /// <summary>
        /// The current working directory the executable was launched from.
        /// </summary>
        private readonly string _workingDirectory = Directory.GetCurrentDirectory();

        /// <summary>
        /// The command line arguments the executable was launched with.
        /// </summary>
        private readonly List<object> _cmdArguments;

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
        private Chipset _runtimeChipset;
        
        private Program(string[] args) {
            _cmdArguments = new List<object>();
            foreach (var arg in args) {
                _cmdArguments.Add(arg);
            }
            
            _is64BitEnv = Environment.Is64BitOperatingSystem;
            _jreX32 = new Jre($"{_workingDirectory}{RuntimeSettings.Jre32Path}");
            _jreX64 = new Jre($"{_workingDirectory}{RuntimeSettings.Jre64Path}");
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

            CoreFiles.Verify();
            ConfigureRuntime();
            
            ISProcess process = new ISProcess(RuntimeSettings, Filter(_cmdArguments),
                _runtimeChipset == Chipset.X32 ? _jreX32 : _jreX64, HasShellFlag()
            );
            process.Run();
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
            if (!(hasJreX32 || hasJreX64)) {
                //Fail
                MessageBox.Show("No JRE runtime found. Please reinstall I-Series.");
                Environment.Exit(-1);
                return;
            }

            if (_is64BitEnv && !hasJreX64) {
                //Warn
                MessageBox.Show("Warning: using x32bit runtime on x64bit environment.");
            }
            
            if (!_is64BitEnv && !hasJreX32) {
                //Fail
                MessageBox.Show("No x32bit runtime found. Please reinstall I-Series for x32bit Windows.");
                Environment.Exit(-2);
                return;
            }

            //Determine runtime mode (x32, x64)
            if (hasJreX32 && hasJreX64) {
                _runtimeChipset = GetChipsetFromArgs();
                if(_runtimeChipset == Chipset.Undetermined)
                    _runtimeChipset = _is64BitEnv ? Chipset.X64 : Chipset.X32;
            } else if (hasJreX32) _runtimeChipset = Chipset.X32;
            else _runtimeChipset = Chipset.X64;
            
            Console.Out.WriteLine("Determined chipset: {0}",
                _runtimeChipset == Chipset.X32 ? "x32bit" : "x64bit"
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
        private Chipset GetChipsetFromArgs() {
            foreach (var arg in _cmdArguments) {
                if (arg.ToString().StartsWith("--architecture")) {
                    string arch = arg.ToString().Replace("--architecture", "");
                    arch = arch.Replace("=", "");
                    arch = arch.Replace(" ", "");

                    switch (arch) {
                        case "x64":
                        case "X64":
                        case "64":
                            return Chipset.X64;
                        case "x32":
                        case "X32":
                        case "32":
                            return Chipset.X32;
                    }
                }
            }

            return Chipset.Undetermined;
        }

        /// <summary>
        /// Used to check if the <code>--shell</code>
        /// flag was used in the command line arguments.
        /// If true, will open a shell window with
        /// the application output.
        /// </summary>
        /// <returns><code>true</code> if the shell flag
        /// was used in the command line arguments.</returns>
        private bool HasShellFlag() {
            foreach (var arg in _cmdArguments) {
                if (arg.ToString().ToLower().StartsWith("--shell"))
                    return true;
            }
            
            return false;
        }

        /// <summary>
        /// Used to filter out specific commands (FilteredArguments)
        /// from the array of command line arguments given.
        /// </summary>
        /// <param name="arguments">an array of command line arguments
        /// that needs filtering.</param>
        /// <returns>the filtered command line arguments.</returns>
        private List<object> Filter(List<object> arguments) {
            List<object> filtered = new List<object>(arguments);

            foreach (var argument in arguments) {
                foreach (var targetArg in FilteredArguments) {
                    if (argument.ToString().StartsWith(targetArg.ToString()))
                        filtered.Remove(argument);
                }
            }
            
            return filtered;
        }

        /// <summary>
        /// Main entry point for the application.
        /// Starts the I-Series launch procedure.
        /// </summary>
        /// <param name="args">the command line arguments.
        /// </param>
        // ReSharper disable once UnusedMember.Local
        private static void Main(string[] args) {
            new Program(args).Start();
        }
    }
}
