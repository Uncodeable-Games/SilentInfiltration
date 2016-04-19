package de.mih.core.engine.network.server;

import java.io.IOException;

public interface DatagramReceiveHandler 
{
	public void receive(Connection connection, BaseDatagram datagram) throws IOException;
}


