package org.lmelaia.iseries.fx.main;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.fx.util.ControlUtil;
import org.lmelaia.iseries.ilibrary.ITableEntry;
import org.lmelaia.iseries.ilibrary.TableEntryFilter;

/**
 * Switchable navigator (library navigator &
 * playlists) component that is added to the
 * main window.
 */
class NavigatorComponent extends StackPane {

    /**
     * The context menu given when right-clicking
     * the navigator tree.
     */
    private final NavigatorContextMenu contextMenu;

    /**
     * Navigator control instance.
     */
    private final NavigatorControl parent;

    /**
     * Navigator tree view (library).
     */
    private TreeView<String> navigatorTree;

    /**
     * Node that holds the default
     * navigations.
     */
    private TreeItem<String> library;

    /**
     * Node that holds the list of user
     * added playlists.
     */
    private TreeItem<String> playlists;

    /**
     * Constructor.
     *
     * @param parent Navigator control instance.
     */
    public NavigatorComponent(NavigatorControl parent) {
        this.parent = parent;

        library = new TreeItem<>("Library");
        playlists = new TreeItem<>("Playlists");

        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);
        //noinspection unchecked
        root.getChildren().addAll(library, playlists);

        navigatorTree = new TreeView<>(root);
        navigatorTree.setShowRoot(false);

