package de.mih.core.game.input.contextmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.mih.core.game.input.ClickListener;

import java.util.ArrayList;

public class CircularContextMenuButton extends InputAdapter
{
	CircularContextMenu parent;

	public Vector2 pos = new Vector2();
	public float iconsize;
	
	Texture texture;
	//public Interaction interaction;
	
	ArrayList<ClickListener> clickListener = new ArrayList<>();
	
	public CircularContextMenuButton(CircularContextMenu parent, Texture texture)// Interaction inter)
	{
		this.parent = parent;
		this.texture = texture;
		//this.interaction = inter;
	}
	
	Vector2 touchPosition = new Vector2();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (button == Input.Buttons.LEFT)
		{
			touchPosition.x = screenX;
			touchPosition.y = screenY;

			boolean result = (touchPosition.dst2(pos) <= (iconsize * iconsize) / 2);
			if (result)
			{
				/*
				 * button is clicked, but maybe wait for touch up
				 */
				this.click();
			}
			return result;
		}
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}
	
	public void addClickListener(ClickListener listener)
	{
		this.clickListener.add(listener);
	}
	
	public void removeClickListener(ClickListener listener)
	{
		this.clickListener.remove(listener);
	}
	
	public void click()
	{
		for (ClickListener listener : this.clickListener)
		{
			listener.click();
		}
	}
	
	public Texture getTexture()
	{
		return this.texture;
	}
}
