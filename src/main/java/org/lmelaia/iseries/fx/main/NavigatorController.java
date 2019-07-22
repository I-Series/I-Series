package org.lmelaia.iseries.fx.main;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.fx.playlist.PlaylistCreatorWindow;
import org.lmelaia.iseries.fx.playlist.PlaylistDeleteWindow;
import org.lmelaia.iseries.fx.playlist.PlaylistEditorWindow;
import org.lmelaia.iseries.fx.playlist.PlaylistRenamerWindow;
import org.lmelaia.iseries.fx.util.AlertUtil;
import org.lmelaia.iseries.fx.util.ControlUtil;
import org.lmelaia.iseries.ilibrary.IPlaylist;
import org.lmelaia.iseries.ilibrary.IPlaylists;
import org.lmelaia.iseries.ilibrary.ITableEntry;
import org.lmelaia.iseries.ilibrary.TableEntryFilter;

/**
 * Sub-control class that handles the navigator/information/
 * episodes/statistics part of the main window.
 */
public class NavigatorController extends SubControl {

    /**
     * The navigators display pane. This is
     * what switchable components are added
     * to.
     */
    private final AnchorPane displayPane;

    /**
     * Button that switches to the navigator
     * "tab".
     */
    private final Button navigator;

    /**
     * Button that switches to the information
     * "tab".
     */
    private final Button information;

    /**
     * Button that switches to the episodes
     * "tab".
     */
    private final Button episodes;

    /**
     * Button that switches to the statistics tab.
     */
    private final Button statistics;

    // Switchable Components.

    /**
     * The switchable navigator/playlist tree component.
     */
    private final NavigatorComponent navigatorComponent;

    /**
     * Constructor.
     *
     * @param window the main window controller class instance.
     * @param comps  list of components this controller class handles.
     */
    NavigatorController(MainWindowController window, Object[] comps) {
        super(window);
        this.displayPane = (AnchorPane) comps[0];
        this.navigator = (Button) comps[1];
        this.information = (Button) comps[2];
        this.episodes = (Button) comps[3];
        this.statistics = (Button) comps[4];

        this.navigatorComponent = new NavigatorComponent();
    }

    /**
     * Sets up this classes components.
     */
    @Override
    void init() {
        navigatorComponent.init();

        AnchorPane.setRightAnchor(navigatorComponent, 0D);
        AnchorPane.setLeftAnchor(navigatorComponent, 0D);
        AnchorPane.setTopAnchor(navigatorComponent, 0D);
        AnchorPane.setBottomAnchor(navigatorComponent, 0D);

        navigator.setOnAction(this::onNavigatorBtnPress);
        information.setOnAction(this::onInformationBtnPress);
        episodes.setOnAction(this::onEpisodesBtnPress);
        statistics.setOnAction(this::onStatisticsBtnPress);

        changeToNavigator();
    }

    // *****************
    // INTERNAL WORKINGS
    // *****************

