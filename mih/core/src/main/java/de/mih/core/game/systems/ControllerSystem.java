package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.render.RenderManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Select;

public class ControllerSystem extends BaseSystem implements InputProcessor {

	RenderSystem rs;
	Input input;

	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_cam_target = new Vector3();

	public ControllerSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager,
			RenderSystem rs, Input in) {
		super(systemManager, entityManager, eventManager);
		this.rs = rs;
		this.input = in;
		//input.setInputProcessor(this);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) {
		VelocityC veloComp = entityManager.getComponent(entity, VelocityC.class);
		Control control = entityManager.getComponent(entity, Control.class);
		PositionC position = entityManager.getComponent(entity, PositionC.class);
		float speed = veloComp.maxspeed;

		if (input.isKeyPressed(Input.Keys.ESCAPE)) {
			entityManager.removeEntity(entity);
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
		Ray ray = rs.camera.getPickRay(screenX, screenY);

		int min_entity = -1;
		for (int i = 0; i < entityManager.entityCount; i++) {
			if (entityManager.hasComponent(i, VisualC.class) && entityManager.hasComponent(i, SelectableC.class)) {
				vis = entityManager.getComponent(i, VisualC.class);
				pos = entityManager.getComponent(i, PositionC.class);

				float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

				temp_pos = pos.position.cpy();
				temp_pos.add(vis.visual.pos);
				temp_pos.y += vis.visual.bounds.getHeight() / 2f;

				if (Intersector.intersectRaySphere(ray, temp_pos, radius, null)) {
					if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos)) {
						min_entity = i;
						min_pos = pos.position;
					}
				}
			}
		}
		
		if (entityManager.hasComponent(min_entity, SelectableC.class)) {
			entityManager.getComponent(min_entity, SelectableC.class).selected = true;
		}
		if (entityManager.hasComponent(min_entity, PositionC.class)) {
			entityManager.getComponent(min_entity, PositionC.class).angle += 45;
		}
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

}
