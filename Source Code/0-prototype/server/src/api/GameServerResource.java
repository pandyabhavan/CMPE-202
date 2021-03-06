package api;

import java.util.NavigableMap;

import org.json.*;
import org.restlet.representation.*;
import org.restlet.ext.json.*;
import org.restlet.resource.*;
import gameServer.GameMachine;

public class GameServerResource extends ServerResource {

	GameMachine machine = GameMachine.getInstance();

	@Get
	public Representation get(JsonRepresentation jsonRep) {

		return post(jsonRep);
	}

	@Post
	public Representation post(JsonRepresentation jsonRep) {

		JSONObject json = jsonRep.getJsonObject();
		JSONObject response = null;

		String action = json.getString("action");

		System.out.println("action: " + action);

		if (action.equals("registerPlayer")) {
			String playerName = json.getString("playerName");
			machine.registerPlayer(playerName);

			response = new JSONObject();
			response.put("ack", machine.getAck());
		}
		if (action.equals("setLevel")) {
			String playerName = json.getString("playerName");
			int level = json.getInt("level");
			machine.setLevel(playerName, level);

			response = new JSONObject();
			response.put("ack", machine.getAck());
		}
		if (action.equals("setScore")) {
			String playerName = json.getString("playerName");
			int correctScore = json.getInt("correctScore");
			int time = json.getInt("time");

			int score = machine.calculateScore(correctScore, time);
			machine.setScore(playerName, score);

			response = new JSONObject();
			response.put("ack", machine.getAck());
		}
		if (action.equals("getHighScore")) {
			NavigableMap<Integer, String> highScore = machine.getHigScore().descendingMap();
			response = new JSONObject(highScore);
			response.put("ack", machine.getAck());
		}

		return new JsonRepresentation(response);

	}
}
