package org.lmelaia.iseries.launcher;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * The popup menu displayed the I-Series tray icon.
 */
public class TrayPopup extends PopupMenu {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The continue watching menu item.
     */
    private static final MenuItem CONTINUE = new MenuItem("Continue Watching");

    /**
     * The open I-Series menu item.
     */
    private static final MenuItem OPEN = new MenuItem("Open I-Series");

    /**
     * The close all menu item.
     */
    private static final MenuItem CLOSE = new MenuItem("Exit");

    /**
     * Constructs a new TrayPopup and
     * adds the needed MenuItems.
     */
    public TrayPopup() {
        super();

        addItems();
        registerListeners();
    }

    /**
     * Adds all the MenuItems to this Popup Menu.
     */
    private void addItems() {
        MenuItem ISeries = new MenuItem("I-Series");
        ISeries.setEnabled(false);
        this.add(ISeries);
        this.addSeparator();

        this.add(CONTINUE);
        this.add(OPEN);
        this.addSeparator();
        this.add(CLOSE);
    }

    /**
     * Registers the MenuItem listeners.
     */
    private void registerListeners() {
        this.addActionListener(this::onOpen);

        CLOSE.addActionListener(this::onClose);
        OPEN.addActionListener(this::onOpen);
    }

    // Action Listeners

    /**
     * Opens the I-Series application and
     * disables Tray Mode.
     *
     * @param e action event.
     */
    protected void onOpen(ActionEvent e) {
        LOG.info("Launching I-Series from Tray Mode");

        TrayMode.deactivate();

        try {
            ISeriesAppController.start(null);
        } catch (IOException ex) {
            LOG.error("Failed to reopen I-Series from Tray Mode.", ex);
            App.getInstance().exit(ExitCode.START_FAILURE);
        }
    }

    /**
     * Closes the launcher.
     *
     * @param e action event.
     */
    private void onClose(ActionEvent e) {
        App.getInstance().exit(ExitCode.NORMAL);
    }
}
