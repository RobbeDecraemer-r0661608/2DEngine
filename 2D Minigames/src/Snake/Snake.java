package Snake;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import game.engine.*;

public class Snake extends SquareCollider
{
	private SnakeSegment firstSegment;
	private SnakeSegment lastSegment;
	
	public void start()
	{
		GameObject segment = new GameObject("SnakeSegment", "Snake", new Component[] { new SnakeSegment(), new Image("Images/Square.png", 1, Color.green) });
		GameObject.create(segment, new Vector2(gameObject.transform.position.x, gameObject.transform.position.y + 40f), 0f);
		firstSegment = segment.getComponent(SnakeSegment.class);
		lastSegment = firstSegment;
		super.start();
	}
	
	public void update()
	{
		Vector2 lastPos = gameObject.transform.position.copy();

		if(Input.getKeyDown(KeyEvent.VK_W))
		{
			gameObject.transform.position.y -= 40f;
			firstSegment.move(lastPos);
			return;
		}
		
		if(Input.getKeyDown(KeyEvent.VK_S))
		{
			gameObject.transform.position.y += 40f;
			firstSegment.move(lastPos);
			return;
		}
		
		if(Input.getKeyDown(KeyEvent.VK_A))
		{
			gameObject.transform.position.x -= 40f;
			firstSegment.move(lastPos);
			return;
		}
		
		if(Input.getKeyDown(KeyEvent.VK_D))
		{
			gameObject.transform.position.x += 40f;
			firstSegment.move(lastPos);
			return;
		}
		
		super.update();
		super.collide();
	}
	
	public void onCollisionStart(Collider col)
	{
		super.onCollisionStart(col);
		
		if(col.gameObject.tag == "Apple")
		{
			GameObject segment = new GameObject("SnakeSegment", "Snake", new Component[] { new SnakeSegment(), new Image("Images/Square.png", 1, Color.green) });
			GameObject.create(segment, lastSegment.gameObject.transform.position, 0f);
			lastSegment.child = segment.getComponent(SnakeSegment.class);
			lastSegment = lastSegment.child;
			GameObject.destroy(col.gameObject);
			
			Random r = new Random();
			GameObject apple = new GameObject("Apple", "Apple", new Component[] { new Apple(), new Image("Images/Square.png", 0, Color.red) });
			GameObject.create(apple, new Vector2(r.nextInt(1000/40 - 2) * 40 + 60f, r.nextInt(800 / 40 - 2) * 40 + 40f), 0f);
		}
		
		else 
		{
			GameObject.destroy(gameObject);
			Game.instance.isRunning = false;
		}
	}
}