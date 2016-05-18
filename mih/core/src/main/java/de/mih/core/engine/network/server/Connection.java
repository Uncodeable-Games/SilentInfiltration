package de.mih.core.engine.network.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;

public class Connection
{
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
		//TODO: magic numbers in vars and changeable!
		long time = System.currentTimeMillis() - 5000;
		return lastResponse < time;
	}
	public void checkLostPackets()
	{
		//TODO: magic numbers in vars and changeable!
		long time = System.currentTimeMillis() - 1000;
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
