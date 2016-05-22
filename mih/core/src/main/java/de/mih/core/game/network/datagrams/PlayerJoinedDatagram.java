package de.mih.core.game.network.datagrams;

import de.mih.core.engine.network.server.datagrams.BaseDatagram;

public class PlayerJoinedDatagram extends BaseDatagram
{
	public int playerID;
	public int heroID;
	public String playerName;
	
	public PlayerJoinedDatagram(int playerID, int heroID, String playerName)
	{
		this.playerID = playerID;
		this.heroID = heroID;
		this.playerName = playerName;
	}
}
