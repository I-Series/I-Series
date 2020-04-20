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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.ReflectionUtil;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utilities for use with the apache logger.
 *
 * @author Luke
 */
public final class AppLogger {

    private static Logger LOG;

    /**
     * Has the logger been initialized, though one
     * of the configure methods.
     */
    private static boolean initialized = false;

    /**
     * Works like {@link #configure(URI)} only the path to a resource file
     * is passed in.
     *
     * @param resource the log4j2 configuration file resource path.
     */
    @SuppressWarnings("unused")
    public static void configure(String resource) throws URISyntaxException {
        configure(AppLogger.class.getResource(resource).toURI());
    }

    /**
     * Works like {@link #silentConfigure(URI)} only the path to a resource file
     * is passed in.
     *
     * @param resource the log4j2 configuration file resource path.
     */
    @SuppressWarnings("SameParameterValue")
    public static void silentConfigure(String resource) {
        try {
            silentConfigure(AppLogger.class.getResource(resource).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Works like {@link #configure(URI)} expect the method will
     * fail silently instead of throwing an exception.
     *
     * @param config the log4j2 configuration file.
     */
    @SuppressWarnings("WeakerAccess")
    public static void silentConfigure(URI config) {
        try {
            configure(config);
        } catch (IllegalStateException ex) {
            LOG.warn("Silent configure failed: ", ex);
        }
    }

    /**
     * Configures the logger with a specified configuration file.
     * <p>
     * This must be called before getting a logger through
     * {@link #getLogger()}.
     *
     * @param config the log4j2 configuration file.
     * @throws IllegalStateException if the logger is already
     *                               configured.
     */
    @SuppressWarnings("WeakerAccess")
    public static void configure(URI config) {
        if (initialized)
            throw new IllegalStateException("App Logger is already configured");

        LoggerContext context = (LoggerContext) LogManager.getContext(false);

        context.setConfigLocation(config);
        initialized = true;

        /*The logger for this class.*/
        LOG = getLogger();
        LOG.info("Logger initialized with configuration file: "
                + config.toString());
    }

    /**
     * Returns a logger provided by the apache log manager.
     * <p>
     * Calling this method will start the app logger.
     */
    public static Logger getLogger() {
        if (!initialized) {
            throw new IllegalStateException("Logger not yet configured.");
        }

        return LogManager.getLogger(ReflectionUtil.getCallerClass(2));
    }
}
