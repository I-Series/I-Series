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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    /**
     * The image graphic used to display a heart shape outline.
     */
    private static final ImageView HEART_OPEN_IMG = new ImageView(
            new Image(EntryWindowController.class.getResourceAsStream("/images/open_heart_16.png"))
    );

    /**
     * The image graphic used to display a shaded heart shape.
     */
    private static final ImageView HEART_CLOSED_IMG = new ImageView(
            new Image(EntryWindowController.class.getResourceAsStream("/images/closed_heart_16.png"))
    );

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

    @FXML
    private ChoiceBox<String> ratingChoice;

    @FXML
    private Button lovedButton;

    @FXML
    private TextArea synopsisField;

    @FXML
    private TextArea commentsField;

    @FXML
    private ToggleButton typeMovieBtn;

    @FXML
    private ToggleButton typeSeriesBtn;

    @FXML
    private ToggleButton typeTrilogyBtn;

    /**
     * The entry this window will modify/add.
     */
    private IEntry workingEntry;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        lovedButton.setGraphic(HEART_OPEN_IMG);

        btnAdd.setOnAction(this::onAddPressed);
        btnRefresh.setOnAction(this::onRefreshPressed);
        btnCancel.setOnAction(this::onCancelPressed);
        lovedButton.setOnAction(this::onLovedPressed);
        typeTrilogyBtn.setOnAction(this::onTrilogyPressed);
        typeMovieBtn.setOnAction(this::onMoviePressed);
        typeSeriesBtn.setOnAction(this::onSeriesPressed);

        for (IEntry.RatingValues type : IEntry.RatingValues.values()) {
            ratingChoice.getItems().add(type.val);
        }

        ratingChoice.getSelectionModel().select(IEntry.RatingValues.NA.val);
    }

    /**
     * Handles what happens when the user clicks on the heart/loved icon -
     * toggles graphic icon.
     */
    private void onLovedPressed(ActionEvent actionEvent) {
        if (lovedButton.getGraphic() == HEART_OPEN_IMG) {
            lovedButton.setGraphic(HEART_CLOSED_IMG);
        } else {
            lovedButton.setGraphic(HEART_OPEN_IMG);
        }
    }

    /**
     * Takes all the user data input from the window and writes
     * it to the {@link #workingEntry} for permanent storage.
     */
    private void writeToEntry() {
        workingEntry.setName(fieldName.getText());
        workingEntry.setLoved(lovedButton.getGraphic() == HEART_CLOSED_IMG);
        workingEntry.setSynopsis(synopsisField.getText());
        workingEntry.setComments(commentsField.getText());

        if (typeMovieBtn.isSelected())
            workingEntry.setType(IEntry.TypeValues.MOVIE);
        else if (typeSeriesBtn.isSelected())
            workingEntry.setType(IEntry.TypeValues.SERIES);
        else if (typeTrilogyBtn.isSelected())
            workingEntry.setType(IEntry.TypeValues.TRILOGY);
        else workingEntry.setType(IEntry.TypeValues.NONE);

        workingEntry.setRating(IEntry.RatingValues.fromRepresentation(ratingChoice.getValue()));
    }

    /**
     * Populates the fields in the window from the
     * data read from the {@link #workingEntry}.
     */
    private void readFromEntry() {
        this.fieldName.setText(this.workingEntry.getName());
        this.lovedButton.setGraphic((workingEntry.isLoved() ? HEART_CLOSED_IMG : HEART_OPEN_IMG));
        this.synopsisField.setText(workingEntry.getSynopsis());
        this.commentsField.setText(workingEntry.getComments());

        if (workingEntry.getType() == IEntry.TypeValues.MOVIE)
            typeMovieBtn.setSelected(true);
        if (workingEntry.getType() == IEntry.TypeValues.SERIES)
            typeSeriesBtn.setSelected(true);
        if (workingEntry.getType() == IEntry.TypeValues.TRILOGY)
            typeTrilogyBtn.setSelected(true);

        ratingChoice.getSelectionModel().select(IEntry.RatingValues.toRepresentation(workingEntry.getRating()));
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
        writeToEntry();

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
     * Toggles the movie button to on.
     */
    private void onMoviePressed(ActionEvent e) {
        typeSeriesBtn.setSelected(false);
        typeTrilogyBtn.setSelected(false);
    }

    /**
     * Toggles the series button to on.
     */
    private void onSeriesPressed(ActionEvent e) {
        typeMovieBtn.setSelected(false);
        typeTrilogyBtn.setSelected(false);
    }

    /**
     * Toggles the trilogy button to on.
     */
    private void onTrilogyPressed(ActionEvent e) {
        typeMovieBtn.setSelected(false);
        typeSeriesBtn.setSelected(false);
    }

    /**
     * Clears any entered user input.
     */
    private void clear() {
        this.fieldName.setText("");
        this.lovedButton.setGraphic(HEART_OPEN_IMG);
        this.synopsisField.setText("");
        this.commentsField.setText("");
        ratingChoice.getSelectionModel().select(IEntry.RatingValues.NA.val);
        typeMovieBtn.setSelected(false);
        typeSeriesBtn.setSelected(false);
        typeTrilogyBtn.setSelected(false);
        fieldName.requestFocus();
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Sets this window instance to add
     * mode.
     */
    public void addMode() {
        this.getWindow().setTitle("Entry Editor - New");
        setWorkingEntry(null);
        this.btnAdd.setText("Add");
        this.btnRefresh.setVisible(true);
    }

    /**
     * Sets this window instance to edit
     * mode.
     */
    public void editMode(IEntry entry) {
        this.getWindow().setTitle("Entry Editor - " + entry.getName());
        setWorkingEntry(entry);
        readFromEntry();
        this.btnAdd.setText("Update");
        this.btnRefresh.setVisible(false);
    }
}
