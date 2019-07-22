package org.lmelaia.iseries.fx.main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.lmelaia.iseries.fx.components.TextProgressBar;

/**
 * Handles the progress bar on the main window.
 */
public class ProgressController extends SubControl {

    /**
     * Container.
     */
    private final HBox container;

    /**
     * Progress bar.
     */
    private final TextProgressBar progressBar;

    /**
     * Constructor.
     *
     * @param container container.
     */
    ProgressController(MainWindowController window, HBox container) {
        super(window);
        this.container = container;
        progressBar = new TextProgressBar(0, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void init() {
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressBar.setMaxHeight(5);

        this.container.getChildren().add(progressBar);
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Updates the progress bar on the main window
     * with the given progress and text.
     *
     * @param progress the given progress (0.0-1.0)
     * @param text     the progress bar text. {@code null}
     *                 will not change the text.
     */
    public void updateProgress(double progress, String text) {
        if (text != null)
            progressBar.setText(text);

        progressBar.setProgress(progress);
    }

    /**
     * Updates the progress bar on the main window
     * with the given progress.
     *
     * @param progress the given progress (0.0-1.0)
     */
    public void updateProgress(double progress) {
        updateProgress(progress, null);
    }

    /**
     * Updates the progress bar on the main window
     * with the text.
     *
     * @param text the progress bar text. {@code null}
     *             will clear the text.
     */
    public void updateProgress(String text) {
        progressBar.setText((text == null) ? "" : text);
    }

    /**
     * @return the TextProgressBar on the main window.
     */
    public TextProgressBar getProgressBar() {
        return this.progressBar;
    }
}
