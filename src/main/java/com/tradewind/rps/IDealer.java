package com.tradewind.rps;

import java.util.Collection;

/**
 * Dealer interface
 */
public interface IDealer {

    /**
     * Registers a new event with the dealer
     * @param info The event information
     */
    void newEvent(EventInfo info);

    /**
     * Registers a new player with the dealer
     * @param playerInfo The player information
     */
    void newPlayer(PlayerInfo playerInfo);

    /**
     * Gets a collection of players currently known to the dealer
     */
    Collection<Player> getCurrentPlayers();
}
