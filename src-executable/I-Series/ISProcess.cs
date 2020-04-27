using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;

namespace I_Series {
    
    /// <summary>
    /// A wrapper process handler that allows easily
    /// launching a new I-Series process with the given
    /// JRE runtime and properties.
    /// </summary>
    // ReSharper disable once InconsistentNaming
    public class ISProcess {

        /// <summary>
        /// The generic process object used to launch the
        /// I-Series process.
        /// </summary>
        private readonly Process _iSeriesProcess = new Process();

        /// <summary>
        /// The current directory where this executable was
        /// launched from.
        /// </summary>
        private readonly string _currentDir = Directory.GetCurrentDirectory();
        
        /// <summary>
        /// Creates a new process from which to launch the I-Series
        /// application.
        /// </summary>
        /// <param name="settings">The various settings used, such as working directory.</param>
        /// <param name="arguments">the list of argument this executable was launched with. Passed on.</param>
        /// <param name="environment">the java runtime environment to use.</param>
        /// <param name="withShell"><code>true</code> to open output shell window.</param>
        public ISProcess(RuntimeSettings settings, List<object> arguments, Jre environment, bool withShell) {
            _iSeriesProcess.StartInfo.FileName = environment.ExecutablePath;
            _iSeriesProcess.StartInfo.WorkingDirectory = $"{_currentDir}{settings.WorkingDirectory}";

            string args = "";

            foreach (var arg in settings.AdditionalArguments) {
                args = $"{args} \"{arg}\"";
            }

            args = $"{args} -jar {_currentDir}{settings.LauncherJar}";
            
            if(arguments.Count != 0)
                foreach (var arg in arguments) {
                    args = $"{args} \"{arg}\"";
                }

            _iSeriesProcess.StartInfo.Arguments = args;
            _iSeriesProcess.StartInfo.UseShellExecute = withShell;
            _iSeriesProcess.StartInfo.CreateNoWindow = !withShell;
        }

        /// <summary>
        /// Starts the configured I-Series process.
        /// Will also print out process details.
        /// </summary>
        public void Run() {
            Console.Out.WriteLine("Launching I-Series...");
            Console.Out.WriteLine("Working directory: {0}", _iSeriesProcess.StartInfo.WorkingDirectory);
            Console.Out.WriteLine("Launch Command: {0}",
                $"{_iSeriesProcess.StartInfo.FileName}{_iSeriesProcess.StartInfo.Arguments}"
            );
            _iSeriesProcess.Start();
            Console.Out.WriteLine("Process ID: {0}", _iSeriesProcess.Id);
            Console.Out.WriteLine("Start time: {0}", _iSeriesProcess.StartTime);
        }
    }
}