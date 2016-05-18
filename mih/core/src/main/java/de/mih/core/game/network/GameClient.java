package de.mih.core.game.network;

import java.io.IOException;
import java.net.InetAddress;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventDatagram;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.engine.network.server.Connection;
import de.mih.core.engine.network.server.DatagramReceiveHandler;
import de.mih.core.engine.network.server.UDPClient;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.game.Game;

public class GameClient implements DatagramReceiveHandler, EventListener<BaseEvent>
{
	UDPClient client;
	Game game;

	public GameClient(Game game, String ip, int port) throws IOException
	{
		this.game = game;
		client = new UDPClient(ip, port);
		client.setDatagramReceiveHandler(this);
		client.start();
	}
	
	
//	public void connect(int timeout, String address, int tcpPort, int udpPort) throws IOException
//	{
//		System.out.println(address + " " +  tcpPort + " " + udpPort);
//		new Thread("GameClientConnect") {
//			public void run()
//			{
//				try
//				{
//					client.connect(timeout, address, tcpPort,udpPort);
//				}
//				catch (IOException ex)
//				{
//					ex.printStackTrace();
//					System.exit(1);
//				}
//			}
//		}.start();
//	}

	
	@Override
	public void connected(Connection connection)
	{
		System.out.println("I HAVE CONNECTED!");
	}


	@Override
	public void disconnected(Connection connection)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(Connection connection, BaseDatagram datagram) throws IOException
	{
		if(datagram instanceof EventDatagram)
		{
			EventDatagram eventDatagram = (EventDatagram) datagram;
			eventDatagram.event.fromRemote = true;
			game.getEventManager().fire(eventDatagram.event);
		}
	}

	@Override
	public void handleEvent(BaseEvent event)
	{
		//TODO: better filtering?
		if(!event.fromRemote)
		{
			EventDatagram eventDatagram = new EventDatagram();
			eventDatagram.event = event;
			client.sendData(eventDatagram, true);
		}
	}
}


