/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.build.izpack;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;

/**
 * A builder object which provides the means to create IzPack configurations.
 *
 * @author Luke Melaia
 */
public class IzPackConfigurationBuilder {

    //Info
    protected String applicationName;

    protected String applicationVersion;

    protected List<Author> authors = new ArrayList<>();

    protected String url;

    protected boolean writeUninstaller;

    protected String uninstallerName;

    protected boolean requiresJdk;

    protected boolean writeInstallInfo;

    protected boolean usePack200Compression;

    protected boolean privileged;

    //Gui preferences
    protected int guiHeight;

    protected boolean resizable;

    protected int guiWidth;

    protected boolean useFlags;

    protected String languageDisplayType;

    //Language
    protected List<Language> languages = new ArrayList<>();

    //Shortcuts
    protected String shortcutSpec;

    protected String unixShortcutSpec;

    //Logos
    protected String logoFile;

    protected String sideLogoFile;

    //Panels
    protected HelloPanel helloPanel;

    protected InfoPanel infoPanel;

    protected LicencePanel licencePanel;

    protected PacksPanel packsPanel;

    private TargetPanel targetPanel;

    private FinishPanel finishPanel;

    //Packs
    protected List<Pack> packs = new ArrayList<>();

    //Shortcuts
    private String groupStructure;

    private Location location = Location.APPLICATIONS;

    private boolean preSelectDesktopShortcuts = false;

    private boolean makeCurrentUserDefault = false;

    protected List<Shortcut> shortcuts = new ArrayList<>();

    /**
     * A constructs a blank configuration builder.S
     */
    public IzPackConfigurationBuilder() {
    }

    //Info
    /**
     * Sets the applications name.
     *
     * @param name The name of the application that will be installed.
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setApplicationName(String name) {
        this.applicationName = validateString(name);
        return this;
    }

    /**
     * Sets the applications version.
     *
     * @param version The version of the application that will be installed.
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setApplicationVersion(String version) {
        this.applicationVersion = validateString(version);
        return this;
    }

    /**
     * Adds an author to the list.
     *
     * @param author The author to be added to the list.
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder addAuthor(Author author) {
        this.authors.add(requireNonNull(author));
        return this;
    }

    /**
     * Removes an author from the list.
     *
     * @param author The author to be removed from the list.
     *
     * @return {@code true} if the author was removed, {@code false} otherwise.
     */
    public boolean removeAuthor(Author author) {
        return this.authors.remove(author);
    }

    /**
     * Sets the URL of the applications web site.
     *
     * @param url the applications web site url
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setUrl(String url) {
        this.url = validateString(url);
        return this;
    }

    /**
     * Sets whether or not an uninstaller is written.
     *
     * @param b {@code true} to write installer.
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder writeUninstaller(boolean b) {
        this.writeUninstaller = b;
        return this;
    }

    /**
     * Sets the uninstallers file name.
     *
     * @param name The name of the uninstaller
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder uninstallerName(String name) {
        this.uninstallerName = validateString(name);
        return this;
    }

    /**
     * Sets whether or not the application requires the java JDK to be
     * installed.
     *
     * @param b {@code true} if the JDK is required.
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setRequiresJdk(boolean b) {
        this.requiresJdk = b;
        return this;
    }

    /**
     * Sets whether or not the installer writes a log.
     *
     * @param b {@code true} to write the log
     *
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setWriteInstallInfo(boolean b) {
        this.writeInstallInfo = b;
        return this;
    }

    /**
     * Sets whether or not pack 200 compression is used.
     * 
     * @param b {@code true} to use pack 200 compression.
     * 
     * @return {@code this} 
     */
    public IzPackConfigurationBuilder setUsePack200Compression(boolean b) {
        this.usePack200Compression = b;
        return this;
    }

    /**
     * Sets whether or not the installer is run with administrator privileges.
     * 
     * @param b {@code true} to run as administrator.
     * 
     * @return {@code this}
     */
    public IzPackConfigurationBuilder setPrivileged(boolean b) {
        this.privileged = b;
        return this;
    }

    //Gui preferences
    public IzPackConfigurationBuilder setGuiHeight(int height) {
        this.guiHeight = height;
        return this;
    }

    public IzPackConfigurationBuilder setResizable(boolean b) {
        this.resizable = b;
        return this;
    }

    public IzPackConfigurationBuilder setGuiWidth(int width) {
        this.guiWidth = width;
        return this;
    }

    public IzPackConfigurationBuilder setUseFlags(boolean b) {
        this.useFlags = b;
        return this;
    }

    public IzPackConfigurationBuilder setLanguageDisplayType(String type) {
        this.languageDisplayType = validateString(type);
        return this;
    }

    public IzPackConfigurationBuilder addLanguage(Language lang) {
        this.languages.add(requireNonNull(lang));
        return this;
    }

    public boolean removeLanguage(Language lang) {
        return this.languages.remove(requireNonNull(lang));
    }

    public IzPackConfigurationBuilder setLogoFile(String logoFile) {
        this.logoFile = validateString(logoFile);
        return this;
    }

    public IzPackConfigurationBuilder setSideLogoFile(String sideLogoFile) {
        this.sideLogoFile = validateString(sideLogoFile);
        return this;
    }

    public IzPackConfigurationBuilder setHelloPanel(HelloPanel helloPanel) {
        this.helloPanel = requireNonNull(helloPanel);
        return this;
    }

    public IzPackConfigurationBuilder setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = requireNonNull(infoPanel);
        return this;
    }

    public IzPackConfigurationBuilder setLicencePanel(
            LicencePanel licencePanel) {
        this.licencePanel = requireNonNull(licencePanel);
        return this;
    }

    public IzPackConfigurationBuilder setPacksPanel(PacksPanel packsPanel) {
        this.packsPanel = requireNonNull(packsPanel);
        return this;
    }

    public IzPackConfigurationBuilder setTargetPanel(TargetPanel targetPanel) {
        this.targetPanel = requireNonNull(targetPanel);
        return this;
    }

    public IzPackConfigurationBuilder setFinishPanel(FinishPanel finishPanel) {
        this.finishPanel = requireNonNull(finishPanel);
        return this;
    }

    public IzPackConfigurationBuilder addPack(Pack pack) {
        this.packs.add(requireNonNull(pack));
        return this;
    }

    public boolean removePack(Pack pack) {
        return this.packs.remove(requireNonNull(pack));
    }

    public IzPackConfigurationBuilder setGroupStructure(String groupStructure) {
        this.groupStructure = validateString(groupStructure);
        return this;
    }

    public IzPackConfigurationBuilder setLocation(Location location) {
        this.location = requireNonNull(location);
        return this;
    }

    public IzPackConfigurationBuilder
            setPreSelectDesktopShortcuts(boolean preSelectDesktopShortcuts) {
        this.preSelectDesktopShortcuts = preSelectDesktopShortcuts;
        return this;
    }

    public IzPackConfigurationBuilder
            setMakeCurrentUserDefault(boolean makeCurrentUserDefault) {
        this.makeCurrentUserDefault = makeCurrentUserDefault;
        return this;
    }

    public IzPackConfigurationBuilder addShortcut(Shortcut shortcut) {
        this.shortcuts.add(requireNonNull(shortcut));
        return this;
    }

    public boolean removeShortcut(Shortcut shortcut) {
        return this.shortcuts.remove(requireNonNull(shortcut));
    }

    public IzPackConfiguration create() {
        return new IzPackConfiguration(this);
    }

    private String validateString(String s) {
        if (requireNonNull(s).equals("")) {
            throw new IllegalArgumentException("String cannot be empty");
        }

        return s;
    }
}
