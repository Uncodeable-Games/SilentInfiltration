package de.mih.core.engine.network.mediation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import de.mih.core.engine.network.mediation.MediationNetwork.*;
import de.mih.core.engine.network.server.UDPServer;
import de.mih.core.engine.network.server.BaseDatagram;
import de.mih.core.engine.network.server.ChatDatagram;
import de.mih.core.engine.network.server.Connection;
import de.mih.core.engine.network.server.DatagramReceiveHandler;

import de.mih.core.game.gamestates.LobbyState;

public class MediationServer implements DatagramReceiveHandler
{
	UDPServer server;
	HashMap<Integer, Lobby> lobbies;
	int lobbyID;
	ArrayList<Integer> freeIds;
	
	public MediationServer() throws IOException
	{
		lobbies = new HashMap<>();
		freeIds = new ArrayList<>();
		
		server = new UDPServer(MediationNetwork.udpPort){
			protected Connection newConnection (InetAddress ip, int port){
			// By providing our own connection implementation, we can store per
			// connection state without a connection ID to state look up.
			return new ChatConnection(ip, port);
		}
	};
	//	MediationNetwork.register(server);
		
	//	server.addListener(this);
		server.setDatagramReceiveHandler(this);
		//server.bind(MediationNetwork.tcpPort, MediationNetwork.udpPort);
		server.start();
	}
	
	@Override
	public void receive(Connection connection, BaseDatagram object) throws IOException
	{
		ChatConnection chatConnection = (ChatConnection)connection;
		System.out.println(object.getClass().getSimpleName());
		if (object instanceof RegisterName) {
			// Ignore the object if a client has already registered a name. This is
			// impossible with our client, but a hacker could send messages at any time.
			if (chatConnection.name != null) return;
			// Ignore the object if the name is invalid.
			String name = ((RegisterName)object).name;
			if (name == null) return;
			name = name.trim();
			if (name.length() == 0) return;
			// Store the name on the connection.
			chatConnection.name = name;
			// Send a "connected" message to everyone except the new client.
			ChatDatagram chatMessage = new ChatDatagram();
			chatMessage.message = name + " connected.";
			server.sendToAllExcept(connection, chatMessage);
			// Send everyone a new list of connection names.
			updateNames();
			ExternalInformation ext = new ExternalInformation();
			ext.address = connection.getIP();
			ext.udpPort = connection.getPort();
		//	ext.address = c.getRemoteAddressTCP().getHostString();
		//	ext.tcpPort = c.getRemoteAddressTCP().getPort();
		//	ext.udpPort = c.getRemoteAddressUDP().getPort();
			server.sendTo(connection, ext);
			return;
		}

		if (object instanceof ChatDatagram) {
			// Ignore the object if a client tries to chat before registering a name.
			if (chatConnection.name == null) return;
			ChatDatagram chatMessage = (ChatDatagram)object;
			// Ignore the object if the chat message is invalid.
			String message = chatMessage.message;
			if (message == null) return;
			message = message.trim();
			if (message.length() == 0) return;
			// Prepend the connection's name and send to everyone.
			chatMessage.message = chatConnection.name + ": " + message;
			server.sendToAll(chatMessage);
			return;
		}
		
		if (object instanceof RegisterLobby)
		{
			if(chatConnection.hostsLobby) return; //no multiple lobbies per client
			Lobby lobby = ((RegisterLobby) object).lobby;
			//lobby.address = c.getRemoteAddressTCP().getHostString();
			int newId = generateLobbyID();
			lobby.id = newId;
			lobbies.put(newId, lobby);
			RegisterLobby result = new RegisterLobby();
			result.lobby = lobby;
			result.id = newId;
			chatConnection.hostsLobby = true;
			chatConnection.lobbyID = newId;
			server.sendTo(connection, result);
		}
		
		if (object instanceof RequestLobbyUpdate)
		{
			UpdateLobbies updateLobbies = new UpdateLobbies();
			updateLobbies.lobbies = lobbies;
			server.sendTo(connection, updateLobbies);
		}
		
		if (object instanceof RequestLobbyJoin)
		{
			String target =  ((RequestLobbyJoin) object).targetAddress;
			Lobby targetLobby = ((RequestLobbyJoin) object).targetLobby;
			//System.out.println(c.oString() + " wants to join " + targetLobby.address + " " + targetLobby.tcpPort + " " + targetLobby.udpPort);
		}
	}
	
	//@Override
	public void disconnected(Connection c)
	{
//		ChatConnection connection = (ChatConnection)c;
//		if (connection.name != null) {
//			// Announce to everyone that someone (with a registered name) has left.
//			ChatMessage chatMessage = new ChatMessage();
//			chatMessage.text = connection.name + " disconnected.";
//			server.sendToAllTCP(chatMessage);
//			updateNames();
////			if(connection.hostsLobby)
////			{
////				this.lobbies.remove(connection.lobbyID);
////				this.freeIds.add(connection.lobbyID);
////			}
////			UpdateLobbies updateLobbies = new UpdateLobbies();
////			updateLobbies.lobbies = lobbies;
////			server.sendToTCP(c.getID(), updateLobbies);		
//		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		
		ArrayList<String> names = new ArrayList<>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			ChatConnection connection = (ChatConnection)connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAll(updateNames);
	}

	// This holds per connection state.
	static class ChatConnection extends Connection {
		
		public ChatConnection(InetAddress ip, int port)
		{
			super(ip, port);
		}
		
		public String name;
		public boolean hostsLobby;
		public int lobbyID;
	}	
	
	public static void main(String[] args) throws IOException
	{
		new MediationServer();
	}
	
	public int generateLobbyID()
	{
		int newID;
		if(!freeIds.isEmpty())
		{
			newID = freeIds.get(0);
			freeIds.remove(0);
		}
		else
		{
			newID = lobbyID++;
		}
		return newID;
	}
	
	public void freeLobbyID(int ID)
	{
		freeIds.add(ID);
	}
	
}
