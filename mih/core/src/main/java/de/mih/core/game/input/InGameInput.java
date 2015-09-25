package de.mih.core.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.MiH;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;
import de.mih.core.game.player.Interaction;
import de.mih.core.game.player.Player;

public class InGameInput implements InputProcessor{
	//TODO: move that to a better place maybe?
	public Player activePlayer;
	
	public CircularContextMenu contextMenu;

	public EntityManager entityManager;
	
	public MiH mih;

	public Camera camera;
	
	public InGameInput(Player player, CircularContextMenu cm, EntityManager em, Camera camera) {
		this.activePlayer = player;
		this.contextMenu = cm;
		this.entityManager = em;
		this.camera = camera;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
	
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		int min_entity = -1;

		if(button == Input.Buttons.LEFT)
		{
			if (this.contextMenu.visible) {
				this.contextMenu.getButtons().clear();
				this.contextMenu.hide();
				return true;
			}
			
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, SelectableC.class);

			if (min_entity != -1){
				this.activePlayer.clearSelection();

				this.activePlayer.selectUnit(min_entity);
				EventManager.getInstance().fire(new SelectEntity_Event(this.activePlayer, min_entity));
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT && !this.activePlayer.isSelectionEmpty()) {
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, InteractableC.class);

			CircularContextMenu contextMenu = this.contextMenu;
			if (min_entity != -1) {
				
				InteractableC interactable = EntityManager.getInstance().getComponent(min_entity, InteractableC.class);

				contextMenu.addButtons(interactable.interactions);
				contextMenu.setPosition(screenX, screenY);
				contextMenu.calculateButtonPositions();
				contextMenu.show();
				for (CircularContextMenuButton b : contextMenu.getButtons()) {
					b.interaction.setActor(activePlayer.selectedunits.get(0));
					b.interaction.setTarget(min_entity);
					b.addClickListener(
							() -> b.interaction.interact());
				}
				return true;
			}
			contextMenu.addButton(new Interaction("goto", AdvancedAssetManager.getInstance().assetManager.get("assets/icons/goto.png",Texture.class)));
			contextMenu.setPosition(screenX, screenY);
			contextMenu.calculateButtonPositions();
			contextMenu.show();
			for (CircularContextMenuButton b : contextMenu.getButtons()) {
				b.addClickListener(() -> {
					for(int i = 0; i < activePlayer.selectedunits.size(); i++)
					{
						int entity = activePlayer.selectedunits.get(i);
						EntityManager entityM = EntityManager.getInstance();
						PositionC actorpos = entityM.getComponent(entity, PositionC.class);
						//PositionC targetpos = entityM.getComponent(target, PositionC.class);
						//TODO: refactor
					
						Tile start = mih.tilemap.getTileAt(mih.tilemap.coordToIndex_x((int)actorpos.getX()), mih.tilemap.coordToIndex_z((int)actorpos.getZ()));
						Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);

						Tile end = mih.tilemap.getTileAt(mih.tilemap.coordToIndex_x((int)mouseTarget.x),mih.tilemap.coordToIndex_z((int)mouseTarget.z));
						MoveOrder order = new MoveOrder(RenderManager.getInstance().getMouseTarget(0f, Gdx.input),
								start, end, mih.pf.findShortesPath(start, end),	mih.tilemap);
						
						entityM.getComponent(entity, OrderableC.class).newOrder(order);
					}
				});

			}
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.RIGHT)
		{
			this.contextMenu.hide();
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