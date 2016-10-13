package game.engine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.*;

import game.engine.Renderer;

public class Game extends JFrame
{
	public Insets insets;
	public Scene ddolScene;
	public Scene activeScene;
	public static Game instance;

	private long startTime;
	private Input input;
	public boolean isRunning = true;
	private static final long serialVersionUID = 1L;
	private String title;
	private BufferedImage guiBuffer;
	private BufferedImage screenBuffer;

	public Game(String title, int width, int height)
	{
		this.title = title;
		Screen.width = width;
		Screen.height = height;
		instance = this;
	}

	public void run()
	{
		initialize();
		runFixedUpdate();
		runUpdate();
	}

	public void runUpdate()
	{
		while(isRunning) 
		{
			long time = System.nanoTime();
			Time.time = (time - startTime) / 1000000000f;

			input.update();
			//collide();
			update();
			draw();

			Time.deltaTime = (System.nanoTime() - time) / 1000000000f;
		}
	}

	void runFixedUpdate()
	{
		Thread loop = new Thread()
		{
			public void run()
			{
				while(isRunning) 
				{
					long time = System.nanoTime();
					Time.fixedTime = (time - startTime) / 1000000000f;

					fixedUpdate();

					int delta = (int)((System.nanoTime() - time) / 1000000f);

					try
					{
						Thread.sleep(20 - delta);
						Time.deltaFixedTime = (System.nanoTime() - time) / 1000000000f;
					}
					catch(Exception e) {}
				}
			}
		};
		loop.start();
	} 

	void initialize()
	{
		ddolScene = new Scene();
		activeScene.activateScene(ddolScene);
		startTime = System.nanoTime();
		setTitle(title); 
		setSize(Screen.width, Screen.height); 
		setResizable(false); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setVisible(true); 

		insets = getInsets(); 
		setSize(insets.left + Screen.width + insets.right, insets.top + Screen.height + insets.bottom);
		guiBuffer = new BufferedImage(Screen.width, Screen.height, BufferedImage.TYPE_INT_ARGB_PRE);
		screenBuffer = new BufferedImage(Screen.width, Screen.height, BufferedImage.TYPE_INT_RGB);
		input = new Input(this);
	}

	void update()
	{
		for(int i = 0; i < activeScene.gameObjects.size(); i++)
		{
			activeScene.gameObjects.get(i).update();
		}
	}

	void collide()
	{
		for(int i = 0; i < activeScene.gameObjects.size(); i++)
		{
			activeScene.gameObjects.get(i).collide();
		}
	}

	void fixedUpdate()
	{
		for(int i = 0; i < activeScene.gameObjects.size(); i++)
		{
			activeScene.gameObjects.get(i).fixedUpdate();
		}
	}

	void draw()
	{
		// Draw gui and objects
		Graphics g = getGraphics();
		Renderer.graphics = screenBuffer.getGraphics();
		GUI.graphics = (Graphics2D)guiBuffer.getGraphics();
		GUI.graphics.setBackground(new Color(0, 255, 0, 0));
		GUI.graphics.clearRect(0, 0, Screen.width, Screen.height);
		GUI.graphics.setColor(Color.black);
		GUI.graphics.fillRect(Screen.width / 2 - 1, Screen.height / 2 - 1, 2, 2);
		activeScene.draw(this);
		
		// StatsDecimal
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.FLOOR);
		GUI.label(new Rect(20, 30, 0, 0), "Time: " + df.format(Time.time));
		GUI.label(new Rect(20, 50, 0, 0), "DeltaTime: " + df.format(Time.deltaTime));
		GUI.label(new Rect(20, 70, 0, 0), "FixedTime: " + df.format(Time.fixedTime));
		GUI.label(new Rect(20, 90, 0, 0), "DeltaFixedTime: " + df.format(Time.deltaFixedTime));
		GUI.label(new Rect(20, 110, 0, 0), df.format((1f / Time.deltaTime)) + " FPS");

		// Draw frame
		Renderer.graphics.drawImage(guiBuffer, 0, 0, this);
		g.drawImage(screenBuffer, insets.left, insets.top, this);
	}
}