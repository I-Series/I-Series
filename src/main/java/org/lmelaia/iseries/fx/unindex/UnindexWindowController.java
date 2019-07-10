package org.lmelaia.iseries.fx.unindex;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.ilibrary.IEntry;
import org.lmelaia.iseries.library.LibraryException;

/**
 * Controller class for the unindex window.
 */
public class UnindexWindowController extends FXController {

    @FXML
    private Label labelEntryName;

    @FXML
    private Button btnUnindex;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox checkBoxDontAsk;

    /**
     * The entry to unindex or not depending
     * on the users choice.
     */
    private IEntry workingEntry;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.btnCancel.setOnAction(this::onCancel);
        this.btnUnindex.setOnAction(this::onUnindex);
        this.getWindow().focusedProperty().addListener((observable) -> {
            if (!this.getWindow().isFocused()) {
                this.getWindow().hide();
            }
        });
    }

    /**
     * Closes the window.
     *
     * @param e Action Event.
     */
    private void onCancel(ActionEvent e) {
        this.getWindow().hide();
    }

    /**
     * Unindex the {@link #workingEntry} from the library.
     *
     * @param e Action Event.
     */
    //Can be called from the window class if the user
    //doesn't want the dialog presented.
    protected void onUnindex(ActionEvent e) {
        try {
            App.getInstance().getILibrary().unindex(workingEntry);
        } catch (LibraryException.EntryModificationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("I-Series");
            alert.setHeaderText(null);
            alert.setContentText("Failed to unindex entry.");
            alert.showAndWait();
            AppLogger.getLogger().warn(
                    "Failed to unindex entry: " + workingEntry.toString(),
                    ex
            );
        }

        if (checkBoxDontAsk.isSelected()) {
            Settings.ALWAYS_UNINDEX.changeValue(true);
        }

        this.getWindow().hide();
    }

    /**
     * Sets the {@link #workingEntry}. This will also
     * update the window to display the entry name.
     *
     * @param entry the entry to set as the workingEntry.
     */
    protected void setEntry(IEntry entry) {
        this.workingEntry = entry;
        this.labelEntryName.setText(entry.toString() + "?");
    }
}
