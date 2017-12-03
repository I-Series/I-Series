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

package org.lmelaia.iseries.common.intercommunication;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.IOException;

/**
 * Provides the ability to send messages to and
 * receive a response from a server.
 */
public class Client {

    /**
     * Logging instance.
     */
    private static final Logger LOG = AppLogger.getLogger();
    /**
     * Communication object used to talk to
     * the server.
     */
    private final CommunicationObject comObj;
    /**
     * The timeout in milliseconds when pinging
     * a server.
     */
    private int pingTimeout = 2000;

    /**
     * Constructs a new client instance
     *
     * @throws IOException if a communication
     *                     object cannot be created.
     */
    public Client() throws IOException {
        comObj = new CommunicationObject();
    }

    /**
     * Constructs a new client instance
     *
     * @throws IOException if a communication
     *                     object cannot be created.
     */
    public Client(int pingTimeout) throws IOException {
        this.pingTimeout = pingTimeout;
        comObj = new CommunicationObject();
    }

    /**
     * Pings a server to see if it is online.
     *
     * @param serverPort port number the server was opened on.
     * @return {@code true} if the server is online, false otherwise.
     * @throws IOException
     */
    public boolean pingServer(int serverPort) throws IOException {
        comObj.send(new Message(MessageType.IS_ALIVE, serverPort, ""));

        final boolean[] timeout = new boolean[1];
        Message received = comObj.waitToReceive(pingTimeout, () -> {
            LOG.info("No response in " + pingTimeout + " ms after pinging server(" + serverPort + ").");
            timeout[0] = true;
        });

        if (timeout[0])
            return false;

        if (received.getMsgType().equals(MessageType.ALIVE)) {
            return true;
        } else {
            LOG.warn("Unknown response received.");
            return false;
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param to the message.
     * @return the response from the server.
     * @throws IOException
     */
    public Message sendToServer(Message to) throws IOException {
        comObj.send(to);
        Message rec = comObj.waitToReceive();
        return rec;
    }

    /**
     * Closes this client instance and communication
     * object in use.
     */
    public void close() {
        comObj.disconnect();
    }

}
