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

package org.lmelaia.iseries.common.system;

/**
 * Callback for application shutdown requests.
 */
public interface ShutdownListener {

    /**
     * Called when the application is requested to close,
     * either through the user or a crash.
     *
     * @param code the code given as the reason for the application to close.
     * @return true if the shutdown process should continue, false to
     * abort the shutdown.
     */
    boolean onShutdown(ExitCode code);
}
