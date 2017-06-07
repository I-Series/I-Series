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
 * This exception is thrown when an option passed to the
 * {@link Launch4jConfigurationBuilder} is invalid.
 *
 * @author Luke Melaia
 */
public class InvalidLaunch4jConfigurationException extends RuntimeException {

    public InvalidLaunch4jConfigurationException() {
        super();
    }

    InvalidLaunch4jConfigurationException(String message) {
        super(message);
    }

    public InvalidLaunch4jConfigurationException(
            String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLaunch4jConfigurationException(Throwable cause) {
        super(cause);
    }

    protected InvalidLaunch4jConfigurationException(
            String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
