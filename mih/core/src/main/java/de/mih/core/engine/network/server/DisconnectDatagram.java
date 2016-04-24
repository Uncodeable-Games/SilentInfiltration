package de.mih.core.engine.network.server;

public class DisconnectDatagram extends BaseDatagram
{
	public DisconnectDatagram()
	{
		this.reliable = true;
	}
}
