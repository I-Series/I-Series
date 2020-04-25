using System;
using System.Diagnostics.CodeAnalysis;
using System.IO;

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
        private readonly object[] _cmdArguments;

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
        
        public Program(string[] args) {
            _cmdArguments = args;
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
            if (_cmdArguments.Length == 0) Console.Out.WriteLine("Command line arguments: None");
            else Console.Out.WriteLine("Command line arguments: {0}", _cmdArguments);

            bool hasJreX32, hasJreX64;
            
            Console.Out.WriteLine("Has x32 JRE: {0}", (hasJreX32 = _jreX32.Check()).ToString());
            Console.Out.WriteLine("Has x64 JRE: {0}", (hasJreX64 = _jreX64.Check()).ToString());
        }

        /// <summary>
        /// Main entry point for the application.
        /// Starts the I-Series launch procedure.
        /// </summary>
        /// <param name="args">the command line arguments.</param>
        static void Main(string[] args) {
            new Program(args).Start();
        }
    }
}
