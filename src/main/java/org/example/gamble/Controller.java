package org.example.gamble;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackPane coin;
    @FXML
    private HBox bags;
    @FXML
    private Button firstBag;
    @FXML
    private Button secondBag;
    @FXML
    private Button thirdBag;
    @FXML
    private Button payButton;
    @FXML
    private Button startButton;
    @FXML
    private ToggleButton soundButton;
    @FXML
    private Label playerName;
    @FXML
    private Label balance;
    @FXML
    private Label roundCount;
    @FXML
    private Label status;

    private final Player player1 = new Player("Игрок", 0);
    private ActionChain action = null;
    private TranslateTransition coinBounce;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstBag.setOnAction(this::onBag);
        secondBag.setOnAction(this::onBag);
        thirdBag.setOnAction(this::onBag);
        coinBounce = new TranslateTransition(Duration.millis(125), coin);
        coinBounce.setFromY(0);
        coinBounce.setToY(-16);
        coinBounce.setCycleCount(2);
        coinBounce.setAutoReverse(true);
        playerName.setText(player1.getName());
        updateView("Внесите монету, чтобы начать игру.");
    }

    @FXML
    private void onSoundToggle() {
        boolean enabled = soundButton.isSelected();
        GameSound.setEnabled(enabled);
        soundButton.setText(enabled ? "Звук: вкл." : "Звук: выкл.");
    }

    @FXML
    public void onPay(ActionEvent event) {
        player1.addNumber(1);
        updateView("Монета внесена. Нажмите «Начать игру».");
        coinBounce.playFromStart();
        GameSound.COIN.play();
    }

    @FXML
    public void onStart(ActionEvent event) {
        if (action != null || !init())
            return;

        action = new ActionChain(player1, startButton.getScene().getWindow());
        showRound("Выберите один из трёх мешочков.");
    }

    private void onBag(ActionEvent event) {
        if (action == null || bags.isDisabled())
            return;

        bags.setDisable(true);
        status.setText("Открываем мешочек…");
        GameSound.DRAW.play();

        Button chosen = (Button) event.getSource();
        Node graphic = chosen.getGraphic();
        Timeline reveal = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(graphic.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(70), new KeyValue(graphic.rotateProperty(), -10)),
                new KeyFrame(Duration.millis(145), new KeyValue(graphic.rotateProperty(), 9)),
                new KeyFrame(Duration.millis(215), new KeyValue(graphic.rotateProperty(), -4)),
                new KeyFrame(Duration.millis(285), new KeyValue(graphic.rotateProperty(), 0))
        );
        Window owner = chosen.getScene().getWindow();
        reveal.setOnFinished(finished -> Platform.runLater(() -> {
            if (owner.isShowing())
                finishRound();
        }));
        reveal.play();
    }

    private void finishRound() {
        RoundResult result = action.process();
        if (result.replay()) {
            if (result.outcome() == RoundResult.Outcome.FREE_ROUND)
                showRound("Бесплатный раунд. Выберите мешочек.");
            else if (init())
                showRound("Новый раунд. Выберите мешочек.");
        } else {
            String message = "Игра окончена. Можно начать заново.";
            if (result.outcome() == RoundResult.Outcome.WIN)
                message = "Монеты выданы: " + player1.takeNumber() + ". Можно начать заново.";

            action = null;
            updateView(message);
        }
    }

    private void showRound(String message) {
        bags.setDisable(false);
        updateView(message);
        bags.requestFocus();

        Duration duration = Duration.millis(220);
        FadeTransition fade = new FadeTransition(duration, bags);
        fade.setFromValue(0);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(duration, bags);
        slide.setFromY(10);
        slide.setToY(0);
        new ParallelTransition(fade, slide).play();
    }

    public boolean init() {
        if (player1.pay(1))
            return true;

        action = null;
        updateView("На балансе нет монет. Внесите монету.");
        GameAlert alert = new GameAlert(payButton.getScene().getWindow(), "Нужна монета",
                "На балансе нет монет", "Внесите одну монету, чтобы сыграть.", ButtonType.OK);
        alert.showAndWait();
        payButton.requestFocus();
        return false;
    }

    private void updateView(String message) {
        boolean playing = action != null;
        bags.setVisible(playing);
        bags.setManaged(playing);
        coin.setVisible(!playing);
        coin.setManaged(!playing);
        payButton.setDisable(playing);
        startButton.setDisable(playing);
        balance.setText(player1.getNumber().toString());
        roundCount.setText(player1.getCount().toString());
        status.setText(message);
    }
}