        contextMenu = new NavigatorContextMenu(this);
    }

    /**
     * Sets up the component properties & children.
     */
    protected void init() {
        library.setGraphic(ControlUtil.getImageView("/images/img_library_24.png"));

        ImageView imagePlaylists = new ImageView("/images/img_playlists.png");
        imagePlaylists.setFitWidth(20);
        imagePlaylists.setFitWidth(20);
        playlists.setGraphic(imagePlaylists);

        navigatorTree.getSelectionModel().selectFirst();

        // Anchor pane constraints
        AnchorPane.setRightAnchor(navigatorTree, 0D);
        AnchorPane.setLeftAnchor(navigatorTree, 0D);
        AnchorPane.setTopAnchor(navigatorTree, 0D);
        AnchorPane.setBottomAnchor(navigatorTree, 0D);

        //Root Node
        library.setExpanded(true);

        library.expandedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue)
                library.setExpanded(true);
        }));

        //Playlists node
        playlists.setExpanded(true);

        playlists.expandedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue)
                playlists.setExpanded(true);
        }));

        //Selection handling
        navigatorTree.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == library || newValue == playlists) {

                if (oldValue == library || oldValue == playlists)
                    Platform.runLater(() -> navigatorTree.getSelectionModel().clearSelection());

                Platform.runLater(() -> navigatorTree.getSelectionModel().select(oldValue));
                return;
            }

            onItemSelected(observable, oldValue, newValue);
        }));

        //Context menu
        this.navigatorTree.setContextMenu(contextMenu);

        this.getChildren().add(navigatorTree);
        initDefaults();
    }

    /**
     * Initializes the default navigation tree nodes.
     */
    private void initDefaults() {
        for (DefaultNavigations navigation : DefaultNavigations.values()) {
            library.getChildren().add(navigation.getTreeItem());
        }
    }

    /**
     * Called when a new node/item in the tree is selected.
     *
     * @param observable The {@code ObservableValue} which value changed.
     * @param oldValue   The old value before the selection was made.
     * @param newValue   The new value selected.
     */
    private void onItemSelected(ObservableValue<? extends TreeItem> observable,
                                TreeItem oldValue, TreeItem newValue) {
        if (newValue instanceof NavigatorTreeItem) {
            NavigatorTreeItem treeItem = (NavigatorTreeItem) newValue;

            App.getInstance().getILibrary().setFilter(treeItem.getFilter());
        }
    }

    /**
     * The context menu given when right-clicking the navigator tree.
     */
    private static class NavigatorContextMenu extends ContextMenu {

        /**
         * Opens the new playlist creator window.
         */
        private static final MenuItem add = new MenuItem("Add New Playlist");

        /**
         * Deletes the selected playlist/library category.
         */
        private static final MenuItem delete = new MenuItem("Delete Selected");

        /**
         * Hides the selected playlist/library category.
         */
        private static final MenuItem hide = new MenuItem("Hide Selected");

        /**
         * Shows any hidden playlists/library categories.
         */
        private static final CheckMenuItem showHidden = new CheckMenuItem("Show Hidden");

        /**
         * Constructs the context menu.
         *
         * @param navigator the navigator component instance.
         */
        public NavigatorContextMenu(NavigatorComponent navigator) {
            super(
                    add, new SeparatorMenuItem(), delete, hide, new SeparatorMenuItem(), showHidden
            );

            this.setOnShowing((value) -> {
                if (navigator.navigatorTree.getSelectionModel().getSelectedItem() != null) {

                    Object si = navigator.navigatorTree.getSelectionModel().getSelectedItem();
                    //Just make sure we have the right class.
                    if (!(si instanceof NavigatorTreeItem))
                        return;

                    NavigatorTreeItem selectedItem = (NavigatorTreeItem) si;

                    if (selectedItem.getParent() == navigator.library) {

                        //Hard code the All item to NOT be hidable
                        if (selectedItem == DefaultNavigations.ALL.getTreeItem()) {
                            hide.setDisable(true);
                        } else {
                            hide.setDisable(false);
                        }

                        delete.setDisable(true);
                    } else if (selectedItem.getParent() == navigator.playlists) {
                        delete.setDisable(false);
                        hide.setDisable(false);
                    }
                }
            });
        }
    }

    /**
     * List of default (always accessible) navigation/filter
     * options.
     */
    protected enum DefaultNavigations implements TableEntryFilter {

        /**
         * The default DefaultNavigation. Always shows all items.
         */
        ALL("All", new ImageView("/images/img_all.png")),

        /**
         * A special default navigation for testing.
         */
        TEST("Test", new ImageView("/images/img_edit.png")) {
            @Override
            public boolean accept(ITableEntry entry) {
                return entry.getName().contains("aa");
            }
        };

        /**
         * The text to display on the node.
         */
        public final String text;

        /**
         * The graphic (icon/picture) displayed on the node.
         */
        public final Node graphic;

        /**
         * The TreeItem representing this object in the NavigatorTree.
         */
        private NavigatorTreeItem treeItem;

        /**
         * Constructor.
         *
         * @param text    The text to display on the node.
         * @param graphic The graphic (icon/picture) displayed on the node.
         */
        DefaultNavigations(String text, Node graphic) {
            this.text = text;
            this.graphic = graphic;
        }

        /**
         * @return A TreeItem that will represent this object
         * in the TreeView. The returned object won't change between calls
         * (is it constant).
         */
        public NavigatorTreeItem getTreeItem() {
            if (treeItem == null) {
                if (graphic instanceof ImageView) {
                    ((ImageView) graphic).setFitWidth(25);
                    ((ImageView) graphic).setFitHeight(25);
                }

                treeItem = new NavigatorTreeItem(this.text, this, graphic);
            }


            return treeItem;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Default filter. Accepts everything
         *
         * @param entry the ITableEntry to check.
         * @return {@code true}.
         */
        @Override
        public boolean accept(ITableEntry entry) {
            return true;
        }
    }

    /**
     * A TreeItem with a {@link TableEntryFilter}. A NavigatorTreeItem
     * is used to represent a filter in the TreeView.
     */
    private static class NavigatorTreeItem extends TreeItem<String> {

        /**
         * The filter linked to this TreeItem.
         */
        private final TableEntryFilter filter;

        /**
         * Constructor.
         *
         * @param text    The text to display on the node.
         * @param filter  The filter linked to this TreeItem.
         * @param graphic The graphic to display on the node.
         */
        public NavigatorTreeItem(String text, TableEntryFilter filter, Node graphic) {
            super(text, graphic);
            this.filter = filter;
        }

        /**
         * @return The {@link TableEntryFilter} linked to this
         * specific NavigatorTreeItem.
         */
        public TableEntryFilter getFilter() {
            return this.filter;
        }
    }
}
