package com.tradewind.rps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Rps Game state
 */
public final class RpsGame {
    public static final int GAME_COST = 5;

    public static final int CAPACITY = 2;

    private final GameId gameId = new GameId();

    private final List<Player> players = new ArrayList<>();

    private final EventInfo eventInfo;

    public RpsGame(EventInfo eventInfo) {
        this.eventInfo = eventInfo;
    }

    /**
     * Tries to add a player to this game. This operation fails if the game is full,
     * or if the player does not have enough money to play.
     * @param player Player to add.
     */
    public synchronized void addPlayer(Player player) {
        if (this.players.contains(player)) {
            return;
        }

        if (this.isFull()) {
            throw new RuntimeException("Game full");
        }

        if (player.getBalance() < GAME_COST) {
            throw new RuntimeException("Insufficient funds to add player");
        }

        player.updateBalance(GAME_COST * -1);
        players.add(player);
    }

    /**
     * Returns the event responsible for establishing this game.
     */
    public EventInfo getEventInfo() {
        return this.eventInfo;
    }

    /**
     * Returns the game id for this game
     */
    public GameId getGameId() {
        return this.gameId;
    }

    /**
     * Returns the player at the specified index.
     * @param index Index of player to return
     */
    public Player getPlayer(int index) {
        return this.players.get(index);
    }

    /**
     * Returns true if the game is full.
     */
    public boolean isFull() {
        return this.numberOfPlayers() >= CAPACITY;
    }

    /**
     * Returns the number of players in this game.
     */
    public int numberOfPlayers() {
        return this.players.size();
    }

    @Override
    public String toString() {
        return "RpsGame{" +
                "gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
