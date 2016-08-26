package de.mih.core.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.ControlC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;


//TODO for what is this system good? was meant for controller like, network controller, ai controller and keyboard/mouse controller, but currently we are only triggering events, so 
// this should be implemented correct or be removed
public class ControllerSystem extends BaseSystem
{

	public ControllerSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return gameLogic.getEntityManager().hasComponent(entityId, VelocityC.class) && gameLogic.getEntityManager().hasComponent(entityId, ControlC.class)
				&& gameLogic.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(double dt, int entity)
	{
	}


	

	@Override
	public void update(double dt)
	{



		

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
