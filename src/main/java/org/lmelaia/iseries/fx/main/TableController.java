package org.lmelaia.iseries.fx.main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.fx.playlist.PlaylistCreatorWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.ilibrary.IPlaylists;
import org.lmelaia.iseries.ilibrary.ITableEntry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Sub-control class that handles the table
 * part of the main window.
 */
public class TableController extends SubControl {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    // *******
    // Columns
    // *******

    /**
     * List of columns that can be put
     * into the table.
     */
    private static class Columns {

        /**
         * Context menu that allows enabling/disabling columns.
         */
        private static final TableColumnContentMenu COLUMN_CONTEXT_MENU;

        /**
         * Column to display the name of the entry.
         */
        public static final TableColumn<ITableEntry, ITableEntry> name = new TableColumn<>("Name");

        /**
         * Column to display the UUID of the entry.
         */
        public static final TableColumn<ITableEntry, ITableEntry> uuid = new TableColumn<>("UUID");

        // *******
        // HELPERS
        // *******

        /**
         * Map of all available columns that can go into the table.
         */
        private static final Map<String, TableColumn<ITableEntry, ITableEntry>> columnMap = new HashMap<>();

        /**
         * List of default columns that should be placed in the
         * table.
         */
        public static final TableColumn[] DEFAULTS = new TableColumn[]{
                name
        };

        /*
         * Initializes the columns.
         */
        static {
            COLUMN_CONTEXT_MENU = new TableColumnContentMenu(new TableColumn[]{
                    name, uuid
            });

            //Name
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            name.setContextMenu(COLUMN_CONTEXT_MENU);
            columnMap.put(name.getText(), name);

            //UUID
            uuid.setCellValueFactory(new PropertyValueFactory<>("UUID"));
            uuid.setContextMenu(COLUMN_CONTEXT_MENU);
            columnMap.put(uuid.getText(), uuid);
        }

        /**
         * @return a column with the given {@link TableColumn#getText()}
         */
        public static TableColumn<ITableEntry, ITableEntry> get(String name) {
            return columnMap.get(name);
        }
    }

    /**
     * The table we're controlling.
     */
    private final TableView<ITableEntry> table;

    /**
     * Context menu given when right-clicking
     * on an entry in the table.
     */
    private TableContextMenu contextMenu;

    /**
     * Context menu presented when the {@link #contextMenu} cannot
     * be given (i.e. when no entry is selected).
     * <p>
     * Simple context menu that allows users to add a new entry by
     * clicking anywhere within the table (provided it's not on an
     * entry within the table).
     */
    private DefaultContextMenu defaultContextMenu;

    /**
     * The selected entry in the table.
     * CAN BE NULL.
     */
    private ITableEntry selectedEntry;

    /**
     * @param table  the table to control.
     */
    TableController(MainWindowController window, TableView<ITableEntry> table) {
        super(window);
        this.table = table;
    }

    /**
     * Initializes components.
     */
    @Override
    void init() {
        this.table.getSelectionModel().selectedItemProperty().addListener(this::onItemSelected);

        Columns.COLUMN_CONTEXT_MENU.setTable(table);

        App.getInstance().addLibraryInitListener((library -> {
            library.linkTable(table);
            TableStateStorer.load(table);
            table.sort();
        }));

        defaultContextMenu = new DefaultContextMenu();

        contextMenu = new TableContextMenu(getMainWindow());

        table.setRowFactory(param -> {
            final TableRow row = new TableRow();

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu) defaultContextMenu).otherwise(contextMenu));

