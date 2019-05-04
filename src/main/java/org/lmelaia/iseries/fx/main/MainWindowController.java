package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.entry_dialog.EntryDialog;
import org.lmelaia.iseries.fx.settings.SettingsWindow;
import org.lmelaia.iseries.fx.util.ControlUtil;

/**
 * The controller class for the main window.
 */
public class MainWindowController extends FXController {

    //***************
    //* FX Controls *
    //***************

    //Control Bar

    @FXML
    private Button controlButtonAdd;

    @FXML
    private Button controlButtonEdit;

    @FXML
    private Button controlButtonDelete;

    @FXML
    private TextField controlInputField;

    //Media Player

    @FXML
    private Slider mediaSliderSeek;

    @FXML
    private Slider mediaSliderVolume;

    @FXML
    private Button mediaButtonPrevious;

    @FXML
    private Button mediaButtonBack;

    @FXML
    private Button mediaButtonPlay;

    @FXML
    private Button mediaButtonForward;

    @FXML
    private Button mediaButtonNext;

    @FXML
    private Button mediaButtonEnlarge;

    //Navigator

    @FXML
    private Button navButtonNavigator;

    @FXML
    private Button navButtonInformation;

    @FXML
    private Button navButtonEpisodes;

    @FXML
    private AnchorPane navDisplayPane;

    //Window controls and table

    @FXML
    protected SplitPane splitPaneVertical;

    @FXML
    protected SplitPane splitPaneHorizontal;

    @FXML
    private TreeTableView entryTable;

    //Menu Bar

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private MenuItem menuItemSettings;

    @FXML
    private MenuItem menuItemRestart;

    //*******
    //* END *
    //*******

    protected final ControlBar controlBar = new ControlBar();

    protected final MediaPlayer mediaPlayer = new MediaPlayer();

    protected final Navigator navigator = new Navigator();

    protected final MenuBar menuBar = new MenuBar();

    /**
     * Initializes sub components.
     */
    @Override
    public void init() {
        controlBar.init();
        mediaPlayer.init();
        navigator.init();
        menuBar.init();
    }

    /**
     * Sub class for controlling the main window control bar.
     */
    protected class ControlBar implements SubControl {

        /**
         * Sets up this classes components.
         */
        @Override
        public void init() {
            controlButtonAdd.setOnAction(this::onAddPressed);
            controlButtonEdit.setOnAction(this::onEditPressed);
            controlButtonDelete.setOnAction(this::onDeletePressed);
            controlInputField.setOnAction(this::onInputEntered);

            controlButtonEdit.setDisable(true);
            controlButtonDelete.setDisable(true);
        }

        private void onAddPressed(ActionEvent e) {
            FXWindowsManager.getInstance().showWindowAndWait(EntryDialog.class, getWindow());
        }

        private void onEditPressed(ActionEvent e) {

        }

        private void onDeletePressed(ActionEvent e) {

        }

        private void onInputEntered(ActionEvent e) {

        }
    }

    /**
     * Sub class for controlling the media player window.
     */
    protected class MediaPlayer implements SubControl {

        /**
         * Sets up this classes components.
         */
        @Override
        public void init() {
            mediaButtonPrevious.setDisable(true);
            mediaButtonBack.setDisable(true);
            mediaButtonPlay.setDisable(true);
            mediaButtonForward.setDisable(true);
            mediaButtonNext.setDisable(true);
            mediaButtonEnlarge.setDisable(true);
        }

        public Slider getVolumeSlider() {
            return mediaSliderVolume;
        }
    }

    /**
     * Sub class for controlling the main windows navigator.
     */
    protected class Navigator implements SubControl {

        /**
         * Navigator tree view (library).
         */
        private TreeView<String> navigatorTree;

        /**
         * Navigator tree view root node.
         */
        private TreeItem<String> navTreeRoot;

        /**
         * Initializes components.
         */
        protected Navigator() {
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

            navButtonNavigator.setOnAction(this::onNavigatorBtnPress);
            navButtonInformation.setOnAction(this::onInformationBtnPress);
            navButtonEpisodes.setOnAction(this::onEpisodesBtnPress);

            changeToNavigator();
        }

        /**
         * Switches to the navigator tab (tree view).
         */
        public void changeToNavigator() {
            resetNavButtons();
            ControlUtil.setBackgroundColor(navButtonNavigator, "lightblue");
            navDisplayPane.getChildren().clear();
            navDisplayPane.getChildren().add(navigatorTree);
        }

        /**
         * Switches to the information tab.
         */
        public void changeToInformation() {
            resetNavButtons();
            ControlUtil.setBackgroundColor(navButtonInformation, "lightblue");
            navDisplayPane.getChildren().clear();
        }

        /**
         * Switches to the episodes tab.
         */
        public void changeToEpisodes() {
            resetNavButtons();
            ControlUtil.setBackgroundColor(navButtonEpisodes, "lightblue");
            navDisplayPane.getChildren().clear();
        }

        /**
         * Resets the navigation buttons background colors.
         */
        private void resetNavButtons() {
            String color = "rgb(240, 240, 240)";
            ControlUtil.setBackgroundColor(navButtonNavigator, color);
            ControlUtil.setBackgroundColor(navButtonInformation, color);
            ControlUtil.setBackgroundColor(navButtonEpisodes, color);
        }

        private void onNavigatorBtnPress(ActionEvent e) {
            this.changeToNavigator();
        }

        private void onInformationBtnPress(ActionEvent e) {
            this.changeToInformation();
        }

        private void onEpisodesBtnPress(ActionEvent e) {
            this.changeToEpisodes();
        }
    }

    /**
     * Controller class for the main windows menu bar.
     */
    private class MenuBar implements SubControl {

        /**
         * {@inheritDoc}
         */
        @Override
        public void init() {
            menuItemQuit.setOnAction(this::onQuit);
            menuItemSettings.setOnAction(this::onSettings);
            menuItemRestart.setOnAction(this::onRestart);
        }

        private void onQuit(ActionEvent e) {
            App.getInstance().exit(ExitCode.NORMAL);
        }

        private void onSettings(ActionEvent e) {
            App.getInstance().getWindowsManager().showWindowAndWait(SettingsWindow.class, getWindow());
        }

        private void onRestart(ActionEvent e) {
            App.getInstance().exit(ExitCode.RESTART);
        }
    }

    /**
     * Interface for sub component controllers.
     */
    private interface SubControl {

        /**
         * Init method for each sub control.
         * Called from the main controllers {@link MainWindowController#init()}
         * method.
         */
        void init();
    }
}
