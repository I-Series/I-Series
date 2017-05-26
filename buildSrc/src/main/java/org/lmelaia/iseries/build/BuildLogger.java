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
package org.lmelaia.iseries.build;

import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.ReflectionUtil;

/**
 * Initializes the log4j logger and provides access to a logger.
 * 
 * @author Luke
 */
public final class BuildLogger{
    
    /**
     * Instance object (not made public).
     */
    private static final BuildLogger INSTANCE = new BuildLogger();
    
    /**
     * Logger Configuration file.
     */
    private final URL LOG4J2_CFG_FILE
            = BuildConfiguration.class.getResource(
                    "/configuration/log4j2_configuration.xml");
    
    private final Logger LOG = getLogger();
    
    /**
     * Initializes the logger context and provides the config file.
     */
    private BuildLogger(){
        LoggerContext context
                = (LoggerContext) LogManager.getContext(false);
        
        try {
            context.setConfigLocation(LOG4J2_CFG_FILE.toURI());
        } catch (URISyntaxException ex) {
            System.err.println("Failed to set logger config file location\n"
                    + ex);
        }
        
        LOG.info("Logger initialized with configuration file: "
                + LOG4J2_CFG_FILE.getFile());
        LOG.info("Ignore configuration file not found warning.");
    }
    
    /**
     * @return a log4j logger as though it was called through
     * {@link org.apache.logging.log4j.LogManager#getLogger()}
     */
    public static final Logger getLogger(){
        return LogManager.getLogger(ReflectionUtil.getCallerClass(2));
    }
}
