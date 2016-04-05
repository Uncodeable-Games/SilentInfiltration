package de.mih.core.engine.network;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.*;

public class GameClient
{
	 public class SomeRequest {
	       public String text;
	    }
	    public class SomeResponse {
	       public String text;
	    }
	Client client;
	    
	public static void main(String args[]) throws Exception
	{
		
	}

	public GameClient() throws IOException
	{
		
		Client client = new Client();

//		SomeRequest request = new SomeRequest();
//		request.text = "Here is the request";
//		client.sendTCP(request);
		

	}
	
	public void start()
	{
		client.start();
	}
	
	public void connect(int timeout, InetAddress host, int tcpPort, int udpPort) throws IOException
	{
		client.connect(timeout, host, tcpPort,udpPort);
	}
	
	public void testListener()
	{
		client.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof SomeResponse) {
		             SomeResponse response = (SomeResponse)object;
		             System.out.println(response.text);
		          }
		       }
		    });
	}
	
	
}

