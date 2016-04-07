package de.mih.core.game.input.contextmenu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

public class CircularContextMenu extends InputAdapter {
	public Vector2 center = new Vector2();

	private List<CircularContextMenuButton> buttons = new ArrayList<>();

	public boolean visible;

	public int ordertarget = -1;

	public final float radius = 50;

	private EntityManager entityManager;

	public CircularContextMenu() {
		this.entityManager = Game.getCurrentGame().getEntityManager();
		ordertarget = entityManager.createEntity();
		entityManager.addComponent(ordertarget, new PositionC());
	}

	public void addButton(CircularContextMenuButton button)
	{
		this.buttons.add(button);
	}



	// float c;
	// float h;
	// float r;
	public void calculateButtonPositions() {

		// TODO: Dynamic radius and iconsize

		// c = (float)
		// Math.sqrt(2*radius*radius*(1-Math.cos(360/buttons.size())));
		// h = (float) Math.sqrt(radius*radius-c*c/4);
		// r = c/2 * 360/buttons.size()*0.02f;
		float r = 20;
		for (int i = 0; i < buttons.size(); i++) {
			CircularContextMenuButton button = buttons.get(i);
			button.pos.x = radius;
			button.pos.y = 0;
			button.iconsize = r;
			button.pos.rotate((i * 360 + 180) / buttons.size());
			button.pos.add(center);
		}
	}

	public CircularContextMenuButton getButton(int index) {
		return this.buttons.get(index);
	}

	public void setPosition(float x, float y) {
		center.x = x;
		center.y = y;
	}

	public void show() {
		this.visible = true;
	}

	public void hide() {
		this.visible = false;
	}

	public class Pair<T> {
		public Pair(T first, T second) {
			this.first = first;
			this.second = second;
		}

		public T first;
		public T second;
	}

	List<Pair> lines = new ArrayList<>();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!visible)
			return false;
		for (CircularContextMenuButton cButton : buttons) {
			if (cButton.touchDown(screenX, screenY, pointer, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!visible)
			return false;
		if (button == Input.Buttons.RIGHT) {
			buttons.clear();
			lines.clear();
			this.hide();
			return true;
		}

		return false;
	}

	public List<CircularContextMenuButton> getButtons() {
		return this.buttons;
	}

}
