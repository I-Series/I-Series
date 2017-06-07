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
package org.lmelaia.iseries;

import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.ReflectionUtil;

/**
 * Logging utility class used by the app.
 * 
 * @author Luke
 */
public final class AppLogger {
    
    /**
     * Apache logging instance.
     */
    private static final Logger LOG;
    
    /**
     * Logger Configuration file.
     */
    private static final URL CONFIG_FILE = AppLogger.class.getResource(
                    "/configuration/log4j.xml");
    
    private AppLogger(){}
    
    /*
     * Initializes the logger context and provides the config file.
     */
    static{
        LoggerContext context
                = (LoggerContext) LogManager.getContext(false);
        
        try {
            context.setConfigLocation(CONFIG_FILE.toURI());
        } catch (URISyntaxException ex) {
            System.err.println("Failed to set logger config file\n"
                    + ex);
        }
        
        LOG = LogManager.getLogger();
        
        LOG.info("Logger initialized with configuration file: "
                + CONFIG_FILE.getFile());
        LOG.info(String.format("Logging error level: "
                + "[Fatal:%s, Error:%s, Warn:%s, Info:%s, Debug:%s, Trace:%s]",
                LOG.isFatalEnabled(), LOG.isErrorEnabled(), LOG.isWarnEnabled(),
                LOG.isInfoEnabled(), LOG.isDebugEnabled(), LOG.isTraceEnabled()
        ));
    }
    
    /**
     * @return a log4j logger as though it was called through
     * {@link org.apache.logging.log4j.LogManager#getLogger()}
     */
    public static Logger getLogger(){
        return LogManager.getLogger(ReflectionUtil.getCallerClass(2));
    }
}
