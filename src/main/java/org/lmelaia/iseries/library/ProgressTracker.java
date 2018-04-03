package org.lmelaia.iseries.library;

/**
 * Used by library classes to track the progress of a file
 * operation such as moving or deleting a folder.
 * <p>
 * <p>
 * This class provides two methods to respond to
 * progress changes {@link #onProgressChange(double, int, int)}
 * and {@link #onCompletion()}.
 */
public abstract class ProgressTracker {

    /**
     * The total amount of work.
     */
    private int max;

    /**
     * The amount of work done from 0
     * to {@link #max}.
     */
    private int position;

    /**
     * Default constructor.
     */
    public ProgressTracker() {
    }

    /**
     * Sets the maximum amount of work
     * to be done.
     *
     * @param max Maximum amount of work
     *            to be done.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Sets the amount of work done from
     * {@code 0} to {@link #max}
     *
     * @param position The amount of work done.
     */
    public void setPosition(int position) {
        this.position = position;
        onProgressChange(((double) position / max), position, max);
    }

    /**
     * Increments the amount of work
     * done by {@code 1}.
     */
    public void increment() {
        setPosition(this.position + 1);
    }

    /**
     * Sets the amount of work done to
     * {@link #max} and calls the
     * {@link #onCompletion()} method.
     */
    public void complete() {
        if (max == 0)
            max = 1;

        setPosition(max);
        onCompletion();
    }

    /**
     * Called whenever the progress changes.
     *
     * @param percentage the progress in percentage from
     *                   {@code 0.0} or {@code 0%} to {@code 1.0} or {@code 100%}.
     * @param pos        the amount of work done.
     * @param max        the maximum amount of work to be done.
     */
    public abstract void onProgressChange(double percentage, int pos, int max);

    /**
     * Called when the operation has completed.
     */
    public abstract void onCompletion();

    /**
     * @return A new progress tracker that does nothing.
     */
    public static ProgressTracker getUnboundTracker() {
        return new ProgressTracker() {
            @Override
            public void onProgressChange(double percentage, int pos, int max) {
                //NO-OP
            }

            @Override
            public void onCompletion() {
                //NO-OP
            }
        };
    }
}
