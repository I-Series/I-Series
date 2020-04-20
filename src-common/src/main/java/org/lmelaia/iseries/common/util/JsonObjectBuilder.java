package org.lmelaia.iseries.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Utility class for creating json objects
 * in a builder pattern style.
 */
public class JsonObjectBuilder {

    /**
     * The json object this class will build.
     */
    private final JsonObject backingObject = new JsonObject();

    /**
     * Adds a json primitive with the property specified.
     *
     * @param property the property key
     * @param value    the value as a json primitive.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, JsonPrimitive value) {
        backingObject.add(property, value);
        return this;
    }

    /**
     * Adds an array of json primitives with the property specified.
     *
     * @param property the property key
     * @param value    the value as a json primitive array.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, JsonPrimitive[] value) {
        JsonArray array = new JsonArray();

        for (JsonPrimitive p : value)
            array.add(p);

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds a string with the property specified.
     *
     * @param property the property key
     * @param value    the value as a string.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, String value) {
        backingObject.add(property, new JsonPrimitive(value));
        return this;
    }

    /**
     * Adds an integer with the property specified.
     *
     * @param property the property key
     * @param value    the value as an integer.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, int value) {
        backingObject.add(property, new JsonPrimitive(value));
        return this;
    }

    /**
     * Adds a boolean with the property specified.
     *
     * @param property the property key
     * @param value    the value as a boolean.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, boolean value) {
        backingObject.add(property, new JsonPrimitive(value));
        return this;
    }

    /**
     * Adds a long with the property specified.
     *
     * @param property the property key
     * @param value    the value as a long.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, long value) {
        backingObject.add(property, new JsonPrimitive(value));
        return this;
    }

    /**
     * Adds a double with the property specified.
     *
     * @param property the property key
     * @param value    the value as a double.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, double value) {
        backingObject.add(property, new JsonPrimitive(value));
        return this;
    }

    /**
     * Adds a string array with the property specified.
     *
     * @param property the property key
     * @param value    the value as a string array.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, String[] value) {
        JsonArray array = new JsonArray();

        for (String v : value)
            array.add(new JsonPrimitive(v));

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds an integer array with the property specified.
     *
     * @param property the property key
     * @param value    the value as an integer array.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, int[] value) {
        JsonArray array = new JsonArray();

        for (int v : value)
            array.add(new JsonPrimitive(v));

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds a boolean array with the property specified.
     *
     * @param property the property key
     * @param value    the value as a boolean array.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, boolean[] value) {
        JsonArray array = new JsonArray();

        for (boolean v : value)
            array.add(new JsonPrimitive(v));

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds a long array with the property specified.
     *
     * @param property the property key
     * @param value    the value as a string long.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, long[] value) {
        JsonArray array = new JsonArray();

        for (long v : value)
            array.add(new JsonPrimitive(v));

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds a double array with the property specified.
     *
     * @param property the property key
     * @param value    the value as a double array.
     * @return {@code this}
     */
    public JsonObjectBuilder add(String property, double[] value) {
        JsonArray array = new JsonArray();

        for (double v : value)
            array.add(new JsonPrimitive(v));

        backingObject.add(property, array);

        return this;
    }

    /**
     * Adds a property with the key "name" and the specified
     * string value. This is used when the json object
     * is going to be sent using the
     * {@link org.lmelaia.iseries.common.net.ipc.Messenger}
     * api.
     *
     * @param value the value of the property.
     * @return {@code this}
     */
    public JsonObjectBuilder addName(String value) {
        add("name", value);
        return this;
    }

    /**
     * @return the JsonObject constructed using this class.
     */
    public JsonObject get() {
        return backingObject;
    }
}
