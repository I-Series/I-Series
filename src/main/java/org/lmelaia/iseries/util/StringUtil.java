package org.lmelaia.iseries.util;

/**
 * A static utility class for manipulating
 * Strings.
 */
public class StringUtil {

    //Private constructor.
    private StringUtil() {
    }

    /**
     * Takes a given String and returns only the
     * alphanumeric characters (including space)
     * in the given String, in the original order.
     * That is, this method will remove any non-
     * alphanumeric characters.
     *
     * @param input the input String.
     * @return the output String containing only alphanumeric
     * characters and spaces.
     */
    public static String toAlphanumeric(String input) {
        return input.replaceAll("[^A-Za-z0-9 ]", "");
    }
}
