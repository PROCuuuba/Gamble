package org.example.gamble;

import java.util.Random;

public abstract class Handler {
    private final Handler processor;
    private final Random random = new Random();

    public Handler(Handler processor) {
        this.processor = processor;
    }

    protected boolean matches() {
        return random.nextInt(3) == 0;
    }

    public RoundResult process() {
        if (processor == null)
            return null;

        return processor.process();
    }
}
