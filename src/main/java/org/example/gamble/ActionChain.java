package org.example.gamble;

import javafx.stage.Window;

public class ActionChain {
    private final Handler chain;
    private final Window owner;

    public ActionChain(Player player, Window owner) {
        this.owner = owner;
        chain = buildChain(player, owner);
    }

    private Handler buildChain(Player player, Window owner) {
        return new NegativeHandler(new PositiveHandler(new ChanceHandler(null, owner), player, owner), owner);
    }

    public RoundResult process() {
        RoundResult result = chain.process();
        if (result == null)
            return NegativeHandler.showLoss(owner);

        return result;
    }
}
