package de.mih.core.engine.network.server.datagrams;

public class KeepAliveDatagram extends BaseDatagram
{
	private static final long serialVersionUID = 8238920809556589180L;
	
	public KeepAliveDatagram()
	{
		this.reliable = true;
	}

}
