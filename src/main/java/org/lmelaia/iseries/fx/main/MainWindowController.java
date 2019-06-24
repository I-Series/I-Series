package org.lmelaia.iseries.fx.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.ilibrary.ITableEntry;

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
    private Button controlButtonUnindex;

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

    //*******
    //* END *
    //*******

    /**
     * Handles the Toolbar part of the main
     * window.
     */
    protected ActionBarControl controlBar;

    /**
     * Handles the media player part of the main
     * window.
     */
    protected MediaPlayerControl mediaPlayer;

    /**
     * Handles the navigator part of the main
     * window.
     */
    protected NavigatorControl navigator;

    /**
     * Handles the menu bar part of the main
     * window.
     */
    protected MenuBarControl menuBar;

    /**
     * Handles the table in the main window.
     */
    protected TableController tableController;

    /**
     * Initializes sub components.
     */
    @Override
    public void init() {
        //Consider initialing from a list
        controlBar = new ActionBarControl(this, new Object[]{
                controlButtonAdd, controlButtonEdit, controlButtonDelete, controlInputField, controlButtonUnindex
        });

        this.mediaPlayer = new MediaPlayerControl(this, new Object[]{
                mediaButtonPrevious, mediaButtonBack, mediaButtonPlay, mediaButtonForward,
                mediaButtonNext, mediaButtonEnlarge, mediaSliderVolume
        });

        navigator = new NavigatorControl(this, new Object[]{
                navDisplayPane, navButtonNavigator, navButtonInformation, navButtonEpisodes
        });

        this.menuBar = new MenuBarControl(this, new Object[]{
                menuItemQuit, menuItemSettings, menuItemRestart, menuItemAbout
        });

        this.tableController = new TableController(this, entryTable);

        controlBar.init();
        mediaPlayer.init();
        navigator.init();
        menuBar.init();
        tableController.init();
    }

    /**
     * Saves the state (user changed)
     * of each sub-controller.
     */
    protected void saveState() {
        controlBar.saveState();
        mediaPlayer.saveState();
        navigator.saveState();
        menuBar.saveState();
        tableController.saveState();
    }

    /**
     * Loads the state (user changed)
     * of each sub-controller.
     */
    protected void loadState() {
        controlBar.loadState();
        mediaPlayer.loadState();
        navigator.loadState();
        menuBar.loadState();
        tableController.loadState();
    }
}
