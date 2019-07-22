package org.lmelaia.iseries.fx.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * A set of static utilities to help displaying
 * {@link Alert}s.
 */
public class AlertUtil {

    /**
     * Private, non-instantiable constructor.
     */
    private AlertUtil() {
    }

    /**
     * Displays a dialog indicating an error occurred
     * with the given header and context text.
     *
     * @param headerText dialog header text.
     * @param context    dialog context text.
     */
    public static void showErrorDialog(String headerText, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setGraphic(new ImageView("/images/img_error.png"));
        populate(headerText, context, alert);
    }

    /**
     * Displays a standard information dialog with
     * the given header and context text.
     *
     * @param headerText dialog header text.
     * @param context    dialog context text.
     */
    public static void showInfoDialog(String headerText, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(new ImageView("/images/img_info.png"));
        populate(headerText, context, alert);
    }

    // *********************
    // Internal Util Methods
    // *********************

    /**
     * Sets the icon, title, header text and
     * context text of the given dialog.
     *
     * @param headerText the dialog header text.
     * @param context    the dialog context text.
     * @param alert      the dialog itself.
     */
    private static void populate(String headerText, String context, Alert alert) {
        alert.setTitle("I-Series");
        alert.setHeaderText(headerText);
        alert.setContentText(context);
        setIcon(alert);
        alert.showAndWait();
    }

    /**
     * Sets the icon of the given dialog to
     * the I-Series logo.
     *
     * @param alert the given dialog.
     */
    private static void setIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/images/iseries-16.png"));
    }
}
