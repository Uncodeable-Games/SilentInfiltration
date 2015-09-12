package de.mih.core.game.tilemap.borders;

import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.BorderColliderFactory;

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
	public BorderCollider colliderForName(String name) 
	{
		BorderCollider collider = null;
		System.out.println(name);
		switch(name)
		{
			case "wall":
				collider = new WallBorder();
				break;
			case "door":
				collider = new DoorBorder();
				break;
			default:
				break;
		}
		return collider;
	}
	

}
