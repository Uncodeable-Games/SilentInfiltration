package de.mih.core.engine.network.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;

public class Connection
{
	public static int CONNECTIONS = 1;
	int id;
	int port;
	InetAddress ip;
	int remoteSequence;
	HashMap<Integer, BaseDatagram> awaitAcknowledge;
	HashMap<Integer, Long> awaitingSince;
	
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
	
	public void checkLostPackets()
	{
		long time = System.currentTimeMillis() - 1000;
		Set<Integer> packetIds = awaitingSince.keySet();
		for(Integer id : packetIds)
		{
			if(awaitingSince.get(id) < time)
			{
				System.out.println("lost: " + awaitAcknowledge.get(id).toString());
				awaitAcknowledge.remove(id);
				awaitingSince.remove(id);
			}
		}
	}
}
