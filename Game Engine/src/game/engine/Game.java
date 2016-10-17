package game.engine;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Game extends Application
{
	private String title;
	private long startTime;
	static Group root = new Group();
	
	public void run(String[] args, String title, int width, int height)
	{
		this.title = title;
		Screen.width = width;
		Screen.height = height;
		launch(args);
	}
	
	public void start(Stage primaryStage)
	{
		startTime = System.nanoTime();
        Scene scene = new Scene();
        scene.setFill(Color.DIMGRAY);
        SceneManager.activeScene = scene;
        
        primaryStage.setResizable(true);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        PerspectiveCamera defaultCamera = new PerspectiveCamera(true);
        defaultCamera.setTranslateZ(-10);
        scene.setCamera(defaultCamera);

        // Experimenting
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
        {
            public void handle(KeyEvent t) 
            {
            	handleInput(t);
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() 
        {
            public void handle(KeyEvent t) 
            {
            	handleInput(t);
            }
        });
        
        //scene.addGameObject(new GameObject("Main Camera", new Component[] { new Camera() }));
        GameObject obj = new GameObject("Box", new Component[] { new Renderer(new Box(1, 1, 1)), new Collider(new Box(1, 1, 1)) });
        obj.transform.translate(new Vector3(2, 0, 0));
        obj.transform.scale(new Vector3(0.5, 0.5, 0.5));
        scene.addGameObject(obj);
        GameObject obj2 = new GameObject("Box2", new Component[] { new Renderer(new Box(1, 1, 1)), new Collider(new Box(1, 1, 1)) });
        scene.addGameObject(obj2);
        GameObject ball = new GameObject("Ball", new Component[] { new Renderer(new Sphere(1, 20)), new Collider(new Sphere(1, 20)) });
        scene.addGameObject(ball);
        ball.transform.translate(new Vector3(-2, 0, 0));
        ball.transform.scale(new Vector3(0.5, 0.5, 0.5));
        obj.getComponent(Collider.class).collide();
        
        new AnimationTimer() 
        {
        	long lastTime = 0;
        	
            public void handle(long currentNanoTime) 
            {
            	Time.deltaTime = (currentNanoTime - lastTime) / 1000000000.0;
            	lastTime = currentNanoTime;
            	Time.time = (currentNanoTime - startTime) / 1000000000.0;
            	
                for (GameObject gameObject : SceneManager.activeScene.gameObjects) 
                {
                	gameObject.update();
                	ball.transform.rotate(new Vector3(0, 1, 0));
                	obj.transform.rotate(new Vector3(0, 0, 1));
                	obj2.transform.rotate(new Vector3(1, 0, 0));
                	obj2.getComponent(Collider.class).collide();
                	if(Input.getKey(KeyCode.W))
                		obj2.transform.translate(new Vector3(0, -1 * Time.deltaTime, 0));
                	if(Input.getKey(KeyCode.S))
                		obj2.transform.translate(new Vector3(0, 1 * Time.deltaTime, 0));
                	if(Input.getKey(KeyCode.A))
                		obj2.transform.translate(new Vector3(-1 * Time.deltaTime, 0, 0));
                	if(Input.getKey(KeyCode.D))
                		obj2.transform.translate(new Vector3(1 * Time.deltaTime, 0, 0));
                }
                
        		DecimalFormat df = new DecimalFormat("#.##");
        		df.setRoundingMode(RoundingMode.FLOOR);
                //System.out.println(df.format(Time.time) + ": " + df.format(1.0 / Time.deltaTime) + " FPS");
                
                Input.keysDown.clear();
                Input.keysUp.clear();
            }
        }.start();
	}
	
	public void stop()
	{
		System.out.println("Terminated.");
	}
	
	private void handleInput(KeyEvent t)
	{
		if(t.getEventType() == KeyEvent.KEY_PRESSED)
		{
			if(!Input.keys.contains(t.getCode()))
			{
				Input.keysDown.add(t.getCode());
				Input.keys.add(t.getCode());
			}
		}
		
		if(t.getEventType() == KeyEvent.KEY_RELEASED)
		{
			Input.keys.remove(t.getCode());
			Input.keysUp.add(t.getCode());
		}
	}
}