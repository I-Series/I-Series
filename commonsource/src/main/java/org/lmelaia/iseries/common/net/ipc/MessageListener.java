package org.lmelaia.iseries.common.net.ipc;

import com.google.gson.JsonObject;

/**
 * Interface used to subscribe to received messages.
 */
public interface MessageListener {

    /**
     * Called when a new message is received.
     *
     * @param message the received message as a
     *                json object.
     */
    void onMessageReceived(JsonObject message);
}
