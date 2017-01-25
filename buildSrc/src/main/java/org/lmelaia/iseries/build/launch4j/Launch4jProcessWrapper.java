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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Wraps an instance of a Launch4j process.
 * 
 * <p>
 * This class makes it possible to wrap a Launch4j process and redirect its
 * output to a {@link StringBuilder}. 
 * </p>
 * 
 * <p>
 * <b>
 *  This class blocks the current thread until the process has completed. This
 *  class is unlikely to be thread-safe.
 * </b>
 * </p>
 * 
 * @author Luke Melaia
 */
public final class Launch4jProcessWrapper {

    /**
     * Number of milliseconds to wait before poling the process
     * while it is silent (i.e. has no more avaliable output).
     * 
     * <p>
     * The smaller the number, the quicker the current thread will
     * pole the process and the quicker it will respond once its finished
     * execution. Although a smaller number will use more computer resources
     * than larger one.
     */
    private static int waitTime = 500;
    
    /**
     * The Launch4j executable (Launch4j/launch4j.exe).
     */
    private final File launch4jExecutable;

    /**
     * The passed in launch4j configuration.
     */
    private final Launch4jConfiguration configuration;
    
    /**
     * The file which holds the launch4j configuration xml.
     */
    private File configurationFile = new File("launch4j.xml");

    /**
     * Used to build the launch4j process.
     */
    private ProcessBuilder l4jProcessBuilder;
    
    /**
     * Creates a new launch4j process wrapper.
     * 
     * @param launch4jPath the path to the launch4j folder. <b>
     * The folder containing the executable, not the executable itself.</b>
     * @param configuration the launch4j configuration settings.
     */
    public Launch4jProcessWrapper(File launch4jPath,
            Launch4jConfiguration configuration) {
        Objects.requireNonNull(launch4jPath);

        if (!launch4jPath.exists()) {
            throw new IllegalArgumentException(
                            new FileNotFoundException(launch4jPath.toString()));
        }

        launch4jExecutable = new File(launch4jPath.getAbsolutePath()
                + "/launch4j.exe");
        
        if(!launch4jExecutable.exists()){
            throw new IllegalArgumentException(
                            new FileNotFoundException(launch4jPath.toString()));
        }

        this.configuration = Objects.requireNonNull(configuration);
    }

    /**
     * Starts the process and pauses the current thread
     * until its finished execution.
     * 
     * @param output the StringBuilder the output from the process
     * will be appended to.
     * @return the exit status from the process
     * @throws IOException if an error occurs while managing a file or handling
     * the process.
     */
    @SuppressWarnings("SleepWhileInLoop")
    public int startProcess(StringBuilder output) throws IOException {
        Objects.requireNonNull(output);
        writeConfigurationFile();
        
        l4jProcessBuilder = new ProcessBuilder(launch4jExecutable.getAbsolutePath(),
                configurationFile.getAbsolutePath());
        
        Process l4jProcess = l4jProcessBuilder.start();
        
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(l4jProcess.getInputStream()));
        
        String line;
        while(l4jProcess.isAlive()){
            if((line = reader.readLine()) != null)
                output.append(line);
            else
                //Won't wait if there is output.
                try{
                    //Just so we don't waist resources if the process is being
                    //quiet
                    Thread.sleep(waitTime);
                } catch (InterruptedException ex){
                    //NO-OP
                }
        }
        
        configurationFile.delete();
        return l4jProcess.exitValue();
    }
    
    /**
     * Writes the launch4j configuration settings to file.
     * 
     * @throws IOException if the file cannot be written to.
     */
    private void writeConfigurationFile() throws IOException{
        try(FileWriter writer = new FileWriter(configurationFile)) {
            writer.write(configuration.getConfigurationSourceAsString());
        }
    }
    
    /**
     * Sets the number of milliseconds to wait before poling the process
     * while it's silent.
     * 
     * @param waitTime between 1 millisecond ({@code 1}) and 10 seconds
     * ({@code 10*1000}). Default is {@code 500} (half a second)
     */
    public static void setWaitTime(int waitTime){
        if(waitTime < 1){
            waitTime = 1;
        } else if(waitTime > 10*1000){//10 seconds
            waitTime = 10*1000;
        }
        
        Launch4jProcessWrapper.waitTime = waitTime;
    }
}
