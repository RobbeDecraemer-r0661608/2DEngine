package character.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;

import org.w3c.dom.Node;

import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamNetworking.P2PSend;

import game.engine.*;
import game.engine.Physics.CharacterController;
import game.engine.UI.GUI;
import multiplayer.game.SteamManager;
import multiplayer.game.SteamPlayer;

public class MultiplayerCharacter extends CharacterController
{
	public boolean isMine;
	public SteamPlayer player;
	public static int score = 0;
	float speed = 200f;
	private ByteBuffer readBuffer = ByteBuffer.allocateDirect(14);
	private ByteBuffer sendBuffer = ByteBuffer.allocateDirect(14);

	public MultiplayerCharacter(boolean mine, SteamPlayer owner) 
	{
		isMine = mine;
		player = owner;
	}
	
	public MultiplayerCharacter(Node xml)
	{
		super(xml);
	}
	
	public void update()
	{
		if(!isMine) return;
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

		if(!Input.getKey(KeyEvent.VK_SHIFT))
		{
			if(Input.getMouseButtonDown(0))
			{
				sendBuffer.clear();
				sendBuffer.put((byte)1);
				
				for(SteamPlayer player : SteamManager.instance.lobby.players)
				{
					try 
					{
						if(player.ID.equals(SteamManager.instance.user.ID)) continue;
						SteamManager.instance.networking.sendP2PPacket(player.ID, sendBuffer, P2PSend.UnreliableNoDelay, 0);
					} 
					
					catch (SteamException e) 
					{
						e.printStackTrace();
					}
				}
				
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
	
	public void fixedUpdate()
	{
		if(isMine)
		{
			sendBuffer.clear();
			sendBuffer.put((byte)0);
			sendBuffer.putFloat(transform.position.x);
			sendBuffer.putFloat(transform.position.y);
			sendBuffer.putFloat(transform.rotation);
			sendBuffer.put(Input.getKey(KeyEvent.VK_SHIFT) && Input.getMouseButton(0) ? (byte)1 : (byte)0);
			
			if(Input.getKey(KeyEvent.VK_SHIFT))
			{
				if(Input.getMouseButton(0))
				{
					GameObject.create(new GameObject("Bullet", "Projectile", new Component[] { new Projectile(), new Image("/Images/Circle.png", 1, Color.orange) }), transform.position.addtemp(transform.forward().multiply(32f)), transform.rotation, new Vector2(0.5f, 0.5f));
				}
			}
			
			for(SteamPlayer player : SteamManager.instance.lobby.players)
			{
				try 
				{
					if(player.ID.equals(SteamManager.instance.user.ID)) continue;
					SteamManager.instance.networking.sendP2PPacket(player.ID, sendBuffer, P2PSend.UnreliableNoDelay, 0);
				} 
				
				catch (SteamException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		else
		{
			int packetSize = SteamManager.instance.networking.isP2PPacketAvailable(0);

			while (packetSize > 0) 
			{
				SteamID steamIDSender = new SteamID();
				readBuffer.clear();

				try 
				{
					if (SteamManager.instance.networking.readP2PPacket(steamIDSender, readBuffer, 0) > 0) 
					{
						int bytesReceived = readBuffer.limit();
						byte[] bytes = new byte[bytesReceived];
						readBuffer.get(bytes);
					}
				} 
				
				catch (SteamException e) 
				{
					e.printStackTrace();
				}
				
				switch(readBuffer.get(0))
				{
				case 0:
					transform.position.x = readBuffer.getFloat(1);
					transform.position.y = readBuffer.getFloat(5);
					transform.rotation = readBuffer.getFloat(9);
					
					if(readBuffer.get(13) == 1)
					{
						GameObject.create(new GameObject("Bullet", "Projectile", new Component[] { new Projectile(), new Image("/Images/Circle.png", 1, Color.orange) }), transform.position.addtemp(transform.forward().multiply(32f)), transform.rotation, new Vector2(0.5f, 0.5f));
					}
					
					packetSize -= 14;
					break;
					
				case 1:
					GameObject.create(new GameObject("Bullet", "Projectile", new Component[] { new Projectile(), new Image("/Images/Circle.png", 1, Color.orange) }), transform.position.addtemp(transform.forward().multiply(32f)), transform.rotation, new Vector2(0.5f, 0.5f));
					break;
				}
			}
		}
	}
	
	public void onGUI()
	{
		GUI.setColor(Color.black);
		GUI.label(new Rect((int)(Screen.width / 2f) - 5, (int)(Screen.height / 2f) - 3, 10, 6), Integer.toString(score));
	}
}