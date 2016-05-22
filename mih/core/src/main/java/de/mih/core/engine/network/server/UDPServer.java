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

import de.mih.core.engine.network.server.datagrams.AckDatagram;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;
import de.mih.core.engine.network.server.datagrams.DisconnectDatagram;

public class UDPServer extends UDPBase implements Runnable
{
	protected Connection newConnection(InetAddress ip, int port)
	{
		return new Connection(ip, port, this);
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

	// TODO: option to block new connections with a setter?
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
				this.executeConnectHandler(connection, null);
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

			// checkConnectionTimeouts();

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
			e.printStackTrace();
		}

	}

	public void sendToAllExcept(Connection exclude, BaseDatagram data, boolean reliable)
	{
		try
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
				System.out.println("send to: " + client.getPort());
				sendTo(client, serializiedData);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void sendToAll(byte[] data, boolean reliable) throws IOException
	{
		for (Connection client : connections.values())
		{
			System.out.println("send to: " + client.port);
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

	@Override
	public void run()
	{
		this.start();
	}

}
