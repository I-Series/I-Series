package org.lmelaia.iseries.fx.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.ExitCode;

import java.io.File;
import java.util.Objects;

/**
 * Controller class for the Library window.
 */
public class LibraryWindowController extends FXController {

    private DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private TextField textFieldLocation;

    @FXML
    private Button btnBrowse;

    @FXML
    private Label labelWarning;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnContinue;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        directoryChooser.setTitle("Select I-Series Library Path");

        this.btnBrowse.setOnAction(this::onBrowsePressed);
        this.btnCancel.setOnAction(e -> this.getWindow().hide());
        this.btnContinue.setOnAction(this::onContinuePressed);
    }

    /**
     * Updates the path text field with the current
     * library with.
     */
    void populateFields() {
        String path = Settings.LIBRARY_PATH.getValue();

        if (path.equals(""))
            path = System.getProperty("user.home") + File.separator + "I-Series Library";

        this.textFieldLocation.setText(path);
    }

    /**
     * Used to specify that the input is required
     * and that the user cannot "cancel" the window.
     *
     * @param required {@code true} if the user
     *                 input is required.
     */
    void setRequired(boolean required) {
        if (required) {
            this.labelWarning.setVisible(true);
            this.btnCancel.setDisable(true);
            this.btnCancel.setVisible(false);
        } else {
            this.labelWarning.setVisible(false);
            this.btnCancel.setDisable(false);
            this.btnCancel.setVisible(true);
        }
    }

    /**
     * Library path browse button on action.
     *
     * @param e -
     */
    private void onBrowsePressed(ActionEvent e) {
        File givenFile = directoryChooser.showDialog(this.getWindow());
        if (givenFile != null)
            this.textFieldLocation.setText(givenFile.toString());
    }

    /**
     * Updates the Library Path (if changed) and restarts
     * the application.
     *
     * @param e action event.
     */
    private void onContinuePressed(ActionEvent e) {
        String libraryPath = Settings.LIBRARY_PATH.getValue();
        String newLibraryPath = this.textFieldLocation.getText();

        //True if this window is being shown before the
        //initialization of the library.
        boolean isSpecialPopup = libraryPath.equals("");

        //Must be done last
        if (!new File(libraryPath).getAbsolutePath().equals(new File(newLibraryPath).getAbsolutePath())) {
            //Library change

            File newLibrary = new File(newLibraryPath);

            //noinspection ConstantConditions
            if (newLibrary.list() != null && newLibrary.list().length != 0) {
                boolean foundIndex = false;

                for (String file : Objects.requireNonNull(newLibrary.list())) {
                    if (file.endsWith("index.json")) {
                        foundIndex = true;

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("I-Series");
                        alert.setHeaderText("Switching to the existing I-Series Library at the selected path.");
                        if (!isSpecialPopup)
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
                if (!isSpecialPopup)
                    alert.setContentText("I-Series must restart before it can switch to the new library.");
                alert.showAndWait();
            }

            Settings.LIBRARY_PATH.changeValue(newLibrary.getAbsolutePath());
            //Always needs a restart.
            App.getInstance().exit(ExitCode.RESTART);
        }

        this.getWindow().hide();
    }
}
