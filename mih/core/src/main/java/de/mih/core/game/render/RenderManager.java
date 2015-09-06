package de.mih.core.game.render;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.render.AdvancedCamera;
import de.mih.core.engine.tilemap.TilemapRenderer;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.systems.RenderSystem;

public class RenderManager {
	AdvancedCamera camera;
	RenderSystem renderSystem;
	EntityManager entityManager;
	TilemapRenderer tilemapRenderer;
	
	
	public void render()
	{
		tilemapRenderer.render();
		for(int entity = 0; entity < entityManager.entityCount; entity++)
		{
			renderSystem.render(entity);
		}
		renderSystem.render();
	}
	public AdvancedCamera getCamera()
	{
		return camera;
	}

}
