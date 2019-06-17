package com.tradewind.rps;

/**
 * A minimal interface that describes a player.
 */
public interface IPlayer {
    /**
     * Sets the players starting money balance
     * @param balance The starting value
     */
    void setStartingAccountBalance(int balance);

    /**
     * Sets the RPS strategy used by the player
     */
    void setRPSStrategy(RPSStrategy strategy);
}
