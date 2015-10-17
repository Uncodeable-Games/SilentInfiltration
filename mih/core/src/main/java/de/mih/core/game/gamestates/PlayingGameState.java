package de.mih.core.game.gamestates;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.gamestates.BaseGameState;
import de.mih.core.engine.gamestates.GameStateManager;
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
	
	public PlayingGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	public Game game;
	
	@Override
	public void onstart() {
		game = new Game();
		game.init("assets/maps/map1.xml");
	}

	//TODO: reorganize!
	@Override
	public void update() {
		game.getSystemManager().update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render() {
		game.getRenderManager().render();
		game.getRenderManager().spriteBatch.begin();
		/*if(this.editMode)
		{
			font.draw(RenderManager.getInstance().spriteBatch, "EDIT MODE - (F11) to save (F12) to close", 10, Gdx.graphics.getHeight() - 10);
			font.draw(RenderManager.getInstance().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight() - 26);
			font.draw(RenderManager.getInstance().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight() - 42);
		}*/
		game.getRenderManager().spriteBatch.end();
	}

	@Override
	public void onend() {}

	@Override
	public void resize(int width, int height) {
		game.getRenderManager().spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		game.getUI().resize(width, height);
	}

}