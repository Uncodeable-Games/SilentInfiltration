package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

import java.util.HashMap;

public class Wall
{

	public Wall(TileBorder border)
	{
		this.border = border;
		setColliderEntity(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("wall.json"));
	}
	
	static HashMap<TileBorder, Wall> walls = new HashMap<TileBorder, Wall>();
	
	int colliderEntity = -1;
	
	private TileBorder border;
	
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
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setPos(new Vector3(this.border.getPos().x, 0, this.border.getPos().y));
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setAngle(this.border.angle);
	}
}