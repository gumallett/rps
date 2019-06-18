package com.tradewind.rps;

import java.util.Objects;
import java.util.UUID;

public class GameId {
    private UUID gameId = UUID.randomUUID();

    public UUID getGameId() {
        return this.gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId1 = (GameId) o;
        return Objects.equals(gameId, gameId1.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }

    @Override
    public String toString() {
        return "GameId{" +
                "gameId=" + gameId +
                '}';
    }
}
