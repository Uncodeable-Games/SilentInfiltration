package de.mih.core.engine.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPBase
{
	boolean isRunning = false;
	int port;
	int sequenceNumber;

	DatagramSocket socket;
	DatagramReceiveHandler receiveHandler;
	ExecutorService threadPool;

	HashMap<InetSocketAddress, Connection> connections;
	
	public UDPBase() throws SocketException
	{
		this.connections = new HashMap<>();
		this.threadPool = Executors.newFixedThreadPool(10);
		this.socket = new DatagramSocket();
	}
	public UDPBase(int port) throws SocketException
	{
		this.port = port;
		this.connections = new HashMap<>();
		this.threadPool = Executors.newFixedThreadPool(10);
		this.socket = new DatagramSocket(port);
	}
	
	protected Connection newConnection(InetAddress ip, int port)
	{
		return new Connection(ip, port);
	}

	public Connection[] getConnections()
	{
		return connections.values().toArray(new Connection[connections.values().size()]);
	}
	
	Connection addConnection(InetSocketAddress socketAddress)
	{
		System.out.println("new connection: " + socketAddress.toString());
		Connection connection = newConnection(socketAddress.getAddress(), socketAddress.getPort());
		this.connections.put(socketAddress, connection);
		return connection;
	}

	
	public void setDatagramReceiveHandler(DatagramReceiveHandler receiveHandler)
	{
		this.receiveHandler = receiveHandler;
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
	
	void executeConnectHandler(final Connection connection)
	{
		if (this.receiveHandler != null)
		{
			threadPool.execute(new Runnable() {
				public void run()
				{
					receiveHandler.connect(connection);
				}
			});
		}
		else
		{
			// Error?
		}
	}
	
	void executeDisconnectHandler(final Connection connection)
	{
		if (this.receiveHandler != null)
		{
			threadPool.execute(new Runnable() {
				public void run()
				{
					receiveHandler.disconnect(connection);
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
		int bufferSize = socket.getReceiveBufferSize();
		byte[] bufferBytes = new byte[bufferSize];
		DatagramPacket receivePacket = new DatagramPacket(bufferBytes, bufferSize);
		socket.receive(receivePacket);
		return receivePacket;
	}
	
	public void sendTo(Connection client, BaseDatagram data, boolean reliable)
	{
		setSequenceNumber(data);
		data.reliable = reliable;
		if (reliable)
		{
			client.awaitAcknowledge(data);
		}
		try
		{
			sendTo(client, Serialization.serializeDatagram(data));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void sendTo(Connection client, byte[] data) throws IOException
	{
		socket.send(new DatagramPacket(data, data.length, client.getIP(), client.getPort()));
	}
	
	protected void setSequenceNumber(BaseDatagram data)
	{
		if (data.sequenceNumber == -1)
		{
			data.sequenceNumber = sequenceNumber++;
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
}
