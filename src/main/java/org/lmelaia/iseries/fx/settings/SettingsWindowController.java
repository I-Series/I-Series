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
package org.lmelaia.iseries.fx.settings;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.ExitCode;

import java.io.File;
import java.util.Objects;

/**
 * Controller class for the Settings window.
 */
public class SettingsWindowController extends FXController {

    @FXML
    private TextField fieldLibraryPath;

    @FXML
    private Button btnBrowse;

    @FXML
    private ChoiceBox<String> choiceOnClose;

    @FXML
    private Spinner<Integer> spinnerLauncherFrequency;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;

    private DirectoryChooser directoryChooser;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select I-Series Library Path");

        spinnerLauncherFrequency.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(250, 5000));
        ObservableList<String> choiceItems = choiceOnClose.getItems();
        choiceItems.add("Show Quit Dialog");
        choiceItems.add("Close I-Series");
        choiceItems.add("Move to Tray");

        btnApply.setOnAction(this::onApplyPressed);
        btnCancel.setOnAction(this::onCancelPressed);
        btnBrowse.setOnAction(this::onBrowsePressed);

        this.getWindow().setOnShowing(e -> loadSettings());
    }

    /**
     * Loads the current app settings into the
     * window components.
     */
    private void loadSettings() {
        switch (Settings.WINDOW_CLOSE_PREFERENCE.getValueAsInt()) {
            case 1:
                choiceOnClose.getSelectionModel().clearAndSelect(1);
                break;
            case 2:
                choiceOnClose.getSelectionModel().clearAndSelect(2);
                break;
            default:
                choiceOnClose.getSelectionModel().clearAndSelect(0);
        }

        this.fieldLibraryPath.setText(Settings.LIBRARY_PATH.getValue());

        this.spinnerLauncherFrequency.getValueFactory().setValue(Settings.LAUNCHER_PING_FREQUENCY.getValueAsInt());
    }

    /**
     * Checks that all user input is valid.
     *
     * @return {@code true} if the provided user input is
     * valid.
     */
    private boolean checkInput() {
        //NO-OP
        return true;
    }

    /**
     * Changes the app settings to reflect the user
     * inputted settings.
     */
    private void applySettings() {
        int launcherPingFrequency = Integer.parseInt(this.spinnerLauncherFrequency.getValue().toString());
        Settings.LAUNCHER_PING_FREQUENCY.changeValue(launcherPingFrequency);

        Settings.WINDOW_CLOSE_PREFERENCE.changeValue(
                choiceOnClose.getSelectionModel().getSelectedIndex()
        );

        String libraryPath = Settings.LIBRARY_PATH.getValue();
        String newLibraryPath = this.fieldLibraryPath.getText();

        //Must be done last
        if (!new File(libraryPath).getAbsolutePath().equals(new File(newLibraryPath).getAbsolutePath())) {
            //Library change

            File newLibrary = new File(newLibraryPath);

            if (Objects.requireNonNull(newLibrary.list()).length != 0) {
                boolean foundIndex = false;

                for (String file : Objects.requireNonNull(newLibrary.list())) {
                    if (file.endsWith("index.json")) {
                        foundIndex = true;

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("I-Series");
                        alert.setHeaderText("Switching to the existing I-Series Library at the selected path.");
                        alert.setContentText("I-Series must restart before it can switch to the new library.");
                        alert.showAndWait();
                    }
                }

                if (!foundIndex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("I-Series");
                    alert.setHeaderText(null);
                    alert.setContentText("New I-Series Library folder must be empty.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("I-Series");
                alert.setHeaderText("Will create a new I-Series Library at the selected path.");
                alert.setContentText("I-Series must restart before it can switch to the new library.");
                alert.showAndWait();
            }

            Settings.LIBRARY_PATH.changeValue(newLibrary.getAbsolutePath());
            App.getInstance().exit(ExitCode.RESTART);
        }
    }

    /**
     * Apply button on action.
     *
     * @param e e
     */
    private void onApplyPressed(ActionEvent e) {
        if (checkInput())
            applySettings();

        this.getWindow().close();
    }

    /**
     * Cancel button on action.
     *
     * @param e -
     */
    private void onCancelPressed(ActionEvent e) {
        this.getWindow().close();
    }

    /**
     * Library path browse button on action.
     *
     * @param e -
     */
    private void onBrowsePressed(ActionEvent e) {
        File givenFile = directoryChooser.showDialog(this.getWindow());
        if (givenFile != null)
            this.fieldLibraryPath.setText(givenFile.toString());
    }
}
