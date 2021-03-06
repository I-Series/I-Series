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
package org.lmelaia.iseries.build.packaging;

import java.io.*;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides support for zipping up the project for distribution.
 *
 * @author Luke
 */
public class ZipPackager {

    /**
     * The zip file.
     */
    private final File ZIP_FILE;

    /**
     * The directory to zip.
     */
    private final File DIRECTORY;

    /**
     * Creates a new zip packager.
     * 
     * @param zip the zip file.
     * @param directory the directory to zip.
     */
    public ZipPackager(File zip, File directory) {
        if (!zip.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("File must be a zip file");
        }

        ZIP_FILE = zip;
        DIRECTORY = Objects.requireNonNull(directory);
    }

    /**
     * Creates the zip file.
     * 
     * @throws IOException if the zip file cannot be created.
     */
    public void create() throws IOException {
        zip(DIRECTORY, ZIP_FILE);
    }

    /**
     * @return the zip file.
     */
    @SuppressWarnings("unused")
    public File getZipFile() {
        return ZIP_FILE;
    }

    /**
     * Zips the files and folder within the directory.
     *  
     * @param directory the directory to zip
     * @param zipFile the zip file
     * @throws IOException  if the zip file cannot be written to.
     */
    @SuppressWarnings("ConstantConditions")
    private static void zip(File directory, File zipFile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(Objects.requireNonNull(directory));
        OutputStream out = new FileOutputStream(zipFile);
        Closeable res = out;
        try {
            ZipOutputStream zipOut = new ZipOutputStream(out);
            res = zipOut;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zipOut.putNextEntry(new ZipEntry(name));
                    } else {
                        zipOut.putNextEntry(new ZipEntry(name));
                        copy(kid, zipOut);
                        zipOut.closeEntry();
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            copy(in, out);
        }
    }

    @SuppressWarnings("unused")
    private static void copy(InputStream in, File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            copy(in, out);
        }
    }
}
