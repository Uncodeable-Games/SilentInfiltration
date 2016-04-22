package de.mih.core.game.render;

import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;

public class TilemapRenderer extends BaseRenderer
{
	
	public TilemapRenderer(Tilemap tilemap, RenderManager renderManager)
	{
		super(renderManager, true, 0);
		this.tilemap = tilemap;
	}
	
	Tilemap tilemap;

	public void render()
	{
		for (int x = 0; x < tilemap.getWidth(); x++)
		{
			for (int y = 0; y < tilemap.getLength(); y++)
			{
				if (renderManager.isVisible(tilemap.getTileAt(x, y).getVisual()))
				{
					tilemap.getTileAt(x, y).render();
					renderManager.getModelBatch().render(tilemap.getTileAt(x, y).getVisual().getModel(), renderManager.getEnvironment());
				}
			}
		}
	}
}
