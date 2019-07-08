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
class TableController implements SubControl {

    /**
     * Logger instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

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
                    name
            });

            //Name
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            name.setContextMenu(COLUMN_CONTEXT_MENU);
            columnMap.put(name.getText(), name);
        }

        /**
         * @return a column with the given {@link TableColumn#getText()}
         */
        public static TableColumn<ITableEntry, ITableEntry> get(String name) {
            return columnMap.get(name);
        }
    }

    /**
     * The main windows controller class.
     */
    private final MainWindowController window;

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
     * The selected entry in the table.
     * CAN BE NULL.
     */
    private ITableEntry selectedEntry;

    /**
     * @param window the controller class for the main window.
     * @param table  the table to control.
     */
    public TableController(MainWindowController window, TableView<ITableEntry> table) {
        this.window = window;
        this.table = table;
    }

    /**
     * Initializes components.
     */
    @Override
    public void init() {
        this.table.getSelectionModel().selectedItemProperty().addListener(this::onItemSelected);

        Columns.COLUMN_CONTEXT_MENU.setTable(table);

        App.getInstance().addLibraryInitListener((library -> {
            library.linkTable(table);
            TableStateStorer.load(table);
            table.sort();
        }));

        contextMenu = new TableContextMenu() {

            @Override
            protected void onAdd(ActionEvent e) {
                window.controlBar.addEntry();
            }

            @Override
            protected void onEdit(ActionEvent e) {
                window.controlBar.editSelectedEntry();
            }

            @Override
            protected void onDelete(ActionEvent e) {
                window.controlBar.deleteSelectedEntry();
            }

            @Override
            protected void onUnindex(ActionEvent e) {
                window.controlBar.unindexSelectedEntry();
            }
        };

        table.setRowFactory(param -> {
            final TableRow row = new TableRow();

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

            //noinspection unchecked
            return row;
        });

        table.setPlaceholder(new Placeholder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveState() {
        TableStateStorer.save(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState() {

    }

    /**
     * @return The selected entry in the table.
     * CAN BE NULL.
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
         * Constructs the placeholder.
         */
        public Placeholder() {
            //Create entry button
            ImageView addEntryButtonImage = new ImageView("/images/img_add.png");
            addEntryButtonImage.setFitWidth(16);
            addEntryButtonImage.setFitHeight(16);

            Button addEntryButton = new Button("", addEntryButtonImage);
            addEntryButton.setStyle("-fx-background-color: transparent;");
            addEntryButton.setOnAction((actionEvent) -> window.controlBar.addEntry());

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
            HBox addEntryRow = new HBox();
            addEntryRow.setAlignment(Pos.CENTER);
            setAnchors(addEntryRow, 70, 5, 5, 5);

            addEntryRow.getChildren().addAll(addEntryText, addEntryButton);

            //
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
}
