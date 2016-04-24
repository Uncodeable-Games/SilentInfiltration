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

import de.mih.core.engine.network.mediation.MediationNetwork;
import de.mih.core.engine.network.mediation.MediationNetwork.RequestLobbyJoin;

public class UDPClient extends UDPBase
{
//	InetAddress server;
//	int port;
//	DatagramSocket clientSocket;
//	DatagramReceiveHandler receiveHandler;
//	ExecutorService threadPool;
//	private boolean isRunning;
	Thread receiverThread;
//	int sequenceNumber;
	Connection serverConnection;
	Thread packetLoss;

	public UDPClient(String ip, int port) throws UnknownHostException, SocketException
	{
		super();
		//this.port = port;
		serverConnection = new Connection(InetAddress.getByName(ip), port);
	}


	public void start() throws IOException
	{
		isRunning = true;
		receiverThread = new Thread("receiver") {

			public void run()
			{
				while (!Thread.interrupted() && isRunning)
				{
					try
					{
						DatagramPacket receivePacket = receivePacket();
						BaseDatagram datagram = Serialization.deserializeDatagram(receivePacket.getData());
						InetSocketAddress socketAddress = (InetSocketAddress) receivePacket.getSocketAddress();
						
						if(!socketAddress.getAddress().equals(serverConnection.getIP()))
						{
							//Drop packets that do not come from the known server
							continue;
						}
						// receive(Serialization.deserializeDatagram(receivePacket.getData()));
//						if (datagram.sequenceNumber > serverConnection.getRemoteSequence())
//						{
							serverConnection.updateRemoteSequence(datagram.sequenceNumber);
							if (datagram.reliable)
							{
								// this.receivedReliablePackets.push(datagram.sequenceNumber);
								AckDatagram ack = new AckDatagram();
								ack.responseID = datagram.sequenceNumber;
								sendTo(serverConnection, ack, false);
							}
							if (datagram instanceof AckDatagram)
							{
								AckDatagram ackDatagram = (AckDatagram) datagram;
								serverConnection.removeAcknowledged(ackDatagram.responseID);
								continue;
							}
							else if(datagram instanceof DisconnectDatagram)
							{
								executeDisconnectHandler(serverConnection);
							}
							executeReceiveHandler(serverConnection, datagram);

//						}
//						else
//						{
//							// Older packet ignore
//						}
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		receiverThread.start();
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();
		while (!sentence.equals("exit"))
		{
			ChatDatagram chatMessage = new ChatDatagram();
			chatMessage.message = sentence;
			sendData(chatMessage, false);
			sentence = inFromUser.readLine();
		}
//		packetLoss = new Thread("lostpackets"){
//			public void run()
//			{
//				while (!Thread.interrupted() && isRunning)
//				{
//					try
//					{
//						System.out.println("Checking packets: ");
//						serverConnection.checkLostPackets();
//						Thread.sleep(500);
//					}
//					catch (InterruptedException e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		packetLoss.start();
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

	public static void main(String args[]) throws Exception
	{

		UDPClient client = new UDPClient("localhost", 9876);
		client.setDatagramReceiveHandler(new DatagramReceiveHandler() {

			@Override
			public void receive(Connection connection, BaseDatagram datagram)
			{
				if (datagram instanceof ChatDatagram)
				{
					System.out.println("FROM SERVER: " + ((ChatDatagram) datagram).message);
				}
			}

			@Override
			public void connect(Connection connection)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void disconnect(Connection connection)
			{
				// TODO Auto-generated method stub
				
			}
		});
		client.start();

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
