package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.fx.delete.DeleteWindow;
import org.lmelaia.iseries.fx.entry.EntryWindow;
import org.lmelaia.iseries.fx.entry.EntryWindowController;
import org.lmelaia.iseries.fx.unindex.UnindexWindow;
import org.lmelaia.iseries.ilibrary.ITableEntry;
import org.lmelaia.iseries.library.LibraryEntryBase;

/**
 * Sub-controller class that handles the toolbar
 * part of the main window.
 */
public class ActionBarController extends SubControl {

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
    ActionBarController(MainWindowController window, Object[] controls) {
        super(window);
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
    void init() {
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
        input.setOnKeyTyped((event) -> this.clearInput.setVisible(!input.getText().equals("")));

        edit.setDisable(true);
        delete.setDisable(true);
        unindex.setDisable(true);
        clearInput.setVisible(false);

        getMainWindow().getTable().addSelectionListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                getMainWindow().getActionBar().enableModificationButtons();
            else
                getMainWindow().getActionBar().disableModificationButtons();
        });
    }

    /**
     * Called when the add button is pressed. Opens
     * the entry window in add mode.
     *
     * @param e action event.
     */
    private void onAddPressed(ActionEvent e) {
        addEntry();
    }

    /**
     * Called when the edit button is pressed. Opens
     * the entry window in edit mode.
     *
     * @param e action event.
     */
    private void onEditPressed(ActionEvent e) {
        editSelectedEntry();
    }

    /**
     * Called when the add button is pressed. Deletes
     * the entry selected in the table if one is selected.
     *
     * @param e action event.
     */
    private void onDeletePressed(ActionEvent e) {
        deleteSelectedEntry();
    }

    /**
     * Called when the unindex button is pressed.
     * Removes the entry selected in the table
     * from the library index. See
     * {@link org.lmelaia.iseries.library.Library#unindex(LibraryEntryBase)}.
     *
     * @param e action event.
     */
    private void onUnindexPressed(ActionEvent e) {
        unindexSelectedEntry();
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

    /**
     * Enables any toolbar buttons
     * that require a selected entry.
     */
    private void enableModificationButtons() {
        this.edit.setDisable(false);
        this.delete.setDisable(false);
        this.unindex.setDisable(false);
    }

    /**
     * Disables any toolbar buttons
     * that require a selected entry.
     */
    private void disableModificationButtons() {
        this.edit.setDisable(true);
        this.delete.setDisable(true);
        this.unindex.setDisable(true);
    }

    // *************
    // Inter Control
    // *************

    /**
     * Opens the entry window in add mode.
     */
    protected void addEntry() {
        EntryWindow entryWindow = FXWindowsManager.getInstance().getWindow(EntryWindow.class);
        EntryWindowController entryWindowController = entryWindow.getController();

        entryWindowController.addMode();

        FXWindowsManager.getInstance().showWindowAndWait(entryWindow, getMainWindow().getWindow());
    }

    /**
     * Opens the selected entry in the entry window.
     */
    protected void editSelectedEntry() {
        EntryWindow entryWindow = FXWindowsManager.getInstance().getWindow(EntryWindow.class);
        EntryWindowController entryWindowController = entryWindow.getController();

        entryWindowController.editMode(getMainWindow().getTable().getSelectedEntry().getEntry());

        FXWindowsManager.getInstance().showWindowAndWait(entryWindow, getMainWindow().getWindow());
    }

    /**
     * Opens the {@link DeleteWindow} confirmation dialog linked
     * to the selected entry in the table, which will then delete
     * the entry if user confirms the delete.
     */
    protected void deleteSelectedEntry() {
        ITableEntry tableEntry = getMainWindow().getTable().getSelectedEntry();
        if (tableEntry == null) return;

        DeleteWindow.present(tableEntry.getEntry());
    }

    /**
     * Opens the {@link UnindexWindow} confirmation dialog linked
     * to the selected entry in the table, which will then unindex
     * the entry if user confirms the unindex, or just unindex the entry
     * if the user has requested entries be unindexed without confirmation.
     */
    protected void unindexSelectedEntry() {
        ITableEntry tableEntry = getMainWindow().getTable().getSelectedEntry();
        if (tableEntry == null) return;

        UnindexWindow.present(tableEntry.getEntry());
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Clears the users search input
     * and clears the search filter
     * applied to the table.
     */
    public void clearSearch() {
        this.clearInput.fire();
    }
}