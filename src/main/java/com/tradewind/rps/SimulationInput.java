package com.tradewind.rps;

import java.util.List;
import java.util.Set;

public class SimulationInput {
    private Set<PlayerInfo> players;
    private List<EventInfo> events;

    public Set<PlayerInfo> getPlayers() {
        return players;
    }

    public List<EventInfo> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "SimulationInput{" +
                "players=" + players +
                ", events=" + events +
                '}';
    }
}
