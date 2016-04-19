package de.mih.core.engine.network.server;

import java.net.InetAddress;

public class Connection
{
	public static int CONNECTIONS = 1;
	int id;
	int port;
	InetAddress ip;
	
	public Connection(InetAddress ip, int port)
	{
		this.id = CONNECTIONS++;
		this.ip = ip;
		this.port = port;
	}
	
	public int getID() { return id; }
	public int getPort() { return port; }
	public InetAddress getIP() { return ip; }
	
	public String toString()
	{
		return ip.toString() + ":" + port;
	}
}
