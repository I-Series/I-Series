using System;
using System.IO;

namespace I_Series {
    
    /// <summary>
    /// A helper class that makes working with a jre runtime
    /// environment easier. Provides helper methods for existence
    /// checking and jar execution.
    /// </summary>
    public class Jre {

        /// <summary>
        /// The relative path to the java executable within
        /// the JRE environment.
        /// </summary>
        private static string _relativeExecutablePath = @"bin\java.exe";
        
        /// <summary>
        /// The directory of the top level JRE
        /// environment.
        /// </summary>
        private readonly string _directory;

        /// <summary>
        /// Creates a new JRE helper object.
        /// </summary>
        /// <param name="directory">The directory of the top level JRE
        /// environment.</param>
        public Jre(string directory) {
            _directory = directory;
        }

        /// <summary>
        /// Checks whether or not the jre environment is
        /// present by looking for the java binary executable.
        /// </summary>
        /// <returns><code>true</code> if the runtime environment
        /// exists, <code>false</code> if it does not.</returns>
        public bool Check() {
            Console.Out.WriteLine("Looking for JRE at: {0}{1}", _directory, _relativeExecutablePath);
            return File.Exists($"{_directory}{_relativeExecutablePath}");
        }
    }
}