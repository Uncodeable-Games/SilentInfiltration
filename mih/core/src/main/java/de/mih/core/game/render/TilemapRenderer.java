package de.mih.core.game.render;

import com.badlogic.gdx.graphics.g3d.Model;

import de.mih.core.engine.GameStates.GameStateManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;

public class TilemapRenderer extends BaseRenderer {
	public TilemapRenderer() {
		super(true,0);
	}
	
	Tilemap tilemap;
	public void render() {
		tilemap = GameStateManager.getInstance().getCurrentGame().tilemap;
		for(int i = 0; i < tilemap.getLength(); i++)
		{
			for(int x = 0; x < tilemap.getWidth(); x++)
			{
				if(RenderManager.getInstance().isVisible(tilemap.getTileAt(x, i).visual))
				{
					tilemap.getTileAt(x, i).render();
					RenderManager.getInstance().getModelBatch().render(tilemap.getTileAt(x, i).visual.model, RenderManager.getInstance().getEnvironment());
				}
			}
		}
		for(Room room : tilemap.rooms)
		{
			//System.out.println(room.getCenterPoint());
			if(!room.render())
				continue;
			RenderManager.getInstance().getModelBatch().render(room.visual.model, RenderManager.getInstance().getEnvironment());
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