    /**
     * Switches to the navigator tab (tree view).
     */
    private void changeToNavigator() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(navigator, "lightblue");
        displayPane.getChildren().clear();
        displayPane.getChildren().add(navigatorComponent);
    }

    /**
     * Switches to the information tab.
     */
    private void changeToInformation() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(information, "lightblue");
        displayPane.getChildren().clear();
    }

    /**
     * Switches to the episodes tab.
     */
    private void changeToEpisodes() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(episodes, "lightblue");
        displayPane.getChildren().clear();
    }

    /**
     * Switches to the statistics tab.
     */
    private void changeToStatistics() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(statistics, "lightblue");
        displayPane.getChildren().clear();
    }

    /**
     * Resets the navigation buttons background colors.
     */
    private void resetNavButtons() {
        String color = "rgb(240, 240, 240)";
        ControlUtil.setBackgroundColor(navigator, color);
        ControlUtil.setBackgroundColor(information, color);
        ControlUtil.setBackgroundColor(episodes, color);
        ControlUtil.setBackgroundColor(statistics, color);
    }

    // *************
    // ACTION EVENTS
    // *************

    /**
     * Called when the navigator button is pressed.
     *
     * @param e action event.
     */
    private void onNavigatorBtnPress(ActionEvent e) {
        this.changeToNavigator();
    }

    /**
     * Called when the information button is pressed.
     *
     * @param e action event.
     */
    private void onInformationBtnPress(ActionEvent e) {
        this.changeToInformation();
    }

    /**
     * Called when the episodes button is pressed.
     *
     * @param e action event.
     */
    private void onEpisodesBtnPress(ActionEvent e) {
        this.changeToEpisodes();
    }

    /**
     * Called when the statistics button is pressed.
     *
     * @param e action event.
     */
    private void onStatisticsBtnPress(ActionEvent e) {
        this.changeToStatistics();
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * @return the playlist selected in the navigator tree. Can be
     * {@code null} if a {@link NavigatorComponent.DefaultNavigations}
     * is selected.
     */
    public IPlaylist getSelectedPlaylist() {
        return this.navigatorComponent.getSelectedPlaylist();
    }

    /**
     * Selects a playlist in the navigator with
     * the same display name as the one provided
     * and applies the table filter.
     *
     * @param name the display name of the playlist
     *             to select.
     */
    public void selectPlaylist(String name) {
        this.navigatorComponent.selectPlaylist(name);
    }

    /**
     * Selects the default library navigation (All) and
     * applies the table filter.
     */
    public void selectDefaultNavigation() {
        this.navigatorComponent.selectDefaultNavigation();
    }

    // *************************
    // NAVIGATOR COMPONENT CLASS
    // *************************

    /**
     * Switchable navigator (library navigator &
     * playlists) component that is added to the
     * main window.
     */
    private static class NavigatorComponent extends StackPane {

        /**
         * The context menu given when right-clicking
         * the navigator tree.
         */
        private final NavigatorContextMenu contextMenu;

        /**
         * Navigator tree view (library).
         */
        private final TreeView<String> navigatorTree;

        /**
         * Node that holds the default
         * navigations.
         */
        private final TreeItem<String> library;

        /**
         * Node that holds the list of user
         * added playlists.
         */
        private final TreeItem<String> playlists;

        private IPlaylist selectedPlaylist;

        /**
         * Constructor.
         */
        public NavigatorComponent() {
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
        void init() {
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

            //Library loaded
            App.getInstance().addLibraryInitListener((library) -> {
                refreshPlaylists(library.playlists().getPlaylistNames());
                library.playlists().addChangeListener((names, playlists) -> refreshPlaylists(names));

                //Select the last playlist the user clicked on
                //Or the default (All) if the above fails.
                if (Settings.LAST_PLAYLIST.getValue() == null ||
                        !selectNavigationByName(Settings.LAST_PLAYLIST.getValue())) {
                    navigatorTree.getSelectionModel().select(DefaultNavigations.ALL.getTreeItem());
                }
            });

            this.getChildren().add(navigatorTree);
            initDefaults();
        }

        // INTERNALS

        /**
         * Selects any navigation (non-root node) in the
         * navigator tree by its name.
         *
         * @param name the navigations name.
         * @return {@code true} if a navigation was found
         * and selected.
         */
        private boolean selectNavigationByName(String name) {
            for (TreeItem<String> value : library.getChildren()) {
                if (value.getValue().equals(name)) {
                    navigatorTree.getSelectionModel().select(value);
                    return true;
                }
            }

            for (TreeItem<String> value : playlists.getChildren()) {
                if (value.getValue().equals(name)) {
                    navigatorTree.getSelectionModel().select(value);
                    return true;
                }
            }

            return false;
        }

        /**
         * Refreshes the nodes in the tree to match
         * the playlist array.
         *
         * @param playlists array of all playlist names.
         */
        private void refreshPlaylists(String[] playlists) {
            this.playlists.getChildren().clear();

            for (String name : playlists) {
                this.playlists.getChildren().add(new PlaylistTreeItem(name));
            }
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
         * Change listener registered to the {@link #selectedPlaylist} that will
         * refresh the table view. The listener is unregistered when a new playlist
         * is selected.
         */
        private final IPlaylist.PlaylistChangeListener selectedPlaylistChangeListener = (playlist) -> {
            App.getInstance().getILibrary().setFilter(playlist);//Should refresh everything.
        };

        /**
         * Called when a new node/item in the tree is selected. This will
         * translate the node into a playlist item/navigation item
         * and apply the filter.
         *
         * @param observable The {@code ObservableValue} which value changed.
         * @param oldValue   The old value before the selection was made.
         * @param newValue   The new value selected.
         */
        private void onItemSelected(ObservableValue<? extends TreeItem<String>> observable,
                                    TreeItem oldValue, TreeItem newValue) {
            if (newValue == null)
                return;

            if (selectedPlaylist != null)
                selectedPlaylist.removeChangeListener(selectedPlaylistChangeListener);

            if (newValue instanceof NavigationTreeItem) {
                NavigationTreeItem treeItem = (NavigationTreeItem) newValue;
                App.getInstance().getILibrary().setFilter(treeItem.getFilter());
                this.selectedPlaylist = null;//Not a playlist
            } else if (newValue instanceof PlaylistTreeItem) {
                PlaylistTreeItem treeItem = (PlaylistTreeItem) newValue;

                try {
                    IPlaylist playlist = App.getInstance().getILibrary().playlists().getPlaylist(treeItem.getValue());

                    if (playlist == null)
                        return;

                    App.getInstance().getILibrary().setFilter(playlist);
                    playlist.addChangeListener(selectedPlaylistChangeListener);
                    this.selectedPlaylist = playlist;
                } catch (IPlaylists.PlaylistException e) {
                    AlertUtil.showErrorDialog("Failed to open selected playlist.", e.getClass().getName());
                }
            }

            Settings.LAST_PLAYLIST.changeValue(newValue.getValue());
        }

        // *********
        // Class API
        // *********

        /**
         * @return the playlist selected in the navigator tree. Can be
         * {@code null} if a {@link NavigatorComponent.DefaultNavigations}
         * is selected.
         */
        private IPlaylist getSelectedPlaylist() {
            return this.selectedPlaylist;
        }

        /**
         * Selects a playlist in the navigator with
         * the same display name as the one provided
         * and applies the table filter.
         *
         * @param name the display name of the playlist
         *             to select.
         */
        private void selectPlaylist(String name) {
            for (TreeItem<String> playlistItem : playlists.getChildren()) {
                if (playlistItem.getValue().toLowerCase().equals(name.toLowerCase()))
                    navigatorTree.getSelectionModel().select(playlistItem);
            }
        }

        /**
         * Selects the default library navigation (All) and
         * applies the table filter.
         */
        private void selectDefaultNavigation() {
            navigatorTree.getSelectionModel().select(DefaultNavigations.ALL.getTreeItem());
        }

        // **************
        // NESTED CLASSES
        // **************

        /**
         * The context menu given when right-clicking the navigator tree.
         */
        private static class NavigatorContextMenu extends ContextMenu {

            /**
             * Opens the new playlist creator window.
             */
            private static final MenuItem add = new MenuItem("Add New Playlist");

            /**
             * Opens the playlist editor window.
             */
            private static final MenuItem edit = new MenuItem("Edit Selected");

            /**
             * Opens the playlist renamer window.
             */
            private static final MenuItem rename = new MenuItem("Rename Selected");

            /**
             * Deletes the selected playlist/library category.
             */
            private static final MenuItem delete = new MenuItem("Delete Selected");

            /**
             * Reference to the navigator component.
             */
            private final NavigatorComponent navigator;

            /**
             * Constructs the context menu.
             *
             * @param navigator the navigator component instance.
             */
            public NavigatorContextMenu(NavigatorComponent navigator) {
                super(
                        add, new SeparatorMenuItem(), edit, rename, delete
                );

                this.navigator = navigator;
                this.setOnShowing(this::onShowing);

                add.setOnAction(this::onAdd);
                edit.setOnAction(this::onEdit);
                rename.setOnAction(this::onRename);
                delete.setOnAction(this::onDelete);
            }

            /**
             * Called when the context menu is opened on an item.
             * <p>
             * <p/>
             * <p>
             * Enables/Disables menu items depending on the selected
             * node.
             *
             * @param event window event.
             */
            private void onShowing(WindowEvent event) {
                if (navigator.navigatorTree.getSelectionModel().getSelectedItem() != null) {

                    Object si = navigator.navigatorTree.getSelectionModel().getSelectedItem();

                    //Just make sure we have the right class.
                    if (si instanceof NavigationTreeItem) {
                        //Library playlists
                        NavigationTreeItem selectedItem = (NavigationTreeItem) si;

                        delete.setDisable(true);
                        rename.setDisable(true);
                        edit.setDisable(true);
                    } else if (si instanceof PlaylistTreeItem) {
                        //User playlists
                        PlaylistTreeItem selectedItem = (PlaylistTreeItem) si;

                        delete.setDisable(false);
                        rename.setDisable(false);
                        edit.setDisable(false);
                    }
                }
            }

            // *************
            // Action Events
            // *************

            /**
             * Called when the add menu item is pressed.
             * Opens the playlist creator window.
             *
             * @param event action event.
             */
            private void onAdd(ActionEvent event) {
                PlaylistCreatorWindow.present();
            }

            /**
             * Called when the edit menu item is pressed.
             * Opens the playlist editor window on the
             * selected playlist.
             *
             * @param event action event.
             */
            private void onEdit(ActionEvent event) {
                try {
                    PlaylistEditorWindow.present(
                            App.getInstance().getILibrary().playlists().getPlaylist(
                                    navigator.navigatorTree.getSelectionModel().getSelectedItem().getValue()
                            )
                    );
                } catch (IPlaylists.PlaylistException.PlaylistNotFoundException |
                        IPlaylists.PlaylistException.PlaylistReadException e) {
                    AlertUtil.showErrorDialog("Failed to get selected playlist.", e.getClass().getName());
                }
            }

            /**
             * Called when the delete menu item is pressed.
             * Opens the playlist renamer window on the
             * selected playlist.
             *
             * @param event action event.
             */
            private void onRename(ActionEvent event) {
                if (navigator.navigatorTree.getSelectionModel().getSelectedItem() instanceof PlaylistTreeItem) {
                    try {
                        IPlaylist playlist = App.getInstance().getILibrary().playlists().getPlaylist(
                                navigator.navigatorTree.getSelectionModel().getSelectedItem().getValue()
                        );

                        if (playlist != null)
                            PlaylistRenamerWindow.present(playlist);
                    } catch (IPlaylists.PlaylistException e) {
                        AlertUtil.showErrorDialog(
                                "Failed to get selected playlist.", e.getClass().getName()
                        );
                    }
                }
            }

            /**
             * Called when the delete menu item is pressed.
             * Opens the playlist delete confirmation dialog on the
             * selected playlist.
             *
             * @param event action event.
             */
            private void onDelete(ActionEvent event) {
                try {
                    PlaylistDeleteWindow.present(App.getInstance().getILibrary().playlists().getPlaylist(
                            navigator.navigatorTree.getSelectionModel().getSelectedItem().getValue()
                    ));
                } catch (IPlaylists.PlaylistException.PlaylistNotFoundException |
                        IPlaylists.PlaylistException.PlaylistReadException e) {
                    AlertUtil.showErrorDialog("Failed to get selected playlist.", e.getClass().getName());
                }
            }
        }

        // *************************
        // Navigations and TreeItems
        // *************************

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
            private NavigationTreeItem treeItem;

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
            public NavigationTreeItem getTreeItem() {
                if (treeItem == null) {
                    if (graphic instanceof ImageView) {
                        ((ImageView) graphic).setFitWidth(25);
                        ((ImageView) graphic).setFitHeight(25);
                    }

                    treeItem = new NavigationTreeItem(this.text, this, graphic);
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
         * Represents a default navigation within the navigation tree.
         * Each NavigationTreeItem has a final reference to the
         * {@link TableEntryFilter} it represents.
         */
        private static class NavigationTreeItem extends TreeItem<String> {

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
            public NavigationTreeItem(String text, TableEntryFilter filter, Node graphic) {
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

        /**
         * Represents a playlist within the navigation tree. No
         * PlaylistTreeItem instances hold references to their {@link
         * TableEntryFilter}s. Rather, they must be obtained using this
         * nodes {@link PlaylistTreeItem#getValue()} method to obtain
         * the playlist name.
         */
        private static class PlaylistTreeItem extends TreeItem<String> {

            /**
             * Constructs a new PlaylistTreeItem.
             *
             * @param name the name of the playlist
             *             this TreeItem represents.
             */
            public PlaylistTreeItem(String name) {
                super(name, getIcon());
            }

            /**
             * @return the {@link ImageView} used
             * for all playlists.
             */
            private static ImageView getIcon() {
                ImageView icon = new ImageView("/images/img_playlist.png");
                icon.setFitWidth(25);
                icon.setFitHeight(25);
                return icon;
            }
        }
    }
}