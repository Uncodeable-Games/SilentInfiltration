package de.mih.core.engine.network;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import de.mih.core.engine.network.Network.*;

public class MediationServer
{
	Server server;
	
	public MediationServer() throws IOException
	{
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new ChatConnection();
			}
		};
		Network.register(server);
		
		server.addListener(new Listener() 
		{
			public void received (Connection c, Object object) {
				MediationServer.this.received(c, object);
			}
			
			public void disconnected (Connection c) {
				MediationServer.this.disconnected(c);
			}
			
		});
		
		server.bind(Network.port);
		server.start();
	}
	
	private void received(Connection c, Object object)
	{
		ChatConnection connection = (ChatConnection)c;

		if (object instanceof RegisterName) {
			// Ignore the object if a client has already registered a name. This is
			// impossible with our client, but a hacker could send messages at any time.
			if (connection.name != null) return;
			// Ignore the object if the name is invalid.
			String name = ((RegisterName)object).name;
			if (name == null) return;
			name = name.trim();
			if (name.length() == 0) return;
			// Store the name on the connection.
			connection.name = name;
			// Send a "connected" message to everyone except the new client.
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.text = name + " connected.";
			server.sendToAllExceptTCP(connection.getID(), chatMessage);
			// Send everyone a new list of connection names.
			updateNames();
			return;
		}

		if (object instanceof ChatMessage) {
			// Ignore the object if a client tries to chat before registering a name.
			if (connection.name == null) return;
			ChatMessage chatMessage = (ChatMessage)object;
			// Ignore the object if the chat message is invalid.
			String message = chatMessage.text;
			if (message == null) return;
			message = message.trim();
			if (message.length() == 0) return;
			// Prepend the connection's name and send to everyone.
			chatMessage.text = connection.name + ": " + message;
			server.sendToAllTCP(chatMessage);
			return;
		}
	}
	
	private void disconnected(Connection c)
	{
		ChatConnection connection = (ChatConnection)c;
		if (connection.name != null) {
			// Announce to everyone that someone (with a registered name) has left.
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.text = connection.name + " disconnected.";
			server.sendToAllTCP(chatMessage);
			updateNames();
		}
	}
	

	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			ChatConnection connection = (ChatConnection)connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}

	// This holds per connection state.
	static class ChatConnection extends Connection {
		public String name;
	}	
	
	public static void main(String[] args) throws IOException
	{
		new MediationServer();
	}
	
	
}
