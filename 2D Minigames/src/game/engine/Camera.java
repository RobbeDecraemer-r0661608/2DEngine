package game.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Camera extends Component
{
	private BufferedImage imageBuffer;
	
	public void start() 
	{
		imageBuffer = new BufferedImage(Screen.width, Screen.height, BufferedImage.TYPE_INT_RGB);
		Game.instance.activeScene.cameras.add(this);
	}
	
	public void draw(Game game, List<GameObject> gameObjects)
	{
		List<Image> result = new ArrayList<Image>();
		
		for(int i = 0; i < Game.instance.activeScene.gameObjects.size(); i++)
		{
			Image img;
			
			if((img = Game.instance.activeScene.gameObjects.get(i).getComponent(Image.class)) != null)
			{
				result.add(img);
			}
		}
		
		result.sort(new LayerHandler());
		Graphics graphics = imageBuffer.getGraphics();
		
		// Background
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, Screen.width, Screen.height);
		graphics.setColor(Color.black);

		for(int i = 0; i < result.size(); i++)
		{
			result.get(i).draw(graphics, gameObject.transform.position);
		}
		
		Renderer.graphics.drawImage(imageBuffer, (int)gameObject.transform.position.x, (int)gameObject.transform.position.y, Screen.width, Screen.height, game);
	}
}