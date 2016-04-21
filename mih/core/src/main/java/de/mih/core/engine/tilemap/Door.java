package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.PositionC;

import java.util.HashMap;

public class Door
{

	static HashMap<TileBorder, Door> doors = new HashMap<>();

	private TileBorder border;
	private boolean closed         = false;
	private int     colliderEntity = -1;

	private String blueprint;

	public Door(TileBorder border,int colliderEntity, String blueprint)
	{
		this.border = border;
		this.blueprint = blueprint;

		setColliderEntity(colliderEntity);
		Game.getCurrentGame().getEntityManager().getComponent(getColliderEntity(), BorderC.class).setTileBorder(border);
	}

	public TileBorder getTileBorder()
	{
		return border;
	}

	public Vector2 getPos()
	{
		return border.getPos();
	}

	public String getBlueprint()
	{
		return blueprint;
	}
	public int getColliderEntity()
	{
		return colliderEntity;
	}

	public void setColliderEntity(int entity)
	{
		colliderEntity = entity;
		if (colliderEntity == -1) return;
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

	public boolean isClosed()
	{
		return closed;
	}

	public void open()
	{
		closed = false;

		NavigationManager navigationManager = Game.getCurrentGame().getNavigationManager();

		NavPoint nav1 = (NavPoint) navigationManager.getNavPoints(this).values().toArray()[0];
		NavPoint nav2 = (NavPoint) navigationManager.getNavPoints(this).values().toArray()[1];

		if (!nav1.getVisibleNavPoints().contains(nav2))
			nav1.addVisibleNavPoint(nav2);

		if (!nav2.getVisibleNavPoints().contains(nav1))
			nav2.addVisibleNavPoint(nav1);
	}

	public void close()
	{
		closed = true;

		NavigationManager navigationManager = Game.getCurrentGame().getNavigationManager();

		NavPoint nav1 = (NavPoint) navigationManager.getNavPoints(this).values().toArray()[0];
		NavPoint nav2 = (NavPoint) navigationManager.getNavPoints(this).values().toArray()[1];

		if (nav1.getVisibleNavPoints().contains(nav2))
			nav1.removeVisibleNavPoint(nav2);

		if (nav2.getVisibleNavPoints().contains(nav1))
			nav2.removeVisibleNavPoint(nav1);
	}
}
