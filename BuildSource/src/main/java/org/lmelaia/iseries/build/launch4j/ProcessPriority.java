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
package org.lmelaia.iseries.build.launch4j;

/**
 * The thread priority of the application.
 *
 * @author Luke Melaia
 */
@SuppressWarnings("unused")
public enum ProcessPriority {

    /**
     * Normal priority.
     */
    NORMAL,
    /**
     * Idle or background priority.
     */
    IDLE,
    /**
     * High priority.
     */
    HIGH
}
