using System;
using System.Windows.Forms;
using System.Collections.Generic;
using System.IO;

namespace I_Series {
    
    /// <summary>
    /// Represents either a complete or individual part
    /// of a verifiable file structure.
    ///
    /// A VeriStructure is used to create representations
    /// of relative file structures which can be used to
    /// verify that each component is present.
    ///
    /// VeriStructures can be chained and nested to
    /// form the complete structure.
    /// </summary>
    public class VeriStructure {

        /// <summary>
        /// The file or directory name this
        /// VeriStructure represents.
        /// </summary>
        private readonly string _name;

        /// <summary>
        /// The list of child VeriStructures added to
        /// this VeriStructure as though it were a
        /// directory.
        /// </summary>
        private readonly List<VeriStructure> _nested;

        /// <summary>
        /// The parent VeriStructures this VeriStructure
        /// was added to, if any.
        /// </summary>
        private VeriStructure Parent { get; set; }

        /// <summary>
        /// The full name of this VeriStructure and all parents
        /// appended together with a <code>\</code>
        /// </summary>
        private string FullName => (Parent != null ? Parent._name + "\\" : "")  + _name;
        
        /// <summary>
        /// Creates a new VeriStructure representing either
        /// a file or directory.
        /// </summary>
        /// <param name="name">the file or directory name.</param>
        public VeriStructure(string name) {
            _name = name;
            _nested = new List<VeriStructure>();
        }

        /// <summary>
        /// Adds a child file or directory, represented by a
        /// VeriStructure, to this VeriStructure. Doing so
        /// forever denotes this VeriStructure as a directory.
        /// </summary>
        /// <param name="child">The child file/directory
        /// VeriStructure representation</param>
        /// <returns>this object.</returns>
        public VeriStructure Add(VeriStructure child) {
            child.Parent = this;
            _nested.Add(child);
            return this;
        }

        /// <summary>
        /// Adds a child file, represented by a VeriStructure,
        /// to this VeriStructure. Doing so forever denotes
        /// this VeriStructure as a directory.
        /// </summary>
        /// <param name="structureName">The child file/directory
        /// VeriStructure representation</param>
        /// <returns>this object.</returns>
        public VeriStructure Add(string structureName) {
            return Add(new VeriStructure(structureName));
        }

        /// <summary>
        /// Verifies all files and directories this VeriStructure
        /// and all children VeriStructures represent on disk exist.
        ///
        /// All output is logged to the console, and a MessageBox
        /// warning is displayed for each missing file.
        /// </summary>
        public void Verify() {
            if (_nested.Count != 0) {
                foreach (var structure in _nested) {
                    structure.Verify();
                }
            } else {
                if (File.Exists(FullName)) Console.Out.WriteLine("Found core file: {0}", FullName);
                else {
                    Console.Out.WriteLine("Core file: {0} is missing...", FullName);
                    MessageBox.Show("Warning: the file \"" + FullName + "\" is missing");
                }
            }
        }
    }
}