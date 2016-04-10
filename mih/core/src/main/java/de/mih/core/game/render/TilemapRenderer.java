package de.mih.core.game.render;

import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;

public class TilemapRenderer extends BaseRenderer
{
	
	public TilemapRenderer(Tilemap tilemap, RenderManager renderManager)
	{
		super(renderManager, true, 0);
		this.tilemap = tilemap;
		//this.renderManager = renderManager;
	}
	
	Tilemap tilemap;

	public void render()
	{
		//tilemap = GameStateManager.getInstance().getCurrentGame().tilemap;
		for (int x = 0; x < tilemap.getWidth(); x++)
		{
			for (int y = 0; y < tilemap.getLength(); y++)
			{
				if (renderManager.isVisible(tilemap.getTileAt(x, y).visual))
				{
					tilemap.getTileAt(x, y).render();
					renderManager.getModelBatch().render(tilemap.getTileAt(x, y).visual.getModel(), renderManager.getEnvironment());
				}
			}
		}
		for (TileBorder border : tilemap.getBorders())
		{
//			if(border.hasBorderCollider() && RenderManager.getInstance().isVisible(border.getBorderCollider().getVisual()))
//			{
//				border.getBorderCollider().render();
//				RenderManager.getInstance().getModelBatch().render(border.getBorderCollider().getVisual().model, RenderManager.getInstance().getEnvironment());
//			}
		}
	}
}
