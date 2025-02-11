package game.engine.Physics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.*;

import org.w3c.dom.Node;

public class SquareCollider extends Collider 
{
	public SquareCollider() { }
	
	public SquareCollider(Node xml)
	{
		
	}
	
	public void start()
	{
		super.start();
		Shape shape = new Rectangle((int)gameObject.transform.position.x - (int)(gameObject.transform.scale.x * 16f),
				(int)gameObject.transform.position.y - (int)(gameObject.transform.scale.y * 16f),
				(int)(gameObject.transform.scale.x * 32f),
				(int)(gameObject.transform.scale.y * 32f));
		
		area = new Area(shape);
	}
	
	public void update()
	{
		Shape shape = new Rectangle((int)gameObject.transform.position.x - (int)(gameObject.transform.scale.x * 16f),
				(int)gameObject.transform.position.y - (int)(gameObject.transform.scale.y * 16f),
				(int)(gameObject.transform.scale.x * 32f),
				(int)(gameObject.transform.scale.y * 32f));
		
		area = new Area(shape);
		super.update();
	}
}