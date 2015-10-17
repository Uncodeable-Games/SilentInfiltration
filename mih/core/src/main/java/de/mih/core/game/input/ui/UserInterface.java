package de.mih.core.game.input.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.game.input.ClickListener;

public class UserInterface extends BaseRenderer implements InputProcessor {

	public enum Border {
		BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT
	}
	
	AdvancedAssetManager assetManager;
	ArrayList<Background> backgrounds = new ArrayList<Background>();
	ArrayList<Button> buttons = new ArrayList<Button>();

	public UserInterface(RenderManager renderManager, AdvancedAssetManager assetManager) {
		super(renderManager, false, 3);
		this.assetManager = assetManager;
		Button b = new Button(Border.BOTTOM_LEFT, 0, 0, 0.5f, 0,
				assetManager.assetManager.get("assets/ui/buttons/testbutton.png", Texture.class));
		b.fixedoffset.x = - b.texture.getWidth()/4f;
		b.addlistener(() -> System.out.println("hallo"));
		addButton(b);
		addBackground(new Background(Border.BOTTOM_LEFT, 0, 0, 0, 0,
				assetManager.assetManager.get("assets/ui/backgrounds/b_bottom_left.png")));
		addBackground(new Background(Border.BOTTOM_RIGHT, 0, 0, 0, 0,
				assetManager.assetManager.get("assets/ui/backgrounds/b_bottom_right.png")));
	}

	public void addBackground(Background b) {
		backgrounds.add(b);
	}

	public void removeBackground(Background b) {
		backgrounds.remove(b);
	}

	public void addButton(Button b) {
		buttons.add(b);
	}

	public void removeButton(Button b) {
		buttons.remove(b);
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			for (Button b : buttons) {
				if (b.rect.contains(screenX, Gdx.graphics.getHeight() - screenY)) {
					for (ClickListener listener : b.listener) {
						listener.click();
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

	@Override
	public void render() {
		for (Background b : backgrounds) {
			if (b.visible) {
				renderManager.spriteBatch.draw(b.texture, b.pos.x, b.pos.y);
			}
		}
		for (Button b : buttons) {
			if (b.visible) {
				renderManager.spriteBatch.draw(b.texture, b.rect.x, b.rect.y);
			}
		}
	}

	public void resize(int width, int height) {
		for (Background b : backgrounds) {
			b.calculatePosition();
		}
		for (Button b : buttons) {
			b.calculatePosition();
		}
	}

}
