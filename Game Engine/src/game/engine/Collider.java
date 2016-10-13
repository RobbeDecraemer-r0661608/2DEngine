package game.engine;

import javafx.scene.shape.Shape3D;

public class Collider extends Component
{
	private Shape3D shape;
	
	public Collider(Shape3D mesh)
	{
		shape = mesh;
	}
	
	public void start()
	{
		super.start();
		transform.getChildren().add(shape);
	}
	
	public void collide()
	{
		for (int i = 0; i < SceneManager.activeScene.gameObjects.size(); i++)
		{
			Collider c;
			
			if ((c = SceneManager.activeScene.gameObjects.get(i).getComponent(Collider.class)) != null)
			{
				if (c.gameObject == gameObject) continue;
				
				if (transform.getBoundsInParent().intersects(c.transform.getBoundsInParent()))
				{
					System.out.println(gameObject.name + " collided with " + c.gameObject.name);
					System.out.println(gameObject.name + ": " + transform.getPosition());
					System.out.println(c.gameObject.name + ": " + c.transform.getPosition());
					System.out.println(gameObject.name + ": " + transform.getBoundsInParent());
					System.out.println(c.gameObject.name + ": " + c.transform.getBoundsInParent());
				}
			}
		}
	}
}