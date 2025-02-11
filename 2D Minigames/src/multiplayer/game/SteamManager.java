package multiplayer.game;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.SteamAuth.AuthSessionResponse;
import com.codedisaster.steamworks.SteamMatchmaking.LobbyComparison;

import character.controller.MultiplayerCharacterScene;
import game.engine.Component;
import game.engine.GameObject;

public class SteamManager extends Component
{
	public static SteamManager instance;
	
	public SteamPlayer user;
	public SteamUser userc;
	public SteamUtils utils;
	public SteamMatchmaking matchmaking;
	public SteamNetworking networking;
	public SteamLobby lobby;
	public Map<Long, SteamID> lobbies = new HashMap<Long, SteamID>();
	
	public void start()
	{
		if(instance != null)
		{
			GameObject.destroy(gameObject);
			return;
		}
		
		GameObject.dontDestroyOnLoad(gameObject);
		instance = this;
		
		try 
		{
			if (!SteamAPI.init()) 
			{
				return;
			}
		} 
		
		catch (SteamException e) 
		{
			e.printStackTrace();
		}

		SteamAPI.printDebugInfo(System.out);

		// Initialize SteamObjects
		networking = new SteamNetworking(networkingCallback);
		matchmaking = new SteamMatchmaking(matchmakingCallback);
		matchmaking.addRequestLobbyListStringFilter("GameType", "CharacterController", LobbyComparison.Equal);

		utils = new SteamUtils(UtilsCallback);
		utils.setWarningMessageHook(MessageHook);
		userc = new SteamUser(userCallback);
		user = new SteamPlayer(userc.getSteamID());
		
		System.out.println("Steam initialized.");
	}
	
	public void fixedUpdate()
	{
		if (SteamAPI.isSteamRunning()) 
		{
			SteamAPI.runCallbacks();
			
			// Update SteamObjects
		}
	}
	
	public void startGame()
	{
		String result = "cmd:startgame";
		for(SteamPlayer player : lobby.players) result += ":" + SteamID.getNativeHandle(player.ID);
		matchmaking.sendLobbyChatMsg(lobby.ID, result);
	}
	
	public void stop()
	{
		if(instance == this && SteamAPI.isSteamRunning())
		{
			utils.dispose();

			// Destroy SteamObjects
			matchmaking.dispose();
		
			SteamAPI.shutdown();
		}
	}
	
	private SteamAPIWarningMessageHook MessageHook = new SteamAPIWarningMessageHook() 
	{
		public void onWarningMessage(int severity, String message) 
		{
			System.err.println("[client debug message] (" + severity + ") " + message);
		}
	};

	private SteamUtilsCallback UtilsCallback = new SteamUtilsCallback() 
	{
		public void onSteamShutdown() 
		{
			System.err.println("Steam client requested to shut down!");
		}
	};
	
