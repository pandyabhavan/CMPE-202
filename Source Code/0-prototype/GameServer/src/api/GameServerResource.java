package api;

import java.util.Map;
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

		JSONObject json = new JSONObject() ;
        json.put( "ack", "connected" ) ;

        return new JsonRepresentation ( json ) ;
	}

	@Post
	public Representation post(JsonRepresentation jsonRep) {

		JSONObject json = jsonRep.getJsonObject();
		JSONObject response = null;

		String action = json.getString("action");

		System.out.println("action: " + action);
		
		this.machine.setAck("false");

		if (action.equals("registerPlayer")) {
			String playerName = json.getString("playerName");
			machine.registerPlayer(playerName);

			response = new JSONObject();
			response.put("playerName", playerName);
			response.put("ack", machine.getAck());
		} else if (action.equals("setLevel")) {
			String playerName = json.getString("playerName");
			String level = json.getString("level");
			machine.setLevel(playerName, level);

			response = new JSONObject();
			response.put("playerName", playerName);
			response.put("level", machine.getPlayerNameLevel().get(playerName));
			response.put("ack", machine.getAck());
		} else if (action.equals("setScore")) {
			String playerName = json.getString("playerName");
			int correctScore = json.getInt("correctScore");
			int time = json.getInt("time");

			String score = machine.calculateScore(correctScore, time);
			machine.setScore(playerName, score);

			response = new JSONObject();
			response.put("score", machine.getPlayerNameScore().get(playerName));
			response.put("ack", machine.getAck());
		} else if (action.equals("getHighScore")) {
			NavigableMap<String, String> highScore = machine.getHigScore().descendingMap();
			response = new JSONObject();
			for (Map.Entry<String, String> entry : highScore.entrySet()){
				response.put(entry.getKey(), entry.getValue());
			}
			response.put("ack", "true");
		} else {
			response = new JSONObject();
			response.put("ack", "connect-waiting for action");
		}

		return new JsonRepresentation(response);

	}
}
