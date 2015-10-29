package de.mih.core.game.gamestates;

import java.util.Map;
import java.util.function.Predicate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
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

public class PlayingGameState extends GameState
{

	public PlayingGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	public Game game;
	BitmapFont font;

	@Override
	public void onEnter()
	{
		game = new Game();
		game.init("assets/maps/map1.xml");
		font = new BitmapFont();
	}

	// TODO: reorganize!
	@Override
	public void update()
	{
		game.getEntityManager().getEntitiesOfType(AttachmentC.class).forEach(entity ->
		{
			if (!game.getEntityManager().hasComponent(entity, AttachmentC.class))
			{
				game.getEntityManager().addComponent(entity, new AttachmentC(entity));
			}

			AttachmentC attachment = game.getEntityManager().getComponent(entity, AttachmentC.class);
			if (attachment.containsAttachment(4))
			{
				attachment.removeAttachment(4);
				// game.getEntityManager().removeComponent(entity, attachment);
			}
		});
		if (!game.getActivePlayer().isSelectionEmpty())
		{
			Vector3 playerPos;// = new Vector3(4, 0, 4);
			int selected = game.getActivePlayer().selectedunits.get(0);
			playerPos = game.getEntityManager().getComponent(selected, PositionC.class).getPos();
			Predicate<Integer> predicate = entity ->
			{
				// game.getActivePlayer().selectedunits;
				PositionC position = game.getEntityManager().getComponent(entity, PositionC.class);

				return position.getPos().dst(playerPos) < 6;
				// return false;
			};

			game.getEntityManager().getEntitiesOfType(predicate, PositionC.class, VisualC.class).forEach(entity ->
			{
				if (!game.getEntityManager().hasComponent(entity, AttachmentC.class))
				{
					game.getEntityManager().addComponent(entity, new AttachmentC(entity));
				}

				AttachmentC attachment = game.getEntityManager().getComponent(entity, AttachmentC.class);
				attachment.addAttachment(4, AdvancedAssetManager.getInstance().getModelByName("center"));
				// game.getEntityManager().addComponent(entity,
				// new AttachmentC(entity,
				// AdvancedAssetManager.getInstance().getModelByName("center"),
				// 4));

			});
		}
		game.getSystemManager().update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render()
	{
		game.getRenderManager().render();
		game.getRenderManager().spriteBatch.begin();

		if (game.isEditMode())
		{
			font.draw(game.getRenderManager().spriteBatch, "EDIT MODE - (F11) to save (F12) to close", 10,
					Gdx.graphics.getHeight() - 10);
			font.draw(game.getRenderManager().spriteBatch, "(w) place/remove wall", 10,
					Gdx.graphics.getHeight() - 26);
			font.draw(game.getRenderManager().spriteBatch, "(d) place/remove door", 10,
					Gdx.graphics.getHeight() - 42);
		}

		game.getRenderManager().spriteBatch.end();
	}

	@Override
	public void onLeave()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		game.getRenderManager().spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		game.getUI().resize(width, height);
	}

}
