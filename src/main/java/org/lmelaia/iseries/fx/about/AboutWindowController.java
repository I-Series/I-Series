package org.lmelaia.iseries.fx.about;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.FilePathConstants;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller class for the about window.
 */
public class AboutWindowController extends FXController {

    /**
     * URL to the GNU GPL v3 official page.
     */
    private static final String LICENSE_URL = "https://www.gnu.org/licenses/gpl-3.0.en.html";

    /**
     * Logger for this class.
     */
    private static final Logger LOG = AppLogger.getLogger();

    @FXML
    Button btnClose;

    @FXML
    private Button btnAcknowledgements;

    @FXML
    private Label labelVersion;

    @FXML
    private Label labelInfo;

    @FXML
    private Hyperlink linkLicense;

    @FXML
    private Hyperlink linkWebsite;

    /**
     * {@inheritDoc}
     * <p>
     * Implements {@code onAction} events for the components.
     * and sets the displayed version number.
     */
    @Override
    public void init() {
        btnClose.setOnAction(this::onBtnClose);
        linkLicense.setOnAction(this::onLinkLicense);
        linkWebsite.setOnAction(this::onLinkWebsite);
        btnAcknowledgements.setOnAction(this::onAcknowledgements);
        this.getWindow().focusedProperty().addListener(this::onFocusLost);

        labelVersion.setText(App.VERSION);
    }

    /**
     * Closes the window.
     */
    private void onBtnClose(ActionEvent e) {
        this.getWindow().hide();
    }

    /**
     * Opens the license.txt file if it can be found,
     * otherwise it opens the license in the users
     * browser using the official site.
     */
    private void onLinkLicense(ActionEvent e) {
        if (!FilePathConstants.APPLICATION_LICENSE_PATH.exists()) {
            try {
                Desktop.getDesktop().browse(new URI(LICENSE_URL));
            } catch (IOException | URISyntaxException e1) {
                LOG.error("Failed to open GNU GLP V3 URL.", e1);
            }
        }

        try {
            Desktop.getDesktop().open(FilePathConstants.APPLICATION_LICENSE_PATH);
        } catch (Exception e1) {
            try {
                Desktop.getDesktop().browse(new URI(LICENSE_URL));
            } catch (Exception e2) {
                LOG.error("Failed to open GNU GLP V3 URL.", e2);
            }
        }
    }

    /**
     * Opens the official website in the users
     * browser.
     */
    private void onLinkWebsite(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://i-series.github.io/"));
        } catch (Exception e3) {
            LOG.error("Failed to open website URL", e3);
        }
    }

    /**
     * Opens the acknowledgements.txt file.
     */
    private void onAcknowledgements(ActionEvent e) {
        try {
            Desktop.getDesktop().open(FilePathConstants.APPLICATION_ACKNOWLEDGEMENTS_PATH);
        } catch (Exception ex) {
            LOG.error("Failed to open acknowledgements.txt file.", ex);
        }
    }

    /**
     * Provides the close on focus lost mechanism.
     */
    private void onFocusLost(Observable e) {
        if (!this.getWindow().isFocused()) {
            this.getWindow().hide();
        }
    }
}
