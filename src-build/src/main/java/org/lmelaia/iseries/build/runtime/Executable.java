package org.lmelaia.iseries.build.runtime;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * A helper object that helps with copying over
 * the I-Series executable during build.
 */
public class Executable {

    /**
     * The path to the release version of the executable,
     * relative to the project directory.
     */
    private static final String EXECUTABLE_BINARY = "/src-executable/I-Series/bin/Release/I-Series.exe";

    /**
     * The resolved absolute I-Series executable binary file.
     */
    private final File executableBinary;

    /**
     * @param rootProjectDir the project directory.
     */
    public Executable(File rootProjectDir) {
        this.executableBinary = new File(rootProjectDir + EXECUTABLE_BINARY);
    }

    /**
     * @return {@code true} if the executable exists.
     */
    public boolean exists() {
        return executableBinary.exists();
    }

    /**
     * @return the executable binary file.
     */
    public File getExecutableBinary() {
        return executableBinary;
    }

    /**
     * Copies the executable binary to the given destination
     * file.
     *
     * @param destination the destination file.
     * @throws IOException if the file cannot be copied.
     */
    public void copy(File destination) throws IOException {
        FileUtils.copyFile(executableBinary, destination);
    }
}
