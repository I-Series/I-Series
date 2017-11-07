/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lmelaia.iseries;

import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.AppLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Enum list of all application settings.
 * <p>
 * Settings are read from and saved to file
 * at the appropriate times without intervention.
 */
public enum Settings {

    LAUNCHER_PING_FREQUENCY("launcher_ping_frequency", 1000,
            "The frequency in milliseconds between pings to the launcher.");

    /**
     * List of registered callbacks.
     */
    private final List<SettingChangeListener> listeners = new ArrayList<>();

    /**
     * Identifying name of the setting.
     */
    public final String idn;

    /**
     * Description of the setting.
     */
    public final String description;

    /**
     * The value of the setting.
     */
    private String value;

    /**
     * Initializes the {@link SettingsStore}
     * and read the settings from file.
     */
    static {
        SettingsStore.read();
    }

    /**
     * Constructor.
     *
     * @param idn         the identifying name of the setting.
     * @param def         the default value.
     * @param description the description of the setting.
     */
    Settings(String idn, Object def, String description) {
        this.idn = idn;
        this.value = String.valueOf(def);
        this.description = description;
    }

    /**
     * @return the current value of the setting.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the current value of the setting as an {@link Integer}.
     * @throws NumberFormatException if the value does not contain a
     *                               parsable integer.
     */
    public int getValueAsInt() {
        return Integer.parseInt(value);
    }

    /**
     * @return the current value of the setting as a {@link Boolean}.
     */
    public boolean getValueAsBoolean() {
        return Boolean.parseBoolean(value);
    }

    /**
     * @return the current value of the setting as a {@link Long}.
     * @throws NumberFormatException if the value does not contain a
     *                               parsable long.
     */
    public long getValueAsLong() {
        return Long.parseLong(value);
    }

    /**
     * Adds a change listener to the setting which is
     * called when the value changes.
     *
     * @param listener the change listener to add.
     */
    public void addChangeListener(SettingChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Changes the current value of the setting
     * and notifies all registered change listeners.
     *
     * @param newVal the new value of the setting.
     */
    public void changeValue(Object newVal) {
        this.value = String.valueOf(newVal);
        notifyChange();
    }

    /**
     * Notifies all registered change listeners
     * of a change.
     */
    private void notifyChange() {
        for (SettingChangeListener listener : listeners) {
            listener.onValueChanged(this, value);
        }
    }

    /**
     * Handles the loading and saving of application settings.
     */
    private static class SettingsStore {

        /**
         * Logger instance.
         */
        private static final Logger LOG = AppLogger.getLogger();

        /**
         * The file the settings are saved to.
         */
        private static final File SAVE_FILE = new File("savedata/settings.xml");

        /**
         * The properties instance.
         */
        private static final Properties store = new Properties();

        /**
         * Loads the settings from file and registers a
         * exit hook for saving settings.
         */
        static {
            if (SAVE_FILE.exists()) {
                try {
                    store.loadFromXML(new FileInputStream(SAVE_FILE));
                } catch (IOException e) {
                    LOG.error("Failed to load settings", e);
                }
            } else {
                try {
                    SAVE_FILE.getParentFile().mkdirs();
                    SAVE_FILE.createNewFile();
                    LOG.info("New settings file created");
                } catch (IOException e) {
                    LOG.warn("Failed to create settings file", e);
                }
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                for (Settings setting : Settings.values()) {
                    store.put(setting.idn, setting.getValue());
                }

                try {
                    store.storeToXML(new FileOutputStream(SAVE_FILE), "I-Series settings");
                } catch (IOException e) {
                    LOG.error("Could not save settings", e);
                }
            }));
        }

        /**
         * Reads the saved settings from file and writes them
         * to the {@link Settings} class.
         */
        static void read() {
            for (Settings setting : Settings.values()) {
                Object val = store.get(setting.idn);
                if (val != null)
                    setting.changeValue(val);
            }
        }
    }

    /**
     * Callback for changes to the value of a {@link Settings}.
     */
    public interface SettingChangeListener {

        /**
         * Called when the value of a setting is changed.
         *
         * @param setting  the changed setting.
         * @param newValue the settings new value.
         */
        void onValueChanged(Settings setting, Object newValue);
    }
}