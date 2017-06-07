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
package org.lmelaia.iseries.build.utils;

import java.io.File;
import static java.io.File.separator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class for working with a file or directory.
 *
 * <p>
 * Provides utilities for reading and writing, modifying, moving and navigating.
 * </p>
 *
 * <b>WARNING: NOT FULLY TESTED</b>
 * 
 * @author Luke
 */
public class SmartFile {

    private static final Logger LOG = LogManager.getLogger();
    
    /**
     * The path to the file or directory in string form.
     */
    private final String path;

    /**
     * File object retrieved from path.
     */
    private final File backingFile;

    /**
     * Path object retrieved from file.
     */
    private final Path backingPath;

    /**
     * Individual file names which make up the path.
     */
    private final String[] paths;

    /**
     * Creates a new smart file with the provided path and rules.
     *
     * @param path the file path
     * @param rules a set of rules the file must adhere to.
     * @return the newly created smart file.
     * @throws Exception if one of the rules cannot be enforced.
     */
    public static SmartFile getSmartFile(File path, Rule... rules)
            throws Exception {
        SmartFile p = getSmartFile(path);

        for (Rule r : Objects.requireNonNull(rules)) {
            r.perform(p);
        }

        return p;
    }

    /**
     * Creates a new smart file with the provided file.
     *
     * @param path the file path
     * @return the newly created smart file.
     */
    public static SmartFile getSmartFile(File path) {
        return new SmartFile(path);
    }

    /**
     * Creates a new smart file with the provided file and rules.
     *
     * @param path the file path
     * @param rules a set of rules the file must adhere to.
     * @return the newly created smart file.
     * @throws Exception
     */
    public static SmartFile getSmartPath(String path, Rule... rules)
            throws Exception {
        SmartFile p = SmartFile.getSmartFile(path);

        for (Rule r : Objects.requireNonNull(rules)) {
            r.perform(p);
        }

        return p;
    }

    /**
     * Creates a new smart file with the provided file.
     *
     * @param path the file path
     * @return the newly created smart file.
     */
    public static SmartFile getSmartFile(String path) {
        SmartFile createdFile = new SmartFile(path);
        return new SmartFile(path);
    }

    /**
     * Creates a new smart file from a file object.
     *
     * @param file
     */
    private SmartFile(File file) {
        this(file.getAbsolutePath());
    }

    /**
     * Creates a new smart file from a file path in String form.
     *
     * @param path
     */
    private SmartFile(String path) {
        this.path = normalize(Objects.requireNonNull(path));
        //Throws an exception if the path looks invalid.
        Path p = Paths.get(path).normalize();
        paths = path.split(separator.equals("\\")
                ? separator + separator : separator);
        backingFile = p.toFile();
        backingPath = p;
        
        LOG.debug("Created smart file: " + this.path);
    }

    /**
     * @return a new file writer which will write to the file or null if the
     * file is a directory
     * @throws IOException if the file does not exist but cannot be created, or
     * cannot be opened for any other reason
     */
    public FileWriter getWriter() throws IOException {
        if (isDirectory()) {
            return null;
        }

        return new FileWriter(backingFile);
    }

    /**
     * @return the file path as a string.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @return a new output stream which will write to the file or null if the
     * file is a directory.
     * @throws FileNotFoundException if the file does not exist but cannot be
     * created, or cannot be opened for any other reason
     */
    public FileOutputStream getOutputStream() throws FileNotFoundException {
        if (isDirectory()) {
            return null;
        }

        return new FileOutputStream(backingFile);
    }

    /**
     * @return a new file reader which will read from this file or null if the
     * file is a directory.
     * @throws FileNotFoundException if the file does not exist, or for some
     * other reason cannot be opened for reading.
     */
    public FileReader getReader() throws FileNotFoundException {
        if (isDirectory()) {
            return null;
        }

        return new FileReader(backingFile);
    }
    
    /**
     * @return a new file input stream which will read from this file or null
     * if the file is a directory.
     * @throws FileNotFoundException if the file does not exist, or for some
     * other reason cannot be opened for reading.
     */
    public FileInputStream getInputStream() throws FileNotFoundException {
        if (isDirectory()) {
            return null;
        }

        return new FileInputStream(backingFile);
    }

    /**
     * @return the file path as a string with a path separator at the end.
     */
    public String getPathWithTrailingSeparator() {
        return path + separator;
    }

    /**
     * @return the name of the file and file extension.
     */
    public String getFileName() {
        return backingFile.getName();
    }

    /**
     * @return a list of all the files contained within this directory cast as
     * smart files or null if the file is an actual file not a directory.
     */
    public SmartFile[] list() {
        if(isFile())
            return null;
        
        List<SmartFile> list = new ArrayList<>();

        for (File f : backingFile.listFiles()) {
            list.add(new SmartFile(f));
        }

        return list.toArray(new SmartFile[list.size()]);
    }

    /**
     * @return true if this file is a directory.
     */
    public boolean isDirectory() {
        return backingFile.isDirectory();
    }

