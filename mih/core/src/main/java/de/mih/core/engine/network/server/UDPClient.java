package de.mih.core.engine.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mih.core.engine.network.server.datagrams.AckDatagram;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;
import de.mih.core.engine.network.server.datagrams.ConnectRequest;
import de.mih.core.engine.network.server.datagrams.DisconnectDatagram;

public class UDPClient extends UDPBase
{
	// InetAddress server;
	// int port;
	// DatagramSocket clientSocket;
	// DatagramReceiveHandler receiveHandler;
	// ExecutorService threadPool;
	// private boolean isRunning;
	Thread receiverThread;
	// int sequenceNumber;
	Connection serverConnection;
	Thread packetLoss;

	public UDPClient(String ip, int port) throws UnknownHostException, SocketException
	{
		super();
		this.maxConnections = 1;
		serverConnection = new Connection(InetAddress.getByName(ip), port, this);
	}

	// long lastPacket = 0;
	public void start() throws IOException
	{
		isRunning = true;
		receiverThread = new Thread() {

			public void run()
			{
				while (!Thread.interrupted() && isRunning)
				{
					try
					{
						DatagramPacket receivePacket = receivePacket();
						BaseDatagram datagram = Serialization.deserializeDatagram(receivePacket.getData());
						InetSocketAddress socketAddress = (InetSocketAddress) receivePacket.getSocketAddress();
						System.out.println("received: " + datagram.getClass().getSimpleName());
						if (!socketAddress.getAddress().equals(serverConnection.getIP()))
						{
							// Drop packets that do not come from the known
							// server
							System.out.println("not from server!");
							continue;
						}
						if (datagram.reliable)
						{
							// this.receivedReliablePackets.push(datagram.sequenceNumber);
							AckDatagram ack = new AckDatagram();
							ack.responseID = datagram.sequenceNumber;
							System.out.println("Sending ack for " + ack.responseID);
							sendTo(serverConnection, ack, false);
						}
						if (datagram instanceof AckDatagram)
						{
							AckDatagram ackDatagram = (AckDatagram) datagram;
							System.out.println("Received ack for " + ackDatagram.responseID);
							serverConnection.removeAcknowledged(ackDatagram.responseID);
							continue;
						}
						else if (datagram instanceof DisconnectDatagram)
						{
							executeDisconnectHandler(serverConnection);
						}
						else if (datagram instanceof ConnectApprove)
						{
							executeConnectHandler(serverConnection, (ConnectApprove) datagram);
						}
						// Ignores too old packets
//						if (datagram.sequenceNumber > serverConnection.getRemoteSequence())
						{
							executeReceiveHandler(serverConnection, datagram);
						}
//						else
//							System.out.println("ignored");
						serverConnection.updateRemoteSequence(datagram.sequenceNumber);
						//checkConnectionTimeouts();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		};
		receiverThread.start();
//		sendData(new ConnectRequest(), true);
//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		String sentence = inFromUser.readLine();
//		while (!sentence.equals("exit"))
//		{
//			ChatDatagram chatMessage = new ChatDatagram();
//			chatMessage.message = sentence;
//			sendData(chatMessage, true);
//			sentence = inFromUser.readLine();
//		}
		// packetLoss = new Thread("lostpackets"){
		// public void run()
		// {
		// while (!Thread.interrupted() && isRunning)
		// {
		// try
		// {
		// System.out.println("Checking packets: ");
		// serverConnection.checkLostPackets();
		// Thread.sleep(500);
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// }
		// }
		// };
		// packetLoss.start();
	}

	public void close()
	{
		isRunning = false;
		receiverThread.stop();
		socket.close();
	}

	public void stop()
	{
		this.close();
	}


	public Connection getServerConnection()
	{
		return serverConnection;
	}

	public void sendData(BaseDatagram data, boolean reliable)
	{
		this.sendTo(serverConnection, data, reliable);
	}

}
