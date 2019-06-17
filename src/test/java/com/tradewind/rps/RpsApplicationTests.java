package com.tradewind.rps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RpsApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSimulation() throws Exception {
		this.mvc.perform(post("/simulations")
				.content(createTestData())
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	private static String createTestData() {
		UUID player1 = UUID.randomUUID();
		UUID player2 = UUID.randomUUID();
		UUID game1 = UUID.randomUUID();
		return "{\"players\": [" +
					createTestPlayer(player1) + "," +
					createTestPlayer(player2) +
				"], \"events\": [" +
				createNewGameEvent(game1) + "," +
				createJoinGameEvent(game1, player1) + "," +
				createJoinGameEvent(game1, player2) +
				"]}";
	}

	private static String createTestPlayer(UUID playerInfoId) {
		return "{" +
				"\"playerInfoId\": \"" + playerInfoId + "\"," +
				"\"startingAccountBalance\": 10" +
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
