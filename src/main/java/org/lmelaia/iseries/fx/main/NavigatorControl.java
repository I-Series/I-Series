package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.lmelaia.iseries.fx.util.ControlUtil;

/**
 * Sub-control class that handles the navigator
 * part of the main window.
 */
class NavigatorControl implements SubControl {

    /**
     * Main window controller class instance.
     */
    private final MainWindowController window;

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

    /**
     * Navigator tree view (library).
     */
    private TreeView<String> navigatorTree;

    /**
     * Navigator tree view root node.
     */
    private TreeItem<String> navTreeRoot;

    /**
     * Constructor.
     *
     * @param window the main window controller class instance.
     * @param comps  list of components this controller class handles.
     */
    protected NavigatorControl(MainWindowController window, Object[] comps) {
        this.window = window;
        this.displayPane = (AnchorPane) comps[0];
        this.navigator = (Button) comps[1];
        this.information = (Button) comps[2];
        this.episodes = (Button) comps[3];
        this.statistics = (Button) comps[4];

        navTreeRoot = new TreeItem<>("Library");
        navigatorTree = new TreeView<>(navTreeRoot);
    }

    /**
     * Sets up this classes components.
     */
    @Override
    public void init() {
        navTreeRoot.setGraphic(ControlUtil.getImageView("/images/img_library_24.png"));
        navigatorTree.getSelectionModel().selectFirst();

        AnchorPane.setRightAnchor(navigatorTree, 0D);
        AnchorPane.setLeftAnchor(navigatorTree, 0D);
        AnchorPane.setTopAnchor(navigatorTree, 0D);
        AnchorPane.setBottomAnchor(navigatorTree, 0D);

        navigator.setOnAction(this::onNavigatorBtnPress);
        information.setOnAction(this::onInformationBtnPress);
        episodes.setOnAction(this::onEpisodesBtnPress);
        statistics.setOnAction(this::onStatisticsBtnPress);

        changeToNavigator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveState() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState() {

    }

    /**
     * Switches to the navigator tab (tree view).
     */
    public void changeToNavigator() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(navigator, "lightblue");
        displayPane.getChildren().clear();
        displayPane.getChildren().add(navigatorTree);
    }

    /**
     * Switches to the information tab.
     */
    public void changeToInformation() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(information, "lightblue");
        displayPane.getChildren().clear();
    }

    /**
     * Switches to the episodes tab.
     */
    public void changeToEpisodes() {
        resetNavButtons();
        ControlUtil.setBackgroundColor(episodes, "lightblue");
        displayPane.getChildren().clear();
    }

    /**
     * Switches to the statistics tab.
     */
    public void changeToStatistics() {
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
}