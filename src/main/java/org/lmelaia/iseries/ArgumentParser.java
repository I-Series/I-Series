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

import java.util.HashMap;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Luke
 */
public class ArgumentParser {
    
    private static final Logger LOG = AppLogger.getLogger();
    
    private final HashMap<String, ArgumentValue> args = new HashMap<>();
    
    public ArgumentParser(String[] args){
        for(String arg : args)
            addArgument(arg);
    }
    
    public void addArgumentAction(String argName, ArgumentAction action){
        ArgumentValue val = args.get(argName);
        
        if(val == null || action == null)
            return;
        
        action.action(val);
    }
    
    private void addArgument(String arg){
        
        String name;
        String value;
        
        //Option (true)
        if(arg.startsWith("-")){
            name = arg.replace("-", "");
            value = String.valueOf(true);
            
            if(name.equals("")){
                warnOfInvalidArg(arg);
                return;
            }
        //Option (false)
        } else if (arg.startsWith("--")){
            name = arg.replace("--", "");
            value = String.valueOf(false);
            
            if(name.equals("")){
                warnOfInvalidArg(arg);
                return;
            }
        //Value
        } else if(arg.contains("=")){
            if(arg.startsWith("=") || arg.endsWith("=")){
                warnOfInvalidArg(arg);
                return;
            }
                
            String[] makeup = arg.split("=");
            
            if(makeup.length != 2){
                warnOfInvalidArg(arg);
                return;
            }
            
            name = makeup[0];
            value = makeup[1];
        //Invalid
        } else {
            warnOfInvalidArg(arg);
            return;
        }
        
        args.put(name, new ArgumentValue(value));
    }
    
    private static void warnOfInvalidArg(String arg){
        LOG.warn("Invalid argument passed in: " + arg);
    }
    
    public static class ArgumentValue{
        
        private final String rawValue;
        
        public ArgumentValue(String raw){
            this.rawValue = raw;
        }
        
        public String getRaw(){return this.rawValue;}
        
        public int getAsInt(){return Integer.parseInt(rawValue);}
        
        public long getAsLong(){return Long.parseLong(rawValue);}
        
        public boolean getAsBoolean(){return Boolean.parseBoolean(rawValue);}
        
        public double getAsDouble(){return Double.parseDouble(rawValue);}
    }
    
    public interface ArgumentAction{
        void action(ArgumentValue val);
    }
}
