package de.mih.core.game.tilemap.borders;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.BorderColliderFactory;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;

public class RoomBorderColliderFactory extends BorderColliderFactory 
{
	
	static RoomBorderColliderFactory factory;
	
	public static RoomBorderColliderFactory getInstance() {
		if(factory == null)
		{
			factory = new RoomBorderColliderFactory();
		}
		return factory;
	}

	@Override
	public BorderCollider colliderForName(TileBorder border, String name) 
	{
		BorderCollider collider = null;
		System.out.println(name);
		switch(name)
		{
			case "wall":
				collider = new Wall();
				int entity = EntityManager.getInstance().createEntity();
				NodeC c = new NodeC();
				c.blocked = true;
				PositionC p = new PositionC(border.getCenter());
				EntityManager.getInstance().addComponent(entity, c, p);
				break;
			case "door":
				collider = new Door();
				break;
			default:
				break;
		}
		return collider;
	}
	

}
