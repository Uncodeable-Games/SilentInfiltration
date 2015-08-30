package com.MiH.game.systems;

import com.MiH.engine.ecs.BaseSystem;
import com.MiH.engine.ecs.EntityManager;
import com.MiH.engine.ecs.EventManager;
import com.MiH.engine.ecs.SystemManager;
import com.MiH.engine.exceptions.ComponentNotFoundEx;
import com.MiH.game.components.Control;
import com.MiH.game.components.PositionC;
import com.MiH.game.components.VelocityC;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class ControllerSystem extends BaseSystem {

	RenderSystem rs;
	Input input = Gdx.input;
	
	final Vector3 X_AXIS = new Vector3(1f,0f,0f);
	final Vector3 Y_AXIS = new Vector3(0f,1f,0f);
	final Vector3 Z_AXIS = new Vector3(0f,0f,1f);
	
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
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		VelocityC veloComp = entityManager.getComponent(entity, VelocityC.class);
		Control control = entityManager.getComponent(entity, Control.class);
		PositionC position = entityManager.getComponent(entity, PositionC.class);
		float speed = veloComp.maxspeed;

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

	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
	}

	@Override
	public void update(double dt) {

		float speed = 5;
		
		if (input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed *= 2f;
		}

		if (input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			
			v_dir_ortho = rs.camera.direction.cpy().crs(Y_AXIS);
			v_cam_target = rs.camera.position.cpy().add(rs.camera.direction.cpy().scl(-(rs.camera.position.y)/(rs.camera.direction.y)));
			
			if (input.isKeyPressed(Input.Keys.UP)) {
				rs.camera.rotateAround(v_cam_target, v_dir_ortho, - 0.1f * speed);
			} else if (input.isKeyPressed(Input.Keys.DOWN)) {
				rs.camera.rotateAround(v_cam_target, v_dir_ortho, 0.1f * speed);
			}
			
			if (input.isKeyPressed(Input.Keys.LEFT)) {
				rs.camera.rotateAround(v_cam_target, Y_AXIS, -0.1f * speed);

			} else if (input.isKeyPressed(Input.Keys.RIGHT)) {
				rs.camera.rotateAround(v_cam_target, Y_AXIS, 0.1f * speed);
			}
			
		} else {
			
			v_dir_ortho = rs.camera.direction.cpy().crs(Y_AXIS);
			
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
