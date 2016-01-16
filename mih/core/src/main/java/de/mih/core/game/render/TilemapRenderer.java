package de.mih.core.game.render;

import com.badlogic.gdx.graphics.g3d.Model;

import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;

public class TilemapRenderer extends BaseRenderer {
	
	
	public TilemapRenderer(Tilemap tilemap, RenderManager renderManager) {
		super(renderManager,true,0);
		this.tilemap = tilemap;
		//this.renderManager = renderManager;
	}
	
	Tilemap tilemap;
	public void render() {
		//tilemap = GameStateManager.getInstance().getCurrentGame().tilemap;
		for(int i = 0; i < tilemap.getLength(); i++)
		{
			for(int x = 0; x < tilemap.getWidth(); x++)
			{
				if(renderManager.isVisible(tilemap.getTileAt(x, i).visual))
				{
					tilemap.getTileAt(x, i).render();
					renderManager.getModelBatch().render(tilemap.getTileAt(x, i).visual.model, renderManager.getEnvironment());
				}
			}
		}
		for(TileBorder border : tilemap.getBorders())
		{
//			if(border.hasBorderCollider() && RenderManager.getInstance().isVisible(border.getBorderCollider().getVisual()))
//			{
//				border.getBorderCollider().render();
//				RenderManager.getInstance().getModelBatch().render(border.getBorderCollider().getVisual().model, RenderManager.getInstance().getEnvironment());
//			}
		}
	}

}
