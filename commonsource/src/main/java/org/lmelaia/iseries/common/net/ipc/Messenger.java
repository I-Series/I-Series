package org.lmelaia.iseries.common.net.ipc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a framework for the launcher and main application
 * to communicate over sockets in json.<p/>
 * <p>
 * Provides methods to send messages and subscribe to message
 * received events, ping an application and read/write
 * the receiving sockets port to file.<p/>
 * <p>
 * Each json message should have a property with the key "name"
 * (eg. {@code {"name": "message_name"} }), or an exception
 * will be thrown. This is to identify what information the
 * message holds and what to do with it.
 *
 * @author Luke Melaia
 */
public class Messenger {

    /**
     * Gson object used to format JsonObjects to/from json.
     */
    private static final Gson GSON = new Gson();

    /**
     * Logging instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Default size for created packets.
     */
    private static final int DEFAULT_PACKET_SIZE = 2048;

    /**
     * Host machines address.
     */
    private static final InetAddress HOST;

    /**
     * List of subscribed message listeners.
     */
    private final List<MessageListener> listeners = new ArrayList<>();

    /**
     * Socket used for sending message.
     */
    private final DatagramSocket sender;

    /**
     * Socket receiving message on loop.
     */
    private final DatagramSocket receiver;

    /*
     * Initialises the INetAddress object.
     */
    static {
        try {
            HOST = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
        } catch (UnknownHostException e) {
            LOG.fatal("Could not set host", e);
            throw new MessengerException("Could not set host", e);
        }
    }

    /**
     * Creates a new messenger object bound to
     * any two available ports on the local machine.
     *
     * @param timeout the timeout for receiving replies and pings.
     * @throws MessengerException if the sockets cannot be created.
     */
    public Messenger(int timeout) throws MessengerException {
        try {
            this.sender = new DatagramSocket(getRandomPort());
            this.sender.setSoTimeout(timeout);
            this.receiver = new DatagramSocket(getRandomPort());
            ReceivingThread receivingThread = new ReceivingThread();
            receivingThread.start();
        } catch (IOException e) {
            LOG.fatal("Failed to create new messenger instance", e);
            throw new MessengerException("Failed to create new messenger instance", e);
        }
    }

    /**
     * Sends a json message to the provided port and returns it's response.
     * <p/>
     * <p>
     * This method blocks until the message is received or the timeout
     * specified in the constructor has passed.
     * <p/>
     * <p>
     * The json message must a property key with the value of "name"
     * (eg. {@code {"name": "value"}}) or an IllegalArgumentException will
     * be thrown.
     *
     * @param message the message to send.
     * @param port    the port the receiving thread is operating on.
     * @return the response from the receiving thread, or
     * {@code null} if the timeout has passed.
     * @throws MessengerException if the message cannot be sent.
     */
    @SuppressWarnings({"WeakerAccess"})
    public JsonObject send(JsonObject message, int port) throws MessengerException {
        if (message.get("name") == null)
            throw new IllegalArgumentException("Json object provided must have" +
                    " a property with a key value of \"name\"");

        try {
            DatagramPacket messageData = toPacket(message, port);
            this.sender.send(messageData);

            if (!message.get("name").getAsString().equals("ping"))
                LOG.debug("Message: " + GSON.toJson(message) + " sent to port: " + port);

            DatagramPacket reply = getEmptyPacket();
            this.sender.receive(reply);
            return fromPacket(reply);
        } catch (SocketTimeoutException e) {
            return null;
        } catch (IOException e) {
            LOG.fatal("Failed to send message: " + GSON.toJson(message), e);
            throw new MessengerException("Failed to send message", e);
        }
    }

    /**
     * Writes the receiving threads port to file.
     *
     * @see #readFromPortFile()
     */
    @SuppressWarnings("WeakerAccess")
    public void writeToPortFile() {
        try {
            PortFileHandler.getPortFileHandler().write(getReceiverPort());
        } catch (IOException e) {
            LOG.fatal("Failed to write receiver port to file", e);
            throw new MessengerException("Failed to write receiver port to file", e);
        }
    }

    /**
     * Reads and returns the last port written to file.
     *
     * @return the last written port.
     * @see #writeToPortFile()
     */
    @SuppressWarnings("WeakerAccess")
    public int readFromPortFile() {
        return PortFileHandler.getPortFileHandler().read();
    }

    /**
     * Adds a listener to the subscribed listeners list, which will
     * be notified when a new message is received.
     *
     * @param listener the listener to add.
     */
    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the subscribed list.
     *
     * @param listener the listener to remove.
     */
    public void removeMessageListener(MessageListener listener) {
        listeners.remove(listener);
    }

