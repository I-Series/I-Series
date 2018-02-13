package org.lmelaia.iseries.common.net.ipc;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.*;

/**
 * Provides convenience methods for writing
 * a receiving threads port to file and
 * reading the last written port.
 */
class PortFileHandler {

    /**
     * Logging instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The file the data is written to.
     */
    private static final File PORT_FILE = new File("savedata/port_1.int");

    /**
     * The private singleton instance of this class.
     */
    private static PortFileHandler INSTANCE;

    /**
     * Private constructor, ensuring the file exists.
     */
    private PortFileHandler() {
        if (!PORT_FILE.exists()) {
            LOG.warn("Port file does not exist: " + PORT_FILE);

            try {
                if (!PORT_FILE.createNewFile()) {
                    LOG.error("Failed to create port file.");
                }
            } catch (IOException e) {
                LOG.error("Failed to create port file.");
            }
        }
    }

    /**
     * @return the instance of this class.
     */
    static PortFileHandler getPortFileHandler() {
        if (INSTANCE == null) {
            INSTANCE = new PortFileHandler();
        }

        return INSTANCE;
    }

    /**
     * Writes the provide port number to file.
     *
     * @param port the port number to write.
     * @throws IOException if the file cannot be written to.
     */
    void write(int port) throws IOException {
        FileWriter writer = new FileWriter(PORT_FILE);
        writer.write(String.valueOf(port));
        writer.flush();
        writer.close();
    }

    /**
     * @return the last port number written to file.
     */
    public int read() {
        int port = -1;
        String content = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PORT_FILE));
            content = reader.readLine();
            reader.close();
            port = Integer.parseInt(content);
        } catch (FileNotFoundException e) {
            LOG.warn("Port file not found when trying to read.", e);
        } catch (IOException e) {
            LOG.error("Couldn't read port file.", e);
        } catch (NumberFormatException e) {
            LOG.warn("Port file content not in expected format (content = " + content + ")", e);
        }

        if (port == -1)
            LOG.warn("No last known port.");

        return port;
    }
}
