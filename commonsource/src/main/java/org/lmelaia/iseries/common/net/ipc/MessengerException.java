package org.lmelaia.iseries.common.net.ipc;

/**
 * Thrown when a messenger cannot be created or send/receive
 * a message.
 */
@SuppressWarnings("WeakerAccess")
public class MessengerException extends RuntimeException {

    /**
     * Constructs a new messenger exception with a name and cause.
     *
     * @param message the detail message.
     * @param cause   the cause for this exception being thrown.
     */
    public MessengerException(String message, Throwable cause) {
        super(message, cause);
    }
}
