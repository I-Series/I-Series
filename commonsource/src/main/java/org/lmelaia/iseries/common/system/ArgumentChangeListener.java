package org.lmelaia.iseries.common.system;

/**
 * Used to subscribe to argument change
 * events.
 *
 * @see ArgumentHandler
 */
@SuppressWarnings("WeakerAccess")
public interface ArgumentChangeListener {

    /**
     * Called when the arguments object
     * this callback is registered to
     * get's an argument update.
     */
    void onArgumentsUpdated();
}