    /**
     * @return true if this file is an actual file and not a directory.
     */
    public boolean isFile() {
        return backingFile.isFile();
    }

    /**
     * Copies this file to another file.
     * 
     * @param to the destination file.
     * @return the file the content was copied to.
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     */
    public SmartFile copy(SmartFile to) throws IOException {
        if (to.isDirectory() && this.isDirectory()) {
            FileUtils.copyDirectory(backingFile, to.backingFile);
        } else if (to.isFile() && this.isFile()) {
            FileUtils.copyFile(backingFile, to.backingFile);
        }

        return to;
    }

    /**
     * Writes the provided string to the file.
     * 
     * @param content the content to write to the file.
     * @throws IOException if the content cannot be written.
     */
    public void write(String content) throws IOException {
        try (FileWriter writer = this.getWriter()) {
            writer.write(content);
        }
    }

    /**
     * Appends the provided string to the end of the file.
     * 
     * @param content the content to append.
     * @throws IOException if the content cannot be appended.
     */
    public void append(String content) throws IOException {
        String originalContent = read();

        try (FileWriter writer = this.getWriter()) {
            writer.write(originalContent + content);
        }
    }

    /**
     * Reads the content from the file.
     * 
     * @return the content within the file.
     * @throws IOException if the content cannot be read.
     */
    public String read() throws IOException {
        String ret = "";

        try (FileInputStream reader = this.getInputStream()) {
            Scanner sc = new Scanner(reader);
            while (sc.hasNextLine()) {
                ret += sc.nextLine();
            }
            return ret;
        }
    }

    /**
     * @return the file extension (e.g. .exe).
     */
    public String getFileExtension() {
        String last = "";

        for (String llast : path.split(".")) {
            last = llast;
        }

        return last;
    }

    /**
     * @return the file object this smart file represents.
     */
    public File getFile() {
        return backingFile;
    }

    /**
     * @return the path object obtained from the file.
     */
    public Path getPathObject() {
        return backingPath;
    }

    /**
     * If this file is not a directory, creates the file if it is nonexistent.
     * Creates the directory and all required but nonexistent parent directories
     * if this file is a directory.
     * 
     * @throws IOException if the file or directories cannot be created.
     */
    public void make() throws IOException {
        if (isFile()) {
            backingFile.createNewFile();
        } else {
            backingFile.mkdirs();
        }
    }

    /**
     * @return true if the file or directory exists.
     */
    public boolean exists() {
        return backingFile.exists();
    }

    /**
     * Deletes the file or directory.
     * 
     * @throws IOException if the file or directories cannot be deleted.
     */
    public void delete() throws IOException {
        FileUtils.forceDelete(backingFile);
    }

    /**
     * Looks for a file or directory that matches the provided path name
     * within this folder and returns it.
     * 
     * @param pathname the name of the file or directory.
     * @return the file or directory as a smart file, or null
     * if this smart file doesn't represent a directory.
     */
    public SmartFile forward(String pathname) {
        if(isFile()){
            return null;
        }
        
        pathname = normalize(pathname);
        if (pathname.startsWith(separator)) {
            pathname = makeRelative(pathname.substring(1));
        }

        return new SmartFile(this.path + separator + pathname);
    }

    /**
     * @return true if a read operation will succeed on this file.
     */
    public boolean canRead() {
        return backingFile.canRead();
    }

    /**
     * @return true if a write operation will succeed on this file.
     */
    public boolean canWrite() {
        return backingFile.canWrite();
    }

    /**
     * Provides a means to read or write to this file in a safe manner.
     * 
     * <p>
     * The steam or reader/writer will be closed when this method returns
     * </p>
     * 
     * This method will propagate any exception thrown during the process. 
     * 
     * @param rsb the type of stream (e.i. read or write) as steam or file
     * reader/writer 
     * @throws IOException if any exception is thrown during the process.
     */
    public void doSafelyWithException(ResourceSafeBlock rsb)
            throws IOException {
        if (rsb instanceof FileReaderResouceSafeBlock) {
            FileReaderResouceSafeBlock frrsb
                    = (FileReaderResouceSafeBlock) rsb;

            try (FileReader reader = getReader()) {
                frrsb.action(reader);
            }
        }

        if (rsb instanceof FileWriterResouceSafeBlock) {
            FileWriterResouceSafeBlock fwrsb
                    = (FileWriterResouceSafeBlock) rsb;

            try (FileWriter writer = getWriter()) {
                fwrsb.action(writer);
            }
        }

        if (rsb instanceof FileInputStreamResouceSafeBlock) {
            FileInputStreamResouceSafeBlock fisrsb
                    = (FileInputStreamResouceSafeBlock) rsb;

            try (FileInputStream input = getInputStream()) {
                fisrsb.action(input);
            }
        }

        if (rsb instanceof FileOutputStreamResouceSafeBlock) {
            FileOutputStreamResouceSafeBlock fisrsb
                    = (FileOutputStreamResouceSafeBlock) rsb;

            try (FileOutputStream output = getOutputStream()) {
                fisrsb.action(output);
            }
        }

        throw new IllegalArgumentException("Resource type not supported");
    }

