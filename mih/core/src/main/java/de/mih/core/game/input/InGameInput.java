package de.mih.core.game.input;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.events.orderevents.SelectEvent;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;
import de.mih.core.game.player.Interaction;

public class InGameInput implements InputProcessor{
	
	Game game;
	
	public InGameInput(Game game) {
		this.game = game;
	}
	
	Map<Tile,Integer> pathToEntity = new HashMap<>();
	private Tile start;
	private Tile end = null;
	
	public boolean editMode;
	
	public void toggleEditMode() {
		this.editMode = !editMode;
		//EntityManager.getInstance();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.F12)
			this.toggleEditMode();
		
		if(this.editMode)
		{
			if(keycode == Keys.W)
			{
				Vector3 mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = game.getTilemap().getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for(TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if(tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if(closest.hasColliderEntity())
				{
					closest.removeColliderEntity();
				}
				else
				{
					closest.setColliderEntity(this.game.getBlueprintManager().createEntityFromBlueprint("wall"));
				}
			}
			else if(keycode == Keys.D)
			{
				Vector3 mouseTarget = this.game.getRenderManager().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = game.getTilemap().getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for(TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if(tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if(closest.hasColliderEntity())
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
				try {
					game.getTilemapParser().writeTilemap(Gdx.files.internal("assets/maps/map1.xml").path(), game.getTilemap());
				} catch (ParserConfigurationException | TransformerException e) {
					e.printStackTrace();
				}
			}
		}
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		int min_entity = -1;

		if(button == Input.Buttons.LEFT)
		{
			if (game.getContextMenu().visible) {
				game.getContextMenu().getButtons().clear();
				game.getContextMenu().hide();
				return true;
			}
			
			min_entity = this.game.getRenderManager().getSelectedEntityByFilter(screenX, screenY, SelectableC.class);

			if (min_entity != -1){
				game.getActivePlayer().clearSelection();

				game.getActivePlayer().selectUnit(min_entity);
				this.game.getEventManager().fire(new SelectEvent(game.getActivePlayer(), min_entity));
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT && !game.getActivePlayer().isSelectionEmpty()) {
			min_entity =  this.game.getRenderManager().getSelectedEntityByFilter(screenX, screenY, InteractableC.class);

			CircularContextMenu contextMenu = game.getContextMenu();
			if (min_entity != -1) {
				
				InteractableC interactable = this.game.getEntityManager().getComponent(min_entity, InteractableC.class);

				contextMenu.addButtons(interactable.interactions, game.getActivePlayer().selectedunits.get(0));
				contextMenu.setPosition(screenX, screenY);
				contextMenu.calculateButtonPositions();
				contextMenu.show();
				for (CircularContextMenuButton b : contextMenu.getButtons()) {
					b.interaction.setActor(game.getActivePlayer().selectedunits.get(0));
					b.interaction.setTarget(min_entity);
					b.addClickListener(
							() -> b.interaction.interact());
				}
				return true;
			}
			Interaction inter = new Interaction("moveto", game.getAssetManager().assetManager.get("assets/icons/goto.png",Texture.class));
			//inter.listener = Interaction.MOVETO;
			inter.listener = (actor, target) -> {
					EntityManager entityM = game.getEntityManager();
					PositionC actorpos = entityM.getComponent(actor, PositionC.class);
					PositionC targetpos = entityM.getComponent(target, PositionC.class);
					
					NavPoint[] path = game.getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());
					OrderableC order = game.getEntityManager().getComponent(actor,OrderableC.class);
					order.newOrder(new MoveOrder(targetpos.getPos(), path));
					
				};
			game.getEntityManager().getComponent(contextMenu.ordertarget, PositionC.class).setPos(game.getRenderManager().getMouseTarget(0, Gdx.input).cpy());
			contextMenu.addButton(inter,game.getActivePlayer().selectedunits.get(0));
			contextMenu.setPosition(screenX, screenY);
			contextMenu.calculateButtonPositions();
			contextMenu.show();
			for (CircularContextMenuButton b : contextMenu.getButtons()) {
				b.interaction.setActor(game.getActivePlayer().selectedunits.get(0));
				b.interaction.setTarget(contextMenu.ordertarget);
				b.addClickListener(
						() -> b.interaction.interact());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.RIGHT)
		{
			game.getContextMenu().hide();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}