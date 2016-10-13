package game.engine;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;
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
        
        Camera defaultCamera = new PerspectiveCamera(true);
        defaultCamera.getTransforms().addAll(new Translate(0, 0, -10));
        scene.setCamera(defaultCamera);

        
        
        // Experimenting
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
        {
            public void handle(KeyEvent t) 
            {
            	if(t.getCode() == KeyCode.ESCAPE)
            	{
            		try 
            		{
            			primaryStage.close();
					} 
            		
            		catch (Exception e) 
            		{
						e.printStackTrace();
					}
            	}
            }
        });
        
        GameObject obj = new GameObject("Box", new Component[] { new Renderer(new Box(1, 1, 1)), new Collider(new Box(1, 1, 1)) });
        obj.transform.rotate(new Vector3(0, 0, 45));
        obj.transform.translate(new Vector3(2, -1, 0));
        obj.transform.scale(new Vector3(1, 0.5, 0.5));
        scene.addGameObject(obj);
        scene.addGameObject(new GameObject("Box2", new Component[] { new Renderer(new Box(1, 1, 1)), new Collider(new Box(1, 1, 1)) }));
        obj.getComponent(Collider.class).collide();
        
        new AnimationTimer() 
        {
            public void handle(long currentNanoTime) 
            {
            	long time = System.nanoTime();
            	Time.time = (time - startTime) / 1000000000.0;
            	
                for (GameObject gameObject : SceneManager.activeScene.gameObjects) 
                {
                	gameObject.update();
                }
                
                Time.deltaTime = (System.nanoTime() - time) / 1000000000.0;
                
        		DecimalFormat df = new DecimalFormat("#.##");
        		df.setRoundingMode(RoundingMode.FLOOR);
                System.out.println(df.format(Time.time) + ": " + df.format(1.0 / Time.deltaTime) + " FPS");
            }
        }.start();
	}
	
	public void stop()
	{
		System.out.println("Terminated.");
	}
}