package de.silentinfiltration.game.systems;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.Event;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.game.Engine;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.engine.tilemap.Tile;
import de.silentinfiltration.engine.tilemap.Tilemap;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;

public class ControllerSystem extends BaseSystem {

	public int cam = -1;
	public Tilemap tilemap;
	Event moveOrder = new Event("MoveOrder");

	public ControllerSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		VelocityC veloComp = entityManager
				.getComponent(entity, VelocityC.class);
		Control control = entityManager.getComponent(entity, Control.class);
		PositionC position = entityManager
				.getComponent(entity, PositionC.class);
		float speed = veloComp.maxspeed;

		if (control.withwasd) {

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				speed *= 1.25f;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				veloComp.velocity.y = -1 * speed;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				veloComp.velocity.y = 1 * speed;
			}
			// else
			// veloComp.velocity.y = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				veloComp.velocity.x = -1 * speed;

			} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				veloComp.velocity.x = 1 * speed;
			}

			// float length = veloComp.velocity.length();
			// if(Math.abs(length) > 1){
			// veloComp.velocity.x /= length;
			// veloComp.velocity.y /= length;
			// }

			// System.out.println(veloComp.velocity);
			// else
			// veloComp.velocity.x = 0;

		}
		if (control.withkeys) {

			// if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//			 speed = 0.75f;
			// }
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				veloComp.velocity.y = -1 * speed;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				veloComp.velocity.y = 1 * speed;
			}
			// else
			// veloComp.velocity.y = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				veloComp.velocity.x = 1 * speed;

			} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				veloComp.velocity.x = -1 * speed;
			}

		}

		if (cam >= 0) {
			Vector2f campos = entityManager.getComponent(cam, PositionC.class).position;
			
			if (control.withmouse) {
				position.angle = (int) Math.toDegrees(Math.atan2(
						Mouse.getY() + campos.y
								- tilemap.mapToScreen(position.position).y,
						Mouse.getX() - campos.x
								- tilemap.mapToScreen(position.position).x)) - 90;
			}

			if (Mouse.isButtonDown(1) && !entityManager.hasComponent(entity, CCamera.class)) {
				moveOrder.entityID = entity;
				Vector2f targetpos = tilemap.screenToMap(new Vector2f(Mouse.getX()
						- campos.x, Mouse.getY() + campos.y));
				moveOrder.ObjectQueue.put(0, tilemap.getTileAt((int)targetpos.x, (int)targetpos.y));
				eventManager.sendEvent(moveOrder, dt);
			}
		}

	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub

	}


}
