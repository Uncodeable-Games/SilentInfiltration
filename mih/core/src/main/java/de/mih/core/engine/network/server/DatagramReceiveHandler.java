package de.mih.core.engine.network.server;

import java.io.IOException;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;

public interface DatagramReceiveHandler 
{
	public void connected(Connection connection, ConnectApprove datagram);
	public void disconnected(Connection connection);
	public void receive(Connection connection, BaseDatagram datagram) throws IOException;
	public void packetLost(Connection connection, BaseDatagram lostDatagram);

}


