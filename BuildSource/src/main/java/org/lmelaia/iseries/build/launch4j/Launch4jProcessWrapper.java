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
     * The launch4j configuration file name.
     */
    private static final String CONFIGURATION_FILE_NAME 
            = System.getProperty("user.home") + "/launch4j%s.xml";

    /**
     * The Launch4j executable (Launch4j/launch4j.exe).
     */
    private final File launch4jExecutable;

    /**
     * The passed in launch4j configuration.
     */
    private final Launch4jConfiguration configuration;

    /**
     * The file which will hold the launch4j configuration xml.
     */
    private final File CONFIGURATION_FILE = getSuitableConfigurationFile();
    
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
    @SuppressWarnings({"SleepWhileInLoop", "ResultOfMethodCallIgnored"})
    public int startProcess(StringBuilder output) throws IOException {
        Objects.requireNonNull(output);
        writeConfigurationFile();
        
        /*
      Used to build the launch4j process.
     */
        ProcessBuilder l4jProcessBuilder = new ProcessBuilder(
                launch4jExecutable.getAbsolutePath(),
                CONFIGURATION_FILE.getAbsolutePath());
        
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
                    /*
      Number of milliseconds to wait before poling the process
      while it is silent (i.e. has no more avaliable output).

      <p>
      The smaller the number, the quicker the current thread will
      pole the process and the quicker it will respond once its finished
      execution. Although a smaller number will use more computer resources
      than larger one.
     */
                    Thread.sleep(500);
                } catch (InterruptedException ex){
                    //NO-OP
                }
        }
        
        CONFIGURATION_FILE.delete();
        return l4jProcess.exitValue();
    }
    
    /**
     * @return an empty launch4j configuration file guaranteed not to exist.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File getSuitableConfigurationFile(){
        int fileNumber = 0;
        
        while(true){
            File cfgFile = new File(CONFIGURATION_FILE_NAME.replace(
                    "%s", String.valueOf(fileNumber)));
            
            if(!cfgFile.exists()){
                try {
                    cfgFile.createNewFile();
                } catch (IOException ex) {
                    //Should never even happen.
                    //Done so a class level field can use this method.
                    throw new RuntimeException(ex);
                }
                return cfgFile;
            }
            
            fileNumber++;
        }
    }
    
    /**
     * Writes the launch4j configuration settings to file.
     * 
     * @throws IOException if the file cannot be written to.
     */
    private void writeConfigurationFile() throws IOException {
        try (FileWriter writer = new FileWriter(CONFIGURATION_FILE)) {
            writer.write(configuration.getConfigurationSourceAsString());
        }
    }
}
