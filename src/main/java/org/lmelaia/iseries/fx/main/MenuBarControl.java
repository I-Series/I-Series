package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.about.AboutWindow;
import org.lmelaia.iseries.fx.exit.ExitWindow;
import org.lmelaia.iseries.fx.settings.SettingsWindow;

import java.awt.*;

/**
 * Sub-controller class that handles the menu bar on
 * the main window.
 */
class MenuBarControl implements SubControl {

    /**
     * The main windows controller class instance.
     */
    private final MainWindowController window;

    /**
     * File->Quit menu item.
     */
    private final MenuItem quit;

    /**
     * File->Settings menu item.
     */
    private final MenuItem settings;

    /**
     * File->Restart menu item.
     */
    private final MenuItem restart;

    /**
     * Help->About menu ite,
     */
    private final MenuItem about;

    /**
     * File->Move to tray.
     */
    private final MenuItem tray;

    /**
     * File->Minimize.
     */
    private final MenuItem minimize;

    /**
     * File->Show Quit Dialog.
     */
    private final MenuItem showQuitDialog;

    /**
     * Constructor.
     *
     * @param window     The main windows controller class instance.
     * @param components array of components this class handles.
     */
    public MenuBarControl(MainWindowController window, Object[] components) {
        this.window = window;
        this.quit = (MenuItem) components[0];
        this.settings = (MenuItem) components[1];
        this.restart = (MenuItem) components[2];
        this.about = (MenuItem) components[3];
        this.tray = (MenuItem) components[4];
        this.minimize = (MenuItem) components[5];
        this.showQuitDialog = (MenuItem) components[6];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        quit.setOnAction(this::onQuit);
        settings.setOnAction(this::onSettings);
        restart.setOnAction(this::onRestart);
        about.setOnAction(this::onAbout);
        tray.setOnAction(this::onTray);
        minimize.setOnAction(this::onMinimize);
        showQuitDialog.setOnAction(this::onShowQuitDialog);

        if (!SystemTray.isSupported())
            tray.setDisable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveState() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState() {

    }

    /**
     * Called when the quit menu item is pressed.
     *
     * @param e action event.
     */
    private void onQuit(ActionEvent e) {
        App.getInstance().exit(ExitCode.NORMAL);
    }

    /**
     * Called when the settings menu item is pressed.
     *
     * @param e action event.
     */
    private void onSettings(ActionEvent e) {
        App.getInstance().getWindowsManager().showWindowAndWait(SettingsWindow.class, window.getWindow());
    }

    /**
     * Called when the restart menu item is pressed.
     *
     * @param e action event.
     */
    private void onRestart(ActionEvent e) {
        App.getInstance().exit(ExitCode.RESTART);
    }

    /**
     * Called when the about menu item is pressed.
     *
     * @param e action event.
     */
    private void onAbout(ActionEvent e) {
        App.getInstance().getWindowsManager().showWindowAndWait(AboutWindow.class);
    }

    /**
     * Called when the move to tray menu item is pressed.
     *
     * @param e action event.
     */
    private void onTray(ActionEvent e) {
        App.getInstance().exit(ExitCode.TRAY);
    }

    /**
     * Called when the minimize menu item is pressed.
     *
     * @param e action event.
     */
    private void onMinimize(ActionEvent e) {
        window.getWindow().setIconified(true);
    }

    /**
     * Called when the show quit dialog menu item is pressed.
     *
     * @param e action event.
     */
    private void onShowQuitDialog(ActionEvent e) {
        ExitWindow.Result result = ExitWindow.present();
        if (result.remember())
            Settings.WINDOW_CLOSE_PREFERENCE.changeValue(result.getOption().value);

        if (result.getOption() == ExitWindow.ResultOption.QUIT)
            App.getInstance().exit(ExitCode.NORMAL);

        if (result.getOption() == ExitWindow.ResultOption.TRAY)
            App.getInstance().exit(ExitCode.TRAY);
    }
}