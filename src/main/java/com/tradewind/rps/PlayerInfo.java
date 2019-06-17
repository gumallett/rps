package com.tradewind.rps;

import java.util.Objects;
import java.util.UUID;

public class PlayerInfo {
    private UUID playerInfoId;
    private int startingAccountBalance;

    public UUID getPlayerInfoId() {
        return playerInfoId;
    }

    public int getStartingAccountBalance() {
        return startingAccountBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfo that = (PlayerInfo) o;
        return playerInfoId.equals(that.playerInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerInfoId);
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "playerInfoId=" + playerInfoId +
                ", startingAccountBalance=" + startingAccountBalance +
                '}';
    }
}
