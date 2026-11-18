package org.example.gamble;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class ChanceHandler extends Handler {
    private final Window owner;

    public ChanceHandler(Handler processor, Window owner) {
        super(processor);
        this.owner = owner;
    }

    @Override
    public RoundResult process() {
        if (!matches())
            return super.process();

        ButtonType replay = new ButtonType("Сыграть бесплатно", ButtonBar.ButtonData.YES);
        ButtonType vacation = new ButtonType("Отдохнуть", ButtonBar.ButtonData.CANCEL_CLOSE);
        GameAlert alert = new GameAlert(owner, "Бесплатный шанс", "Следующий раунд бесплатный",
                "В мешочке нет монет, но можно выбрать ещё раз.", replay, vacation);
        GameSound.CHANCE.play();
        boolean playAgain = alert.showAndWait().orElse(vacation) == replay;
        return new RoundResult(RoundResult.Outcome.FREE_ROUND, playAgain);
    }
}
