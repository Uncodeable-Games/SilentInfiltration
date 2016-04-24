package de.mih.core.engine.network.server;

public class AckDatagram extends BaseDatagram
{
	private static final long serialVersionUID = -2867079604309545973L;
	
	public int responseID;
//	public int recentAcks;
	//bitset for more packets?
	
	public String toString() {
		return "ACK[id: " + this.sequenceNumber + ", responseID: " + responseID + "]";
	}
}
