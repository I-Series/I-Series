package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.fx.entry.EntryWindow;
import org.lmelaia.iseries.fx.entry.EntryWindowController;
import org.lmelaia.iseries.ilibrary.IEntry;
import org.lmelaia.iseries.ilibrary.ITableEntry;
import org.lmelaia.iseries.library.LibraryEntry;
import org.lmelaia.iseries.library.LibraryException;

/**
 * Sub-control class that handles the toolbar
 * part of the main window.
 */
class ActionBarControl implements SubControl {

    /**
     * The main windows controller class.
     */
    private final MainWindowController window;

    /**
     * The add-entry button on the main window.
     */
    private final Button add;

    /**
     * The edit-entry button on the main window.
     */
    private final Button edit;

    /**
     * The delete-entry button on the main window.
     */
    private final Button delete;

    /**
     * The unindex-entry button on the main window.
     */
    private final Button unindex;

    /**
     * The search bar on the main window.
     */
    private final TextField input;

    /**
     * The clear search button in the search bar.
     */
    private final Button clearInput;

    /**
     * Constructor.
     *
     * @param window   the main windows controller class.
     * @param controls an array of all the controls this class handles.
     */
    ActionBarControl(MainWindowController window, Object[] controls) {
        this.window = window;
        this.add = (Button) controls[0];
        this.edit = (Button) controls[1];
        this.delete = (Button) controls[2];
        this.input = (TextField) controls[3];
        this.unindex = (Button) controls[4];
        this.clearInput = (Button) controls[5];
    }

    /**
     * Sets up this classes components.
     */
    @Override
    public void init() {
        add.setOnAction(this::onAddPressed);
        edit.setOnAction(this::onEditPressed);
        delete.setOnAction(this::onDeletePressed);
        input.setOnAction(this::onInputEntered);
        unindex.setOnAction(this::onUnindexPressed);
        clearInput.setOnAction(e -> {
            this.input.clear();
            this.clearInput.setVisible(false);
            App.getInstance().getILibrary().clearSearchFilter();
        });

        //Handles showing/hiding the clear text button.
        input.setOnKeyTyped((event) -> {
            if (!input.getText().equals(""))
                this.clearInput.setVisible(true);
            else
                this.clearInput.setVisible(false);
        });

        edit.setDisable(true);
        delete.setDisable(true);
        unindex.setDisable(true);
        clearInput.setVisible(false);

        window.tableController.addSelectionListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                window.controlBar.enableModificationButtons();
            else
                window.controlBar.disableModificationButtons();
        });
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
     * Clears the users search input
     * and clears the table filter.
     */
    public void clearSearch() {
        this.clearInput.fire();
    }

    /**
     * Called when the add button is pressed. Opens
     * the entry window in add mode.
     *
     * @param e action event.
     */
    private void onAddPressed(ActionEvent e) {
        EntryWindow entryWindow = FXWindowsManager.getInstance().getWindow(EntryWindow.class);
        EntryWindowController entryWindowController = entryWindow.getController();

        entryWindowController.addMode();

        FXWindowsManager.getInstance().showWindowAndWait(entryWindow, window.getWindow());
    }

    /**
     * Called when the edit button is pressed. Opens
     * the entry window in edit mode.
     *
     * @param e action event.
     */
    private void onEditPressed(ActionEvent e) {
        EntryWindow entryWindow = FXWindowsManager.getInstance().getWindow(EntryWindow.class);
        EntryWindowController entryWindowController = entryWindow.getController();

        entryWindowController.editMode(window.tableController.getSelectedEntry().getEntry());

        FXWindowsManager.getInstance().showWindowAndWait(entryWindow, window.getWindow());
    }

    /**
     * Called when the add button is pressed. Deletes
     * the entry selected in the table if one is selected.
     *
     * @param e action event.
     */
    private void onDeletePressed(ActionEvent e) {
        ITableEntry tableEntry = window.tableController.getSelectedEntry();
        if (tableEntry == null) return;

        IEntry entry = tableEntry.getEntry();

        try {
            App.getInstance().getILibrary().delete(entry);
        } catch (LibraryException.EntryModificationException ex) {
            //TODO: Dialog
            ex.printStackTrace();
        }
    }

    /**
     * Called when the unindex button is pressed.
     * Removes the entry selected in the table
     * from the library index. See
     * {@link org.lmelaia.iseries.library.Library#unindex(LibraryEntry)}.
     *
     * @param e action event.
     */
    private void onUnindexPressed(ActionEvent e) {
        ITableEntry tableEntry = window.tableController.getSelectedEntry();
        if (tableEntry == null) return;

        IEntry entry = tableEntry.getEntry();

        try {
            App.getInstance().getILibrary().unindex(entry);
        } catch (LibraryException.EntryModificationException ex) {
            //TODO: Dialog
            ex.printStackTrace();
        }
    }

    /**
     * Called when the user presses enter in the search bar.
     *
     * @param e action event.
     */
    private void onInputEntered(ActionEvent e) {
        App.getInstance().getILibrary().setSearchFilter((entry ->
                entry.getName().toLowerCase().contains(input.getText().toLowerCase())
        ));

        this.input.selectAll();
    }

    // *************
    // Inter Control
    // *************

    /**
     * Enables any toolbar buttons
     * that require a selected entry.
     */
    protected void enableModificationButtons() {
        this.edit.setDisable(false);
        this.delete.setDisable(false);
        this.unindex.setDisable(false);
    }

    /**
     * Disables any toolbar buttons
     * that require a selected entry.
     */
    protected void disableModificationButtons() {
        this.edit.setDisable(true);
        this.delete.setDisable(true);
        this.unindex.setDisable(true);
    }
}