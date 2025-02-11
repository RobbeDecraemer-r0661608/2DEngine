package character.controller;

import java.awt.Color;
import java.util.*;

import game.engine.*;
import game.engine.Animation.Animation;
import game.engine.Animation.AnimationFrame;
import game.engine.Physics.CircleCollider;
import game.engine.Physics.SquareCollider;
import multiplayer.game.SteamManager;
import multiplayer.game.SteamPlayer;

public class MultiplayerCharacterScene extends Scene
{
	public void activate(SteamPlayer[] players)
	{
		super.activate();
		GameObject p = null;
		
		for(int i = 0; i < players.length; i++)
		{
			boolean isMine = players[i].ID.equals(SteamManager.instance.user.ID);
			
			if(isMine)
			{
				p = new GameObject("Character", "Character", new Component[] { new CircleCollider(), new MultiplayerCharacter(isMine, players[i]), new Image("/Images/Circle.png", 10, new Color(50 * i, 150, 50 * i, 255)) });
				GameObject.create(p, new Vector2(Screen.width / 2, Screen.height / 2 + 40f * i), 0f);
			}
			
			else
			{
				GameObject op = new GameObject("Character", "Character", new Component[] { new CircleCollider(), new MultiplayerCharacter(isMine, players[i]), new Image("/Images/Circle.png", 10, new Color(50 * i, 150, 50 * i, 255)) });
				GameObject.create(op, new Vector2(Screen.width / 2, Screen.height / 2 + 40f * i), 0f);
			}
		}
		
		GameObject.create(new GameObject("Main Camera", new Component[] { new CameraFollow(p.transform), new AudioListener() }), new Vector2(Screen.width / 2f, Screen.height / 2f), 0f);
		
		Image aiImg = new Image("/Images/Square.png", 0, new Color(0, 0, 0, 0));
		List<AnimationFrame> frames = new ArrayList<AnimationFrame>();
		frames.add(new AnimationFrame(0f, new Action[] { () -> aiImg.changeImage("/Images/animationTest.png", 0, new Color(0, 0, 0, 0), 32, 32, 0) }));
		frames.add(new AnimationFrame(0.5f, new Action[] { () -> aiImg.changeImage("/Images/animationTest.png", 0, new Color(0, 0, 0, 0), 32, 32, 1) }));
		frames.add(new AnimationFrame(1f, new Action[] { () -> aiImg.changeImage("/Images/animationTest.png", 0, new Color(0, 0, 0, 0), 32, 32, 2) }));
		frames.add(new AnimationFrame(1.5f, new Action[] { () -> aiImg.changeImage("/Images/animationTest.png", 0, new Color(0, 0, 0, 0), 32, 32, 3) }));
		
		GameObject.create(new GameObject("AI", "AI", new Component[] { new SquareCollider(), new AI(), 
				aiImg, new Animation(true, 2f, frames) }), new Vector2(Screen.width / 2 - 20f, Screen.height / 2 - 150f), 0f);
		
		GameObject wall1 = new GameObject("Wall1", new Component[] { new SquareCollider(), new Image("/Images/Square.png", -10, new Color(0, 0, 0, 255)) });
		GameObject wall2 = new GameObject("Wall2", new Component[] { new SquareCollider(), new Image("/Images/Square.png", -10, new Color(0, 0, 0, 255)) });
		GameObject wall3 = new GameObject("Wall3", new Component[] { new SquareCollider(), new Image("/Images/Square.png", -10, new Color(0, 0, 0, 255)) });
		GameObject wall4 = new GameObject("Wall4", new Component[] { new SquareCollider(), new Image("/Images/Square.png", -10, new Color(0, 0, 0, 255)) });
		GameObject wallCircle = new GameObject("WallCircle", new Component[] { new CircleCollider(), new Image("/Images/Circle.png", -10, new Color(0, 0, 0, 255)) });
		GameObject tree = new GameObject("Tree", new Component[] { new SquareCollider(), new Image("/Images/Square.png", -10, new Color(60, 60, 60, 255)) });
		GameObject.create(wall1, new Vector2(Screen.width / 2f, Screen.height / 2f - 400f), 0f, new Vector2(Screen.height / 32f, 1f));
		GameObject.create(wall2, new Vector2(Screen.width / 2f, Screen.height / 2f + 400f), 0f, new Vector2(Screen.height / 32f, 1f));
		GameObject.create(wall3, new Vector2(Screen.width / 2f - 400f, Screen.height / 2f), 0f, new Vector2(1f, Screen.height / 32f));
		GameObject.create(wall4, new Vector2(Screen.width / 2f + 400f, Screen.height / 2f), 0f, new Vector2(1f, Screen.height / 32f));
		GameObject.create(wallCircle, new Vector2(Screen.width / 2f + -150f, Screen.height / 2f), 0f);
		GameObject.create(tree, new Vector2(Screen.width / 2f + 150f, Screen.height / 2f), 0f);
	}
}