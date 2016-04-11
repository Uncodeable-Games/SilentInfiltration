package de.mih.core.engine.network;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.*;

import de.mih.core.engine.network.mediation.MediationNetwork.RegisterName;

public class GameClient extends Listener
{
	Client client;
	    
	public static void main(String args[]) throws Exception
	{
		
	}

	public GameClient() throws IOException
	{
		
		client = new Client();
		client.start();
		client.addListener(this);

	}
	
	
	public void connect(int timeout, String address, int tcpPort, int udpPort) throws IOException
	{
		System.out.println(address + " " +  tcpPort + " " + udpPort);
		client.connect(timeout, address, tcpPort,udpPort);
	}
	
	public void testListener()
	{
		client.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		    	   System.out.println("GAMECLIENT RECEIVED");
		       }
		    });
	}
	
	@Override
	public void connected(Connection connection)
	{
		System.out.println("I HAVE CONNECTED!");
	}
}