    /**
     * Behaves the same way as
     * {@link #doSafelyWithException
     * (org.lmelaia.iseries.build.utils.SmartFile.ResourceSafeBlock)}, only if
     * an exception occurs false will be returned and true if no exception is
     * thrown.
     * 
     * @param rsb the type of stream (e.i. read or write) as steam or file
     * reader/writer 
     * @return true if not exception was thrown, false if one was.
     */
    public boolean doSafely(ResourceSafeBlock rsb) {
        try {
            doSafelyWithException(rsb);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Renames this file.
     * 
     * @param dest the file the contents of this file will be copied to.
     * @return the new file as a smart file.
     */
    public SmartFile renameTo(SmartFile dest) {
        if (backingFile.renameTo(dest.backingFile)) {
            return dest;
        }

        return null;
    }

    /**
     * @return the parent directory of this file or directory as a smart file.
     */
    public SmartFile goBack() {
        return goBack(1);
    }

    /**
     * @param times how many directories to go back.
     * @return one of the parent directories (determined by {@codde times})
     * of this file or directory.
     */
    public SmartFile goBack(int times) {
        String newpath = "";
        
        for(int i = 0; i < paths.length - times; i++){
            newpath += paths[i] + separator;
        }
        
        return new SmartFile(newpath);
    }

    /**
     * Takes a path as a string and turns all file separators into the os
     * file separator and removes any trailing path separators.
     * 
     * @param path the path.
     * @return the path after normalization.
     */
    public static String normalize(String path) {
        path = path.replace("\\", separator);
        path = path.replace("/", separator);

        if (path.endsWith(separator)) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    /**
     * Makes the provided path absolute (i.e. begins with a path separator).
     * 
     * @param path
     * @return 
     */
    public static String makeAbsolute(String path) {
        if (path.indexOf(separator) != 1) {
            path = separator + path;
        }

        return path;
    }

    /**
     * Makes the provided path relative (i.e. doesn't begin with a path
     * separator).
     * 
     * @param path
     * @return 
     */
    public static String makeRelative(String path) {
        if (path.indexOf(separator) == 1) {
            path = path.substring(1);
        }

        return path;
    }

    /**
     * A set of rules that can applied to a file which must be adhered to.
     */
    public enum Rules implements Rule {
        
        /**
         * Throws an exception if the file or directory doesn't exist at
         * creation time.
         */
        MUST_EXIST {
            @Override
            public void perform(SmartFile path) throws IOException {
                if (!path.backingFile.exists()) {
                    throw new FileNotFoundException(
                            "The file: " + path.path + " does not exist");
                }
            }
        },
        
        /**
         * If the file or directory doesn't exist at creation time, the file or
         * directory will be created or an exception will be thrown if the file
         * or directory cannot be created.
         */
        CREATE_IF_POSSIBLE {
            @Override
            public void perform(SmartFile path) throws IOException {
                if (!path.backingFile.exists()) {
                    path.make();
                }
            }
        },
        
        /**
         * If the file or directory exists at creation time, it will
         * be deleted and recreated or an exception will be thrown if
         * the file or directory cannot be deleted or created.
         */
        MAKE_NEW {
            @Override
            public void perform(SmartFile path) throws IOException {
                if (path.backingFile.exists()) {
                    path.delete();
                    path.make();
                }
            }
        };

        /**
         * {@inheritDoc }
         */
        @Override
        public void perform(SmartFile path) throws Exception {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Base class of all file rules.
     * 
     * Provides the method required to ensure the rule is adhered to. 
     */
    public interface Rule {
        void perform(SmartFile path) throws Exception;
    }

    /**
     * The base class of resource safe classes. A resource safe class
     * provides one method which allows a read or write to the file to be
     * performed in a same manner.
     */
    public interface ResourceSafeBlock {}

    /**
     * see {@link SmartFile.ResourceSafeBlock}
     * 
     * Provides a file reader.
     */
    public interface FileReaderResouceSafeBlock extends ResourceSafeBlock {
        void action(FileReader reader) throws IOException;
    }

    /**
     * see {@link SmartFile.ResourceSafeBlock}
     * 
     * Provides a file writer.
     */
    public interface FileWriterResouceSafeBlock extends ResourceSafeBlock {
        void action(FileWriter writer) throws IOException;
    }

    /**
     * see {@link SmartFile.ResourceSafeBlock}
     * 
     * Provides a file output stream.
     */
    public interface FileOutputStreamResouceSafeBlock extends ResourceSafeBlock {
        void action(FileOutputStream reader) throws IOException;
    }

    /**
     * see {@link SmartFile.ResourceSafeBlock}
     * 
     * Provides a file input stream.
     */
    public interface FileInputStreamResouceSafeBlock extends ResourceSafeBlock {
        void action(FileInputStream reader) throws IOException;
    }
}
