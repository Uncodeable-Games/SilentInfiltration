package de.mih.core.game.GameStates;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;

public class PlayingGameState extends BaseGameState {

	BitmapFont font;

	public boolean editMode;
	
	@Override
	public void onstart() {
		new Game("assets/maps/map1.xml");
	}

	@Override
	public void update() {
		SystemManager.getInstance().update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render() {
		RenderManager.getInstance().render();
		RenderManager.getInstance().spriteBatch.begin();
		if(this.editMode)
		{
			font.draw(RenderManager.getInstance().spriteBatch, "EDIT MODE - (F11) to save (F12) to close", 10, Gdx.graphics.getHeight() - 10);
			font.draw(RenderManager.getInstance().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight() - 26);
			font.draw(RenderManager.getInstance().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight() - 42);
		}
		RenderManager.getInstance().spriteBatch.end();
	}

	@Override
	public void onend() {}

}
