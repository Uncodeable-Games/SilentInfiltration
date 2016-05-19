package de.mih.core.engine.network.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;

public class Connection
{
	public static int CONNECTION_LOST_TIME = 5000;
	public static int PACKET_LOST_TIME = 1000;
	public static int CONNECTIONS = 1;
	int id;
	int port;
	InetAddress ip;
	int remoteSequence;
	HashMap<Integer, BaseDatagram> awaitAcknowledge;
	HashMap<Integer, Long> awaitingSince;
	boolean isAlive = true;
	
	private long lastResponse = 0;
	
	public Connection(InetAddress ip, int port)
	{
		this.id = CONNECTIONS++;
		this.ip = ip;
		this.port = port;
		this.awaitAcknowledge = new HashMap<>();
		this.awaitingSince = new HashMap<>();
	}
	
	public int getID() { return id; }
	public int getPort() { return port; }
	public InetAddress getIP() { return ip; }
	public int getRemoteSequence() { return remoteSequence; }
	
	public void updateRemoteSequence(int remoteSequence)
	{
		this.remoteSequence = remoteSequence;
		lastResponse = System.currentTimeMillis();
	}
	
	public String toString()
	{
		return ip.toString() + ":" + port;
	}
	
	public void awaitAcknowledge(BaseDatagram packet)
	{
		this.awaitAcknowledge.put(packet.sequenceNumber, packet);
		this.awaitingSince.put(packet.sequenceNumber, System.currentTimeMillis());
	}
	
	public void removeAcknowledged(int packetId)
	{
		if(awaitAcknowledge.containsKey(packetId))
		{
			awaitAcknowledge.remove(packetId);
			awaitingSince.remove(packetId);
		}
	}
	
	public boolean isConnectionLost()
	{
		long time = System.currentTimeMillis() - CONNECTION_LOST_TIME;
		return lastResponse < time;
	}
	public void checkLostPackets()
	{
		long time = System.currentTimeMillis() - PACKET_LOST_TIME;
		Set<Integer> packetIds = awaitingSince.keySet();
		for(Integer id : packetIds)
		{
			if(awaitingSince.get(id) < time)
			{
				//TODO: handler for lost packets!
				System.out.println("lost: " + awaitAcknowledge.get(id).toString());
				awaitAcknowledge.remove(id);
				awaitingSince.remove(id);
			}
		}
	}
}
