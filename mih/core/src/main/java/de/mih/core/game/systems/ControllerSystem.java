package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Select;

public class ControllerSystem extends BaseSystem{

	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_cam_target = new Vector3();

	public ControllerSystem(Game game) {
		super(game);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return EntityManager.getInstance().hasComponent(entityId, VelocityC.class) && EntityManager.getInstance().hasComponent(entityId, Control.class)
				&& EntityManager.getInstance().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) {
		VelocityC veloComp = EntityManager.getInstance().getComponent(entity, VelocityC.class);
		Control control = EntityManager.getInstance().getComponent(entity, Control.class);
		PositionC position = EntityManager.getInstance().getComponent(entity, PositionC.class);
		float speed = veloComp.maxspeed;

		if (control.withwasd) {

			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				speed *= 2f;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				veloComp.velocity.z = 1 * speed;
			} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				veloComp.velocity.z = -1 * speed;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				veloComp.velocity.x = -1 * speed;

			} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				veloComp.velocity.x = 1 * speed;
			}

		}
		if (control.withkeys) {

		}

		if (control.withmouse) {
			position.position.x = RenderManager.getInstance().getMouseTarget(-.5f, Gdx.input).x;
			position.position.z = RenderManager.getInstance().getMouseTarget(-.5f, Gdx.input).z;
		}

	}

	@Override
	public void render(int entity) {
	}

	@Override
	public void update(double dt) {

		float speed = 5;

		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed *= 2f;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.camera.position.add(game.camera.direction.cpy().scl(0.20f));
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.camera.position.sub(game.camera.direction.cpy().scl(0.20f));
			}

		}

		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {

			v_dir_ortho = RenderManager.getInstance().getCamera().direction.cpy().crs(game.renderS.Y_AXIS);
			v_cam_target = RenderManager.getInstance().getCameraTarget(0);

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.camera.rotateAround(v_cam_target, v_dir_ortho, -0.1f * speed);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.camera.rotateAround(v_cam_target, v_dir_ortho, 0.1f * speed);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				game.camera.rotateAround(v_cam_target, game.renderS.Y_AXIS, -0.1f * speed);

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				game.camera.rotateAround(v_cam_target, game.renderS.Y_AXIS, 0.1f * speed);
			}

		} else {

			v_dir_ortho = game.camera.direction.cpy().crs(game.renderS.Y_AXIS);

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.camera.position.x += 0.01f * speed * game.camera.direction.x;
				game.camera.position.z += 0.01f * speed * game.camera.direction.z;

			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.camera.position.x -= 0.01f * speed * game.camera.direction.x;
				game.camera.position.z -= 0.01f * speed * game.camera.direction.z;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				game.camera.position.x -= 0.01f * speed * v_dir_ortho.x;
				game.camera.position.z -= 0.01f * speed * v_dir_ortho.z;

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				game.camera.position.x += 0.01f * speed * v_dir_ortho.x;
				game.camera.position.z += 0.01f * speed * v_dir_ortho.z;
			}
		}
	}

	public void render() {
	}

	@Override
	public void onEventRecieve(BaseEvent event) {
		// TODO Auto-generated method stub

	}

}
