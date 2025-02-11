package multiplayer.game;

import character.controller.CharacterScene;
import game.engine.*;
import game.engine.UI.Button;
import game.engine.UI.TextField;

public class MainScene extends Scene
{
	public void activate()
	{
		super.activate();
		GameObject.create(new GameObject("Main Camera", new Component[] { new Camera(), new AudioListener() }), new Vector2(), 0f);
		GameObject.create(new GameObject("Steam Manager", new Component[] { new SteamManager() }), new Vector2(), 0f);
		GameObject.create(new GameObject("Textfield Title", new Component[] { new TextField(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 125, 200, 50), false, "Main Menu", "", "/Images/Square.png") }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Start", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 75, 200, 50), "Start", "/Images/Square.png", () -> new CharacterScene().activate()) }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Multiplayer", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 - 25, 200, 50), "Multiplayer", "/Images/Square.png", () -> new LobbyScene().activate()) }), new Vector2(), 0f);
		GameObject.create(new GameObject("Button Stop", new Component[] { new Button(new Rect(Screen.width / 2 - 100, Screen.height / 2 + 25, 200, 50), "Stop", "/Images/Square.png", () -> Engine.instance.isRunning = false) }), new Vector2(), 0f);
	}
}