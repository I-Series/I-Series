package org.lmelaia.iseries.fx.main;

import javafx.scene.control.*;

/**
 * Context menu that allows enabling/disabling
 * a given array of TableColumns.
 */
class TableColumnContentMenu extends ContextMenu {

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
