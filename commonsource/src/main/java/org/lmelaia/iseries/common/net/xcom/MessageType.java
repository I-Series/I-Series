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

/**
 * A type of {@link Message}.
 */
public enum MessageType {

    /**
     * Used when pinging a server to see if
     * it is online.
     */
    IS_ALIVE("is_alive"),

    /**
     * Used when responding to an
     * is alive ping.
     */
    ALIVE("alive"),

    /**
     * Used when sending arguments to a server.
     */
    ARGS("args"),

    /**
     * Used when a message has been received.
     */
    RECEIVED("received");

    /**
     * The types ID.
     */
    public final String id;

    MessageType(String id) {
        this.id = id;
    }

    /**
     * @param id the messages ID.
     * @return a message type from it's ID.
     */
    public static MessageType getTypeFromID(String id) {
        for (MessageType m : MessageType.values()) {
            if (m.id.equals(id))
                return m;
        }

        return null;
    }
}
