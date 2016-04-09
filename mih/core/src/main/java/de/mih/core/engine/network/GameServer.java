package de.mih.core.engine.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;


public class GameServer
{
	Server server;
	
	public GameServer(int port) throws IOException
	{
		server = new Server();
		//MediationNetwork.register(server);
		
		//server.addListener(this);
		
		server.bind(port);
		server.start();
	}
}
