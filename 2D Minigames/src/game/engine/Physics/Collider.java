package game.engine.Physics;

import java.awt.geom.*;

import game.engine.Component;

public class Collider extends Component
{
	public Area area;
	
	public void start()
	{
		Physics.colliders.add(this);
	}
	
	public void stop()
	{
		Physics.colliders.remove(this);
	}
	
	public void collide()
	{	
		for(int i = 0; i < Physics.colliders.size(); i++)
		{
			Collider c = Physics.colliders.get(i);
			
			if (c.gameObject.tag.equals(gameObject.tag)) continue;
			
			if (CheckInterSection(c))
			{
				onCollisionStart((Collider)c);
			}
		}
	}
	
	private boolean CheckInterSection(Collider b)
	{
		if(area == null || b.area == null) return false;
		
		AffineTransform af = new AffineTransform();
		af.rotate(Math.toRadians(gameObject.transform.rotation), gameObject.transform.position.x, 
				gameObject.transform.position.y);
		Area rotatedA = area.createTransformedArea(af);
		
		AffineTransform bf = new AffineTransform();
		bf.rotate(Math.toRadians(b.gameObject.transform.rotation), b.gameObject.transform.position.x, 
        		b.gameObject.transform.position.y);
		Area rotatedB = b.area.createTransformedArea(bf);
		
		rotatedA.intersect(rotatedB);
		return !rotatedA.isEmpty();
	}
	
	public void onCollisionStart(Collider col)
	{
		System.out.println(gameObject.name + " collided with " + col.gameObject.name);
	}
	
	public void onCollisionStay(Collider col)
	{
		
	}
	
	public void onCollisionStop(Collider col)
	{
		
	}
}