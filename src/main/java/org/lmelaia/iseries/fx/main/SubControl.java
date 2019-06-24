package org.lmelaia.iseries.fx.main;

/**
 * Implemented by class that control
 * a part of the main window.
 */
//Consider abstract class.
interface SubControl {

    /**
     * Called when the main window
     * is in its initialization phase.
     */
    void init();

    /**
     * Called when the main window
     * is closing. Used to save anything
     * the user has changed.
     */
    void saveState();

    /**
     * Called when the main window
     * is in its final initialization phase.
     * Used to save anything the user has changed.
     */
    void loadState();
}
