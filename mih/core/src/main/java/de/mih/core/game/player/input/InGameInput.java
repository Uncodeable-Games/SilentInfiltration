package de.mih.core.game.player.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.events.orderevents.SelectEntity_Event;
import de.mih.core.game.MiH;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
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

	VisualC vis;
	PositionC pos;
	Vector3 temp_pos = new Vector3();
	Vector3 min_pos = new Vector3();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

		Ray ray = camera.getPickRay(screenX, screenY);

		int min_entity = -1;
		if (button == Input.Buttons.LEFT) {
			for (int i = 0; i < entityM.entityCount; i++) {
				if (entityM.hasComponent(i, VisualC.class)) {
					vis = entityM.getComponent(i, VisualC.class);
					pos = entityM.getComponent(i, PositionC.class);

					float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

					temp_pos = pos.position.cpy();
					temp_pos.add(vis.visual.pos);
					temp_pos.y += vis.visual.bounds.getHeight() / 2f;

					if (Intersector.intersectRaySphere(ray, temp_pos, radius, null)) {
						if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos)) {
							min_entity = i;
							min_pos = pos.position;
						}
					}
				}
			}
		}
		if (button == Input.Buttons.RIGHT) {
			for (int i = 0; i < entityM.entityCount; i++) {
				if (entityM.hasComponent(i, VisualC.class)&& entityM.hasComponent(i, InteractableC.class)){
					vis = entityM.getComponent(i, VisualC.class);
					pos = entityM.getComponent(i, PositionC.class);

					float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

					temp_pos = pos.position.cpy();
					temp_pos.add(vis.visual.pos);
					temp_pos.y += vis.visual.bounds.getHeight() / 2f;

					if (Intersector.intersectRaySphere(ray, temp_pos, radius, null)) {
						if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos)) {
							min_entity = i;
							min_pos = pos.position;
						}
					}
				}
			}
		}
		if (entityM.hasComponent(min_entity, InteractableC.class) && button == Input.Buttons.RIGHT
				&& !this.activePlayer.isSelectionEmpty()) {

			InteractableC interactable = entityM.getComponent(min_entity, InteractableC.class);
			CircularContextMenu contextMenu = CircularContextMenu.getInstance();

			contextMenu.addButtons(interactable.baseobject.interactions);
			contextMenu.setPosition(screenX, screenY);
			contextMenu.calculateButtonPositions();
			contextMenu.show();
			for (CircularContextMenuButton b : contextMenu.buttons) {
				b.addClickListener(
						() -> interactable.baseobject.onInteraction(activePlayer.selectedunits.get(0), b.label));
			}
			return true;
		}

		if (button == Input.Buttons.LEFT) {

			if (CircularContextMenu.getInstance().visible) {
				CircularContextMenu.getInstance().buttons.clear();
				CircularContextMenu.getInstance().hide();
				return true;
			}

			if (entityM.hasComponent(min_entity, SelectableC.class)) {
				EventManager.getInstance().fire(new SelectEntity_Event(MiH.activePlayer, min_entity));
				return true;
			} else {
				MiH.activePlayer.clearSelection();
				return false;
			}
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
