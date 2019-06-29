package org.lmelaia.iseries.ilibrary;

/**
 * Allows a registree to be notified as to when
 * the global Library & ILibrary are initialized
 * and can be used. See
 * {@link org.lmelaia.iseries.App#addLibraryInitListener(LibraryInitializedListener)}
 * & {@link org.lmelaia.iseries.App#removeLibraryInitListener(LibraryInitializedListener)}
 * for registering & unregistering listeners.
 */
public interface LibraryInitializedListener {

    /**
     * Called when both the global Library & ILibrary
     * are initialized and usable.
     *
     * @param library the global ILibrary instance.
     */
    void onInitialized(ILibrary library);
}
