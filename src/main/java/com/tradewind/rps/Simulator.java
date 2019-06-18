package com.tradewind.rps;

import com.tradewind.rps.service.CasinoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Simulator {
    @Autowired
    private Dealer dealer;

    @Autowired
    private CasinoServiceImpl casinoService;

    public int simulate(SimulationInput input) {
        for (EventInfo info : input.getEvents()) {
            dealer.newEvent(info);
        }

        for (PlayerInfo playerInfo : input.getPlayers()) {
            dealer.newPlayer(playerInfo);
        }

        return casinoService.getBalance();
    }
}
