package de.mih.core.engine.network.server.datagrams;

public class DisconnectDatagram extends BaseDatagram
{
	public DisconnectDatagram()
	{
		this.reliable = true;
	}
}
