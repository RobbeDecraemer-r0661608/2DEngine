package character.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.w3c.dom.Node;

import game.engine.*;
import game.engine.Physics.CharacterController;
import game.engine.UI.GUI;

public class Character extends CharacterController
{
	public static int score = 0;
	float speed = 200f;

	public Character() { }
	public Character(Node xml)
	{
		super(xml);
	}
	
	public void update()
	{
		Vector2 delta = new Vector2();
		
		if(Input.getKey(KeyEvent.VK_W) || Input.getKey(KeyEvent.VK_UP))
		{
			delta.y--;
		}
		
		if(Input.getKey(KeyEvent.VK_S) || Input.getKey(KeyEvent.VK_DOWN))
		{
			delta.y++;
		}
		
		if(Input.getKey(KeyEvent.VK_A) || Input.getKey(KeyEvent.VK_LEFT))
		{
			delta.x--;
		}
		
		if(Input.getKey(KeyEvent.VK_D) || Input.getKey(KeyEvent.VK_RIGHT))
		{
			delta.x++;
		}
		
		if(Input.getKey(KeyEvent.VK_SHIFT))
		{
			if(Input.getMouseButton(0))
			{
				GameObject.create(new GameObject("Bullet", "Projectile", new Component[] { new Projectile(), new Image("/Images/Circle.png", 1, Color.orange) }), transform.position.addtemp(transform.forward().multiply(32f)), transform.rotation, new Vector2(0.5f, 0.5f));
			}
		}
		
		else
		{
			if(Input.getMouseButtonDown(0))
			{
				GameObject.create(new GameObject("Bullet", "Projectile", new Component[] { new Projectile(), new Image("/Images/Circle.png", 1, Color.orange) }), transform.position.addtemp(transform.forward().multiply(32f)), transform.rotation, new Vector2(0.5f, 0.5f));
			}
		}
		
		Vector2 mouse = Input.getMousePosition();
		mouse.add(new Vector2(-Screen.width / 2f, -Screen.height / 2f));
		float angle = (float)Math.toDegrees(Math.atan(mouse.y / mouse.x));
		if(mouse.x >= 0f) angle += 180f;
		transform.rotation = angle;
		
		super.move(delta.multiply(Time.deltaTime * speed));
	}
	
	public void onGUI()
	{
		GUI.setColor(Color.black);
		GUI.label(new Rect((int)(Screen.width / 2f) - 5, (int)(Screen.height / 2f) - 3, 10, 6), Integer.toString(score));
	}
}