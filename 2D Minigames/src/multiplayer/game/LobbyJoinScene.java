package multiplayer.game;

import game.engine.*;
import game.engine.UI.Button;
import game.engine.UI.TextField;

public class LobbyJoinScene extends Scene
{
	private int lobbies = 0;
	
	public void activate()
	{
		super.activate();
		GameObject.create(new GameObject("Main Camera", new Component[] { new Camera(), new AudioListener() }), new Vector2(), 0f);
		GameObject.create(new GameObject("Textfield Title", new Component[] { new TextField(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 125, 200, 50), false, "Join Lobby List", "", "/Images/Square.png") }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Back", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 75, 200, 50), "Back", "/Images/Square.png", () -> new LobbyScene().activate()) }), new Vector2(), 0f);
		
		SteamManager.instance.lobbies.values().forEach(x -> 
		{
			GameObject.create(new GameObject("Button Lobby " + lobbies, new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 25 + 50 * lobbies, 200, 50), SteamManager.instance.matchmaking.getLobbyData(x, "Name"), "/Images/Square.png", () -> SteamManager.instance.matchmaking.joinLobby(x)) }), new Vector2(), 0f);
			lobbies++;
		});
	}
}