package de.mih.core.game.input.contextmenu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Painter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.player.Interaction;

public class CircularContextMenu extends InputAdapter {
	public Vector2 center = new Vector2();

	private List<CircularContextMenuButton> buttons = new ArrayList<>();

	public boolean visible;

	public final float radius = 50;

	public CircularContextMenu() {
	}

	public void addButtons(ArrayList<Interaction> inters, int actor) {
		for (Interaction label : inters) {
			addButton(label, actor);
		}
	}

	public void addButton(Interaction inter, int actor) {
		StatsC stats = EntityManager.getInstance().getComponent(actor, StatsC.class);
		for (String filter : inter.filter){
			try {
				Field field = stats.getClass().getField(filter);
				if (!field.getBoolean(stats)) return;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
		}
		this.buttons.add(new CircularContextMenuButton(this, inter));
	}

	public void removeButton(Interaction inter) {
		for (CircularContextMenuButton b : buttons) {
			if (b.interaction.command.equals(inter.command)) {
				this.buttons.remove(b);
				return;
			}
		}
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
