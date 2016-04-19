package de.mih.core.engine.network.server;

import java.io.Serializable;

public abstract class BaseDatagram implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -859423711241096862L;
	
	public byte type;
	public int ID;
		
//	public abstract void readData(byte[] data);
//	public abstract void writeData(byte[] data);
	
}
