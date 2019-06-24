package org.lmelaia.iseries.fx.main;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;

/**
 * Sub-controller class for handling the media player
 * part of the main window.
 */
class MediaPlayerControl implements SubControl {

    /**
     * The main windows controller class instance.
     */
    private final MainWindowController window;

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
    public MediaPlayerControl(MainWindowController window, Object[] comps) {
        this.window = window;
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
    public void init() {
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
    public void saveState() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState() {

    }

    /**
     * @return the volume slider control.
     */
    public Slider getVolumeSlider() {
        return volume;
    }
}