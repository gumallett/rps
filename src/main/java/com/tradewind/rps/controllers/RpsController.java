package com.tradewind.rps.controllers;

import com.tradewind.rps.*;
import com.tradewind.rps.service.CasinoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RpsController {
    private static final Logger LOG = LoggerFactory.getLogger(RpsApplication.class);

    @Autowired
    private Simulator simulator;

    @Autowired
    private Dealer dealer;

    @Autowired
    private CasinoServiceImpl casinoService;

    @RequestMapping(value = "/simulations", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int simulations(@RequestBody SimulationInput input) {
        return this.simulator.simulate(input);
    }

    @RequestMapping(value = "/casino/balance", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int casinoBalance() {
        return casinoService.getBalance();
    }

    @RequestMapping(value = "/dealer/players", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void newPlayer(@RequestBody PlayerInfo playerInfo) {
        this.dealer.newPlayer(playerInfo);
    }

    @RequestMapping(value = "/dealer/events", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void newEvent(@RequestBody EventInfo eventInfo) {
        this.dealer.newEvent(eventInfo);
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Player> players() {
        return this.dealer.getCurrentPlayers();
    }
}
