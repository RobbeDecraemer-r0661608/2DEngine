package game.engine.Physics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import org.w3c.dom.Node;

import game.engine.Component;
import game.engine.Vector2;

public class CharacterController extends Component
{
	private Collider collider;

	public CharacterController() { }
	public CharacterController(Node xml)
	{
		
	}
	
	public void start()
	{
		collider = gameObject.getComponent(Collider.class);
	}
	
	public void move(Vector2 delta)
	{
		Vector2 result = new Vector2(delta.x, delta.y).normalized();
		
		AffineTransform vf = new AffineTransform();
		vf.translate(0f, result.y);
		vf.rotate(Math.toRadians(gameObject.transform.rotation), gameObject.transform.position.x, 
				gameObject.transform.position.y + result.y);
		Area vertical = collider.area.createTransformedArea(vf);
		
		AffineTransform hf = new AffineTransform();
		hf.translate(result.x, 0f);
		hf.rotate(Math.toRadians(gameObject.transform.rotation), gameObject.transform.position.x + result.x, 
				gameObject.transform.position.y);
		Area horizontal = collider.area.createTransformedArea(hf);
		
		RaycastHit[] hitsV = Physics.areacast(gameObject, vertical);
		RaycastHit[] hitsH = Physics.areacast(gameObject, horizontal);
		
		if(hitsV.length == 0)
		{
			transform.position.add(new Vector2(0f, result.y));
		}
		
		if(hitsH.length == 0)
		{
			transform.position.add(new Vector2(result.x, 0f));
		}
	}
}