package de.mih.core.engine.network.server.datagrams;

import java.io.Serializable;

public abstract class BaseDatagram implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -859423711241096862L;
	
	public short protocoll; 
	public byte type;
	public int sequenceNumber = -1;
	boolean reliable;
		
//	public abstract void readData(byte[] data);
//	public abstract void writeData(byte[] data);
	
}
