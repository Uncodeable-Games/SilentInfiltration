package de.mih.core.engine.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialization
{
	public static byte[] serializeDatagram(BaseDatagram datagram)
	{
		byte[] data = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos))
		{
			out.writeObject(datagram);
			data = bos.toByteArray();
			out.close();
			bos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	public static BaseDatagram deserializeDatagram(byte[] data)
	{
		BaseDatagram baseDatagram = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); 
					   ObjectInput in = new ObjectInputStream(bis))
		{
			baseDatagram = (BaseDatagram) in.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return baseDatagram;
	}
	
	public static void main(String[] args)
	{
		AckDatagram ackDatagram = new AckDatagram();
		ackDatagram.sequenceNumber = 2;
		ackDatagram.responseID = 1;
		System.out.println(ackDatagram);
		byte[] data = serializeDatagram(ackDatagram);
		System.out.println(data);
		AckDatagram tmp = (AckDatagram) deserializeDatagram(data);
		System.out.println(tmp);
	}
}
