package org.lmelaia.iseries.updater;

import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;

public class App extends AppBase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void start() {
        AppLogger.getLogger().info("Updater started!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMessengerTimeout() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean initializeFX() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXThreadName() {
        return "Updater";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXWindowsClassPath() {
        return "org.lmelaia.iseries.updater.fx.";
    }
}
