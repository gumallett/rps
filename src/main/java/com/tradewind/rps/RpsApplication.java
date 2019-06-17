package com.tradewind.rps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RpsApplication {

	private static final Logger LOG = LoggerFactory.getLogger(RpsApplication.class);

	@Autowired
	private Simulator simulator;

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	@RequestMapping(value = "/simulations", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int simulations(@RequestBody SimulationInput input) {
		LOG.info("simulation input: {}", input);
		return this.simulator.simulate(input);
	}

	public static void main(String[] args) {
		SpringApplication.run(RpsApplication.class, args);
	}

}
