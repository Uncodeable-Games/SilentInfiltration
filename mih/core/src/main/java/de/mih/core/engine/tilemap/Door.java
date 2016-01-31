package de.mih.core.engine.tilemap;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Node;
import de.mih.core.game.Game;

public class Door implements Node {
	
	static HashMap<TileBorder,Door> doors = new HashMap<TileBorder,Door>();
	
	public Door(TileBorder border){
		this.border = border;
		colliderEntity = Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("door");
	}
	
	private TileBorder border;

	int colliderEntity = -1;

	@Override
	public float getDistance(Node target) {
		return 0;
	}
	
	public TileBorder getTileBorder(){
		return border;
	}

	public Vector2 getPos(){
		return border.getPos();
	}
	
	public int getColliderEntity(){
		return colliderEntity;
	}
	
	public void setColliderEntity(int entity){
		colliderEntity = entity;
	}
	
	public boolean hasSameRoom(Door door)
	{
		for (Room room : Game.getCurrentGame().getTilemap().getRooms())
		{
			if (room.allDoors.contains(this) && room.allDoors.contains(door))
				return true;
		}
		return false;
	}
}
