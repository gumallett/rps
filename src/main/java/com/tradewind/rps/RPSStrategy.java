package com.tradewind.rps;

public class RPSStrategy {
    private final String strategyName = "ROCK Only";

    public String getStrategyName() {
        return strategyName;
    }

    public RPSMove playGame(GameId gameId) {
        return RPSMove.ROCK;
    }
}
