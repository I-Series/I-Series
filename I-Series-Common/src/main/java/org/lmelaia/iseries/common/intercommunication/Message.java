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

import java.net.DatagramPacket;
import java.net.UnknownHostException;

/**
 * A message which can be sent or received
 * from or to a client or server.
 */
public class Message {

    /**
     * The type of message.
     */
    private final MessageType msgType;

    /**
     * The port the message came from or
     * is being sent to.
     */
    private final int port;

    /**
     * The arguments provided with
     * the message.
     */
    private final String args;

    /**
     * Constructs a new message.
     *
     * @param msgType the type of message.
     * @param port    the port to send the message to.
     * @param args    the messages arguments.
     */
    public Message(MessageType msgType, int port, String args) {
        this.msgType = msgType;
        this.port = port;
        this.args = args.replace("\0", "");
    }

    /**
     * Turns this message into a datagram packet.
     *
     * @return the datagram packet created from this message.
     * @throws UnknownHostException
     */
    public DatagramPacket toDatagram() throws UnknownHostException {
        DatagramPacket p = CommunicationObject.getNewPacket(getPort());

        String data = getMsgType().id + "+" + getArgs();

        p.setData(data.getBytes());
        p.setLength(data.length());

        return p;
    }

    /**
     * Turns a datagram packet into a message.
     *
     * @param packet the packet to turn into a message.
     * @return the message created from the datagram packet.
     */
    public static Message FromDatagram(DatagramPacket packet) {
        String dataAsString = new String(packet.getData());
        String msgType = dataAsString.split("\\+")[0];
        String args = dataAsString.split("\\+")[1];
        return new Message(MessageType.getTypeFromID(msgType), packet.getPort(), args);
    }

    /**
     * @return the type of message.
     */
    public MessageType getMsgType() {
        return msgType;
    }

    /**
     * @return the port number this message is going to
     * or coming from.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the arguments of the message.
     */
    public String getArgs() {
        return args;
    }

    /**
     * @return a string representation of this message.
     */
    public String toString() {
        return this.getClass().getCanonicalName()
                + "[type=" + getMsgType().id
                + ", args=" + args
                + ", port=" + getPort() + "]";
    }
}
