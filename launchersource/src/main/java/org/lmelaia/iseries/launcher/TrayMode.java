package org.lmelaia.iseries.launcher;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.net.ipc.Messenger;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles the Tray Mode state of the
 * launcher application.
 * <p>
 * Tray Mode is a special state of the launcher
 * in which it's not running the I-Series
 * application, but rather waiting in the background
 * for any user input (this includes any new launchers
 * run or from the System Tray), ready to start the
 * I-Series application at any moment.
 * <p>
 * This mode should allow for very low cpu/memory usage
 * while the main application is not in use, while still
 * providing some way for the user to quickly interact
 * with the application and allowing the main I-Series
 * application to start quicker.
 * <p>
 * It is worth noting: Tray Mode is lazy initialized. It
 * will only initialize/construct fields/objects/references
 * when called for the first time. However, it doesn't
 * unregister anything, so any resources taken by Tray Mode
 * will NOT BE reclaimed. Tray Mode is however capable of
 * of being activated multiple times without any
 * re-initialization.
 */
//TODO: Grey-Out any go-into-tray-mode buttons when tray mode is not available.
public class TrayMode {

    /**
     * Singleton instance.
     */
    private static final TrayMode INSTANCE = new TrayMode();

    /**
     * Activates the Tray Mode state of the application.
     * <p>
     * When active, a {@link SystemTray} icon will appear
     * with the {@link TrayPopup} as its popup menu. This will
     * then handle any user input.
     * <p>
     * Activating Tray Mode will also write this launchers
     * port receiver to file so any new launchers run
     * can connect to this launcher (which will then
     * start the main I-Series application) rather
     * than start a new I-Series instance themselves.
     * NOTE: Any new I-Series instances started will
     * overwrite this launchers port receiver, so any
     * newly started I-Series instances will take
     * over without having to undo the action.
     * <p>
     * Tray Mode is able to be activated multiple times
     * without being deactivated.
     *
     * @param messenger the {@link App#getMessenger()} instance.
     */
    public static void activate(Messenger messenger) {
        INSTANCE._activate(messenger);
    }

    /**
     * Removes the SystemTray icon. NOTE: This
     * does not start any I-Series instances
     * or register/unregister anything. That must
     * be done elsewhere.
     * <p>
     * Tray Mode is able to be activated multiple times
     * without being deactivated.
     */
    public static void deactivate() {
        INSTANCE._deactivate();
    }

    // *****
    // Class
    // *****

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * The icon displayed in the System Tray when in Tray Mode.
     */
    private final TrayIcon icon = new TrayIcon(
            Objects.requireNonNull(createImage("/images/iseries-16.png", "Tray Icon"))
    );

    /**
     * System Tray instance.
     */
    private final SystemTray tray;

    /**
     * The {@link App#getMessenger()} instance.
     */
    private Messenger messenger;

    /**
     * Constructs and initializes fields.
     */
    private TrayMode() {
        TrayPopup popupMenu;
        if (SystemTray.isSupported()) {
            popupMenu = new TrayPopup();
            tray = SystemTray.getSystemTray();
        } else {
            tray = null;
            popupMenu = null;
        }

        if (tray == null)
            return;

        icon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    popupMenu.onOpen(null);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        icon.addActionListener(popupMenu::onOpen);
        icon.setActionCommand("I-Series");
        icon.setToolTip("I-Series");
        icon.setImageAutoSize(true);
        icon.setPopupMenu(popupMenu);
    }

    /**
     * Activates the Tray Mode state of the application.
     * <p>
     * When active, a {@link SystemTray} icon will appear
     * with the {@link TrayPopup} as its popup menu. This will
     * then handle any user input.
     * <p>
     * Activating Tray Mode will also write this launchers
     * port receiver to file so any new launchers run
     * can connect to this launcher (which will then
     * start the main I-Series application) rather
     * than start a new I-Series instance themselves.
     * NOTE: Any new I-Series instances started will
     * overwrite this launchers port receiver, so any
     * newly started I-Series instances will take
     * over without having to undo the action.
     * <p>
     * Tray Mode is able to be activated multiple times
     * without being deactivated.
     *
     * @param messenger the {@link App#getMessenger()} instance.
     */
    private void _activate(Messenger messenger) {
        if (tray == null)
            return;

        if (this.messenger == null) {
            this.messenger = messenger;
            registerLauncherListener();
        }

        LOG.info("Going into Tray Mode...");

        for (TrayIcon icon : tray.getTrayIcons()) {
            if (icon == this.icon)
                return;
        }

        try {
            tray.add(icon);
            messenger.writeToPortFile();
        } catch (AWTException e) {
            LOG.error("Failed to finalize (start) tray mode", e);
            App.getInstance().exit(ExitCode.TRAY_MODE_FAILURE);
        }
    }

    /**
     * Removes the SystemTray icon. NOTE: This
     * does not start any I-Series instances
     * or register/unregister anything. That must
     * be done elsewhere.
     * <p>
     * Tray Mode is able to be activated multiple times
     * without being deactivated.
     */
    private void _deactivate() {
        if (tray == null)
            return;

        LOG.info("Exiting Tray Mode...");
        tray.remove(icon);
    }

    /**
     * Registers a listener that will wait for
     * any new launchers run and then run the
     * I-Series application from this launcher
     * process instead of the newly run launcher.
     */
    private void registerLauncherListener() {
        messenger.addMessageListener(message -> {
            if (message.get("name").getAsString().equals("argument_change")) {
                ArrayList<String> arguments = new ArrayList<>();
                message.get("arguments").getAsJsonArray().forEach(
                        element -> arguments.add(element.getAsString()));

                LOG.info("Launching I-Series from Tray Mode (Launcher call)");
                deactivate();
                try {
                    ISeriesAppController.start(arguments.toArray(new String[0]));
                } catch (IOException ex) {
                    LOG.error("Failed to reopen I-Series from Tray Mode.", ex);
                    App.getInstance().exit(ExitCode.START_FAILURE);
                }
            }
        });
    }

    /**
     * Gets an image from the class path or
     * closes the application if it can't be found.
     *
     * @param path        the path.
     * @param description the image description.
     * @return the image from the given class path.
     */
    private static Image createImage(String path, String description) {
        URL imageURL = TrayMode.class.getResource(path);

        if (imageURL == null) {
            LOG.error("TrayMode image not found: " + path);
            App.getInstance().exit(ExitCode.NO_TRAY_ICON);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
