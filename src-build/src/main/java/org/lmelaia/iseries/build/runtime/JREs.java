package org.lmelaia.iseries.build.runtime;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * A JREs object helps in the validation
 * and copying of both x32 & x64 bit Java
 * JRE runtime during the full build of the
 * I-Series application.
 */
public class JREs {

    /**
     * An ordered array of all the files and folders
     * expected to be in a valid Java JRE runtime.
     */
    private static final String[] JRE_STRUCTURE = new String[]{
            "bin", "conf", "legal", "lib",
            "LICENSE", "readme.md", "readme.txt", "release"
    };

    /**
     * The x32bit and x64bit Java JRE runtime folders.
     */
    private final File jreX64, jreX32;

    /**
     * Creates a new JREs helper instance.
     *
     * @param jreX64 the x64bit runtime directory.
     * @param jreX32 the x32bit runtime directory.
     * @throws IllegalArgumentException if either of
     *                                  the runtimes are invalid for use.
     */
    public JREs(File jreX64, File jreX32) throws IllegalArgumentException {
        this.jreX32 = jreX32;
        this.jreX64 = jreX64;
        validateJREs();
        validateJREStructures();
    }

    /**
     * Ensures both JRE runtimes are available in an
     * existing directory.
     *
     * @throws IllegalArgumentException if a JRE is not accessible.
     */
    private void validateJREs() throws IllegalArgumentException {
        if (!jreX32.exists())
            throw new IllegalArgumentException("The JREx32 runtime directory does not exist.");
        if (!jreX64.exists())
            throw new IllegalArgumentException("The JREx64 runtime directory does not exist.");

        if (!jreX32.isDirectory())
            throw new IllegalArgumentException("The JREx32 runtime is not a directory.");
        if (!jreX64.isDirectory())
            throw new IllegalArgumentException("The JREx32 runtime is not a directory.");
    }

    /**
     * Ensures the file/folder structure of the provided JREs are
     * correct, according to {@link #JRE_STRUCTURE}.
     *
     * @throws IllegalArgumentException if the file/folder structure
     *                                  of a provided JRE is not valid.
     */
    private void validateJREStructures() throws IllegalArgumentException {
        File[] jreX32Files = jreX32.listFiles();
        File[] jreX64Files = jreX64.listFiles();

        if (jreX32Files == null)
            throw new IllegalArgumentException("The JREx32 runtime directory contains no files.");
        if (jreX64Files == null)
            throw new IllegalArgumentException("The JREx64 runtime directory contains no files.");

        if (jreX32Files.length != JRE_STRUCTURE.length)
            throw new IllegalArgumentException("The JREx32 runtime directory has the incorrect amount of files.");
        if (jreX64Files.length != JRE_STRUCTURE.length)
            throw new IllegalArgumentException("The JREx64 runtime directory has the incorrect amount of files.");

        for (int i = 0; i <= JRE_STRUCTURE.length - 1; i++) {
            if (!jreX32Files[i].getName().equals(JRE_STRUCTURE[i])) {
                throw new IllegalArgumentException(
                        "The JREx32 runtime directory is missing the file/folder: " + JRE_STRUCTURE[i]
                );
            }

            if (!jreX32Files[i].getName().equals(JRE_STRUCTURE[i])) {
                throw new IllegalArgumentException(
                        "The JREx64 runtime directory is missing the file/folder: " + JRE_STRUCTURE[i]
                );
            }
        }
    }

    /**
     * Copies the specified JRE runtime over to the
     * specified folder.
     *
     * @param x64                  {@code true} to copy over the x64bit runtime.
     *                             {@code false} to copy over the x32bit
     *                             runtime.
     * @param destinationDirectory the directory to copy the runtime to.
     * @throws IOException if an exception arises during the copying process.
     */
    public void copyOver(boolean x64, File destinationDirectory) throws IOException {
        if (x64) FileUtils.copyDirectory(jreX64, destinationDirectory);
        else FileUtils.copyDirectory(jreX32, destinationDirectory);
    }
}
