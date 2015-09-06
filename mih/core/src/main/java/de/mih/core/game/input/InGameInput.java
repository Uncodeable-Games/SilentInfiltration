package de.mih.core.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.Visual;
import de.mih.core.game.player.Player;

public class InGameInput implements InputProcessor{
	//TODO: move that to a better place maybe?
	public Player activePlayer;
	
	public CircularContextMenu contextMenu;

	public EntityManager entityManager;

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
		// TODO Auto-generated method stub
		if(button == Input.Buttons.LEFT)
		{
			//activePlayer.clearSelection();
			//Select
			/*Ray ray = camera.getPickRay(screenX, screenY);

			int min_entity = -1;
			Vector3 min_pos = Vector3.Zero;

			for (int i = 0; i < entityManager.entityCount; i++) {
				if (entityManager.hasComponent(i, Visual.class) && entityManager.hasComponent(i, SelectableC.class)) {
					Visual vis = entityManager.getComponent(i, Visual.class);
					PositionC pos = entityManager.getComponent(i, PositionC.class);

					float radius = (vis.bounds.getWidth() + vis.bounds.getDepth()) / 2f;

					Vector3 temp_pos = pos.position.cpy();
					temp_pos.add(vis.pos);
					temp_pos.y += vis.bounds.getHeight() / 2f;

					if (Intersector.intersectRaySphere(ray, temp_pos, radius, null)) {
						if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos)) {
							min_entity = i;
							min_pos = pos.position;
						}
					}
				}
			}
			
			if (entityManager.hasComponent(min_entity, SelectableC.class)) {
				activePlayer.selectUnit(min_entity);
				entityManager.getComponent(min_entity, SelectableC.class).selected = true;
			}*/
			return false;
		}
		else if(button == Input.Buttons.RIGHT )//&& !this.activePlayer.isSelectionEmpty())
		{
			//Context Menu
			this.contextMenu.setPosition(screenX, screenY);
			this.contextMenu.show();
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
