package de.mih.core.game.network.mediation;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.*;

import de.mih.core.engine.network.mediation.MediationNetwork.*;
import de.mih.core.engine.network.server.Connection;
import de.mih.core.engine.network.server.DatagramReceiveHandler;
import de.mih.core.engine.network.server.UDPClient;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.game.gamestates.LobbyState;
import de.mih.core.game.network.GameClient;
import de.mih.core.game.network.GameServer;

public class MediationClient2 implements DatagramReceiveHandler
{
	UDPClient client;
	String name;
	HashMap<Integer, Lobby> lobbies;

	int tcpPort, udpPort;

	// DEMO
	public static final String MEDIATIONSERVER = "127.0.0.1";
	protected GameServer gameServer;
	protected GameClient gameClient;

	public MediationClient2() throws IOException
	{
		client = new UDPClient(MEDIATIONSERVER, MediationNetwork.udpPort);

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		// MediationNetwork.register(client);
		client.setDatagramReceiveHandler(this);
		String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server",
				JOptionPane.QUESTION_MESSAGE, null, null, MEDIATIONSERVER);
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		final String host = input.trim();

		// Request the user's name.
		input = (String) JOptionPane.showInputDialog(null, "Name:", "Connect to chat server",
				JOptionPane.QUESTION_MESSAGE, null, null, "Test");
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		name = input.trim();

		// All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter
		// the KryoNet example code.
		// This listener is called when the send button is clicked.

		// This listener is called when the chat window is closed.

		//
		// chatFrame.selectCallback = new listSelect() {
		//
		// @Override
		// public void selected(Lobby selected)
		// {
		// RequestLobbyJoin joinRequest = new RequestLobbyJoin();
		// joinRequest.targetLobby = selected;
		// joinRequest.targetAddress = selected.address;
		// client.sendData(joinRequest, true);
		// new Thread("Connect") {
		// public void run()
		// {
		//// while(true)
		//// {
		// try
		// {
		// MediationClient2.this.gameClient = new GameClient();
		// client.close();
		// gameClient.connect(5000, selected.address, selected.tcpPort,
		// selected.udpPort);
		// // Server communication after connection can go here, or in
		// // Listener#connected().
		// //break;
		// }
		// catch (IOException ex)
		// {
		// ex.printStackTrace();
		// }
		//// }
		// }
		// }.start();
		// }
		//
		// };

		System.out.println("starting");
		client.start();
		System.out.println("started!");
	}

	@Override
	public void connected(Connection connection)
	{
		RegisterName registerName = new RegisterName();
		registerName.name = name;
		client.sendTo(connection, registerName, true);
		System.out.println("send register name: " + name);
	}

	@Override
	public void receive(Connection connection, BaseDatagram object)
	{
		// System.out.println(connection.getID());
		if (object instanceof ExternalInformation)
		{
			ExternalInformation ext = (ExternalInformation) object;
			this.udpPort = ext.udpPort;
			this.tcpPort = ext.tcpPort;
			System.out.println(ext.address + " " + ext.tcpPort + " " + ext.udpPort);
			return;
		}
		if (object instanceof UpdateNames)
		{
			UpdateNames updateNames = (UpdateNames) object;
			return;
		}

		if (object instanceof ChatDatagram)
		{
			ChatDatagram chatMessage = (ChatDatagram) object;
			return;
		}

		if (object instanceof UpdateLobbies)
		{
			UpdateLobbies update = (UpdateLobbies) object;
			// this.lobbies = update.lobbies;
		}

		if (object instanceof RegisterLobby)
		{

		}
	}

	@Override
	public void disconnected(Connection connection)
	{

	}

	public void sendChatMessage(String message)
	{
		ChatDatagram chatMessage = new ChatDatagram();
		chatMessage.message = message;
		client.sendData(chatMessage, true);
	}

	public void requestLobbyUpdate()
	{
		client.sendData(new RequestLobbyUpdate(), true);
	}

	public void createNewLobby()
	{
		RegisterLobby newLobby = new RegisterLobby();
		newLobby.lobby = new Lobby();
		newLobby.lobby.name = "TestLobby";
		newLobby.lobby.players = 1;
		// newLobby.lobby.address =
		// this send has somehow to come from the server itself?
		// try
		// {
		newLobby.lobby.tcpPort = tcpPort;
		newLobby.lobby.udpPort = udpPort;
		client.sendData(newLobby, true);
		// client.close();
		// MediationClient2.this.gameServer = new GameServer(
		// MediationNetwork.tcpPort, MediationNetwork.udpPort, MEDIATIONSERVER);

		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
	}
}
