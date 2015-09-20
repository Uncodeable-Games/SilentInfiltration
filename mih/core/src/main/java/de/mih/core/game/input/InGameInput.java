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
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;
import de.mih.core.game.player.Interaction;
import de.mih.core.game.player.Player;

public class InGameInput implements InputProcessor{
	
	Game game;
	
	public InGameInput(Game game) {
		this.game = game;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
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
			if (game.contextMenu.visible) {
				game.contextMenu.getButtons().clear();
				game.contextMenu.hide();
				return true;
			}
			
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, SelectableC.class);

			if (min_entity != -1){
				game.activePlayer.clearSelection();

				game.activePlayer.selectUnit(min_entity);
				EventManager.getInstance().fire(new SelectEntity_Event(game.activePlayer, min_entity));
				return true;
			}
			return false;
		}
		if (button == Input.Buttons.RIGHT && !game.activePlayer.isSelectionEmpty()) {
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, InteractableC.class);

			CircularContextMenu contextMenu = game.contextMenu;
			if (min_entity != -1) {
				
				InteractableC interactable = EntityManager.getInstance().getComponent(min_entity, InteractableC.class);

				contextMenu.addButtons(interactable.interactions, game.activePlayer.selectedunits.get(0));
				contextMenu.setPosition(screenX, screenY);
				contextMenu.calculateButtonPositions();
				contextMenu.show();
				for (CircularContextMenuButton b : contextMenu.getButtons()) {
					b.interaction.setActor(game.activePlayer.selectedunits.get(0));
					b.interaction.setTarget(min_entity);
					b.addClickListener(
							() -> b.interaction.interact());
				}
				return true;
			}
			
			Interaction inter = new Interaction("moveto", AdvancedAssetManager.getInstance().assetManager.get("assets/icons/sit.png",Texture.class));
			inter.listener = Interaction.MOVETO;
			EntityManager.getInstance().getComponent(contextMenu.ordertarget, PositionC.class).position = RenderManager.getInstance().getMouseTarget(0, Gdx.input).cpy();
			contextMenu.addButton(inter,game.activePlayer.selectedunits.get(0));
			contextMenu.setPosition(screenX, screenY);
			contextMenu.calculateButtonPositions();
			contextMenu.show();
			for (CircularContextMenuButton b : contextMenu.getButtons()) {
				b.interaction.setActor(game.activePlayer.selectedunits.get(0));
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
			game.contextMenu.hide();
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