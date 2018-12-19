package org.lmelaia.iseries.fx.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.util.ControlUtil;

import static java.util.Objects.requireNonNull;

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

    //*******
    //* END *
    //*******

    protected ControlBar controlBar;

    protected MediaPlayer mediaPlayer;

    protected Navigator navigator;

    /**
     * Initializes sub components.
     */
    @Override
    public void init() {
        controlBar = new ControlBar(controlButtonAdd, controlButtonEdit, controlButtonDelete, controlInputField);
        controlBar.init();

        mediaPlayer = new MediaPlayer(mediaSliderSeek, mediaSliderVolume, mediaButtonPrevious, mediaButtonBack,
                mediaButtonPlay, mediaButtonForward, mediaButtonNext, mediaButtonEnlarge);
        mediaPlayer.init();

        navigator = new Navigator(navButtonNavigator, navButtonInformation, navButtonEpisodes, navDisplayPane);
        navigator.init();
    }

    /**
     * Closes the application.
     *
     * @param e quit menu item action event.
     */
    @FXML
    protected void onMenuQuit(ActionEvent e) {
        App.getInstance().exit(ExitCode.NORMAL);
    }

    /**
     * Sub class for controlling the main window control bar.
     */
    protected static class ControlBar implements SubControl {

        /**
         * Add entry button.
         */
        protected Button controlButtonAdd;

        /**
         * Edit entry button.
         */
        protected Button controlButtonEdit;

        /**
         * Delete entry button.
         */
        protected Button controlButtonDelete;

        /**
         * Search bar.
         */
        protected TextField controlInputField;

        /**
         * Initializes components.
         *
         * @param add
         * @param edit
         * @param delete
         * @param input
         */
        @SuppressWarnings("JavaDoc")
        private ControlBar(Button add, Button edit, Button delete, TextField input) {
            controlButtonAdd = requireNonNull(add, "ControlButtonAdd=null");
            controlButtonEdit = requireNonNull(edit, "ControlButtonEdit=null");
            controlButtonDelete = requireNonNull(delete, "ControlButtonDelete=null");
            controlInputField = requireNonNull(input, "ControlInputField=null");
        }

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
    protected static class MediaPlayer implements SubControl {

        /**
         * Media window seek slider.
         */
        protected Slider mediaSliderSeek;

        /**
         * Media window volume bar.
         */
        protected Slider mediaSliderVolume;

        /**
         * Previous file/episode media button.
         */
        protected Button mediaButtonPrevious;

        /**
         * Rewind media button.
         */
        protected Button mediaButtonBack;

        /**
         * Play/pause media button.
         */
        protected Button mediaButtonPlay;

        /**
         * Media fast forward button.
         */
        protected Button mediaButtonForward;

        /**
         * Next file/episode media button.
         */
        protected Button mediaButtonNext;

        /**
         * Media button full screen.
         */
        protected Button mediaButtonEnlarge;

        /**
         * Initializes components.
         *
         * @param seek
         * @param volume
         * @param previous
         * @param back
         * @param play
         * @param forward
         * @param next
         * @param enlarge
         */
        @SuppressWarnings("JavaDoc")
        private MediaPlayer(Slider seek, Slider volume, Button previous, Button back, Button play,
                            Button forward, Button next, Button enlarge) {
            this.mediaSliderSeek = requireNonNull(seek);
            this.mediaSliderVolume = requireNonNull(volume);
            this.mediaButtonPrevious = requireNonNull(previous);
            this.mediaButtonBack = requireNonNull(back);
            this.mediaButtonPlay = requireNonNull(play);
            this.mediaButtonForward = requireNonNull(forward);
            this.mediaButtonNext = requireNonNull(next);
            this.mediaButtonEnlarge = requireNonNull(enlarge);
        }

        /**
         * Sets up this classes components.
         */
        @Override
        public void init() {
            this.mediaButtonPrevious.setDisable(true);
            this.mediaButtonBack.setDisable(true);
            this.mediaButtonPlay.setDisable(true);
            this.mediaButtonForward.setDisable(true);
            this.mediaButtonNext.setDisable(true);
            this.mediaButtonEnlarge.setDisable(true);
        }
    }

    /**
     * Sub class for controlling the main windows navigator.
     */
    protected static class Navigator implements SubControl {

        /**
         * Navigator button.
         */
        private Button navButtonNavigator;

        /**
         * Information button.
         */
        private Button navButtonInformation;

        /**
         * Episodes button.
         */
        private Button navButtonEpisodes;

        /**
         * Anchor pane to which switchable components are
         * added to/removed from.
         */
        private AnchorPane navDisplayPane;

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
         *
         * @param navigator
         * @param information
         * @param episodes
         * @param display
         */
        @SuppressWarnings("JavaDoc")
        protected Navigator(Button navigator, Button information, Button episodes, AnchorPane display) {
            this.navButtonNavigator = requireNonNull(navigator);
            this.navButtonInformation = requireNonNull(information);
            this.navButtonEpisodes = requireNonNull(episodes);
            this.navDisplayPane = requireNonNull(display);

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
     * Interface for sub component controllers.
     */
    private interface SubControl {
        void init();
    }
}
