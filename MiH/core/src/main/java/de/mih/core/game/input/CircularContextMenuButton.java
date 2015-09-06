package de.mih.core.game.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.game.input.CircularContextMenu.Pair;

public class CircularContextMenuButton extends InputAdapter {
	CircularContextMenu parent;

	public float angle;
	float endAngle;
	
	ArrayList<ClickListener> clickListener = new ArrayList<>();
	
	public CircularContextMenuButton(CircularContextMenu parent, float angle, float endAngle)
	{
		this.parent = parent;
		this.angle = angle;
		this.endAngle = endAngle;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		if(button == Input.Buttons.LEFT)
		{
			Vector2 touchPosition = new Vector2(screenX ,Gdx.graphics.getHeight() - screenY);

			float dx = touchPosition.x - parent.center.x;
			float dy = touchPosition.y - parent.center.y;
			float clickAngle = MathUtils.atan2(dx, dy) + MathUtils.PI/2;
			if(clickAngle < 0)
			{
				clickAngle += MathUtils.PI2;
			}

			boolean result = clickAngle >= angle && clickAngle < endAngle;
			if(result)
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
		for(ClickListener listener : this.clickListener)
		{
			listener.click();
		}
	}
}
