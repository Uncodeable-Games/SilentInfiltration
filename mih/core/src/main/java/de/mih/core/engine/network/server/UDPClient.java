package de.mih.core.engine.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mih.core.engine.network.mediation.MediationNetwork;

public class UDPClient
{
	InetAddress server;
	int port;
	DatagramSocket clientSocket;
	DatagramReceiveHandler receiveHandler;
	ExecutorService threadPool;
	private boolean isRunning;
	Thread receiverThread;

	public UDPClient(String ip, int port) throws UnknownHostException, SocketException
	{
		this.port = port;
		this.server = InetAddress.getByName(ip);
		clientSocket = new DatagramSocket();
		this.threadPool = Executors.newFixedThreadPool(10);
	}
	
	public void setDatagramReceiveHandler(DatagramReceiveHandler receiveHandler)
	{
		this.receiveHandler = receiveHandler;
	}

	public void start() throws IOException
	{
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//String sentence = inFromUser.readLine();
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
						//receive(Serialization.deserializeDatagram(receivePacket.getData()));
						executeReceiveHandler(null, datagram);
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
//		while (!sentence.equals("exit"))
//		{
//			ChatDatagram chatMessage = new ChatDatagram();
//			chatMessage.message = sentence;
//			sendData(chatMessage);
//			sentence = inFromUser.readLine();
//		}
//		clientSocket.close();
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
			//Error?
		}
	}

	public void sendData(BaseDatagram datagram)
	{
		try
		{
			this.sendData(Serialization.serializeDatagram(datagram));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void sendData(byte[] data) throws IOException
	{
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, server, port);
		clientSocket.send(sendPacket);
	
	}

	DatagramPacket receivePacket() throws IOException
	{
		int bufferSize = clientSocket.getReceiveBufferSize();
		byte[] bufferBytes = new byte[bufferSize];
		DatagramPacket receivePacket = new DatagramPacket(bufferBytes, bufferSize);
		clientSocket.receive(receivePacket);
		return receivePacket;
	}

	public static void main(String args[]) throws Exception
	{

		UDPClient client = new UDPClient("localhost", 9876);
		client.setDatagramReceiveHandler(new DatagramReceiveHandler() {
			
			@Override
			public void receive(Connection connection, BaseDatagram datagram)
			{
				if(datagram instanceof ChatDatagram)
				{
					System.out.println("FROM SERVER: " + ((ChatDatagram) datagram).message);
				}
			}
		});
		client.start();

	}

	public void close()
	{
		//clientSocket.send
		isRunning = false;
		receiverThread.stop();
		clientSocket.close();
	}

	public void stop()
	{
		this.close();
	}
}
