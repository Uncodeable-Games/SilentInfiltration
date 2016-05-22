package de.mih.core.engine.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.ValidationEvent;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;

public class UDPBase
{
	int maxConnections;
	int activeConnections;

	boolean isRunning = false;
	int port;
	int sequenceNumber;

	long lastConnectionCheck = 0;
	final long connectionTimeout = 20000;//5000;
	long lastSendPacket = 0;

	DatagramSocket socket;
	DatagramReceiveHandler receiveHandler;
	ExecutorService threadPool;

	HashMap<InetSocketAddress, Connection> connections;
	
	public UDPBase() throws SocketException
	{
		this.connections = new HashMap<>();
		this.threadPool = Executors.newFixedThreadPool(10);
		this.socket = new DatagramSocket();
		this.port = socket.getLocalPort();
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
		return new Connection(ip, port, this);
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
		activeConnections++;
		return connection;
	}

	
	public void setDatagramReceiveHandler(DatagramReceiveHandler receiveHandler)
	{
		this.receiveHandler = receiveHandler;
	}

	protected void checkConnectionTimeouts()
	{
		long timediff = System.currentTimeMillis() - lastConnectionCheck;
		lastConnectionCheck = System.currentTimeMillis();
		if(timediff >= connectionTimeout)
		{
			Collection<InetSocketAddress> connectionKeys = this.connections.keySet();
			HashMap<InetSocketAddress, Connection>  validConnections = new HashMap<>();
			
			for(InetSocketAddress key : connectionKeys)
			{
				Connection c = this.connections.get(key);		
				if(c.isConnectionLost())
				{
					executeDisconnectHandler(c);
					activeConnections--;
				}
				else
				{
					validConnections.put(key, c);
				}
			}
			this.connections = validConnections;
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
	
	void executePacketLostHandler(final Connection connection, final BaseDatagram lostDatagram)
	{
		if (this.receiveHandler != null)
		{
			threadPool.execute(new Runnable() {
				public void run()
				{
					receiveHandler.packetLost(connection, lostDatagram);
				}
			});
		}
		else
		{
			// Error?
		}
	}
	
	void executeConnectHandler(final Connection connection, final ConnectApprove datagram)
	{
		if (this.receiveHandler != null)
		{
			threadPool.execute(new Runnable() {
				public void run()
				{
					receiveHandler.connected(connection, datagram);
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
					receiveHandler.disconnected(connection);
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
			System.out.println("To be acked: " + data.sequenceNumber);
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
		this.lastSendPacket = System.currentTimeMillis();
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
