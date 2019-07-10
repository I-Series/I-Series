package org.lmelaia.iseries.fx.delete;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.ilibrary.IEntry;
import org.lmelaia.iseries.library.LibraryException;

/**
 * Controller class for the delete confirmation dialog.
 */
public class DeleteWindowController extends FXController {

    @FXML
    private Label labelEntryName;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUnindex;

    @FXML
    private Button btnCancel;

    /**
     * The entry to delete/unindex depending
     * on the users choice.
     */
    private IEntry workingEntry;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.btnDelete.setOnAction(this::onDelete);
        this.btnCancel.setOnAction(this::onCancel);
        this.btnUnindex.setOnAction(this::onUnindex);
        this.getWindow().focusedProperty().addListener((observable) -> {
            if (!this.getWindow().isFocused()) {
                this.getWindow().hide();
            }
        });
    }

    /**
     * Deletes the {@link #workingEntry} from the library.
     *
     * @param event Action Event.
     */
    private void onDelete(ActionEvent event) {
        try {
            App.getInstance().getILibrary().delete(workingEntry);
        } catch (LibraryException.EntryModificationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("I-Series");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete entry.");
            alert.showAndWait();
            AppLogger.getLogger().warn(
                    "Failed to delete entry: " + workingEntry.toString(),
                    ex
            );
        }

        this.getWindow().hide();
    }

    /**
     * Unindex the {@link #workingEntry} from the library.
     *
     * @param e Action Event.
     */
    private void onUnindex(ActionEvent e) {
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

        this.getWindow().hide();
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
