package character.controller;

import game.engine.*;
import game.engine.Physics.Physics;
import game.engine.Physics.RaycastHit;

public class Projectile extends Component
{
	public float speed = 1000f;
	
	public void update()
	{
		RaycastHit[] hits = Physics.raycast(transform.position, transform.forward(), speed * Time.deltaTime);
		
		if(hits.length > 0)
		{
			for(RaycastHit hit : hits)
			{
				if(hit.gameObject.tag != "Projectile")
					GameObject.destroy(gameObject);
				
				if(hit.gameObject.tag == "AI")
					Character.score += 1;
			}
		}
		
		transform.position.add(transform.forward().multiply(speed * Time.deltaTime));
	}
}