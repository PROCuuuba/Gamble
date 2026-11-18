package org.example.gamble;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class NegativeHandler extends Handler {
    private final Window owner;

    public NegativeHandler(Handler processor, Window owner) {
        super(processor);
        this.owner = owner;
    }

    @Override
    public RoundResult process() {
        if (!matches())
            return super.process();

        return showLoss(owner);
    }

    static RoundResult showLoss(Window owner) {
        ButtonType replay = new ButtonType("Продолжить играть", ButtonBar.ButtonData.YES);
        ButtonType vacation = new ButtonType("Отдохнуть", ButtonBar.ButtonData.CANCEL_CLOSE);
        GameAlert alert = new GameAlert(owner, "Проигрыш", "В мешочке нет монет",
                "Хотите сыграть ещё раз?", replay, vacation);
        GameSound.LOSS.play();
        boolean playAgain = alert.showAndWait().orElse(vacation) == replay;
        return new RoundResult(RoundResult.Outcome.LOSS, playAgain);
    }
}
