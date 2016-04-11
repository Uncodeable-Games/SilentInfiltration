package de.mih.core.engine.network;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import de.mih.core.engine.network.mediation.MediationNetwork;
import de.mih.core.engine.network.mediation.MediationNetwork.RequestLobbyJoin;



public class GameServer extends Listener
{
	Server server;
	
	public GameServer(int tcpPort, int udpPort, InetAddress mediationServer) throws IOException
	{
		this(tcpPort,udpPort,mediationServer.toString());
	}
	
	public GameServer(int tcpPort, int udpPort, String mediationServer) throws IOException
	{
		server = new Server();
		
		MediationNetwork.register(server);
		
		server.bind(tcpPort, udpPort);
		server.start();
	}
	
	@Override
	public void received(Connection c, Object object)
	{
		if (object instanceof RequestLobbyJoin)
		{
			String target =  ((RequestLobbyJoin) object).targetAddress;
			System.out.println(c.getRemoteAddressTCP().toString() + " wants to join " + target);
		}
		
//		if (object instanceof RegisterName) {
//			// Ignore the object if a client has already registered a name. This is
//			// impossible with our client, but a hacker could send messages at any time.
//			if (connection.name != null) return;
//			// Ignore the object if the name is invalid.
//			String name = ((RegisterName)object).name;
//			if (name == null) return;
//			name = name.trim();
//			if (name.length() == 0) return;
//			// Store the name on the connection.
//			connection.name = name;
//			// Send a "connected" message to everyone except the new client.
//			ChatMessage chatMessage = new ChatMessage();
//			chatMessage.text = name + " connected.";
//			server.sendToAllExceptTCP(connection.getID(), chatMessage);
//			// Send everyone a new list of connection names.
//			updateNames();
//			return;
//		}
//
//		if (object instanceof ChatMessage) {
//			// Ignore the object if a client tries to chat before registering a name.
//			if (connection.name == null) return;
//			ChatMessage chatMessage = (ChatMessage)object;
//			// Ignore the object if the chat message is invalid.
//			String message = chatMessage.text;
//			if (message == null) return;
//			message = message.trim();
//			if (message.length() == 0) return;
//			// Prepend the connection's name and send to everyone.
//			chatMessage.text = connection.name + ": " + message;
//			server.sendToAllTCP(chatMessage);
//			return;
//		}
		

	}
	
	@Override
	public void disconnected(Connection c)
	{
//		ChatConnection connection = (ChatConnection)c;
//		if (connection.name != null) {
//			// Announce to everyone that someone (with a registered name) has left.
//			ChatMessage chatMessage = new ChatMessage();
//			chatMessage.text = connection.name + " disconnected.";
//			server.sendToAllTCP(chatMessage);
//			updateNames();
//			if(connection.hostsLobby)
//			{
//				this.lobbies.remove(connection.lobbyID);
//				this.freeIds.add(connection.lobbyID);
//			}
//			UpdateLobbies updateLobbies = new UpdateLobbies();
//			updateLobbies.lobbies = lobbies;
//			server.sendToTCP(c.getID(), updateLobbies);		
//		}
	}
}
