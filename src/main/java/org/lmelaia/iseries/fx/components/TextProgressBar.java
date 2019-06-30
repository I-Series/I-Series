package org.lmelaia.iseries.fx.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * A StackPane that emulates a ProgressBar capable
 * of overlaying text.
 */
public class TextProgressBar extends StackPane {

    /**
     * Default padding on the text.
     */
    private static final int DEFAULT_LABEL_PADDING = 3;

    /**
     * The actual progress of the ProgressIndicator. A negative value for
     * progress indicates that the progress is indeterminate. A positive value
     * between 0 and 1 indicates the percentage of progress where 0 is 0% and 1
     * is 100%. Any value greater than 1 is interpreted as 100%.
     */
    private DoubleProperty progress;

    public final DoubleProperty progressProperty() {
        if (progress == null) {
            progress = new DoublePropertyBase(-1.0) {
                @Override
                protected void invalidated() {
                    backingProgressBar.setProgress(backingProgressBar.getProgress());
                }

                @Override
                public Object getBean() {
                    return TextProgressBar.this;
                }

                @Override
                public String getName() {
                    return "progress";
                }
            };
        }
        return progress;
    }

    /**
     * The actual text overlaid atop the progress bar.
     */
    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new StringPropertyBase("") {
                @Override
                protected void invalidated() {
                    backingText.setText("");
                }

                @Override
                public Object getBean() {
                    return TextProgressBar.this;
                }

                @Override
                public String getName() {
                    return "text";
                }
            };
        }
        return text;
    }

    /**
     * The backing progress bar used to display a progress bar.
     */
    private final ProgressBar backingProgressBar = new ProgressBar();

    /**
     * The text object used to display text atop the progress bar.
     */
    private final Text backingText = new Text();

    /**
     * Constructs a new TextProgressBar with the given initial
     * progress and text.
     *
     * @param initialProgress the progress of the progress bar.
     * @param initialText     the text displayed atop the progress bar.
     */
    public TextProgressBar(double initialProgress, String initialText) {
        textProperty().setValue(initialText);
        progressProperty().setValue(initialProgress);

        syncProgress();
        progressProperty().addListener((observableValue, number, number2) -> syncProgress());
        textProperty().addListener((observable -> syncProgress()));

        backingProgressBar.setMaxWidth(Double.MAX_VALUE);

        getChildren().setAll(backingProgressBar, backingText);
    }

    /**
     * Sets the new text to display atop the progress bar.
     *
     * @param text the new text to display.
     */
    public void setText(String text) {
        this.textProperty().setValue(text);
    }

    /**
     * Sets the new progress of the progress bar.
     *
     * @param progress the progress of the progress bar (0.0-1.0)
     */
    public void setProgress(double progress) {
        this.progressProperty().setValue(progress);
    }

    /**
     * Synchronises the values (progress & text) in this class
     * with the backing progress bar and text.
     */
    private void syncProgress() {
        backingText.setText(textProperty().getValue());
        backingProgressBar.setProgress(progressProperty().doubleValue());

        backingProgressBar.setMinHeight(backingText.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
        backingProgressBar.setMinWidth(backingText.getBoundsInLocal().getWidth() + DEFAULT_LABEL_PADDING * 2);
    }
}
