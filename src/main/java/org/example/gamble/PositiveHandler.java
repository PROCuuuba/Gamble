package org.example.gamble;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class PositiveHandler extends Handler {
    private static final int WINNING = 3;
    private final Player player;
    private final Window owner;

    public PositiveHandler(Handler processor, Player player, Window owner) {
        super(processor);
        this.player = player;
        this.owner = owner;
    }

    @Override
    public RoundResult process() {
        if (!matches())
            return super.process();

        player.addNumber(WINNING);
        ButtonType replay = new ButtonType("Сыграть ещё", ButtonBar.ButtonData.YES);
        ButtonType collect = new ButtonType("Забрать монеты", ButtonBar.ButtonData.CANCEL_CLOSE);
        GameAlert alert = new GameAlert(owner, "Выигрыш", "В мешочке три монеты!",
                "Баланс после выигрыша: " + player.getNumber() + ". Можно продолжить игру или забрать монеты.",
                replay, collect);
        GameSound.WIN.play();
        boolean playAgain = alert.showAndWait().orElse(collect) == replay;
        return new RoundResult(RoundResult.Outcome.WIN, playAgain);
    }
}
