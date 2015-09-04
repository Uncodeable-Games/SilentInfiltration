package de.mih.core.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ContextMenu extends InputAdapter{
	float cx,cy;
	
	float xS, yS;
	boolean visible;
	float radius;
	float rSquared;
	Texture background;
	float background_x, background_y;
	
	public ContextMenu(float radius, Texture background)
	{
		this.radius = radius;
		this.rSquared = radius * radius;
		this.background = background;
	}
	
	public void setPosition(float x, float y)
	{
		this.background_x = x;
		this.background_y = Gdx.graphics.getHeight() - y - 2*  radius;
		
		this.cx = x - radius;
		this.cy = y - radius;
		this.xS = x*x;
		this.yS = y*y;
	}
	
	public void setCenterPosition(float x, float y)
	{
		this.background_x = x - radius;
		this.background_y = Gdx.graphics.getHeight() - y - radius;
		
		this.cx = x;
		this.cy = y;
		this.xS = x*x;
		this.yS = y*y;
	}
	
	public void show()
	{
		this.visible = true;
	}
	
	public void hide()
	{
		this.visible = false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(!visible)
			return false;
		
		if(button == Input.Buttons.LEFT)
		{
			if(this.xS - (screenX*screenX) + this.yS - (screenY * screenY) <= rSquared)
			{
				//is in context menu
				return true;
			}
		}
		
		return false;
	}
	
	public void update()
	{
		/*int mousex = Gdx.input.getX();
		int mousey = Gdx.input.getY();
		if(visible)
		{
			if(mousex < background_x || mousex > background_x + 2*radius || mousey > background_y || mousey < background_y - 2*radius)
			{
				this.hide();
			}
		}*/
	}
	
	public void render(SpriteBatch spriteBatch)
	{
		if(visible)
		{
			spriteBatch.draw(background,background_x,background_y,radius*2,radius*2,0,0,background.getWidth(),background.getHeight(),false,false);
			//System.out.println("draw context!");
		}
	}
}
