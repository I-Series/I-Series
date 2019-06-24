package org.lmelaia.iseries.fx.main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class TableController implements SubControl {

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
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
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
        this.table.getSelectionModel().selectedItemProperty().addListener(this::onItemSelected);

        App.getInstance().getILibrary().linkTable(table);

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
        TableStateStorer.load(table);
        table.sort();
    }

    /**
     * @return The selected entry in the table.
     * CAN BE NULL.
     */
    protected ITableEntry getSelectedEntry() {
        return this.selectedEntry;
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

        if (selectedEntry != null)
            window.controlBar.enableModificationButtons();
        else
            window.controlBar.disableModificationButtons();
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
            if (!SAVE_FILE.exists()) {
                for (TableColumn column : Columns.DEFAULTS) {
                    tableView.getColumns().add((TableColumn<ITableEntry, ITableEntry>) column);
                }
            }

            try {
                FileReader reader = new FileReader(SAVE_FILE);

                StringBuilder contents = new StringBuilder();
                int chr;
                while ((chr = reader.read()) != -1)
                    contents.append((char) chr);

                JsonArray array = new Gson().fromJson(contents.toString(), JsonObject.class)
                        .get("array").getAsJsonArray();

                array.forEach((jsonElement -> {
                    JsonObject jColumn = jsonElement.getAsJsonObject();

                    TableColumn<ITableEntry, ITableEntry> column
                            = Columns.get(jColumn.get("value").getAsString());

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

                    if (jColumn.has("width"))
                        //noinspection deprecation
                        column.impl_setWidth(jColumn.get("width").getAsDouble());

                    tableView.getColumns().add(column);
                }));
            } catch (IOException e) {
                LOG.error("Failed to read table.json", e);
            }
        }

    }
}
