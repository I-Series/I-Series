package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;

/**
 * Context menu added to the main windows table view. This context
 * menu gives shortcut actions for the select entry in the table.
 * This context menu will only be shown when right-clicking on a table entry.
 */
abstract class TableContextMenu extends ContextMenu {

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

    /*
        Resizes images to fit the context menu.
     */
    static {
        set16x16(imageAdd);
        set16x16(imageEdit);
        set16x16(imageDelete);
        set16x16(imageUnindex);
    }

    /**
     * Initializes menu items.
     */
    public TableContextMenu() {
        MenuItem add = new MenuItem("Add New Entry", imageAdd);
        add.setOnAction(this::onAdd);

        MenuItem edit = new MenuItem("Edit", imageEdit);
        edit.setOnAction(this::onEdit);

        MenuItem delete = new MenuItem("Delete", imageDelete);
        delete.setOnAction(this::onDelete);

        MenuItem unindex = new MenuItem("Unindex", imageUnindex);
        unindex.setOnAction(this::onUnindex);

        this.getItems().addAll(
                add,
                new SeparatorMenuItem(),
                edit,
                delete,
                unindex
        );
    }

    /**
     * Called when the "Add" menu item is pressed.
     *
     * @param e action event.
     */
    protected abstract void onAdd(ActionEvent e);

    /**
     * Called when the "Edit" menu item is pressed.
     *
     * @param e action event.
     */
    protected abstract void onEdit(ActionEvent e);

    /**
     * Called when the "Delete" menu item is pressed.
     *
     * @param e action event.
     */
    protected abstract void onDelete(ActionEvent e);

    /**
     * Called when the "Unindex" menu item is pressed.
     *
     * @param e action event.
     */
    protected abstract void onUnindex(ActionEvent e);

    /**
     * Sets a given image view to 16x16 pixels.
     *
     * @param image the given image view.
     */
    private static void set16x16(ImageView image) {
        image.setFitHeight(16);
        image.setFitWidth(16);
    }
}
