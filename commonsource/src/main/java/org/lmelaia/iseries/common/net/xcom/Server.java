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

package org.lmelaia.iseries.common.net.xcom;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * Provides the xcom abilities.
     */
    private final CommunicationObject comObj;

    /**
     * Waits from messages from clients in a infinite loop.
     */
    private final Thread responseThread = new ResponseThread();

    /**
     * The list of registered message listeners.
     */
    private final List<MessageListener> listeners = new ArrayList<>();

    /**
     * Default constructor.
     *
     * @throws IOException if xcom could not established.
     */
    public Server() throws IOException {
        comObj = new CommunicationObject();
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
     * Writes the threads xcom objects port number
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
     * Registers a message listener.
     *
     * @param listener the listener to register.
     */
    public void addMessageListener(MessageListener listener) {
        this.listeners.add(listener);
    }

    /**
     * @return the port number of the xcom object
     * in use by the thread.
     */
    public int getPort() {
        responseThread.interrupt();
        return comObj.getPort();
    }

    /**
     * Notifies all registered listeners of a new message.
     *
     * @param m the message.
     */
    private void notifyListeners(Message m) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(m);
        }
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
                        notifyListeners(m);
                        comObj.send(new Message(MessageType.RECEIVED, m.getPort(), ""));
                    }
                } catch (Exception e) {
                    LOG.info("Error in response thread", e);
                }
            }
        }
    }
}
