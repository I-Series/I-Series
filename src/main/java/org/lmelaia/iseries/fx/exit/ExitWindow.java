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
package org.lmelaia.iseries.fx.exit;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.FXWindowsManager;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;

/**
 * Main window close confirmation dialog.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/exit_window.fxml",
        cssFileName = "windows/exit_window.css",
        controllerClass = ExitWindowController.class
)
public class ExitWindow extends FXWindow<ExitWindowController> {

    /**
     * Sets the window properties.
     */
    @Override
    protected void onInitialization() {
        this.setResizable(false);
        this.getIcons().add(new Image("/images/iseries-32.png"));
    }

    /**
     * NO-OP
     */
    @Override
    protected void onPostInitialization() {
    }

    /**
     * Calls {@link Stage#showAndWait()} on this window
     * and returns the result from the window.
     *
     * @return The users choice given to the dialog.
     */
    public static Result present() {
        ExitWindow dialog = FXWindowsManager.getInstance().getWindow(ExitWindow.class);
        dialog.showAndWait();

        return new Result(
                dialog.controller.result,
                dialog.controller.remember()
        );
    }

    /**
     * Possible options the user can choose from
     * this window.
     */
    public enum ResultOption {
        CANCEL(0), QUIT(1), TRAY(2);

        /**
         * Integer value that identifies each enum.
         */
        public final int value;

        ResultOption(int val) {
            this.value = val;
        }

        /**
         * @param val enums integer value.
         * @return the result option with the same
         * value as the one given. Returns
         * ResultOption.CANCEL if the value is
         * invalid.
         */
        public static ResultOption get(int val) {
            switch (val) {
                case 1:
                    return QUIT;
                case 2:
                    return TRAY;
                default:
                    return CANCEL;
            }
        }
    }

    /**
     * The users choice when presented with this dialog.
     */
    public static class Result {

        /**
         * Option selected.
         */
        private final ResultOption option;

        /**
         * Should the users choice be remembered.
         */
        private final boolean remember;

        private Result(ResultOption option, boolean remember) {
            this.option = option;
            this.remember = remember;
        }

        /**
         * Option selected.
         */
        public ResultOption getOption() {
            return option;
        }

        /**
         * Should the users choice be remembered.
         */
        public boolean remember() {
            return remember;
        }
    }
}
