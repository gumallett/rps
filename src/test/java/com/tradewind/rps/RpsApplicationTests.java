package com.tradewind.rps;

import com.tradewind.rps.service.CasinoServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RpsApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CasinoServiceImpl casinoService;

	@Before
	public void prepare() {
		casinoService.resetBalance();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSimulation() throws Exception {
		this.mvc.perform(post("/simulations")
				.content(createTestData())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().string("10"));

		// player's balances should be $5
		List<Integer> balances = Arrays.asList(5, 5);
		this.mvc.perform(get("/players"))
				.andExpect(jsonPath("$.length()").value(is(2)))
				.andExpect(jsonPath("$..balance").value(Matchers.containsInAnyOrder(balances.toArray())));
	}

	@Test
	public void testMultipleGameSimulation() throws Exception {
		this.mvc.perform(post("/simulations")
				.content(createTestDataTwoGames())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().string("20"));
	}

	@Test
	public void testNotEnoughPlayerMoney() throws Exception {
		this.mvc.perform(post("/simulations")
				.content(createTestDataLowBalance())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().string("10"));
	}

	@Test
	public void concurrentTest() throws Exception {
		ExecutorService threadpool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 100; i++) {
			threadpool.submit(() -> {
				try {
					RpsApplicationTests.this.mvc.perform(post("/simulations")
							.content(createTestData())
							.contentType(MediaType.APPLICATION_JSON_UTF8));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		threadpool.shutdown();
		try {
			threadpool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// ignore
		}

		this.mvc.perform(get("/casino/balance"))
				.andExpect(status().isOk())
				.andExpect(content().string("1000"));
	}

	private static String createTestData() {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID game1 = UUID.randomUUID();
		return "{\"players\": [" +
					createTestPlayer(player1, 10) + "," +
					createTestPlayer(player2, 10) +
				"], \"events\": [" +
				createJoinGameEvent(game1, player1) + "," +
				createJoinGameEvent(game1, player2) + "," +
				createNewGameEvent(game1) +
				"]}";
	}

	private static String createTestDataLowBalance() {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID game1 = UUID.randomUUID();
		UUID game2 = UUID.randomUUID();
		return "{\"players\": [" +
				createTestPlayer(player1, 5) + "," +
				createTestPlayer(player2, 10) +
				"], \"events\": [" +
				createNewGameEvent(game1) + "," +
				createJoinGameEvent(game1, player1) + "," +
				createJoinGameEvent(game1, player2) + "," +
				createNewGameEvent(game2) + "," +
				createJoinGameEvent(game2, player1) + "," +
				createJoinGameEvent(game2, player2) +
				"]}";
	}

	private static String createTestDataTwoGames() {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID game1 = UUID.randomUUID();
		UUID game2 = UUID.randomUUID();
		return "{\"players\": [" +
				createTestPlayer(player1, 10) + "," +
				createTestPlayer(player2, 10) +
				"], \"events\": [" +
				createNewGameEvent(game1) + "," +
				createJoinGameEvent(game1, player1) + "," +
				createJoinGameEvent(game1, player2) + "," +
				createNewGameEvent(game2) + "," +
				createJoinGameEvent(game2, player1) + "," +
				createJoinGameEvent(game2, player2) +
				"]}";
	}

	private static String createTestPlayer(UUID playerInfoId, int startingBalance) {
		return "{" +
				"\"playerInfoId\": \"" + playerInfoId + "\"," +
				"\"startingAccountBalance\": " + startingBalance +
				"}";
	}

	private static String createNewGameEvent(UUID eventID) {
		return "{" +
				"\"timestampInMS\":" + System.currentTimeMillis() + "," +
				"\"eventID\": \"" + eventID + "\"," +
				"\"eventType\": \"" + EventType.ESTABLISH_NEW_GAME + "\"" +
				"}";
	}

	private static String createJoinGameEvent(UUID establishNewGameEventId, UUID playerInfoID) {
		return "{" +
				"\"timestampInMS\":" + System.currentTimeMillis() + "," +
				"\"eventID\": \"" + UUID.randomUUID() + "\"," +
				"\"eventType\": \"" + EventType.JOIN_GAME + "\"," +
				"\"establishNewGameEventId\": \"" + establishNewGameEventId + "\"," +
				"\"playerInfoID\": \"" + playerInfoID + "\"" +
				"}";
	}
}
