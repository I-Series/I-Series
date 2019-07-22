package org.lmelaia.iseries.fx.main;

/**
 * Implemented by classes that control
 * a part of the main window. Provides basic support
 * for initialization and the saving/loading of settings,
 * as well as the template.
 */
abstract class SubControl {

    /**
     * Reference to the main window controller instance.
     * The name omits the "controller" part of the name
     * for simplicity and better readability.
     */
    private final MainWindowController mainWindow;

    /**
     * Constructor.
     *
     * @param controller the main window controller instance.
     */
    SubControl(MainWindowController controller) {
        this.mainWindow = controller;
    }

    /**
     * @return the reference to the main window controller instance.
     * The "controller" part of the name is omitted
     * for simplicity and better readability.
     */
    public final MainWindowController getMainWindow() {
        return this.mainWindow;
    }

    /**
     * Called when the main window
     * is in its initialization phase.
     */
    abstract void init();

    /**
     * Called when the main window
     * is closing. Used to save anything
     * the user has changed.
     */
    //Meant to be overridden occasionally
    void saveState() {
    }

    /**
     * Called when the main window
     * is in its final initialization phase.
     * Used to load any saved settings.
     */
    //Meant to be overridden occasionally
    void loadState() {
    }
}
