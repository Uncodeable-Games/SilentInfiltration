package de.mih.core.engine.network.server.datagrams;

public class ConnectApprove extends BaseDatagram
{
	//Connection connection;
	/**
	 * more like player entity id currently :/
	 */
	public int playerId;
	
	public ConnectApprove(int playerId)
	{
		this.playerId = playerId;
	}
}
