package multiplayer.game;

import com.codedisaster.steamworks.SteamID;

public class SteamPlayer 
{
	public SteamID ID;
	public String name;
	
	public SteamPlayer(SteamID id)
	{
		update(id);
	}
	
	public void update(SteamID id)
	{
		ID = id;
	}
}