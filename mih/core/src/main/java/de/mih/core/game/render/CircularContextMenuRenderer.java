package de.mih.core.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.game.MiH;
import de.mih.core.game.player.input.contextmenu.CircularContextMenu;
import de.mih.core.game.player.input.contextmenu.CircularContextMenuButton;

public class CircularContextMenuRenderer extends BaseRenderer {

	public CircularContextMenuRenderer() {
		super(false);
	}
	
	@Override
	public void render() {
		for (CircularContextMenuButton button: CircularContextMenu.getInstance().buttons){
			RenderManager.getInstance().spriteBatch.draw(button.interaction.icon, button.pos.x-button.iconsize, Gdx.graphics.getHeight()-button.pos.y-button.iconsize, 2*button.iconsize,2*button.iconsize);
		}
	}
}
