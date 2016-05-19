package de.mih.core.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.Blueprints.Tilemap.TilemapBlueprint;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
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
		this.game = Game.getCurrentGame();
	}

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
				float            closestDist = closest.getCenter().dst(mouseTarget.x, mouseTarget.z);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget.x, mouseTarget.z);
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
				float            closestDist = closest.getCenter().dst(mouseTarget.x, mouseTarget.z);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget.x, mouseTarget.z);
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
				float            closestDist = closest.getCenter().dst(mouseTarget.x, mouseTarget.z);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget.x, mouseTarget.z);
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
				float            closestDist = closest.getCenter().dst(mouseTarget.x, mouseTarget.z);
				for (TileBorder border : borders)
				{
					float tmp = border.getCenter().dst(mouseTarget.x, mouseTarget.z);
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
				Game.getCurrentGame().getNavigationManager().calculateNavigation();
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

				if (min_entity != -1)
				{
					activePlayer.getAbilityBeingTargeted().castOnTarget(activePlayer.getHero(), min_entity, intersect.cpy());
				}
				else
				{
					activePlayer.getAbilityBeingTargeted().castOnPoint(activePlayer.getHero(), game.getRenderManager().getMouseTarget(0, Gdx.input));
				}
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT)
		{
			if (game.getActivePlayer().getPlayerType() != Player.PlayerType.Attacker) return false;

			Player player = Game.getCurrentGame().getActivePlayer();

			EntityManager entityM  = game.getEntityManager();
			Vector3       target   = game.getRenderManager().getMouseTarget(0, Gdx.input);

			Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(actor,  target));//targetpos.getPos()));
			
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
}