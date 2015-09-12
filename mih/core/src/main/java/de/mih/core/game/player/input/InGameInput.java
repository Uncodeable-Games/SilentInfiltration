package de.mih.core.game.player.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.game.MiH;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.player.Interaction;
import de.mih.core.game.player.Player;
import de.mih.core.game.player.input.contextmenu.CircularContextMenu;
import de.mih.core.game.player.input.contextmenu.CircularContextMenuButton;

public class InGameInput implements InputProcessor {
	// TODO: move that to a better place maybe?
	public Player activePlayer;

	public EntityManager entityM;

	public Camera camera;

	public InGameInput(Player player, EntityManager em, Camera camera) {
		this.activePlayer = player;
		this.entityM = em;
		this.camera = camera;
	}

	@Override
	public boolean keyDown(int keycode) {
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

	VisualC vis;
	PositionC pos;
	Vector3 temp_pos = new Vector3();
	Vector3 min_pos = new Vector3();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		int min_entity = -1;
		
		if (button == Input.Buttons.LEFT) {
			
			if (CircularContextMenu.getInstance().visible) {
				CircularContextMenu.getInstance().buttons.clear();
				CircularContextMenu.getInstance().hide();
				return true;
			}
			
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, SelectableC.class);
			
			if (min_entity != -1){
				EventManager.getInstance().fire(new SelectEntity_Event(MiH.activePlayer, min_entity));
				return true;
			}
			MiH.activePlayer.clearSelection();
			return false;
		}
		
		
		if (button == Input.Buttons.RIGHT && !this.activePlayer.isSelectionEmpty()) {
			min_entity = RenderManager.getInstance().getSelectedEntityByFilter(screenX, screenY, InteractableC.class);

			CircularContextMenu contextMenu = CircularContextMenu.getInstance();
			if (min_entity != -1) {
				
				InteractableC interactable = entityM.getComponent(min_entity, InteractableC.class);

				contextMenu.addButtons(interactable.interactions);
				contextMenu.setPosition(screenX, screenY);
				contextMenu.calculateButtonPositions();
				contextMenu.show();
				for (CircularContextMenuButton b : contextMenu.buttons) {
					b.interaction.setActor(activePlayer.selectedunits.get(0));
					b.interaction.setTarget(min_entity);
					b.addClickListener(
							() -> b.interaction.interact());
				}
				return true;
			}
			contextMenu.addButton(new Interaction("goto", MiH.assetManager.get("assets/icons/sit.png",Texture.class)));
			contextMenu.setPosition(screenX, screenY);
			contextMenu.calculateButtonPositions();
			contextMenu.show();
			for (CircularContextMenuButton b : contextMenu.buttons) {
				b.addClickListener(() -> System.out.println("move"));
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.RIGHT) {
			CircularContextMenu.getInstance().buttons.clear();
			CircularContextMenu.getInstance().hide();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
