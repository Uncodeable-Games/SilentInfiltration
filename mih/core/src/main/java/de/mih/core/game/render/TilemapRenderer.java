package de.mih.core.game.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;

public class TilemapRenderer implements BaseRenderer
{
	RenderManager renderManager;
	
	public TilemapRenderer(Tilemap tilemap, RenderManager renderManager)
	{
		this.renderManager = renderManager;
		renderManager.register(this, 0, true);
		this.tilemap = tilemap;
	}
	
	Tilemap tilemap;

	public void render()
	{
		for (int x = 0; x < tilemap.getWidth(); x++)
		{
			for (int y = 0; y < tilemap.getLength(); y++)
			{
				if (renderManager.isVisible(tilemap.getTileAt(x, y).getCenter()))
				{
				//	tilemap.getTileAt(x, y).render();
					Tile current = tilemap.getTileAt(x, y);
					if(current.getVisual() == null)
					{
						Visual visual = new Visual(current.getModelType());
						current.setVisual(visual);
						visual.getModel().materials.get(0).set(TextureAttribute.createDiffuse(Game.getCurrentGame().getAssetManager().assetManager.get(current.getTexture(),Texture.class)));
						
					}
					current.render();
					renderManager.getModelBatch().render(tilemap.getTileAt(x, y).getVisual().getModel(), renderManager.getEnvironment());
				}
			}
		}
	}
}