            //noinspection unchecked
            return row;
        });

        table.setPlaceholder(new Placeholder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void saveState() {
        TableStateStorer.save(table);
    }

    /**
     * @return the TableEntry in the table at the given index.
     */
    private ITableEntry get(int index) {
        return this.table.getItems().get(index);
    }

    /**
     * Called when a new entry in the
     * table is selected (or deselected).
     *
     * @param observable -
     * @param oldValue   -
     * @param newValue   -
     */
    private void onItemSelected(ObservableValue<? extends ITableEntry> observable,
                                ITableEntry oldValue, ITableEntry newValue) {
        this.selectedEntry = newValue;
    }

    // *************
    // PROTECTED API
    // *************

    /**
     * @return The selected entry in the table.
     * CAN BE NULL if a TreeNode in the library node is selected                                                                    .
     */
    protected ITableEntry getSelectedEntry() {
        return this.selectedEntry;
    }

    /**
     * Adds a listener to be notified when the selected
     * entry in the table changes.
     *
     * @param listener the listener to add.
     */
    protected void addSelectionListener(ChangeListener<? super ITableEntry> listener) {
        table.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    // **************
    // NESTED CLASSES
    // **************

    /**
     * Handles saving state state of the table on application
     * close and restoring the state on application initialization.
     * <p>
     * Saved data:
     * <ul>
     * <li>Columns in table</li>
     * <li>Order of columns</li>
     * <li>Column width</li>
     * <li>Column sort order & type</li>
     * </ul>
     */
    private static class TableStateStorer {

        /**
         * File to which the state will be read/written.
         */
        private static final File SAVE_FILE = new File("savedata/table.json");

        /**
         * Writes the state (defined in the class doc)
         * of the given table to the {@link #SAVE_FILE}.
         *
         * @param tableView the given table.
         */
        public static void save(TableView<ITableEntry> tableView) {
            JsonArray array = new JsonArray();

            //POPULATE
            tableView.getColumns().forEach((column) -> {
                JsonObject jColumn = new JsonObject();

                jColumn.add("value", new JsonPrimitive(column.getText()));
                jColumn.add("sort", new JsonPrimitive(
                        (tableView.getSortOrder().contains(column))
                                ? (column.getSortType() == TableColumn.SortType.ASCENDING) ?
                                //Ascending, Descending, No-Sort
                                1 : 2 : 0
                ));

                jColumn.add("width", new JsonPrimitive(column.getWidth()));

                array.add(jColumn);
            });

            JsonObject arrayHolder = new JsonObject();
            arrayHolder.add("array", array);

            //WRITE
            FileWriter writer = null;
            try {
                writer = new FileWriter(SAVE_FILE);
                writer.write(new Gson().toJson(arrayHolder));
            } catch (IOException e) {
                LOG.error("Failed to write table.json", e);
            } finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        LOG.error("Failed to close table.json writer", e);
                    }
                }
            }
        }

        /**
         * Reads the state (defined in the class doc)
         * of the given table from the {@link #SAVE_FILE}
         * and modifies the table to match.
         *
         * @param tableView the given table.
         */
        @SuppressWarnings("unchecked")
        public static void load(TableView<ITableEntry> tableView) {
            //Add defaults if no save exists.
            if (!SAVE_FILE.exists()) {
                for (TableColumn column : Columns.DEFAULTS) {
                    tableView.getColumns().add((TableColumn<ITableEntry, ITableEntry>) column);
                    Columns.COLUMN_CONTEXT_MENU.checkItem(column);
                }
            }

            try {
                //Read
                FileReader reader = new FileReader(SAVE_FILE);

                StringBuilder contents = new StringBuilder();
                int chr;
                while ((chr = reader.read()) != -1)
                    contents.append((char) chr);

                JsonArray array = new Gson().fromJson(contents.toString(), JsonObject.class)
                        .get("array").getAsJsonArray();

                array.forEach((jsonElement -> {
                    JsonObject jColumn = jsonElement.getAsJsonObject();
                    TableColumn<ITableEntry, ITableEntry> column = Columns.get(jColumn.get("value").getAsString());

                    //Sort Type
                    if (jColumn.has("sort"))
                        switch (jColumn.get("sort").getAsInt()) {
                            case 0:
                                break;
                            case 1:
                                column.setSortType(TableColumn.SortType.ASCENDING);
                                tableView.getSortOrder().add(column);
                                break;
                            case 2:
                                column.setSortType(TableColumn.SortType.DESCENDING);
                                tableView.getSortOrder().add(column);
                                break;
                        }

                    //Width
                    if (jColumn.has("width"))
                        //noinspection deprecation
                        column.impl_setWidth(jColumn.get("width").getAsDouble());

                    //Add to table
                    if (!tableView.getColumns().contains(column)) {
                        tableView.getColumns().add(column);
                        Columns.COLUMN_CONTEXT_MENU.checkItem(column);
                    }

                }));
            } catch (IOException e) {
                LOG.error("Failed to read table.json", e);
            }

            //Always make sure this column gets added.
            if (!tableView.getColumns().contains(Columns.name))
                tableView.getColumns().add(Columns.name);
        }
    }

    /**
     * Placeholder node displayed on the table when no entries
     * are available. This placeholder directs the user to the
     * add entry window & help documentation as well as displaying
     * the no entries available message.
     */
    private class Placeholder extends AnchorPane {

        /**
         * Context menu given when this node is right-clicked.
         */
        private final ContextMenu contextMenu = new DefaultContextMenu();

        /**
         * Constructs the placeholder.
         */
        public Placeholder() {
            //Create entry button
            ImageView addEntryButtonImage = new ImageView("/images/img_add.png");
            addEntryButtonImage.setFitWidth(16);
            addEntryButtonImage.setFitHeight(16);

            Button addEntryButton = new Button("", addEntryButtonImage);
            addEntryButton.setStyle("-fx-background-color: transparent;");
            addEntryButton.setOnAction((actionEvent) -> getMainWindow().getActionBar().addEntry());

            //No Entries text
            Label noEntriesText = new Label("No Entries to display");
            noEntriesText.setFont(Font.font(25));
            noEntriesText.setTextAlignment(TextAlignment.CENTER);
            noEntriesText.setAlignment(Pos.CENTER);
            noEntriesText.setUnderline(true);
            setAnchors(noEntriesText, 0, 5, 5, 5);

            //Add Entry text
            Label addEntryText = new Label("To add an entry to the table, press");
            addEntryText.setFont(Font.font(15));
            addEntryText.setTextAlignment(TextAlignment.CENTER);

            Label addEntryText2 = new Label("or right-click anywhere in the table.");
            addEntryText2.setFont(Font.font(15));
            addEntryText2.setTextAlignment(TextAlignment.CENTER);

            HBox addEntryRow = new HBox();
            addEntryRow.setAlignment(Pos.CENTER);
            setAnchors(addEntryRow, 70, 5, 5, 5);

            addEntryRow.getChildren().addAll(addEntryText, addEntryButton, addEntryText2);

            //View Help
            Hyperlink viewHelpLink = new Hyperlink("Documentation");
            viewHelpLink.setOnAction((actionEvent) -> {
                //TODO: Make and open doc window.
            });

            Label viewHelpText = new Label("To view the Help menu, go to Help,");
            viewHelpText.setFont(Font.font(15));
            viewHelpText.setTextAlignment(TextAlignment.CENTER);
            viewHelpLink.setFont(Font.font(15));
            viewHelpLink.setTextAlignment(TextAlignment.CENTER);

            HBox viewHelpRow = new HBox();
            viewHelpRow.setAlignment(Pos.CENTER);
            setAnchors(viewHelpRow, 120, 5, 5, 5);

            viewHelpRow.getChildren().addAll(viewHelpText, viewHelpLink);

            this.setOnContextMenuRequested((contextMenuEvent) ->
                    contextMenu.show(
                            getMainWindow().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()
                    )
            );

            this.getChildren().addAll(noEntriesText, addEntryRow, viewHelpRow);
        }

        /**
         * Sets a given nodes anchor constraints
         * (e.g. {@link AnchorPane#setTopAnchor(Node, Double)}).
         *
         * @param node   the node.
         * @param top    -
         * @param bottom -
         * @param left   -
         * @param right  -
         */
        private void setAnchors(Node node, double top, double bottom, double left, double right) {
            AnchorPane.setBottomAnchor(node, bottom);
            AnchorPane.setTopAnchor(node, top);
            AnchorPane.setLeftAnchor(node, left);
            AnchorPane.setRightAnchor(node, right);
        }
    }

    /**
     * Simple context menu that allows users to add a new entry by
     * right-clicking anywhere within the table (provided it's not on an
     * entry within the table).
     */
    private class DefaultContextMenu extends ContextMenu {

        /**
         * Constructs the context menu.
         */
        public DefaultContextMenu() {
            ImageView imageAdd = new ImageView("/images/img_add.png");
            imageAdd.setFitHeight(16);
            imageAdd.setFitWidth(16);

            MenuItem menuItemAdd = new MenuItem("Add New Entry", imageAdd);
            menuItemAdd.setOnAction((event -> getMainWindow().getActionBar().addEntry()));
            this.getItems().add(menuItemAdd);
        }
    }

    /**
     * Context menu that allows enabling/disabling
     * a given array of TableColumns.
     */
    private static class TableColumnContentMenu extends ContextMenu {

        /**
         * The table to add/remove the columns from.
         * CAN BE NULL.
         */
        private TableView table;

        /**
         * Creates a new table column context menu
         * with menu items for the given table columns.
         *
         * @param columns array of columns to add menu items for.
         */
        public TableColumnContentMenu(TableColumn[] columns) {
            createMenuItems(columns);
        }

        /**
         * Creates the menu items from the given
         * columns.
         *
         * @param columns the given columns.
         */
        private void createMenuItems(TableColumn[] columns) {
            for (TableColumn column : columns) {
                CheckMenuItem item = new CheckMenuItem(column.getText());

                //We always want this one shown.
                if (column.getText().toLowerCase().equals("name"))
                    item.setDisable(true);

                item.setOnAction(event -> {
                    if (table == null)
                        return;

                    if (!item.isSelected())
                        table.getColumns().remove(column);
                    else
                        //noinspection unchecked
                        table.getColumns().add(column);
                });

                this.getItems().add(item);
            }
        }

        /**
         * Check a menu item with the same name as the
         * given column.
         *
         * @param column the given column.
         */
        protected void checkItem(TableColumn column) {
            for (MenuItem item : this.getItems())
                if (item.getText().equals(column.getText()))
                    ((CheckMenuItem) item).setSelected(true);
        }

        /**
         * Sets the table view instance that columns will be
         * added to/removed from.
         *
         * @param table the table view columns will be
         *              added to/removed from.
         */
        protected void setTable(TableView table) {
            this.table = table;
        }
    }

    /**
     * Context menu added to the main windows table view. This context
     * menu gives shortcut actions for the select entry in the table.
     * This context menu will only be shown when right-clicking on a table entry.
     */
    private static class TableContextMenu extends ContextMenu {

        /**
         * Image shown on the Add menu item.
         */
        private static final ImageView imageAdd = new ImageView("/images/img_add.png");

        /**
         * Image shown on the Edit menu item.
         */
        private static final ImageView imageEdit = new ImageView("/images/img_edit.png");

        /**
         * Image shown on the Delete menu item.
         */
        private static final ImageView imageDelete = new ImageView("/images/img_delete.png");

        /**
         * Image shown on the Unindex menu item.
         */
        private static final ImageView imageUnindex = new ImageView("/images/img_unindex.png");

        /**
         * Image shown on the Add To Index menu item.
         */
        private static final ImageView imageAddToPlaylist = new ImageView("/images/img_playlist_add.png");

        /**
         * Image shown on the Create New Playlist menu item.
         */
        private static final ImageView imageCreateNewPlaylist = new ImageView("/images/img_playlist_add.png");

        /**
         * Images shown on the Remove From Playlist menu item.
         */
        private static final ImageView imageRemoveFromPlaylist = new ImageView("/images/img_playlist_remove.png");

        /**
         * Reference to the main window instance.
         */
        private final MainWindowController window;

        /*
            Resizes images to fit the context menu.
         */
        static {
            set16x16(imageAdd);
            set16x16(imageEdit);
            set16x16(imageDelete);
            set16x16(imageUnindex);
            set16x16(imageAddToPlaylist);
            set16x16(imageCreateNewPlaylist);
            set16x16(imageRemoveFromPlaylist);
        }

        /**
         * Initializes menu items.
         */
        TableContextMenu(MainWindowController mainWindowController) {
            this.window = mainWindowController;

            MenuItem add = new MenuItem("Add New Entry", imageAdd);
            add.setOnAction(this::onAdd);

            MenuItem edit = new MenuItem("Edit", imageEdit);
            edit.setOnAction(this::onEdit);

            MenuItem delete = new MenuItem("Delete", imageDelete);
            delete.setOnAction(this::onDelete);

            MenuItem unindex = new MenuItem("Unindex", imageUnindex);
            unindex.setOnAction(this::onUnindex);

            Menu playlist = new Menu("Add to Playlist", imageAddToPlaylist);

            MenuItem removeFromPlaylist = new MenuItem("Remove from Playlist", imageRemoveFromPlaylist);
            removeFromPlaylist.setOnAction(this::onRemoveFromPlaylist);

            MenuItem newPlaylist = new MenuItem("Create New Playlist", imageCreateNewPlaylist);
            newPlaylist.setOnAction(event -> PlaylistCreatorWindow.present());

            //Add menu items
            this.getItems().addAll(
                    add,
                    new SeparatorMenuItem(),
                    playlist,
                    new SeparatorMenuItem(),
                    edit,
                    delete,
                    unindex
            );

            //Playlist integration
            this.setOnShowing(event -> {
                if (mainWindowController.getNavigator().getSelectedPlaylist() != null) {
                    this.getItems().add(3, removeFromPlaylist);
                } else {
                    if (this.getItems().get(3) == removeFromPlaylist)
                        this.getItems().remove(3);
                }

                playlist.getItems().clear();
                playlist.getItems().addAll(newPlaylist, new SeparatorMenuItem());

                for (String playlistName : App.getInstance().getILibrary().playlists().getPlaylistNames()) {
                    MenuItem item = new MenuItem(playlistName, getNewPlaylistImage());
                    item.setOnAction(event1 -> {
                        try {
                            App.getInstance().getILibrary().playlists().getPlaylist(playlistName).add(
                                    mainWindowController.getTable().getSelectedEntry().getEntry()
                            );
                        } catch (IPlaylists.PlaylistException e) {
                            AlertUtil.showErrorDialog(
                                    "Failed to add entry to playlist.", e.getClass().getName()
                            );
                        }
                    });

                    playlist.getItems().add(item);
                }
            });
        }

        /**
         * Called when the "Add" menu item is pressed.
         *
         * @param e action event.
         */
        private void onAdd(ActionEvent e) {
            window.getActionBar().addEntry();
        }

        /**
         * Called when the "Edit" menu item is pressed.
         *
         * @param e action event.
         */
        private void onEdit(ActionEvent e) {
            window.getActionBar().editSelectedEntry();
        }

        /**
         * Called when the "Delete" menu item is pressed.
         *
         * @param e action event.
         */
        private void onDelete(ActionEvent e) {
            window.getActionBar().deleteSelectedEntry();
        }

        /**
         * Called when the "Unindex" menu item is pressed.
         *
         * @param e action event.
         */
        private void onUnindex(ActionEvent e) {
            window.getActionBar().unindexSelectedEntry();
        }

        /**
         * Called when the "Remove from Playlist" menu item is pressed.
         *
         * @param event action event.
         */
        private void onRemoveFromPlaylist(ActionEvent event) {
            try {
                window.getNavigator().getSelectedPlaylist().remove(
                        window.getTable().getSelectedEntry().getEntry()
                );
            } catch (IPlaylists.PlaylistException.PlaylistWriteException e) {
                AlertUtil.showErrorDialog("Failed delete entry from playlist.", e.getClass().getName());
            }
        }

        /**
         * Sets a given image view to 16x16 pixels.
         *
         * @param image the given image view.
         */
        private static void set16x16(ImageView image) {
            image.setFitHeight(16);
            image.setFitWidth(16);
        }

        /**
         * @return a new {@link ImageView} that displays
         * the playlist image.
         */
        private static ImageView getNewPlaylistImage() {
            ImageView image = new ImageView("/images/img_playlist.png");
            set16x16(image);
            return image;
        }
    }
}
