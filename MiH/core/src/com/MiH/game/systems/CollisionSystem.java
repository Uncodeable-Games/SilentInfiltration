package com.MiH.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.MiH.engine.ecs.BaseSystem;
import com.MiH.engine.ecs.EntityManager;
import com.MiH.engine.ecs.Event;
import com.MiH.engine.ecs.EventManager;
import com.MiH.engine.ecs.SystemManager;
import com.MiH.engine.exceptions.ComponentNotFoundEx;
import com.MiH.game.components.Collision;
import com.MiH.game.components.PositionC;

public class CollisionSystem extends BaseSystem {

	List<Integer> tmpSave = new ArrayList<Integer>();
	//TODO: Delete!
	Event coll = new Event("onCollision");
	
	public CollisionSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		
		//TODO: Is there a better place to initialize events?
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, Collision.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		Collision collision = entityManager.getComponent(entity,
				Collision.class);
		PositionC position = entityManager
				.getComponent(entity, PositionC.class);

		if (collision.isCircle) {
			for (int otherentity = 0; otherentity < entityManager.entityCount; otherentity++) {
				if (entityManager.hasComponent(otherentity, Collision.class) && entity != otherentity) {
					Collision othercollision = entityManager.getComponent(
							otherentity, Collision.class);
					PositionC otherposition = entityManager.getComponent(
							otherentity, PositionC.class);
					if (othercollision.isCircle) {

						float posx = position.position.x;
						float posy = position.position.y;
						float oposx = otherposition.position.x;
						float oposy = otherposition.position.y;
						//tmpSave.add(entity);
						
						float x1 = Math.max(posx,oposx);
						float x2 = Math.min(posx,oposx);
						float y1 = Math.max(posy,oposy);
						float y2 = Math.min(posy,oposy);

//						System.out.print(Math.sqrt(((x1 - x2) * (x1 - x2))
//								+ ((y1 - y2) * (y1 - y2))));
//						System.out.println(" <= " + ( collision.ccol
//								+ othercollision.ccol));
						if (Math.sqrt(((x1 - x2) * (x1 - x2))
								+ ((y1 - y2) * (y1 - y2))) <= collision.ccol
								+ othercollision.ccol) {
							coll.entityID = entity;
							eventManager.sendEvent(coll, dt);
//							Vector2f newpos = new Vector2f();
//							newpos.x = x - nextwidget.cord_x;
//							newpos.y = y - nextwidget.cord_y;
//							newpos.x = newpos.x
//									* (thismoveable.col + nextmoveable.col)
//									/ newpos.length();
//							newpos.y = newpos.y
//									* (thismoveable.col + nextmoveable.col)
//									/ newpos.length();
//
//							int deltax = 0;
//							int deltwDway = 0;
//
//							if (newpos.x < 0)
//								deltax = -1;
//							if (newpos.x > 0)
//								deltax = 1;
//							if (newpos.y < 0)
//								deltay = -1;
//							if (newpos.y > 0)
//								deltay = 1;
//
//							newcord_x = (int) (nextwidget.cord_x + newpos.x + deltax);
//							newcord_y = (int) (nextwidget.cord_y + newpos.y + deltay);
//							again = true;
						}
					}
				}
			}
		}
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	
	

}
