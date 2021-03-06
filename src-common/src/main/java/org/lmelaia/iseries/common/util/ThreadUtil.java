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

package org.lmelaia.iseries.common.util;

/**
 * Utilities for use in thread manipulation.
 */
public class ThreadUtil {

    private ThreadUtil() {
    }

    /**
     * Non exception throwing {@link Thread#sleep(long)}.
     *
     * @param mills the number of milliseconds to sleep for.
     * @return true if no {@link InterruptedException} was thrown,
     * false otherwise.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean silentSleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            return false;
        }

        return true;
    }
}
