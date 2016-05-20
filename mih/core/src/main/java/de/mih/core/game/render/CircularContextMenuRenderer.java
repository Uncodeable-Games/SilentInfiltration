package de.mih.core.game.render;

import com.badlogic.gdx.Gdx;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.contextmenu.CircularContextMenuButton;

public class CircularContextMenuRenderer implements BaseRenderer
{
	RenderManager renderManager;
	
	CircularContextMenu contextMenu;

	public CircularContextMenuRenderer(RenderManager renderManager, CircularContextMenu contextMenu)
	{
		//super(renderManager, false, 2);
		this.contextMenu = contextMenu;
		this.renderManager = renderManager;
		this.renderManager.register(this, 2, false);
	}

	@Override
	public void render()
	{
		if (contextMenu.visible)
		{
			for (CircularContextMenuButton button : contextMenu.getButtons())
			{
				renderManager.spriteBatch.draw(button.getTexture(), button.pos.x - button.iconsize,
						Gdx.graphics.getHeight() - button.pos.y - button.iconsize, 2 * button.iconsize,
						2 * button.iconsize);
			}
		}
	}
}
