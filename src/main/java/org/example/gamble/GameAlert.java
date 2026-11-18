package org.example.gamble;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

class GameAlert extends Alert {
    GameAlert(Window owner, String title, String header, String text, ButtonType... buttons) {
        super(AlertType.NONE, text, buttons);
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);
        setTitle(title);
        setHeaderText(header);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(GameAlert.class.getResource("gamble.css").toExternalForm());
        pane.getStyleClass().add("game-dialog");
        for (ButtonType button : buttons) {
            String style = button.getButtonData().isCancelButton() ? "pay-button" : "start-button";
            pane.lookupButton(button).getStyleClass().add(style);
        }
    }
}
