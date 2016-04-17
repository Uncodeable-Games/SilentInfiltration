package de.mih.core.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.ControlC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class ControllerSystem extends BaseSystem
{

	public ControllerSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return game.getEntityManager().hasComponent(entityId, VelocityC.class) && game.getEntityManager().hasComponent(entityId, ControlC.class)
				&& game.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity)
	{
		VelocityC veloComp = game.getEntityManager().getComponent(entity, VelocityC.class);
		ControlC  control  = game.getEntityManager().getComponent(entity, ControlC.class);
		PositionC position = game.getEntityManager().getComponent(entity, PositionC.class);
		float     speed    = veloComp.maxspeed;

		if (entity == 74)
		{
			//System.out.println("PLAYER: " + control.withwasd);
		}
//		if (control.withwasd) {
//			//System.out.println("with wasd" + entity);
//			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
//				speed *= 2f;
//			}
//
//			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//				veloComp.velocity.z = 1 * speed;
//			} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//				veloComp.velocity.z = -1 * speed;
//			}
//
//			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//				veloComp.velocity.x = -1 * speed;
//
//			} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//				veloComp.velocity.x = 1 * speed;
//			}
//			//veloComp.steering = veloComp.velocity.cpy();
//
//		}
		if (control.withkeys)
		{

		}

		if (control.withmouse)
		{
			position.setPos(this.game.getRenderManager().getMouseTarget(0, Gdx.input).x, 0, this.game.getRenderManager().getMouseTarget(0, Gdx.input).z);
		}
	}

	@Override
	public void render(int entity)
	{
	}

	Vector3 v_dir_ortho  = new Vector3();
	Vector3 v_dir        = new Vector3();
	Vector3 v_cam_target = new Vector3();

	@Override
	public void update(double dt)
	{

		float speed = 20;

		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
		{
			speed *= 2f;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT))
		{

			if (Gdx.input.isKeyPressed(Input.Keys.UP))
			{
				game.getCamera().position.add(game.getCamera().direction.cpy().scl(0.20f));
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				game.getCamera().position.sub(game.getCamera().direction.cpy().scl(0.20f));
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
		{

			v_dir_ortho.set(game.getRenderManager().getCamera().direction).crs(game.getRenderSystem().Y_AXIS);
			v_cam_target = game.getRenderManager().getCameraTarget(0);

			if (Gdx.input.isKeyPressed(Input.Keys.UP))
			{
				game.getCamera().rotateAround(v_cam_target, v_dir_ortho, -0.1f * speed);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				game.getCamera().rotateAround(v_cam_target, v_dir_ortho, 0.1f * speed);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				game.getCamera().rotateAround(v_cam_target, game.getRenderSystem().Y_AXIS, -0.1f * speed);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				game.getCamera().rotateAround(v_cam_target, game.getRenderSystem().Y_AXIS, 0.1f * speed);
			}
		}
		else
		{
			//System.out.println("camera steuerung");
			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
			
			if (Gdx.input.isKeyPressed(Input.Keys.UP))
			{
				//System.out.println("UP");
				game.getCamera().position.x += 0.01f * speed * v_dir.x;
				game.getCamera().position.z += 0.01f * speed * v_dir.z;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				game.getCamera().position.x -= 0.01f * speed * v_dir.x;
				game.getCamera().position.z -= 0.01f * speed * v_dir.z;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				game.getCamera().position.x -= 0.01f * speed * v_dir_ortho.x;
				game.getCamera().position.z -= 0.01f * speed * v_dir_ortho.z;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				game.getCamera().position.x += 0.01f * speed * v_dir_ortho.x;
				game.getCamera().position.z += 0.01f * speed * v_dir_ortho.z;
			}
		}
		//game.getCamera().update();
	}

	public void render()
	{
	}
}
