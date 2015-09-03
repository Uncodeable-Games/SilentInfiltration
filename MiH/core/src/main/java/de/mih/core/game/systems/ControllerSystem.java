package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class ControllerSystem extends BaseSystem {

	RenderSystem rs;
	Input input = Gdx.input;

	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_cam_target = new Vector3();

	public ControllerSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager,
			RenderSystem rs) {
		super(systemManager, entityManager, eventManager);
		this.rs = rs;
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity){
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
			position.position.x = rs.getMouseTarget(-.5f, input).x;
			position.position.z = rs.getMouseTarget(-.5f, input).z;
		}

	}

	@Override
	public void render(int entity){
	}

	@Override
	public void update(double dt) {

		float speed = 5;

		if (input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed *= 2f;
		}

		if (input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {

			v_dir_ortho = rs.camera.direction.cpy().crs(rs.Y_AXIS);
			v_cam_target = rs.getCameraTarget(0);

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

}
