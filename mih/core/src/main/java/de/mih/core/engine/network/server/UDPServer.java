package de.mih.core.engine.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mih.core.engine.network.mediation.MediationNetwork.ChatMessage;
import de.mih.core.engine.network.server.datagrams.AckDatagram;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;
import de.mih.core.engine.network.server.datagrams.DisconnectDatagram;

public class UDPServer extends UDPBase
{

	protected Connection newConnection(InetAddress ip, int port)
	{
		return new Connection(ip, port);
	}

	public UDPServer(int port) throws SocketException
	{
		super(port);
		this.maxConnections = 4;
	}

	public void start()
	{
		try
		{
			isRunning = true;
			receive();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (socket != null)
			{
				socket.close();
				isRunning = false;
			}
		}

	}

	private void receive() throws IOException
	{
		DatagramPacket receivePacket = null;
		while (!Thread.interrupted() && isRunning)
		{
			receivePacket = receivePacket();
			InetSocketAddress socketAddress = (InetSocketAddress) receivePacket.getSocketAddress();

			Connection connection;
			if (connections.containsKey(socketAddress))
			{
				connection = connections.get(socketAddress);
			}
			else if (activeConnections < maxConnections)
			{
				connection = addConnection(socketAddress);
				this.executeConnectHandler(connection);
			}
			else
			{
				// Ignore Packet
				continue;
			}

			BaseDatagram datagram = Serialization.deserializeDatagram(receivePacket.getData());

			if (datagram.reliable)
			{
				// this.receivedReliablePackets.push(datagram.sequenceNumber);
				AckDatagram ack = new AckDatagram();
				ack.responseID = datagram.sequenceNumber;
				sendTo(connection, ack, false);
				System.out.println("sending ack packet for: " + ack.responseID);
			}
			if (datagram instanceof AckDatagram)
			{
				AckDatagram ackDatagram = (AckDatagram) datagram;
				connection.removeAcknowledged(ackDatagram.responseID);
				continue;
			}
			else if (datagram instanceof DisconnectDatagram)
			{
				this.executeDisconnectHandler(connection);
			}
			// Ignores too old packets
			if (datagram.sequenceNumber > connection.getRemoteSequence())
			{
				executeReceiveHandler(connection, datagram);
			}
			connection.updateRemoteSequence(datagram.sequenceNumber);

			checkConnectionTimeouts();

		}
	}

	public void sendToAll(BaseDatagram data, boolean reliable)
	{

		try
		{
			setSequenceNumber(data);
			data.reliable = reliable;
			byte[] serializiedData = Serialization.serializeDatagram(data);
			for (Connection client : connections.values())
			{
				if (reliable)
				{
					client.awaitAcknowledge(data);
				}
				this.sendTo(client, serializiedData);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendToAllExcept(Connection exclude, BaseDatagram data, boolean reliable) throws IOException
	{

		setSequenceNumber(data);
		data.reliable = reliable;
		byte[] serializiedData = Serialization.serializeDatagram(data);
		for (Connection client : connections.values())
		{
			if (client.equals(exclude))
			{
				continue;
			}
			if (reliable)
			{
				client.awaitAcknowledge(data);
			}
			sendTo(client, serializiedData);
		}
	}

	void sendToAll(byte[] data, boolean reliable) throws IOException
	{
		for (Connection client : connections.values())
		{
			System.out.println("send to: " + client.toString());
			sendTo(client, data);
		}
	}

	public void stop()
	{
		if (isRunning)
		{
			isRunning = false;
			socket.close();
		}
	}

	public static void main(String args[]) throws SocketException
	{
		UDPServer server = new UDPServer(19876);
		server.setDatagramReceiveHandler(new DatagramReceiveHandler() {

			@Override
			public void receive(Connection connection, BaseDatagram datagram)
			{
				if (datagram instanceof ChatDatagram)
				{
					ChatDatagram chat = (ChatDatagram) datagram;
					System.out.println("RECEIVED: " + chat.message.toUpperCase());

					server.sendToAll(chat, true);

				}

			}

			@Override
			public void connected(Connection connection)
			{
				server.sendTo(connection, new ConnectApprove(), true);
				// server.sendToAllExcept(connection, new ConnectApprove,
				// reliable);
			}

			@Override
			public void disconnected(Connection connection)
			{
				// TODO Auto-generated method stub

			}
		});
		server.start();
		server.stop();
	}

}
