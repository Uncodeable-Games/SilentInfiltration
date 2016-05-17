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
	}

	@Override
	public void render(int entity)
	{
	}

	private Vector3 v_dir_ortho  = new Vector3();
	private Vector3 v_dir        = new Vector3();

	private final float SCROLLBORDER   = 0.15f;
	private final float MAXSCROLLSPEED = 30f;

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
			Vector3 v_cam_target = game.getRenderManager().getCameraTarget(0);

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

		if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE))
		{
			if (Gdx.input.justTouched())
			{
				Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			}

			v_dir_ortho.set(game.getRenderManager().getCamera().direction).crs(Vector3.Y);

			game.getCamera().rotateAround(game.getRenderManager().getCameraTarget(0), v_dir_ortho, 0.5f * (Gdx.graphics.getHeight() / 2 - Gdx.input.getY()));
			game.getCamera().rotateAround(game.getRenderManager().getCameraTarget(0), Vector3.Y, 0.5f * (Gdx.graphics.getWidth() / 2 - Gdx.input.getX()));

			Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		}



		if (game.getUI().isMouseOverUI()) return;


		// RIGHT SCROLL BORDER
		if (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth() <= SCROLLBORDER)
		{
			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);

			game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
			game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;

			/*
			if (1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) >= MAXSCROLLSPEED)
			{
				game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
				game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
			}
			else
			{
				game.getCamera().position.x += 0.01f * 1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) * v_dir_ortho.x;
				game.getCamera().position.z += 0.01f * 1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) * v_dir_ortho.z;
			}
			*/
		}

		// LEFT SCROLL BORDER
		if ((float)Gdx.input.getX()/ Gdx.graphics.getWidth() <= SCROLLBORDER )
		{
			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);


			game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
			game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
			/*
			if (1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) >= MAXSCROLLSPEED)
			{
				game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
				game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
			}
			else
			{
				game.getCamera().position.x -= 0.01f * 1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) * v_dir_ortho.x;
				game.getCamera().position.z -= 0.01f * 1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) * v_dir_ortho.z;
			}
			*/
		}

		// UP SCROLL BORDER
		if ((float)Gdx.input.getY()/ Gdx.graphics.getHeight() <= SCROLLBORDER)
		{
			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);


			game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir.x;
			game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir.z;

			/*
			if ( 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) >= MAXSCROLLSPEED)
			{
				game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir.x;
				game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir.z;
			}
			else
			{
				game.getCamera().position.x += 0.01f * 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) * v_dir.x;
				game.getCamera().position.z += 0.01f * 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) * v_dir.x;
			}
			*/
		}

		// DOWN SCROLL BORDER
		if (Math.abs((float)Gdx.input.getY()-Gdx.graphics.getHeight())/ Gdx.graphics.getHeight() <= SCROLLBORDER)
		{
			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);

			game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir.x;
			game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir.z;

			/*
			if (1 / (Math.abs((float)Gdx.input.getY()-Gdx.graphics.getHeight())/ Gdx.graphics.getHeight()) >= MAXSCROLLSPEED)
			{
				game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir.x;
				game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir.z;
			}
			else
			{
				game.getCamera().position.x -= 0.01f * 1 / (Math.abs((float)Gdx.input.getY()-Gdx.graphics.getHeight())/ Gdx.graphics.getHeight()) * v_dir.x;
				game.getCamera().position.z -= 0.01f * 1 / (Math.abs((float)Gdx.input.getY()-Gdx.graphics.getHeight())/ Gdx.graphics.getHeight()) * v_dir.x;
			}
			*/
		}
	}

	public void render()
	{
	}
}
