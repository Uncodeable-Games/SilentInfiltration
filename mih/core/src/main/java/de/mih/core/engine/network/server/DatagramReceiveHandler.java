package de.mih.core.engine.network.server;

import java.io.IOException;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;

public interface DatagramReceiveHandler 
{
	public void connected(Connection connection);
	public void disconnected(Connection connection);
	public void receive(Connection connection, BaseDatagram datagram) throws IOException;
}


