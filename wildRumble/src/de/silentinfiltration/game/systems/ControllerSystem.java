package de.silentinfiltration.game.systems;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ai.Pathfinder;
import de.silentinfiltration.engine.ai.behaviourtree.Blackboard;
import de.silentinfiltration.engine.ai.behaviourtree.ParentTaskController;
import de.silentinfiltration.engine.ai.behaviourtree.Sequence;
import de.silentinfiltration.engine.ai.behaviourtree.Task;
import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.Event;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.game.Engine;
import de.silentinfiltration.game.ai.behaviourtree.MoveToTileTask;
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
	OrderSystem os;

	public ControllerSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager, OrderSystem orders , Tilemap map) {
		super(systemManager, entityManager, eventManager);
		map = tilemap;
		os = orders;
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
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				veloComp.velocity.x = -1 * speed;

			} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				veloComp.velocity.x = 1 * speed;
			}
		}
		if (control.withkeys) {

			// if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//			 speed = 0.75f;
			// }
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				veloComp.velocity.y = 1 * speed;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				veloComp.velocity.y = -1 * speed;
			}
			// else
			// veloComp.velocity.y = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				veloComp.velocity.x = -1 * speed;

			} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				veloComp.velocity.x = 1 * speed;
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
				Vector2f targetpos = tilemap.screenToMap(new Vector2f(Mouse.getX()
						- campos.x, Mouse.getY() + campos.y));
//				moveOrder.entityID = entity;
//				Vector2f targetpos = tilemap.screenToMap(new Vector2f(Mouse.getX()
//						- campos.x, Mouse.getY() + campos.y));
//				moveOrder.ObjectQueue.put(0, tilemap.getTileAt((int)targetpos.x, (int)targetpos.y));
//				eventManager.sendEvent(moveOrder, dt);
	
				os.moveToDestination(entity, tilemap.getTileAt((int)targetpos.x, (int)targetpos.y));
			}
		}

	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub

	}


}
