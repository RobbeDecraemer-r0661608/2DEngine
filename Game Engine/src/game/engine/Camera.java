package game.engine;

import javafx.scene.PerspectiveCamera;

public class Camera extends Component
{
	javafx.scene.Camera camera;
	
	public Camera()
	{
		camera = new PerspectiveCamera(true);
		SceneManager.activeScene.addCamera(this);
	}
}