    /**
     * Pings the port of a receiving thread to check
     * if it is online and/or operating.
     *
     * @param portToPing the port to ping.
     * @return {@code true} if the receiving thread is
     * online, {@code false} otherwise.
     * @throws MessengerException if a message cannot be sent to ping
     *                            the receiving thread.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean ping(int portToPing) throws MessengerException {
        JsonObject message = new JsonObject();
        message.add("name", new JsonPrimitive("ping"));

        JsonObject returnMessage;
        returnMessage = send(message, portToPing);

        return returnMessage != null && returnMessage.get("name")
                .getAsString().equals("return_ping");

    }

    /**
     * @return the port the sending socket is operating on.
     */
    public int getSenderPort() {
        return this.sender.getLocalPort();
    }

    /**
     * @return the port the receiving socket is operating on.
     */
    @SuppressWarnings("WeakerAccess")
    public int getReceiverPort() {
        return this.receiver.getLocalPort();
    }

    /**
     * @return {@code true} if both sockets are open, {@code false} otherwise.
     */
    public boolean isAlive() {
        return (!this.sender.isClosed())
                && (!this.receiver.isClosed());
    }

    /**
     * Sends a message on the receiving thread. This is used to
     * reply to received messages.
     *
     * @param message the reply message.
     * @param port    the port to the send the reply to.
     * @throws IOException if the message cannot be sent.
     */
    private void reply(JsonObject message, int port) throws IOException {
        this.receiver.send(toPacket(message, port));
    }

    /**
     * Notifies all subscribed listeners that a message has been received.
     *
     * @param message the received message.
     */
    private void notifyListeners(JsonObject message) {
        for (MessageListener listener : listeners)
            listener.onMessageReceived(message);
    }

    /**
     * @return an empty datagram packet with the default packet size.
     */
    private static DatagramPacket getEmptyPacket() {
        return new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
    }

    /**
     * @param port the destination port.
     * @return an empty datagram packet with the default packet
     * size and a destination port.
     */
    private static DatagramPacket getEmptyPacket(int port) {
        return new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE,
                HOST, port);
    }

    /**
     * Constructs a new json object from the provided datagram packet.
     *
     * @param packet the packet used in constructing the json object.
     * @return the constructed json object.
     */
    private static JsonObject fromPacket(DatagramPacket packet) {
        return GSON.fromJson(new String(packet.getData(), 0, packet.getLength()), JsonObject.class);
    }

    /**
     * Constructs a new datagram packet from the provided json object
     * and destination port.
     *
     * @param message the json object used in constructing the packet.
     * @param port    the destination port.
     * @return the constructed datagram packet.
     */
    private static DatagramPacket toPacket(JsonObject message, int port) {
        DatagramPacket packet = getEmptyPacket(port);
        String messageData = GSON.toJson(message);

        packet.setLength(messageData.length());
        packet.setData(messageData.getBytes());

        return packet;
    }

    /**
     * @return a random available port on the local machine.
     * @throws IOException if a port cannot be obtained.
     */
    private static int getRandomPort() throws IOException {
        return new ServerSocket(0).getLocalPort();
    }

    /**
     * Thread classes used in receiving messages on a loop.
     */
    private class ReceivingThread extends Thread implements Runnable {

        /**
         * Default constructor.
         */
        ReceivingThread() {
            this.setName("Message receiver thread");
            this.setDaemon(true);
        }

        /**
         * Blocks until a message is received. If the received
         * message is a ping request, a reply will be sent notifying
         * the sender it is online, otherwise all subscribed listeners
         * will be notified of a new message received and a received
         * reply will be sent. This is performed on a loop.
         */
        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            LOG.info("Response thread started on port: " + getReceiverPort());
            while (true) {
                try {
                    DatagramPacket packet = getEmptyPacket();
                    receiver.receive(packet);

                    JsonObject message;
                    try {
                        message = fromPacket(packet);
                    } catch (JsonSyntaxException e) {
                        LOG.warn("Invalid json message received: " + new String(packet.getData()), e);
                        continue;
                    }

                    if (message.get("name").getAsString().equals("ping")) {
                        JsonObject reply = new JsonObject();
                        reply.add("name", new JsonPrimitive("return_ping"));
                        reply(reply, packet.getPort());
                    } else {
                        LOG.info("Message received: " + GSON.toJson(message));
                        notifyListeners(message);
                        JsonObject reply = new JsonObject();
                        reply.add("name", new JsonPrimitive("received"));
                        reply(reply, packet.getPort());
                    }
                } catch (Exception e) {
                    LOG.fatal("Failed to reply to message", e);
                }
            }
        }
    }
}
