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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * @throws IOException 
     */
    public ZipPackager(File zip, File directory) throws IOException {
        if (!zip.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("File must be a zip file");
        }

        if (Objects.requireNonNull(zip).exists()) {
            zip.delete();
        }

        ZIP_FILE = zip;
        DIRECTORY = Objects.requireNonNull(directory);
    }

    /**
     * Creates the zip file.
     * 
     * @throws IOException 
     */
    public void create() throws IOException {
        zip(DIRECTORY, ZIP_FILE);
    }

    /**
     * @return the zip file.
     */
    public File getZIP_FILE() {
        return ZIP_FILE;
    }

    /**
     * Zips the files and folder within the directory.
     *  
     * @param directory
     * @param zipfile
     * @throws IOException 
     */
    private static void zip(File directory, File zipfile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
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

    private static void copy(InputStream in, File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            copy(in, out);
        }
    }
}
