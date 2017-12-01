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
 * Provides the ability to receive messages from connecting clients
 * and send a response back.
 */
public class Server {

    /**
     * The logging framework.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Provides the communication abilities.
     */
    private final CommunicationObject comObj;
    /**
     * Waits from messages from clients in a infinite loop.
     */
    private final Thread responseThread = new ResponseThread();
    /**
     * Responds to a message received from a client.
     */
    private MessageReceivedListener messageReceivedListener;

    /**
     * Default constructor.
     *
     * @throws IOException if communication could not established.
     */
    public Server() throws IOException {
        comObj = new CommunicationObject();
    }

    /**
     * Sets the {@link #messageReceivedListener}.
     *
     * @param listener
     */
    public void setOnMessageReceived(MessageReceivedListener listener) {
        this.messageReceivedListener = listener;
    }

    /**
     * Starts the receiving thread.
     *
     * @param name name of the response thread.
     */
    public void start(String name) {
        responseThread.setName(name + " server response thread");
        responseThread.setDaemon(true);
        responseThread.start();
    }

    /**
     * Writes the threads communication objects port number
     * to file for client to read.
     */
    public void writePortNumber() {
        FileComUtil.writePortNumber(comObj.getPort());
    }

    /**
     * @return true if the read is still active.
     */
    public boolean isRunning() {
        return responseThread.isAlive();
    }

    /**
     * @return the port number of the communication object
     * in use by the thread.
     */
    public int getPort() {
        responseThread.interrupt();
        return comObj.getPort();
    }

    /**
     * Provides the callback to response
     * to messages from clients.
     */
    public interface MessageReceivedListener {
        Message onMessageReceived(Message m);
    }

    /**
     * The thread responsible for receiving and handling messages
     * from clients.
     */
    private class ResponseThread extends Thread implements Runnable {

        /**
         * Default constructor.
         */
        public ResponseThread() {
            this.setDaemon(true);
        }

        /**
         * Receives messages in an infinite loop.
         */
        @Override
        public void run() {
            LOG.info("Response thread started on port: " + comObj.getPort());
            while (true) {
                try {
                    Message m = comObj.waitToReceive();

                    if (m.getMsgType().equals(MessageType.IS_ALIVE)) {
                        comObj.send(new Message(MessageType.ALIVE, m.getPort(), ""));
                    } else {
                        comObj.send(messageReceivedListener.onMessageReceived(m));
                    }
                } catch (Exception e) {
                    LOG.info("Error in response thread", e);
                }
            }
        }
    }
}
