package de.silentinfiltration.engine.ai.behaviourtree;

import java.util.HashMap;
import java.util.Map;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.ai.Pathfinder;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.tilemap.IsometricTileMapRenderer;
import de.silentinfiltration.engine.tilemap.Tile;
import de.silentinfiltration.engine.tilemap.Tilemap;
import de.silentinfiltration.game.AssetManager;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.Collision;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.game.components.Visual;

public class Blackboard {
		
		public Pathfinder pf;
		
		public Map<Node,Node> currentPath = new HashMap<>();
	
		public EntityManager entityM;
		
		public SystemManager systemM;
		
		public EventManager eventM;
		
		public AssetManager assetM;
	
		public IsometricTileMapRenderer tilemapRenderer;
		
		public CCamera cam;
		
		public Collision col;
		
		public Control con;
		
		public PositionC pos;
		
		public VelocityC vel;
		
		public Visual vis;
	
		public int entity;

		public Tilemap map;
		
		public Node destination;
		
		public int targetentity;
}
