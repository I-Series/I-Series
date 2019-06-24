/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.library;

import java.io.File;

/**
 * An EntrySorter that attempts to sort entries on
 * disk by their name.
 * <br/>
 * <p>
 * If no "name" property for the entry
 * exists, the UUID will be used instead.
 * If an entry with the same name already
 * exists, the entry folder will include
 * the name and UUID.
 */
public class NamedEntrySorter implements EntrySorter {

    /**
     * Static instance for this sorter.
     */
    public static final NamedEntrySorter NAMED_ENTRY_SORTER = new NamedEntrySorter();

    /**
     * Private (non instantiable) constructor.
     */
    private NamedEntrySorter() {
    }

    /**
     * {@inheritDoc}
     *
     * @param entry       the entry to be sorted.
     * @param libraryPath the file path to the I-Series Library
     *                    on disk.
     * @return {@inheritDoc}
     */
    @Override
    public String getRelativeFilePath(LibraryEntry entry, File libraryPath) {
        if (entry.getInformation().get("name") == null)
            return entry.getUUID();

        String name = entry.getInformation().get("name").getAsString();
        name = name
                .replace("?", "")
                .replace("/", "")
                .replace("\\", "")
                .replace(":", "");

        File entryFolder = new File(libraryPath + "/" + name);

        if (entryFolder.exists())
            return name + " (" + entry.getUUID() + ")";

        return name;
    }
}
