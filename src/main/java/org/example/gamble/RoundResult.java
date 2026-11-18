package org.example.gamble;

public record RoundResult(Outcome outcome, boolean replay) {
    public enum Outcome {
        WIN, FREE_ROUND, LOSS
    }
}
