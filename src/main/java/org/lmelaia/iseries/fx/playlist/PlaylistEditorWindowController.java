package org.lmelaia.iseries.fx.playlist;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.fx.main.MainWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.ilibrary.IEntry;
import org.lmelaia.iseries.ilibrary.IPlaylist;
import org.lmelaia.iseries.ilibrary.IPlaylists;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the playlist editor window.
 */
public class PlaylistEditorWindowController extends FXController {

    @FXML
    private ListView<SelectableEntry> listViewEntries;

    @FXML
    private Button btnFinish;

    @FXML
    private Button btnCancel;

    @FXML
    private Label labelPlaylist;

    /**
     * The playlist we're displaying/editing.
     */
    private IPlaylist workingPlaylist;

    // ****
    // Init
    // ****

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.btnFinish.setOnAction(this::onFinish);
        this.btnCancel.setOnAction(this::onCancel);

        listViewEntries.setCellFactory(CheckBoxListCell.forListView(SelectableEntry::getSelectedProperty));

        //Allow checking (check box) values by clicking on them.
        listViewEntries.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null)
                        return;

                    newValue.setSelected(!newValue.isSelected());
                }
        );
    }

    // *************
    // Action Events
    // *************

    /**
     * Called when the cancel button is pressed.
     * Closes the window without saving any changes.
     *
     * @param event action event.
     */
    private void onCancel(ActionEvent event) {
        this.getWindow().close();
    }

    /**
     * Called when the apply button is pressed.
     * Clears the playlist and adds only the
     * selected entries in the {@link #listViewEntries}
     * to the playlist.
     *
     * @param event action event.
     */
    private void onFinish(ActionEvent event) {
        List<IEntry> entriesToAdd = new ArrayList<>();

        for (SelectableEntry entry : listViewEntries.getItems()) {
            if (entry.isSelected())
                entriesToAdd.add(entry.getEntry());
        }

        if (workingPlaylist != null) {
            try {
                workingPlaylist.clear();
                workingPlaylist.addAll(entriesToAdd.toArray(new IEntry[0]));
            } catch (IPlaylists.PlaylistException.PlaylistWriteException e) {
                AlertUtil.showErrorDialog("Failed to save playlist.", e.getClass().getName());
                return;
            }
        }

        App.getInstance().getWindowsManager().getWindow(MainWindow.class)
                .getController().getNavigator().selectPlaylist(workingPlaylist.getName());

        this.getWindow().close();
    }

    // *************
    // Protected API
    // *************

    /**
     * Populates the {@link #labelPlaylist} with
     * the playlist name and populates the {@link
     * #listViewEntries} with the playlist data.
     *
     * @param playlist the playlist used to populate
     *                 window data.
     */
    protected void populate(IPlaylist playlist) {
        this.workingPlaylist = playlist;
        this.labelPlaylist.setText(playlist.getName());

        this.listViewEntries.getItems().clear();
        for (IEntry entry : App.getInstance().getILibrary().getEntries()) {
            this.listViewEntries.getItems().add(new SelectableEntry(entry, playlist.has(entry)));
        }
    }

    // *********
    // Internals
    // *********

    /**
     * Represents an {@link IEntry} that is selectable/
     * deselectable. Used to decide what entries to add/
     * remove from the playlist.
     */
    private static class SelectableEntry {

        /**
         * The {@link IEntry} this object represents.
         */
        private final IEntry entry;

        /**
         * Observable boolean indicating if the entry
         * is checked or not.
         */
        private final SimpleBooleanProperty selected;

        /**
         * Constructs the SelectableEntry reference.
         *
         * @param entry    the {@link IEntry} we're representing.
         * @param selected the default selection.
         */
        public SelectableEntry(IEntry entry, boolean selected) {
            this.entry = entry;
            this.selected = new SimpleBooleanProperty(selected);
        }

        /**
         * @return {@code true} if the entry
         * is selected in the window.
         */
        public boolean isSelected() {
            return selected.get();
        }

        /**
         * @return the observable boolean value that
         * indicates if the entry is selected.
         */
        public SimpleBooleanProperty getSelectedProperty() {
            return selected;
        }

        /**
         * Sets the entry as selected or unselected in
         * the list view.
         *
         * @param selected {@code true} to select the
         *                 entry, {@code false} otherwise.
         */
        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        /**
         * @return the {@link IEntry} reference
         * this object represents.
         */
        public IEntry getEntry() {
            return this.entry;
        }

        /**
         * @return the String representation of this object.
         * The name and UUID of the {@link IEntry} reference.
         * Used by the ListView to display the text.
         */
        @Override
        public String toString() {
            return entry.getName() + " (" + entry.getUUID() + ")";
        }
    }
}
