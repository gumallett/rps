package com.tradewind.rps;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements IPlayer {
    private final AtomicInteger balance = new AtomicInteger(0);
    private final RPSStrategy strategy;
    private final UUID playerId;

    public Player(PlayerInfo info, RPSStrategy strategy) {
        this.strategy = strategy;
        this.playerId = info.getPlayerInfoId();
        this.setStartingAccountBalance(info.getStartingAccountBalance());
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public RPSStrategy getStrategy() {
        return this.strategy;
    }

    public int getBalance() {
        return this.balance.get();
    }

    public int updateBalance(int change) {
        return balance.updateAndGet(prev -> prev + change);
    }

    @Override
    public void setStartingAccountBalance(int balance) {
        this.balance.addAndGet(balance);
    }

    @Override
    public void setRPSStrategy(RPSStrategy strategy) {
        // this.strategy = strategy;
    }

    @Override
    public String toString() {
        return "Player{" +
                "balance=" + balance +
                ", strategy=" + strategy +
                ", playerId=" + playerId +
                '}';
    }
}
