package de.mih.core.engine.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mih.core.engine.network.mediation.MediationNetwork.ChatMessage;

public class UDPServer
{
	boolean isRunning = false;
	int port;
	DatagramSocket serverSocket;
	DatagramReceiveHandler receiveHandler;
	ExecutorService threadPool;

	HashMap<InetSocketAddress, Connection> clients;

	protected Connection newConnection(InetAddress ip, int port)
	{
		return new Connection(ip, port);
	}
	
	public Connection[] getConnections()
	{
		return clients.values().toArray(new Connection[clients.values().size()]);
	}
	
	public UDPServer(int port)
	{
		this.port = port;
		clients = new HashMap<>();
		this.threadPool = Executors.newFixedThreadPool(10);
	}

	public void start()
	{
		try
		{
			serverSocket = new DatagramSocket(port);
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
			if(serverSocket != null)
			{
				serverSocket.close();
				isRunning = false;
			}
		}

	}

	public void setDatagramReceiveHandler(DatagramReceiveHandler receiveHandler)
	{
		this.receiveHandler = receiveHandler;
	}

	Connection addClientConnection(InetSocketAddress socketAddress)
	{
		System.out.println("new connection: " + socketAddress.toString());
		Connection connection = newConnection(socketAddress.getAddress(), socketAddress.getPort());
		this.clients.put(socketAddress, connection);
		return connection;
	}

	private void receive() throws IOException
	{
		DatagramPacket receivePacket = null;
		while (!Thread.interrupted() && isRunning)
		{
			receivePacket = receivePacket();
			InetSocketAddress socketAddress = (InetSocketAddress) receivePacket.getSocketAddress();
			// receivePacket.get

			Connection connection;
			if (clients.containsKey(socketAddress))
			{
				connection = clients.get(socketAddress);
//				if (!(connection.port == port))
//				{
//					connection = addClientConnection(socketAddress);
//				}
			}
			else
			{
				connection = addClientConnection(socketAddress);
			}

			BaseDatagram datagram = Serialization.deserializeDatagram(receivePacket.getData());
			executeReceiveHandler(connection, datagram);

			// DEMO:
			// receive(connection,
			// Serialization.deserializeDatagram(receivePacket.getData()));
		}
	}

	void executeReceiveHandler(final Connection connection, final BaseDatagram datagram)
	{
		if (this.receiveHandler != null)
		{
			threadPool.execute(new Runnable() {
				public void run()
				{
					try
					{
						receiveHandler.receive(connection, datagram);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		else
		{
			// Error?
		}
	}

	DatagramPacket receivePacket() throws IOException
	{
		int bufferSize = serverSocket.getReceiveBufferSize();
		byte[] bufferBytes = new byte[bufferSize];
		DatagramPacket receivePacket = new DatagramPacket(bufferBytes, bufferSize);
		serverSocket.receive(receivePacket);
		return receivePacket;
	}

	public void sendToAll(BaseDatagram data)
	{

		try
		{
			this.sendToAll(Serialization.serializeDatagram(data));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void sendToAllExcept(Connection exclude, BaseDatagram data) throws IOException
	{
		for (Connection client : clients.values())
		{
			if(client.equals(exclude)) { continue; }
			sendTo(client, data);
		}
	}

	void sendToAll(byte[] data) throws IOException
	{
		for (Connection client : clients.values())
		{
			System.out.println("send to: " + client.toString());
			sendToClient(client, data);
		}
	}

	public void sendTo(Connection client, BaseDatagram data) throws IOException
	{
		sendToClient(client, Serialization.serializeDatagram(data));
	}

	void sendToClient(Connection client, byte[] data) throws IOException
	{
		serverSocket.send(new DatagramPacket(data, data.length, client.getIP(), client.getPort()));
	}

	public void stop()
	{
		if (isRunning)
		{
			isRunning = false;
			serverSocket.close();
		}
	}

	public static void main(String args[])
	{
		UDPServer server = new UDPServer(9876);
		server.setDatagramReceiveHandler(new DatagramReceiveHandler() {

			@Override
			public void receive(Connection connection, BaseDatagram datagram)
			{
				if (datagram instanceof ChatDatagram)
				{
					ChatDatagram chat = (ChatDatagram) datagram;
					System.out.println("RECEIVED: " + chat.message.toUpperCase());

					server.sendToAll(chat);

				}

			}
		});
		server.start();
		server.stop();
	}



}
