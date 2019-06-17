package com.tradewind.rps;

import java.util.Objects;
import java.util.UUID;

public class EventInfo {
    private long timestampInMS;
    private UUID eventID;
    private EventType eventType;
    private UUID establishNewGameEventId;
    private UUID playerInfoID;

    public long getTimestampInMS() {
        return timestampInMS;
    }

    public UUID getEventID() {
        return eventID;
    }

    public EventType getEventType() {
        return eventType;
    }

    public UUID getEstablishNewGameEventId() {
        return establishNewGameEventId;
    }

    public UUID getPlayerInfoID() {
        return playerInfoID;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventInfo eventInfo = (EventInfo) o;
        return eventID.equals(eventInfo.eventID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID);
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "timestampInMS=" + timestampInMS +
                ", eventID=" + eventID +
                ", eventType=" + eventType +
                ", establishNewGameEventId=" + establishNewGameEventId +
                ", playerInfoID=" + playerInfoID +
                '}';
    }
}