	private SteamMatchmakingCallback matchmakingCallback = new SteamMatchmakingCallback() 
	{
		private final ByteBuffer chatMessage = ByteBuffer.allocateDirect(4096);
		private final SteamMatchmaking.ChatEntry chatEntry = new SteamMatchmaking.ChatEntry();
		private final Charset messageCharset = Charset.forName("UTF-8");

		public void onLobbyEnter(SteamID steamIDLobby, int chatPermissions, boolean blocked, SteamMatchmaking.ChatRoomEnterResponse response) 
		{
			System.out.println("Lobby entered: " + steamIDLobby);

			int numMembers = matchmaking.getNumLobbyMembers(steamIDLobby);
			System.out.println("  - " + numMembers + " members in lobby");
			
			for (int i = 0; i < numMembers; i++) 
			{
				SteamID member = matchmaking.getLobbyMemberByIndex(steamIDLobby, i);
				System.out.println("    - " + i + ": accountID=" + member.getAccountID());
			}
			
			lobby = new SteamLobby(steamIDLobby);
			new LobbyPlayersScene().activate();
		}

		public void onLobbyDataUpdate(SteamID steamIDLobby, SteamID steamIDMember, boolean success) 
		{
			System.out.println("Lobby data update for " + steamIDLobby);

			if(lobby != null && lobby.ID.equals(steamIDLobby))
				lobby.update(steamIDLobby);
		}

		public void onLobbyChatUpdate(SteamID steamIDLobby, SteamID steamIDUserChanged, SteamID steamIDMakingChange, SteamMatchmaking.ChatMemberStateChange stateChange) 
		{
			System.out.println("Lobby chat update for " + steamIDLobby);
			System.out.println("  - state changed: " + stateChange.name());
			
			if(lobby != null && lobby.ID.equals(steamIDLobby))
				lobby.update(steamIDLobby);
		}

		public void onLobbyChatMessage(SteamID steamIDLobby, SteamID steamIDUser, SteamMatchmaking.ChatEntryType entryType, int chatID) 
		{
			try 
			{
				int size = matchmaking.getLobbyChatEntry(steamIDLobby, chatID, chatEntry, chatMessage);
				System.out.println("Lobby chat message #" + chatID + " has " + size + " bytes");

				byte[] bytes = new byte[size];
				chatMessage.get(bytes);

				String message = new String(bytes, messageCharset);
				String[] split = message.trim().split(":");
				
				if(!split[0].equals("cmd")) return;
				
				switch(split[1])
				{
				case "startgame":
					SteamPlayer[] players = new SteamPlayer[split.length - 2];
					for(int i = 0; i < players.length; i++) players[i] = new SteamPlayer(SteamID.createFromNativeHandle(Long.parseLong(split[i + 2])));
					new MultiplayerCharacterScene().activate(players);
					break;
				}

			} 
			
			catch (SteamException e) 
			{
				e.printStackTrace();
			}
		}

		public void onLobbyGameCreated(SteamID steamIDLobby, SteamID steamIDGameServer, int ip, short port) 
		{
			System.out.println("[onLobbyGameCreated]");
		}

		public void onLobbyMatchList(int lobbiesMatching) 
		{
			lobbies.clear();
			
			for (int i = 0; i < lobbiesMatching; i++)
			{
				SteamID lobby = matchmaking.getLobbyByIndex(i);
				matchmaking.requestLobbyData(lobby);
				lobbies.put(SteamID.getNativeHandle(lobby), lobby);
			}
			
			System.out.println("Found " + lobbies.size() + " matching lobbies.");

			for (Map.Entry<Long, SteamID> lobby : lobbies.entrySet()) 
			{
				System.out.print("  " + Long.toHexString(lobby.getKey()) + ": ");
				
				if (lobby.getValue().isValid()) 
				{
					int members = matchmaking.getNumLobbyMembers(lobby.getValue());
					System.out.println(members + " members");
				} 
				
				else 
				{
					System.err.println("invalid SteamID!");
				}
			}
			
			new LobbyJoinScene().activate();
		}

		public void onLobbyKicked(SteamID steamIDLobby, SteamID steamIDAdmin, boolean kickedDueToDisconnect) 
		{
			System.out.println("Kicked from lobby: " + steamIDLobby);
			System.out.println("  - by user (admin): " + steamIDAdmin.getAccountID());
			System.out.println("  - kicked due to disconnect: " + (kickedDueToDisconnect ? "yes" : "no"));

			lobby = null;
			new LobbyScene().activate();
		}

		public void onLobbyCreated(SteamResult result, SteamID steamIDLobby) 
		{
			System.out.println("Lobby created: " + steamIDLobby);
			System.out.println("  - result: " + result.name());
			
			if (result == SteamResult.OK) 
			{
				lobbies.put(SteamID.getNativeHandle(steamIDLobby), steamIDLobby);
				matchmaking.setLobbyData(steamIDLobby, "GameType", "CharacterController");
			}

			lobby = new SteamLobby(steamIDLobby);
			new LobbyPlayersScene().activate();
		}

		public void onFavoritesListAccountsUpdated(SteamResult arg0) { }
		public void onFavoritesListChanged(int arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, int arg6) { }
		public void onLobbyInvite(SteamID arg0, SteamID arg1, long arg2) { }
	};
	
	private SteamNetworkingCallback networkingCallback = new SteamNetworkingCallback() 
	{
		public void onP2PSessionConnectFail(SteamID steamIDRemote, SteamNetworking.P2PSessionError sessionError) 
		{
			System.out.println("P2P connection failed: userID=" + steamIDRemote.getAccountID() + ", error: " + sessionError);
		}

		public void onP2PSessionRequest(SteamID steamIDRemote) 
		{
			System.out.println("P2P connection requested by userID " + steamIDRemote.getAccountID());
			networking.acceptP2PSessionWithUser(steamIDRemote);
		}
	};
	
	private SteamUserCallback userCallback = new SteamUserCallback() 
	{
		@Override
		public void onAuthSessionTicket(SteamAuthTicket steamAuthTicket, SteamResult steamResult) {

		}

		@Override
		public void onValidateAuthTicket(SteamID steamID, AuthSessionResponse authSessionResponse, SteamID steamID1) {

		}

		@Override
		public void onMicroTxnAuthorization(int i, long l, boolean b) {

		}

		@Override
		public void onEncryptedAppTicket(SteamResult steamResult) {

		}
	};
}