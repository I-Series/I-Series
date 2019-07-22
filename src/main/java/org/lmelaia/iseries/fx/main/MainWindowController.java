package org.lmelaia.iseries.fx.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.ilibrary.ITableEntry;

/**
 * The controller class for the main window.
 * <p/>
 * Due to the size and complexity of the main window.
 * It is split up into sub classes that each handle
 * a small portion of the window.
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
    private Button controlButtonUnindex;

    @FXML
    private TextField controlInputField;

    @FXML
    private Button controlBtnClearSearch;

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
    private Button navButtonStatistics;

    @FXML
    private AnchorPane navDisplayPane;

    //Window controls and table

    @FXML
    protected SplitPane splitPaneVertical;

    @FXML
    protected SplitPane splitPaneHorizontal;

    @FXML
    private TableView<ITableEntry> entryTable;

    //Menu Bar

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private MenuItem menuItemSettings;

    @FXML
    private MenuItem menuItemRestart;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private MenuItem menuItemTray;

    @FXML
    private MenuItem menuItemMinimize;

    @FXML
    private MenuItem menuItemQuitDialog;

    @FXML
    private MenuItem menuItemChangeLibrary;

    @FXML
    private MenuItem menuItemAddEntry;

    @FXML
    private MenuItem menuItemEditEntry;

    @FXML
    private MenuItem menuItemDeleteEntry;

    @FXML
    private MenuItem menuItemUnindexEntry;

    // OTHER

    @FXML
    private HBox hBoxProgress;

    //*******
    //* END *
    //*******

    /**
     * Handles the Toolbar part of the main
     * window.
     */
    private ActionBarController actionBar;

    /**
     * Handles the media player part of the main
     * window.
     */
    private MediaPlayerController mediaPlayer;

    /**
     * Handles the navigator part of the main
     * window.
     */
    private NavigatorController navigator;

    /**
     * Handles the menu bar part of the main
     * window.
     */
    private MenuBarController menuBar;

    /**
     * Handles the table in the main window.
     */
    private TableController table;

    /**
     * Handles the progress bar.
     */
    private ProgressController progress;

    /**
     * List of all the sub controls. Used to
     * save/load/init the controls in a loop.
     */
    private SubControl[] subControls;

    /**
     * Initializes sub components.
     */
    @Override
    public void init() {
        subControls = new SubControl[]{
                //Init sub controls within the list to easily group them.
                actionBar = new ActionBarController(this, new Object[]{
                        controlButtonAdd, controlButtonEdit, controlButtonDelete, controlInputField,
                        controlButtonUnindex, controlBtnClearSearch
                }),
                mediaPlayer = new MediaPlayerController(this, new Object[]{
                        mediaButtonPrevious, mediaButtonBack, mediaButtonPlay, mediaButtonForward,
                        mediaButtonNext, mediaButtonEnlarge, mediaSliderVolume
                }),
                navigator = new NavigatorController(this, new Object[]{
                        navDisplayPane, navButtonNavigator, navButtonInformation, navButtonEpisodes,
                        navButtonStatistics
                }),
                menuBar = new MenuBarController(this, new Object[]{
                        menuItemQuit, menuItemSettings, menuItemRestart, menuItemAbout, menuItemTray,
                        menuItemMinimize, menuItemQuitDialog, menuItemChangeLibrary, menuItemAddEntry,
                        menuItemEditEntry, menuItemDeleteEntry, menuItemUnindexEntry
                }),
                table = new TableController(this, entryTable),
                progress = new ProgressController(this, hBoxProgress)
        };

        for (SubControl control : subControls)
            control.init();
    }

    /**
     * Saves the state (user changed)
     * of each sub-controller.
     */
    protected void saveState() {
        for (SubControl control : subControls)
            control.saveState();
    }

    /**
     * Loads the state (user changed)
     * of each sub-controller.
     */
    protected void loadState() {
        for (SubControl control : subControls)
            control.loadState();
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * @return the {@link ActionBarController} sub control instance.
     */
    public ActionBarController getActionBar() {
        return actionBar;
    }

    /**
     * @return the {@link MediaPlayerController} sub control instance.
     */
    public MediaPlayerController getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * @return the {@link ActionBarController} sub control instance.
     */
    public NavigatorController getNavigator() {
        return navigator;
    }

    /**
     * @return the {@link MenuBarController} sub control instance.
     */
    public MenuBarController getMenuBar() {
        return menuBar;
    }

    /**
     * @return the {@link TableController} sub control instance.
     */
    public TableController getTable() {
        return table;
    }

    /**
     * @return the {@link ProgressController} sub control instance.
     */
    public ProgressController getProgress() {
        return progress;
    }

}
