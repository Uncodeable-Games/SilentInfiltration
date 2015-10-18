package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.events.orderevents.SelectEntity_Event;

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

	public ControllerSystem(SystemManager systemManager, Game game) {
		super(systemManager, game);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return game.getEntityManager().hasComponent(entityId, VelocityC.class) && game.getEntityManager().hasComponent(entityId, Control.class)
				&&game.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity) {
		VelocityC veloComp = game.getEntityManager().getComponent(entity, VelocityC.class);
		Control control = game.getEntityManager().getComponent(entity, Control.class);
		PositionC position = game.getEntityManager().getComponent(entity, PositionC.class);
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
			position.setPos( this.game.getRenderManager().getMouseTarget(0, Gdx.input).x, 0, this.game.getRenderManager().getMouseTarget(0, Gdx.input).z);
		}

	}

	@Override
	public void render(int entity) {
	}

	
	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_dir = new Vector3();
	Vector3 v_cam_target = new Vector3();
	@Override
	public void update(double dt) {

		float speed = 5;

		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed *= 2f;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.getCamera().position.add(game.getCamera().direction.cpy().scl(0.20f));
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.getCamera().position.sub(game.getCamera().direction.cpy().scl(0.20f));
			}

		}

		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {

			v_dir_ortho.set(game.getRenderManager().getCamera().direction).crs(game.getRenderSystem().Y_AXIS);
			v_cam_target = game.getRenderManager().getCameraTarget(0);

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.getCamera().rotateAround(v_cam_target, v_dir_ortho, -0.1f * speed);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.getCamera().rotateAround(v_cam_target, v_dir_ortho, 0.1f * speed);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				game.getCamera().rotateAround(v_cam_target, game.getRenderSystem().Y_AXIS, -0.1f * speed);

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				game.getCamera().rotateAround(v_cam_target, game.getRenderSystem().Y_AXIS, 0.1f * speed);
			}

		} else {

			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
			
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				game.getCamera().position.x += 0.01f * speed * v_dir.x;
				game.getCamera().position.z += 0.01f * speed * v_dir.z;

			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.getCamera().position.x -= 0.01f * speed * v_dir.x;
				game.getCamera().position.z -= 0.01f * speed * v_dir.z;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				game.getCamera().position.x -= 0.01f * speed * v_dir_ortho.x;
				game.getCamera().position.z -= 0.01f * speed * v_dir_ortho.z;

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				game.getCamera().position.x += 0.01f * speed * v_dir_ortho.x;
				game.getCamera().position.z += 0.01f * speed * v_dir_ortho.z;
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
