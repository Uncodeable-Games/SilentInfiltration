package de.mih.core.game.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.files.FileHandle;

import de.mih.core.engine.ability.AbilityManager;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventDatagram;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.BlueprintManager;
import de.mih.core.engine.lua.LuaScriptManager;
import de.mih.core.engine.network.server.Connection;
import de.mih.core.engine.network.server.DatagramReceiveHandler;
import de.mih.core.engine.network.server.UDPServer;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.GameLogic;
import de.mih.core.game.MiH;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.network.datagrams.PlayerJoinedDatagram;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.systems.StateMachineSystem;
import de.mih.core.game.systems.StatsSystem;


public class GameServer extends ApplicationAdapter implements  DatagramReceiveHandler, EventListener//<BaseEvent>
{
	UDPServer server;
	
	GameLogic game;
	String mappath;
	
	Thread serverThread;
	
	boolean isInitizialised = false;
	
	public GameServer(int port, String mappath) throws IOException
	{
		server = new UDPServer(port);
		server.setDatagramReceiveHandler(this);
		game = new GameLogic();
		this.mappath = mappath;
		System.out.println("constructor");
	}
	
	
	@Override
	public void create()
	{
		System.out.println("create");
		init(mappath);
		//TODO: move this inside the server!
		serverThread = new Thread(server);
		serverThread.start();
	}
	
	public void init(String mappath)
	{
		game.init(mappath);
		game.getEventManager().register(this);
		isInitizialised = true;
	}
	
	@Override
	public void render()
	{
		update(Gdx.graphics.getDeltaTime());
	}
	
	private void update(double deltaTime)
	{
		game.update(deltaTime);
	}
	
	double time = 0f;
	double deltaTime = 1.0f / 60.0f;
	
	public void start()
	{ 
		//TODO: better organisation
		if(!isInitizialised) { return; }
		server.start();
		while(!game.isGameOver)
		{
			long timeDiff = -System.currentTimeMillis();
			game.update(deltaTime);
			timeDiff += System.currentTimeMillis();
			if(timeDiff < 1000 && timeDiff >= 20)
			{
				try
				{
					Thread.sleep(1000 - timeDiff);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			time += deltaTime;
		}
	}
	
	@Override
	public void receive(Connection connection, BaseDatagram datagram) throws IOException
	{
		System.out.println("from: " + connection.getPort());
		if(datagram instanceof EventDatagram)
		{
			EventDatagram eventDatagram = (EventDatagram) datagram;
			System.out.println("got event: " + eventDatagram.event.getClass());
			game.getEventManager().fire(eventDatagram.event);
		}
	}
	
	int playersIds = 1;
	HashMap<Integer, Integer> playerToEntity = new HashMap<>();
	
	@Override
	public void connected(Connection connection, ConnectApprove datagram)
	{
		// TODO Auto-generated method stub
		int playerId = playersIds;
		System.out.println(connection.getPort() + " connected!");
		int robo = game.getBlueprintManager().createEntityFromBlueprint("robocop.json");

		server.sendTo(connection, new ConnectApprove(robo), true);

		for(int i = 1; i < playersIds; i++)
		{
			server.sendTo(connection, new PlayerJoinedDatagram(i, playerToEntity.get(i), "other player"), true);
		}
		server.sendToAllExcept(connection, new PlayerJoinedDatagram(playerId, robo, "new player"), true);
		//TODO: need some kind of player management
		//activePlayer = new Player("localplayer", 0, Player.PlayerType.Attacker);

		game.getEntityManager().getComponent(robo, PositionC.class).setPos(8, 0, 53);
		playerToEntity.put(playerId, robo);
		
		playersIds++;
		//server.sendToAll(data, reliable);
		//game.getBlueprintManager().createEntityFromBlueprint(name)
	}

	
	@Override
	public void disconnected(Connection c)
	{

	}
	

	@Override
	public void packetLost(Connection connection, BaseDatagram lostDatagram)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(BaseEvent event)
	{
		EventDatagram eventDatagram = new EventDatagram();
		eventDatagram.event = event;
		server.sendToAll(eventDatagram, true);
	}
	

	//TODO: args should contain, port, map and host
	public static void main(String[] args)
	{
		try
		{
			HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
			new HeadlessApplication(new GameServer(13337, "assets/maps/map1.json"), config);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}





}
