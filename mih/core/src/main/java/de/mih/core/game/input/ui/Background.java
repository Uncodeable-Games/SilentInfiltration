package de.mih.core.game.input.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.game.input.ui.UserInterface.Border;

public class Background {

	Border border;

	public Vector2 pos = new Vector2();
	
	public Vector2 fixedoffset = new Vector2();
	public Vector2 ratiooffset = new Vector2();
	public Texture texture;
	
	public boolean visible = true;

	public Background() {}
	
	public Background(Border b,float fix_x, float fix_y, float ratio_x, float ratio_y, Texture t){
		border = b;
		fixedoffset.x = fix_x;
		fixedoffset.y = fix_y;
		ratiooffset.x = ratio_x;
		ratiooffset.y = ratio_y;
		this.texture = t;
		calculatePosition();
	}
	
	public void hide(){
		visible = false;
	}
	
	public void show(){
		visible = true;
	}
	
	public void calculatePosition(){
		switch (border) {
		case BOTTOM_LEFT: {
			pos.set(Gdx.graphics.getWidth() * ratiooffset.x + fixedoffset.x,
					Gdx.graphics.getHeight() * ratiooffset.y + fixedoffset.y);
			break;
		}
		case BOTTOM_RIGHT: {
			pos.set(Gdx.graphics.getWidth() - texture.getWidth() + Gdx.graphics.getWidth() * ratiooffset.x
					+ fixedoffset.x, Gdx.graphics.getHeight() * ratiooffset.y + fixedoffset.y);
			break;
		}
		case TOP_LEFT: {
			pos.set(Gdx.graphics.getWidth() * ratiooffset.x + fixedoffset.x, Gdx.graphics.getHeight()
					- texture.getHeight() + Gdx.graphics.getHeight() * ratiooffset.y + fixedoffset.y);
			break;
		}
		case TOP_RIGHT: {
			pos.set(
					Gdx.graphics.getWidth() - texture.getWidth() + Gdx.graphics.getWidth() * ratiooffset.x
							+ fixedoffset.x,
					Gdx.graphics.getHeight() - texture.getHeight() + Gdx.graphics.getHeight() * ratiooffset.y
							+ fixedoffset.y);
			break;
		}
		}
	}
}
