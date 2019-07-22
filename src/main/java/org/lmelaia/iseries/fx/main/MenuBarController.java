package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.about.AboutWindow;
import org.lmelaia.iseries.fx.exit.ExitWindow;
import org.lmelaia.iseries.fx.library.LibraryWindow;
import org.lmelaia.iseries.fx.settings.SettingsWindow;

import java.awt.*;

/**
 * Sub-controller class that handles the menu bar on
 * the main window.
 */
public class MenuBarController extends SubControl {

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
     * Library->Change library location.
     */
    private final MenuItem changeLibrary;

    /**
     * Entry->Add New Entry.
     */
    private final MenuItem addEntry;

    /**
     * Entry->Edit.
     */
    private final MenuItem editEntry;

    /**
     * Entry->Delete.
     */
    private final MenuItem deleteEntry;

    /**
     * Entry->Unindex.
     */
    private final MenuItem unindexEntry;

    /**
     * Constructor.
     *
     * @param window     The main windows controller class instance.
     * @param components array of components this class handles.
     */
    MenuBarController(MainWindowController window, Object[] components) {
        super(window);
        this.quit = (MenuItem) components[0];
        this.settings = (MenuItem) components[1];
        this.restart = (MenuItem) components[2];
        this.about = (MenuItem) components[3];
        this.tray = (MenuItem) components[4];
        this.minimize = (MenuItem) components[5];
        this.showQuitDialog = (MenuItem) components[6];
        this.changeLibrary = (MenuItem) components[7];
        this.addEntry = (MenuItem) components[8];
        this.editEntry = (MenuItem) components[9];
        this.deleteEntry = (MenuItem) components[10];
        this.unindexEntry = (MenuItem) components[11];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void init() {
        quit.setOnAction(this::onQuit);
        settings.setOnAction(this::onSettings);
        restart.setOnAction(this::onRestart);
        about.setOnAction(this::onAbout);
        tray.setOnAction(this::onTray);
        minimize.setOnAction(this::onMinimize);
        showQuitDialog.setOnAction(this::onShowQuitDialog);
        changeLibrary.setOnAction(this::onChangeLibrary);
        addEntry.setOnAction(this::onAddEntry);
        editEntry.setOnAction(this::onEditEntry);
        deleteEntry.setOnAction(this::onDeleteEntry);
        unindexEntry.setOnAction(this::onUnindexEntry);

        if (!SystemTray.isSupported())
            tray.setDisable(true);

        this.editEntry.setDisable(true);
        this.deleteEntry.setDisable(true);
        this.unindexEntry.setDisable(true);

        getMainWindow().getTable().addSelectionListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.editEntry.setDisable(false);
                this.deleteEntry.setDisable(false);
                this.unindexEntry.setDisable(false);
            } else {
                this.editEntry.setDisable(true);
                this.deleteEntry.setDisable(true);
                this.unindexEntry.setDisable(true);
            }
        }));
    }

    // *************
    // Action Events
    // *************

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
        App.getInstance().getWindowsManager().showWindowAndWait(SettingsWindow.class, getMainWindow().getWindow());
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
        getMainWindow().getWindow().setIconified(true);
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

    /**
     * Called when the change library menu item is pressed.
     *
     * @param e action event.
     */
    private void onChangeLibrary(ActionEvent e) {
        LibraryWindow.present(false);
    }

    /**
     * Called when add new entry menu item is pressed.
     *
     * @param e action event.
     */
    private void onAddEntry(ActionEvent e) {
        getMainWindow().getActionBar().addEntry();
    }

    /**
     * Called when the edit entry menu item is pressed.
     *
     * @param e action event.
     */
    private void onEditEntry(ActionEvent e) {
        getMainWindow().getActionBar().editSelectedEntry();
    }

    /**
     * Called when the delete entry menu item is pressed.
     *
     * @param e action event.
     */
    private void onDeleteEntry(ActionEvent e) {
        getMainWindow().getActionBar().deleteSelectedEntry();
    }

    /**
     * Called when the unindex entry menu item is pressed.
     *
     * @param e action event.
     */
    private void onUnindexEntry(ActionEvent e) {
        getMainWindow().getActionBar().unindexSelectedEntry();
    }
}