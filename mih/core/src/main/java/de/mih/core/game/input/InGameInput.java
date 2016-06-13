package de.mih.core.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ability.Ability;
import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.Blueprints.Tilemap.TilemapBlueprint;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.events.order.AbilityCastOnPointEvent;
import de.mih.core.game.events.order.AbilityCastOnTargetEvent;
import de.mih.core.game.events.order.OrderToPointEvent;
import de.mih.core.game.player.Player;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class InGameInput implements InputProcessor
{

	private static final float ROTATESPEED = 0.5f;
	Game game;

	public InGameInput()
	{
		this.game = (Game) GameLogic.getCurrentGame();
	}
	
	private Vector3 v_dir_ortho  = new Vector3();
	private Vector3 v_dir        = new Vector3();

	private final float SCROLLBORDER   = 0.15f;
	private final float MAXSCROLLSPEED = 30f;
	float speed = 20;


	@Override
	public boolean keyDown(int keycode)
	{
		if (keycode == Keys.F12)
			this.game.toggleEditMode();

		if (this.game.isEditMode())
		{

			if (keycode == Keys.W)
			{
				Vector3          mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders     = game.getTilemap().getBorders();
				TileBorder       closest     = borders.get(0);
				float            closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget);
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasCollider())
				{
					closest.removeCollider();
				}
				else
				{
					closest.setToWall(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("wall.json"), "wall.json");
				}
			}
			else if (keycode == Keys.D)
			{
				Vector3          mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders     = game.getTilemap().getBorders();
				TileBorder       closest     = borders.get(0);
				float            closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget);
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasCollider())
				{
					closest.removeCollider();
				}
				else
				{
					closest.setToDoor(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("door.json"), "door.json");
				}
			}
			else if (keycode == Keys.S)
			{
				Vector3          mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders     = game.getTilemap().getBorders();
				TileBorder       closest     = borders.get(0);
				float            closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget);
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasCollider())
				{
					closest.removeCollider();
				}
				else
				{
					closest.setToWall(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("halfwall.json"), "halfwall.json");
				}
			}
			else if (keycode == Keys.A)
			{
				Vector3          mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders     = game.getTilemap().getBorders();
				TileBorder       closest     = borders.get(0);
				float            closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget);
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasCollider())
				{
					closest.removeCollider();
				}
				else
				{
					closest.setToDoor(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("halfdoor.json"), "halfdoor.json");
				}
			}
			else if (keycode == Keys.F11)
			{
				try
				{
					game.getBlueprintManager().writeBlueprintToJson(new TilemapBlueprint(game.getTilemap()), "assets/maps/map1.json");
					System.out.println("Map saved to assets/maps/map1.json!");
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else if (keycode == Keys.F10)
			{
				Game.getCurrentGame().getTilemap().calculateRooms();
				Game.getCurrentGame().getNavigationManager().calculateNavigation(Game.getCurrentGame().getTilemap());
				System.out.println("Navigation calculated!");
			}
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	private Vector3 intersect = new Vector3();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		int min_entity = -1;
		Predicate<Integer> predicate = entity ->
		{
			Ray ray = game.getCamera().getPickRay(screenX, screenY);
			if (!game.getEntityManager().hasComponent(entity, VisualC.class)
					|| !game.getEntityManager().hasComponent(entity, PositionC.class))
			{
				return false;
			}

			VisualC   vis = game.getEntityManager().getComponent(entity, VisualC.class);
			PositionC pos = game.getEntityManager().getComponent(entity, PositionC.class);

			float radius = (vis.getVisual().getBounds().getWidth() + vis.getVisual().getBounds().getDepth()) / 2f;

			Vector3 temp_pos = Vector3.Zero;
			temp_pos.set(pos.getPos());
			temp_pos.add(vis.getVisual().getPos());
			temp_pos.y += vis.getVisual().getBounds().getHeight() / 2f;

			if (Intersector.intersectRaySphere(ray, temp_pos, radius, intersect))
			{
				return true;
			}

			return false;
		};


		if (button == Input.Buttons.LEFT)
		{
			Player activePlayer = game.getActivePlayer();

			if (activePlayer.isTargeting())
			{

				if (activePlayer.getAbilityBeingTargeted() == null)
				{
					System.out.println("ERROR: No ability targeted!");
					return true;
				}

				List<Integer> all = this.game.getEntityManager().getEntitiesOfType(predicate, PositionC.class,
						SelectableC.class);
				if (!all.isEmpty())
					min_entity = all.get(0);
				
				Ability usedAbility = activePlayer.getAbilityBeingTargeted();
				if (min_entity != -1)
				{
					//usedAbility.castOnTarget(activePlayer.getHero(), min_entity, intersect.cpy());
					Game.getCurrentGame().getEventManager().fire(new AbilityCastOnTargetEvent(activePlayer.getHero(), min_entity, intersect.cpy(), usedAbility.getId()));
				}
				else
				{
				//	usedAbility.castOnPoint(activePlayer.getHero(), game.getRenderManager().getMouseTarget(0, Gdx.input));
					Game.getCurrentGame().getEventManager().fire(new AbilityCastOnPointEvent(activePlayer.getHero(),  game.getRenderManager().getMouseTarget(0, Gdx.input), usedAbility.getId()));
				}
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT)
		{
			if (game.getActivePlayer().getPlayerType() != Player.PlayerType.Attacker) return false;

			Player player = this.game.getActivePlayer();

			EntityManager entityM  = game.getEntityManager();
			Vector3       target   = game.getRenderManager().getMouseTarget(0, Gdx.input);

			Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(player.getHero(),  target));//targetpos.getPos()));
			
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO Auto-generated method stub
		return false;
	}

	boolean waspressed = false;

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return  false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if (amount > 0)
		{
			game.getCamera().position.sub(game.getCamera().direction.cpy().scl(2));
		}
		else if (amount < 0)
		{
			game.getCamera().position.add(game.getCamera().direction.cpy().scl(2));
		}
		return false;
	}
	
	public void update(double deltaTime)
	{
		float speed = 10 * (float) deltaTime;

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


//		// RIGHT SCROLL BORDER
//		if (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth() <= SCROLLBORDER)
//		{
//			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
//			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
//
//			game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
//			game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
//
//			/*
//			if (1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) >= MAXSCROLLSPEED)
//			{
//				game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
//				game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
//			}
//			else
//			{
//				game.getCamera().position.x += 0.01f * 1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) * v_dir_ortho.x;
//				game.getCamera().position.z += 0.01f * 1 / (Math.abs((float)Gdx.input.getX() - Gdx.graphics.getWidth()) / Gdx.graphics.getWidth()) * v_dir_ortho.z;
//			}
//			*/
//		}
//
//		// LEFT SCROLL BORDER
//		if ((float)Gdx.input.getX()/ Gdx.graphics.getWidth() <= SCROLLBORDER )
//		{
//			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
//			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
//
//
//			game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
//			game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
//			/*
//			if (1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) >= MAXSCROLLSPEED)
//			{
//				game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.x;
//				game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir_ortho.z;
//			}
//			else
//			{
//				game.getCamera().position.x -= 0.01f * 1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) * v_dir_ortho.x;
//				game.getCamera().position.z -= 0.01f * 1 / ((float)Gdx.input.getX()/ Gdx.graphics.getWidth()) * v_dir_ortho.z;
//			}
//			*/
//		}
//
//		// UP SCROLL BORDER
//		if ((float)Gdx.input.getY()/ Gdx.graphics.getHeight() <= SCROLLBORDER)
//		{
//			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
//			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
//
//
//			game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir.x;
//			game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir.z;
//
//			/*
//			if ( 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) >= MAXSCROLLSPEED)
//			{
//				game.getCamera().position.x += 0.01f * MAXSCROLLSPEED * v_dir.x;
//				game.getCamera().position.z += 0.01f * MAXSCROLLSPEED * v_dir.z;
//			}
//			else
//			{
//				game.getCamera().position.x += 0.01f * 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) * v_dir.x;
//				game.getCamera().position.z += 0.01f * 1 / ((float)Gdx.input.getY()/ Gdx.graphics.getHeight()) * v_dir.x;
//			}
//			*/
//		}
//
//		// DOWN SCROLL BORDER
//		if (Math.abs((float)Gdx.input.getY()-Gdx.graphics.getHeight())/ Gdx.graphics.getHeight() <= SCROLLBORDER)
//		{
//			v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
//			v_dir.set(game.getCamera().direction.x, 0, game.getCamera().direction.z).setLength(1);
//
//			game.getCamera().position.x -= 0.01f * MAXSCROLLSPEED * v_dir.x;
//			game.getCamera().position.z -= 0.01f * MAXSCROLLSPEED * v_dir.z;
//			
//		}
	}
}