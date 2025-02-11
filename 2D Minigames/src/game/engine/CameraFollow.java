package game.engine;

public class CameraFollow extends Camera 
{
	private Transform target;
	
	public CameraFollow(Transform target)
	{
		this.target = target;
	}
	
	public void update()
	{
		transform.position = target.position;
		super.update();
	}
}