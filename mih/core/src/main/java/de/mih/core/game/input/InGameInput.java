package de.mih.core.game.input;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.Pathfinder.Path;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.TileBorder.Facing;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.events.order.OrderToPointEvent;
import de.mih.core.game.events.order.SelectEvent;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;
import de.mih.core.game.player.Interaction;

public class InGameInput implements InputProcessor
{

	Game game;

	public InGameInput(Game game)
	{
		this.game = game;
	}

	Map<Tile, Integer> pathToEntity = new HashMap<>();
	private Tile start;
	private Tile end = null;

	Vector3 v_dir_ortho = new Vector3();
	Vector3 v_dir = new Vector3();
	Vector3 v_cam_target = new Vector3();

	@Override
	public boolean keyDown(int keycode)
	{
		if (keycode == Keys.F12)
			this.game.toggleEditMode();
		// float speed = 5;
		//
		// if (keycode == Keys.SHIFT_LEFT) {
		// speed *= 2f;
		// }
		//
		// if (keycode == Keys.ALT_LEFT) {
		//
		// if (keycode == Keys.UP) {
		// game.getCamera().position.add(game.getCamera().direction.cpy().scl(0.20f));
		// } else if (keycode == Keys.DOWN) {
		// game.getCamera().position.sub(game.getCamera().direction.cpy().scl(0.20f));
		// }
		//
		// }
		//
		// if (keycode == Keys.CONTROL_LEFT) {
		//
		// v_dir_ortho.set(game.getRenderManager().getCamera().direction).crs(game.getRenderSystem().Y_AXIS);
		// v_cam_target = game.getRenderManager().getCameraTarget(0);
		//
		// if (keycode == Keys.UP) {
		// game.getCamera().rotateAround(v_cam_target, v_dir_ortho, -0.1f *
		// speed);
		// } else if (keycode == Keys.DOWN) {
		// game.getCamera().rotateAround(v_cam_target, v_dir_ortho, 0.1f *
		// speed);
		// }
		//
		// if (keycode == Keys.LEFT) {
		// game.getCamera().rotateAround(v_cam_target,
		// game.getRenderSystem().Y_AXIS, -0.1f * speed);
		//
		// } else if (keycode == Keys.RIGHT) {
		// game.getCamera().rotateAround(v_cam_target,
		// game.getRenderSystem().Y_AXIS, 0.1f * speed);
		// }
		//
		// } else {
		// System.out.println("camera steuerung");
		// v_dir_ortho.set(game.getCamera().direction).crs(game.getRenderSystem().Y_AXIS).setLength(1);
		// v_dir.set(game.getCamera().direction.x, 0,
		// game.getCamera().direction.z).setLength(1);
		//
		// if (keycode == Keys.UP) {
		// game.getCamera().position.x += 0.01f * speed * v_dir.x;
		// game.getCamera().position.z += 0.01f * speed * v_dir.z;
		//
		// } else if (keycode == Keys.DOWN) {
		// game.getCamera().position.x -= 0.01f * speed * v_dir.x;
		// game.getCamera().position.z -= 0.01f * speed * v_dir.z;
		// }
		//
		// if (keycode == Keys.LEFT) {
		// game.getCamera().position.x -= 0.01f * speed * v_dir_ortho.x;
		// game.getCamera().position.z -= 0.01f * speed * v_dir_ortho.z;
		//
		// } else if (keycode == Keys.RIGHT) {
		// game.getCamera().position.x += 0.01f * speed * v_dir_ortho.x;
		// game.getCamera().position.z += 0.01f * speed * v_dir_ortho.z;
		// }
		// }

		if (this.game.isEditMode())
		{

			if (keycode == Keys.W)
			{
				Vector3 mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = game.getTilemap().getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasColliderEntity())
				{
					closest.removeColliderEntity();
				}
				else
				{
					int wall = this.game.getBlueprintManager().createEntityFromBlueprint("wall");
//					if(closest.facing == Facing.WE)
//					{
//						this.game.getEntityManager().getComponent(wall, VisualC.class).visual.angle = 90;
//					}
					closest.setColliderEntity(wall);
				}
			}
			else if (keycode == Keys.D)
			{
				Vector3 mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = game.getTilemap().getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for (TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if (tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if (closest.hasColliderEntity())
				{
					closest.removeColliderEntity();
				}
				else
				{
					closest.setColliderEntity(this.game.getBlueprintManager().createEntityFromBlueprint("door"));
				}
			}
			else if (keycode == Keys.F11)
			{
				try
				{
					game.getTilemapParser().writeTilemap(Gdx.files.internal("assets/maps/map1.xml").path(),
					game.getTilemap());
				}
				catch (ParserConfigurationException | TransformerException e)
				{
					e.printStackTrace();
				}
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

			VisualC vis = game.getEntityManager().getComponent(entity, VisualC.class);
			PositionC pos = game.getEntityManager().getComponent(entity, PositionC.class);

			float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

			Vector3 temp_pos = Vector3.Zero;
			temp_pos.set(pos.getPos());
			temp_pos.add(vis.visual.pos);
			temp_pos.y += vis.visual.bounds.getHeight() / 2f;

			if (Intersector.intersectRaySphere(ray, temp_pos, radius, null))
			{
				return true;
			}

			return false;
		};

		if (button == Input.Buttons.LEFT)
		{
			if (game.getContextMenu().visible)
			{
				game.getContextMenu().getButtons().clear();
				game.getContextMenu().hide();
				return true;
			}

			List<Integer> all = this.game.getEntityManager().getEntitiesOfType(predicate, PositionC.class,
					SelectableC.class);
			if (!all.isEmpty())
				min_entity = all.get(0);

			// min_entity =
			// this.game.getRenderManager().getSelectedEntityByFilter(screenX,
			// screenY, SelectableC.class);

			if (min_entity != -1)
			{
				game.getActivePlayer().clearSelection();

				game.getActivePlayer().selectUnit(min_entity);
				
				System.out.println(game.getEntityManager().getComponent(min_entity, PositionC.class).position.toString());
				this.game.getEventManager().fire(new SelectEvent(game.getActivePlayer(), min_entity));
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT && !game.getActivePlayer().isSelectionEmpty())
		{
			List<Integer> all = this.game.getEntityManager().getEntitiesOfType(predicate, PositionC.class,
					SelectableC.class);
			if (!all.isEmpty())
				min_entity = all.get(0);

			// min_entity =
			// this.game.getRenderManager().getSelectedEntityByFilter(screenX,
			// screenY, InteractableC.class);

			CircularContextMenu contextMenu = game.getContextMenu();
			
			Interaction inter = new Interaction("moveto",
					game.getAssetManager().assetManager.get("assets/icons/goto.png", Texture.class));
			// inter.listener = Interaction.MOVETO;
			inter.listener = (actor, target) ->
			{
				EntityManager entityM = game.getEntityManager();
				PositionC actorpos = entityM.getComponent(actor, PositionC.class);
				PositionC targetpos = entityM.getComponent(target, PositionC.class);


				Path path = game.getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());

				if (path == null){
					System.out.println("No Path found!");
					return;
				}

				OrderableC order = game.getEntityManager().getComponent(actor, OrderableC.class);
			//	Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(actor,  targetpos.getPos()));
				order.isinit = false;
				if(order.hasOrder())
				{
					order.currentorder.finish();
				}
				order.addOrder(new MoveOrder(path));

			};
			game.getEntityManager().getComponent(contextMenu.ordertarget, PositionC.class)
					.setPos(game.getRenderManager().getMouseTarget(0, Gdx.input).cpy());

//			if (Interaction.canUse(game.getActivePlayer().selectedunits.get(0), inter))
//			{
				CircularContextMenuButton _button = new CircularContextMenuButton(contextMenu, inter.icon);
				// contextMenu.addButton(inter,game.getActivePlayer().selectedunits.get(0));
				inter.setActor(game.getActivePlayer().selectedunits.get(0));
				inter.setTarget(contextMenu.ordertarget);

				_button.addClickListener(() -> inter.interact());
				contextMenu.addButton(_button);
				contextMenu.setPosition(screenX, screenY);
				contextMenu.calculateButtonPositions();
				contextMenu.show();
//				System.out.println("clicking on the ground");
//				for (CircularContextMenuButton b : contextMenu.getButtons())
//				{
//					// b.interaction.setActor(game.getActivePlayer().selectedunits.get(0));
//					// b.interaction.setTarget(contextMenu.ordertarget);
//					// b.addClickListener(
//					// () -> b.interaction.interact());
//				}

				return true;
//			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (button == Input.Buttons.RIGHT)
		{
			game.getContextMenu().hide();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		/*
		 * if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
		 * game.getCamera().position.add(game.getCamera().direction.cpy().scl(0.
		 * 20f)); } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		 * game.getCamera().position.sub(game.getCamera().direction.cpy().scl(0.
		 * 20f)); }
		 */
		// float scale = game.getCamera().position.len();
//		System.out.println(amount);
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