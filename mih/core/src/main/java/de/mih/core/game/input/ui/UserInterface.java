package de.mih.core.game.input.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.game.Game;

import java.util.ArrayList;

public class UserInterface  implements InputProcessor, BaseRenderer
{
	RenderManager renderManager;
	
	public enum Border
	{
		BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT
	}

	private ArrayList<Background> backgrounds = new ArrayList<>();
	private ArrayList<Button>     buttons     = new ArrayList<>();

	private boolean mouseOverUI = false;

	public UserInterface()
	{
		this.renderManager = Game.getCurrentGame().getRenderManager();
		this.renderManager.register(this, 3, false);
		//super(Game.getCurrentGame().getRenderManager(), false, 3);
	}

	public void addBackground(Background b)
	{
		backgrounds.add(b);
	}

	public void removeBackground(Background b)
	{
		backgrounds.remove(b);
	}

	public void addButton(Button b)
	{
		buttons.add(b);
	}

	public void removeButton(Button b)
	{
		buttons.remove(b);
	}

	public boolean isMouseOverUI()
	{
		return mouseOverUI;
	}

	@Override
	public boolean keyDown(int keycode)
	{
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
		if (button == Input.Buttons.LEFT)
		{
			for (Button b : buttons)
			{
				if (b.rect.contains(screenX, Gdx.graphics.getHeight() - screenY))
				{
					for (ClickListener listener : b.clickListeners)
					{
						listener.click();
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		for (Background b : backgrounds)
		{
			if (b.visible)
			{
				Rectangle rectangle = new Rectangle();
				rectangle.set(b.pos.x, b.pos.y, b.texture.getWidth(), b.texture.getHeight());
				if (rectangle.contains(screenX, screenY))
				{
					mouseOverUI = true;
					return true;
				}
			}
		}
		for (Button b : buttons)
		{
			if (b.visible)
			{
				if (b.rect.contains(screenX, Gdx.graphics.getHeight() - screenY))
				{
					mouseOverUI = true;
					return true;
				}
			}
		}
		mouseOverUI = false;
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	@Override
	public void render()
	{
		for (Background b : backgrounds)
		{
			if (b.visible)
			{
				renderManager.spriteBatch.draw(b.texture, b.pos.x, b.pos.y);
			}
		}
		for (Button b : buttons)
		{
			if (b.visible)
			{
				renderManager.spriteBatch.draw(b.texture, b.rect.x, b.rect.y);
				if (b.rect.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
				{
					for (HoverListener listener : b.hoverListeners)
					{
						listener.onHover();
					}
				}
			}
		}
	}

	public void resize(int width, int height)
	{
		for (Background b : backgrounds)
		{
			b.calculatePosition();
		}
		for (Button b : buttons)
		{
			b.calculatePosition();
		}
	}
}
