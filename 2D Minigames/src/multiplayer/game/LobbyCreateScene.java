package multiplayer.game;

import com.codedisaster.steamworks.SteamMatchmaking.LobbyType;

import game.engine.*;
import game.engine.UI.Button;
import game.engine.UI.TextField;

public class LobbyCreateScene extends Scene
{
	public void activate()
	{
		super.activate();
		GameObject.create(new GameObject("Main Camera", new Component[] { new Camera(), new AudioListener() }), new Vector2(), 0f);
		GameObject.create(new GameObject("Textfield Title", new Component[] { new TextField(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 125, 200, 50), false, "Create Lobby Menu", "", "/Images/Square.png") }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Back", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 75, 200, 50), "Back", "/Images/Square.png", () -> new LobbyScene().activate()) }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Create Lobby", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 25, 200, 50), "Create Lobby", "/Images/Square.png", () -> SteamManager.instance.matchmaking.createLobby(LobbyType.Public, 4) ) }), new Vector2(), 0f);
	}
}