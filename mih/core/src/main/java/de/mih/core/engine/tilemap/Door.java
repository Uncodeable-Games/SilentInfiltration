package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

import java.util.HashMap;

public class Door
{

	static HashMap<TileBorder, Door> doors = new HashMap<TileBorder, Door>();

	public Door(TileBorder border)
	{
		this.border = border;
		setColliderEntity(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("door.json"));
	}

	private TileBorder border;

	int colliderEntity = -1;

	public TileBorder getTileBorder()
	{
		return border;
	}

	public Vector2 getPos()
	{
		return border.getPos();
	}

	public int getColliderEntity()
	{
		return colliderEntity;
	}

	public void setColliderEntity(int entity)
	{
		colliderEntity = entity;
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class)
				.setPos(new Vector3(this.border.getPos().x, 0, this.border.getPos().y));
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setAngle(this.border.angle);
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

	public Room getSameRoom(Door door)
	{
		for (Room room : Game.getCurrentGame().getTilemap().getRooms())
		{
			if (room.allDoors.contains(this) && room.allDoors.contains(door))
				return room;
		}
		return null;
	}
}
