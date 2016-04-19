package de.mih.core.engine.network.server;

public class AckDatagram extends BaseDatagram
{
	public int responseID;
	
	public String toString() {
		return "ACK[id: " + this.ID + ", responseID: " + responseID + "]";
	}
}
