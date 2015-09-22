package de.mih.core.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.game.MiH;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;

public class CircularContextMenuRenderer extends BaseRenderer {

	CircularContextMenu contextMenu;

	public CircularContextMenuRenderer(CircularContextMenu contextMenu) {
		super(false,2);
		this.contextMenu = contextMenu;
	}

	@Override
	public void render() {
		if (contextMenu.visible) {
			for (CircularContextMenuButton button : contextMenu.getButtons()) {
				RenderManager.getInstance().spriteBatch.draw(button.interaction.icon, button.pos.x - button.iconsize,
						Gdx.graphics.getHeight() - button.pos.y - button.iconsize, 2 * button.iconsize,
						2 * button.iconsize);
			}
		}
	}
}
