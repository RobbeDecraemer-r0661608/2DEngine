package game.engine;

import java.io.File;

import org.w3c.dom.Node;

import javafx.scene.media.*;

public class AudioSource extends Component
{
	public String path;
	public boolean loop;
	public boolean mute;
	public boolean spacialSound;
	public float pan;
	public float speed;
	public float volume;
	private MediaPlayer mediaPlayer;
	
	public AudioSource(String audioPath)
	{
		path = audioPath;
	}
	
	public AudioSource(Node xml)
	{
		
	}
	
	public void start()
	{
		super.start();
		mediaPlayer = new MediaPlayer(new Media(new File("D:/Documents/GitHub/2DEngine/" + path).toURI().toString()));
		setLoop(false);
		setMute(false);
		setPan(0f);
		setSpeed(1f);
		setVolume(1f);
		spacialSound = false;
		mediaPlayer.play();
	}
	
	public void play()
	{
		mediaPlayer.play();
	}
	
	public void pause()
	{
		mediaPlayer.pause();
	}
	
	public void stop()
	{
		super.stop();
		mediaPlayer.stop();
	}
	
	public void setLoop(boolean loop)
	{
		mediaPlayer.setAutoPlay(loop);
	}
	
	public void setMute(boolean	mute)
	{
		mediaPlayer.setMute(mute);
	}
	
	public void setSpeed(float speed)
	{
		mediaPlayer.setRate(speed);
	}
	
	public void setPan(float pan)
	{
		mediaPlayer.setBalance(pan);
	}
	
	public void setVolume(float volume)
	{
		mediaPlayer.setVolume(volume);
	}
	
	public void setValues(AudioListener audioListener)
	{
		if(spacialSound)
		{
			float pan = (gameObject.transform.position.x - audioListener.gameObject.transform.position.x) / 1000f;
			float volume = Vector2.distance(gameObject.transform.position, audioListener.gameObject.transform.position) / 1000f;
			if(mediaPlayer.getBalance() != pan) mediaPlayer.setBalance(pan);
			if(mediaPlayer.getVolume() != volume) mediaPlayer.setVolume(volume);
		}
	}
}