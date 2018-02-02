package org.lmelaia.iseries.common.system;

import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Provides an easy way to look up arguments,
 * update them and subscribe to changes.
 * <p>
 * This sorts arguments given from
 * the {@code main(String[] args)} method
 * into a list of named and unnamed
 * arguments.
 * <p>
 * The unnamed arguments are a list
 * of arguments without a key-value
 * format, which are normally file
 * names.
 * <p>
 * The named arguments are a a list
 * of arguments with a name(key)
 * and value (in the format --key=value).
 * These are normally used as settings.
 *
 * @see ArgumentChangeListener
 */
public class ArgumentHandler {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * Registered event change listeners.
     */
    private final List<ArgumentChangeListener> listeners = new ArrayList<>();

    /**
     * List of arguments with a name and value. Normally a
     * setting.
     */
    private final Map<String, String> namedArguments = new HashMap<>();

    /**
     * List of arguments with no name. Normally a file name
     * or file names.
     */
    private String[] unnamedArguments;

    /**
     * Updates this object with the arguments given
     * from the command line.
     * <p>
     * This will overwrite the unnamed arguments
     * list and update the named arguments list,
     * as well as notifying all registered change
     * listeners.
     *
     * @param args the arguments given to the
     *             application through the command
     *             line.
     */
    void update(String[] args) {
        Objects.requireNonNull(args, "Update arguments cannot be null");

        String oldUnnamed = (unnamedArguments == null) ? null : Arrays.toString(unnamedArguments);
        String oldNamed = namedArguments.toString();

        ArrayList<String> unnamedArgs = new ArrayList<>();

        for (String arg : args) {
            if (!arg.startsWith("--")) {
                unnamedArgs.add(arg);
                continue;
            }

            String[] split = arg.split("=");

            if (split.length == 2) {
                String name = split[0].replace("--", "");
                String value = split[1];

                namedArguments.put(name, value);
            }
        }

        unnamedArguments = unnamedArgs.toArray(new String[unnamedArgs.size()]);
        notifyListeners();
        LOG.info("Arguments changed from: " + oldUnnamed + " " + oldNamed
                + " to: " + Arrays.toString(unnamedArguments) + " " + namedArguments.toString());
    }

    /**
     * Adds a change listener which will
     * fire when the arguments are updated.
     *
     * @param listener the change listener.
     */
    public void addChangeListener(ArgumentChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a change listener from the list
     * of registered listeners.
     *
     * @param listener the change listener.
     */
    public void removeChangeListener(ArgumentChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return the list of arguments not in a key-value
     * format. These are usually file names.
     */
    public String[] getUnnamedArguments() {
        return this.unnamedArguments;
    }

    /**
     * Returns the value of a named argument
     * if provided.
     *
     * @param name the name of the argument.
     * @return the value of the matching argument
     * if present.
     */
    public String getNamedArgument(String name) {
        return namedArguments.get(name);
    }

    /**
     * @return a string array containing the
     * arguments contained in this object. These
     * are in the form they would be if given through
     * the {@code main(String[] args)} method.
     */
    public String[] reconstruct() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(unnamedArguments));

        for (Map.Entry<String, String> arg : namedArguments.entrySet()) {
            list.add("--" + arg.getKey() + "=" + arg.getValue());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Notifies all registered listeners of a change.
     */
    private void notifyListeners() {
        for (ArgumentChangeListener listener : listeners) {
            listener.onArgumentsUpdated();
        }
    }
}
