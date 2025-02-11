package game.engine;

import org.w3c.dom.Node;

public class Transform extends Component
{
	public Vector2 position = new Vector2();
	public float rotation = 0f;
	public Vector2 scale = new Vector2(1, 1);
	
	public Transform() { }
	public Transform(Node xml)
	{
		
	}
	
	public Vector2 forward()
	{
		return new Vector2(-(float)Math.cos(Math.toRadians(rotation)), -(float)Math.sin(Math.toRadians(rotation)));
	}
}