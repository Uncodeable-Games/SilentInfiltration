package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

import java.util.HashMap;

public class Wall
{
	
	static HashMap<TileBorder, Wall> walls = new HashMap<>();
	
	private TileBorder border;
	private int colliderEntity = -1;

	private String blueprint;

	public Wall(TileBorder border,int colliderEntity, String blueprint)
	{
		this.border = border;
		this.blueprint = blueprint;

		setColliderEntity(colliderEntity);
		Game.getCurrentGame().getEntityManager().getComponent(getColliderEntity(), BorderC.class).setTileBorder(border);

		Visual vis = Game.getCurrentGame().getEntityManager().getComponent(getColliderEntity(), VisualC.class).getVisual();
	}
	
	public TileBorder getTileBorder()
	{
		return border;
	}
	
	public Vector3 getPos()
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
		if (colliderEntity == -1) return;
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setPos(border.getPos());//new Vector3(this.border.getPos().x, 0, this.border.getPos().z));
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setAngle(this.border.angle);
	}

	public String getBlueprint()
	{
		return blueprint;
	}
}