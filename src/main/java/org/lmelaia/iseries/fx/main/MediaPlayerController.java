package org.lmelaia.iseries.fx.main;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import org.lmelaia.iseries.Settings;

/**
 * Sub-controller class for handling the media player
 * part of the main window.
 */
public class MediaPlayerController extends SubControl {

    /**
     * Media player previous media button
     */
    private final Button previous;

    /**
     * Media player rewind button.
     */
    private final Button back;

    /**
     * Media player play/pause button.
     */
    private final Button play;

    /**
     * Media player fast forward button.
     */
    private final Button forward;

    /**
     * Media player next media button.
     */
    private final Button next;

    /**
     * Media player full-screen button.
     */
    private final Button enlarge;

    /**
     * Media player volume slider.
     */
    private final Slider volume;

    /**
     * Constructor.
     *
     * @param window the main windows controller class instance.
     * @param comps  array of components this class handles.
     */
    MediaPlayerController(MainWindowController window, Object[] comps) {
        super(window);
        this.previous = (Button) comps[0];
        this.back = (Button) comps[1];
        this.play = (Button) comps[2];
        this.forward = (Button) comps[3];
        this.next = (Button) comps[4];
        this.enlarge = (Button) comps[5];
        this.volume = (Slider) comps[6];
    }

    /**
     * Sets up this classes components.
     */
    @Override
    void init() {
        previous.setDisable(true);
        back.setDisable(true);
        play.setDisable(true);
        forward.setDisable(true);
        next.setDisable(true);
        enlarge.setDisable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void saveState() {
        Settings.MEDIA_PLAYER_VOLUME.changeValue(volume.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void loadState() {
        volume.setValue(Settings.MEDIA_PLAYER_VOLUME.getValueAsDouble());
    }
}