/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.fx.entry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.fx.main.MainWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.ilibrary.IEntry;
import org.lmelaia.iseries.library.LibraryException;

/**
 * The controller class the entry dialog window.
 */
public class EntryWindowController extends FXController {

    @FXML
    private Label uuid;

    @FXML
    private TextField fieldName;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnRefresh;

    /**
     * The entry this window will modify/add.
     */
    private IEntry workingEntry;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        btnAdd.setOnAction(this::onAddPressed);
        btnRefresh.setOnAction(this::onRefreshPressed);
        btnCancel.setOnAction(this::onCancelPressed);
    }

    /**
     * Add button listener.
     * <br/>
     * Gets the user input and adds a newly
     * created entry to the library from it.
     *
     * @param e -
     */
    private void onAddPressed(ActionEvent e) {
        workingEntry.setName(fieldName.getText());

        try {
            App.getInstance().getILibrary().add(workingEntry);
        } catch (LibraryException.EntryModificationException e1) {
            AlertUtil.showErrorDialog("Failed to add entry", e1.getClass().getName());
        }

        App.getInstance().getWindowsManager().getWindow(MainWindow.class).getController().getActionBar().clearSearch();
        this.getWindow().close();
    }

    /**
     * Refresh button listener.
     * <br/>
     * Replaces the {@link #workingEntry}
     * with a new instance with a new UUID.
     *
     * @param e -
     */
    private void onRefreshPressed(ActionEvent e) {
        setWorkingEntry(null);
    }

    /**
     * Cancel button listener.
     * <br/>
     * Closes the window.
     *
     * @param e -
     */
    private void onCancelPressed(ActionEvent e) {
        this.getWindow().close();
    }

    /**
     * Sets the entry this window will
     * modify and add/update.
     *
     * @param entry the entry or {@code null}
     *              for a new entry.
     */
    private void setWorkingEntry(IEntry entry) {
        if (entry == null)
            entry = new IEntry();

        this.workingEntry = entry;

        this.uuid.setText(workingEntry.getUUID());
        this.clear();
    }

    /**
     * Populates the fields in the window
     * from the data in the {@link #workingEntry}.
     */
    private void populateFromEntry() {
        this.fieldName.setText(this.workingEntry.getName());
    }

    /**
     * Clears any entered user input.
     */
    private void clear() {
        this.fieldName.setText("");
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Sets this window instance to add
     * mode.
     */
    public void addMode() {
        setWorkingEntry(null);
        this.btnAdd.setText("Add");
        this.btnRefresh.setVisible(true);
    }

    /**
     * Sets this window instance to edit
     * mode.
     */
    public void editMode(IEntry entry) {
        setWorkingEntry(entry);
        populateFromEntry();
        this.btnAdd.setText("Update");
        this.btnRefresh.setVisible(false);
    }
}
