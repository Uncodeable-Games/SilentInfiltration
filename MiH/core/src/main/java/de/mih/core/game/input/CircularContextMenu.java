package de.mih.core.game.input;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Painter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class CircularContextMenu extends InputAdapter{
	Circle circle;
	Vector2 center;
	
	final int SEGMENTS = 6;
	List<CircularContextMenuButton> buttons = new ArrayList<>();;
	
	boolean visible;

	Texture background;
	float background_x, background_y;
	
	public CircularContextMenu(float radius, Texture background)
	{
		this.background = background;
		
		this.circle = new Circle(0,0,radius);
		
		for(int i = 0; i < SEGMENTS; i++)
		{
			float startAngle = MathUtils.PI*2 / SEGMENTS * i;
			float endAngle = startAngle + MathUtils.PI*2 / SEGMENTS;
			buttons.add(new CircularContextMenuButton(this,startAngle,endAngle));
		}
	}
	
	public CircularContextMenuButton getButton(int index)
	{
		return this.buttons.get(index);
	}
	
	public void setPosition(float x, float y)
	{
		this.background_x = x;
		this.background_y = Gdx.graphics.getHeight() - y - 2*  circle.radius;

		this.circle.setX(x + circle.radius);
		this.circle.setY(y + circle.radius);
		this.center = new Vector2(circle.x,Gdx.graphics.getHeight() -  circle.y);

	}

	
	public void show()
	{
		this.visible = true;
	}
	
	public void hide()
	{
		this.visible = false;
	}
	
	public class Pair<T>
	{
		public Pair(T first, T second)
		{
			this.first = first;
			this.second = second;
		}
		public T first;
		public T second;
	}
	
	List<Pair> lines = new ArrayList<>();
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		if(!visible)
			return false;

		if(circle.contains(screenX,screenY))
		{
			for(CircularContextMenuButton cButton : buttons)
			{
				if(cButton.touchDown(screenX, screenY, pointer, button))
				{
					return true;
				}
			}
		}		
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
		if(!visible)
			return false;
		if(button == Input.Buttons.RIGHT)
		{
			lines.clear();
			this.hide();
			return true;
		}
		
		return false;
	}
	
	public void render(SpriteBatch spriteBatch)
	{
		if(visible)
		{
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			Vector2 center = new Vector2(circle.x,Gdx.graphics.getHeight() -  circle.y);
			Vector2 lineend;
			spriteBatch.draw(background,background_x,background_y,circle.radius*2,circle.radius*2,0,0,background.getWidth(),background.getHeight(),false,false);
			spriteBatch.end();
			shapeRenderer.begin(ShapeType.Line);
			for(Pair<Vector2> p : lines)
			{
				shapeRenderer.line(p.first, p.second);
			}
			for(int i = 0; i < SEGMENTS; i++)
			{
				float x = (float) (center.x - circle.radius *  Math.cos(Math.PI*2 / SEGMENTS * i));
				float y = (float) (center.y + circle.radius *  Math.sin(Math.PI*2 / SEGMENTS * i));
				lineend = new Vector2(x, y);
				shapeRenderer.line(center,lineend);
			}
			shapeRenderer.end();
			spriteBatch.begin();
		}
	}
}
