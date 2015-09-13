package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
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

public class ControllerSystem extends BaseSystem implements InputProcessor {

	EntityManager entityM = EntityManager.getInstance();
	EventManager eventM = EventManager.getInstance();

	RenderSystem rs;
	Input input;

	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_cam_target = new Vector3();

	public ControllerSystem(RenderSystem rs, Input in) {
		super();
		this.rs = rs;
		this.input = in;
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityM.hasComponent(entityId, VelocityC.class) && entityM.hasComponent(entityId, Control.class)
				&& entityM.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) {
		VelocityC veloComp = entityM.getComponent(entity, VelocityC.class);
		Control control = entityM.getComponent(entity, Control.class);
		PositionC position = entityM.getComponent(entity, PositionC.class);
		float speed = veloComp.maxspeed;

		if (input.isKeyPressed(Input.Keys.ESCAPE)) {
			entityM.removeEntity(entity);
		}

		if (control.withwasd) {

			if (input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				speed *= 2f;
			}

			if (input.isKeyPressed(Input.Keys.S)) {
				veloComp.velocity.z = 1 * speed;
			} else if (input.isKeyPressed(Input.Keys.W)) {
				veloComp.velocity.z = -1 * speed;
			}

			if (input.isKeyPressed(Input.Keys.A)) {
				veloComp.velocity.x = -1 * speed;

			} else if (input.isKeyPressed(Input.Keys.D)) {
				veloComp.velocity.x = 1 * speed;
			}

		}
		if (control.withkeys) {

		}

		if (control.withmouse) {
			position.position.x = RenderManager.getInstance().getMouseTarget(-.5f, input).x;
			position.position.z = RenderManager.getInstance().getMouseTarget(-.5f, input).z;
		}

	}

	@Override
	public void render(int entity) {
	}

	@Override
	public void update(double dt) {

		float speed = 5;

		if (input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed *= 2f;
		}

		if (input.isKeyPressed(Input.Keys.ALT_LEFT)) {

			if (input.isKeyPressed(Input.Keys.UP)) {
				rs.camera.position.add(rs.camera.direction.cpy().scl(0.20f));
			} else if (input.isKeyPressed(Input.Keys.DOWN)) {
				rs.camera.position.sub(rs.camera.direction.cpy().scl(0.20f));
			}

		}

		if (input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {

			v_dir_ortho = RenderManager.getInstance().getCamera().direction.cpy().crs(rs.Y_AXIS);
			v_cam_target = RenderManager.getInstance().getCameraTarget(0);

			if (input.isKeyPressed(Input.Keys.UP)) {
				rs.camera.rotateAround(v_cam_target, v_dir_ortho, -0.1f * speed);
			} else if (input.isKeyPressed(Input.Keys.DOWN)) {
				rs.camera.rotateAround(v_cam_target, v_dir_ortho, 0.1f * speed);
			}

			if (input.isKeyPressed(Input.Keys.LEFT)) {
				rs.camera.rotateAround(v_cam_target, rs.Y_AXIS, -0.1f * speed);

			} else if (input.isKeyPressed(Input.Keys.RIGHT)) {
				rs.camera.rotateAround(v_cam_target, rs.Y_AXIS, 0.1f * speed);
			}

		} else {

			v_dir_ortho = rs.camera.direction.cpy().crs(rs.Y_AXIS);

			if (input.isKeyPressed(Input.Keys.UP)) {
				rs.camera.position.x += 0.01f * speed * rs.camera.direction.x;
				rs.camera.position.z += 0.01f * speed * rs.camera.direction.z;

			} else if (input.isKeyPressed(Input.Keys.DOWN)) {
				rs.camera.position.x -= 0.01f * speed * rs.camera.direction.x;
				rs.camera.position.z -= 0.01f * speed * rs.camera.direction.z;
			}

			if (input.isKeyPressed(Input.Keys.LEFT)) {
				rs.camera.position.x -= 0.01f * speed * v_dir_ortho.x;
				rs.camera.position.z -= 0.01f * speed * v_dir_ortho.z;

			} else if (input.isKeyPressed(Input.Keys.RIGHT)) {
				rs.camera.position.x += 0.01f * speed * v_dir_ortho.x;
				rs.camera.position.z += 0.01f * speed * v_dir_ortho.z;
			}
		}
	}

	public void render() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	VisualC vis;
	PositionC pos;
	Vector3 temp_pos = new Vector3();
	Vector3 min_pos = new Vector3();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void onEventRecieve(BaseEvent event) {
		// TODO Auto-generated method stub

	}

}
