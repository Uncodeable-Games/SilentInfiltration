package de.mih.core.engine.network.mediation;

import java.net.InetSocketAddress;
import java.util.HashMap;

//Copied from Chattutorial

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class MediationNetwork {
	static public final int tcpPort = 54555;
	static public final int udpPort = 55555;


	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(RegisterLobby.class);
		kryo.register(UpdateLobbies.class);
		kryo.register(RequestLobbyUpdate.class);
		kryo.register(Lobby.class);
		kryo.register(java.util.HashMap.class);
		kryo.register(RequestLobbyJoin.class);
		kryo.register(ExternalInformation.class);
	}
	static public class Lobby {
		public String name;
		public int players;
		public String address;
		public int tcpPort;
		public int udpPort;
		public int id;
		
		public String toString() {
			return name + " [" + address + "]";
		}
	}
	
	static public class RegisterLobby {
		public Lobby lobby;
		public int id;
	}
	
	
	static public class UpdateLobbies {
		public HashMap<Integer, Lobby> lobbies;
	}
	
	static public class RequestLobbyUpdate {
		
	}
	
	static public class RequestLobbyJoin {
		public Lobby targetLobby;
		public String sourceAddress;
		public String targetAddress;
	}

	static public class RegisterName {
		public String name;
	}
	
	static public class ExternalInformation {
		public String address;
		public int tcpPort;
		public int udpPort;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class ChatMessage {
		public String text;
	}
}