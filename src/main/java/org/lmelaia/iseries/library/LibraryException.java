package org.lmelaia.iseries.library;

/**
 * Base class for all checked exceptions thrown
 * by a library or related class.
 */
public abstract class LibraryException extends Exception {

    /**
     * Default constructor.
     *
     * @param message Display message.
     * @param cause   The cause for the exception
     *                being thrown.
     */
    LibraryException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Thrown when an entry cannot be modified (added, edited, removed)
     * on file.
     */
    public static class EntryModificationException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        EntryModificationException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when an entry cannot be read from file.
     */
    public static class EntryFetchException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        EntryFetchException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when a new library cannot be created.
     */
    public static class LibraryCreationException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        LibraryCreationException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when a library cannot be read from file.
     */
    public static class LibraryFetchException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        LibraryFetchException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when a file UID does not exists and an operation
     * on the file is attempted.
     */
    public static class FileUIDNotFoundException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        FileUIDNotFoundException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when a file cannot be modified (moved, copied, deleted, rewritten).
     */
    public static class FileModificationException extends LibraryException {

        /**
         * Default constructor.
         *
         * @param message Display message.
         * @param cause   The cause for the exception
         *                being thrown.
         */
        FileModificationException(String message, Exception cause) {
            super(message, cause);
        }
    }
}
