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

import java.io.IOException;
import java.net.*;

/**
 * Provides the ability to send and receive messages
 * through datagram sockets.
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess"})
class CommunicationObject {

    /**
     * Default size of created packets.
     */
    private static final int DEFAULT_PACKET_SIZE = 1024;

    /**
     * This computers INet address.
     */
    private static InetAddress host = null;

    /**
     * The socket connection used to communicate.
     */
    private final DatagramSocket socketConnection;

    /**
     * The port number the socket was opened on.
     */
    private final int port;

    /**
     * Creates a new communication object
     * opened on a random unused port.
     *
     * @throws IOException if the object could
     * not be created.
     */
    @SuppressWarnings("WeakerAccess")
    public CommunicationObject() throws IOException {
        this(getRandomPort());
    }

    /**
     * Creates a new communication object on
     * the given port.
     *
     * @param port the port number the socket
     *             should be opened on.
     * @throws IOException
     */
    @SuppressWarnings("WeakerAccess")
    public CommunicationObject(int port) throws IOException {
        socketConnection = new DatagramSocket(port, getHost());
        this.port = port;
    }

    /**
     * Sends a message to a given server.
     *
     * @param msg the message to be sent.
     * @throws IOException
     */
    @SuppressWarnings("WeakerAccess")
    public void send(Message msg) throws IOException {
        socketConnection.send(msg.toDatagram());
    }

    /**
     * Blocks until a message is received.
     *
     * @return the message received.
     * @throws IOException
     */
    @SuppressWarnings("WeakerAccess")
    public Message waitToReceive() throws IOException {
        DatagramPacket packet = getNewPacket();
        socketConnection.receive(packet);
        return Message.FromDatagram(packet);
    }

    /**
     * Blocks until a message is received or the timeout is reached.
     *
     * @param timeout  the timeout is milliseconds.
     * @param listener the callback on timeout reached.
     * @return the message received or null if the timeout was reached.
     * @throws IOException
     */
    @SuppressWarnings("WeakerAccess")
    public Message waitToReceive(int timeout, TimeoutListener listener) throws IOException {
        socketConnection.setSoTimeout(timeout);
        Message m = null;

        try {
            m = waitToReceive();
        } catch (SocketTimeoutException e) {
            listener.onTimeout();
        }

        socketConnection.setSoTimeout(0);
        return m;
    }

    /**
     * @return the port number this object was opened on.
     */
    @SuppressWarnings("WeakerAccess")
    public int getPort() {
        return port;
    }

    /**
     * Disconnects the socket connection.
     */
    public void disconnect() {
        socketConnection.disconnect();
    }

    /**
     * Creates a new datagram packet for use in sending a message.
     *
     * @param port the port number to send the message to.
     * @return the newly created packet.
     * @throws UnknownHostException if the host cannot be found.
     *                              <b>Should never happen.</b>
     */
    public static DatagramPacket getNewPacket(int port) throws UnknownHostException {
        return new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE,
                getHost(), port);
    }

    /**
     * Creates a new datagram packet for use in receiving messages.
     *
     * @return the newly created datagram packet.
     */
    @SuppressWarnings("WeakerAccess")
    public static DatagramPacket getNewPacket() {
        return new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
    }

    /**
     * @return this computers INet address.
     * @throws UnknownHostException
     */
    @SuppressWarnings("WeakerAccess")
    public static InetAddress getHost() throws UnknownHostException {
        if (host == null)
            host = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});

        return host;
    }

    /**
     * @return a random unused port number.
     * @throws IOException
     */
    @SuppressWarnings("WeakerAccess")
    public static int getRandomPort() throws IOException {
        return new ServerSocket(0).getLocalPort();
    }

    /**
     * Callback interface for timeouts.
     */
    public interface TimeoutListener {

        /**
         * Called when a timeout is reached.
         */
        void onTimeout();
    }
}
