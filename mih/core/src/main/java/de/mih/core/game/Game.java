package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.mih.core.engine.ability.AbilityManager;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.BlueprintManager;
import de.mih.core.engine.lua.LuaScriptManager;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.ui.UserInterface;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.*;

public class Game extends GameLogic
{
	private RenderManager        renderManager;
	private RenderSystem     renderS;
	private TilemapRenderer tilemapRenderer;

	private InputMultiplexer inputMultiplexer;
	private UserInterface    ui;
	private InGameInput      ingameinput;

	private PerspectiveCamera camera;

	private Player activePlayer;

	//static Game currentGame;

	private boolean editMode = false;
	private BitmapFont font;// = new BitmapFont();

	public static Game getCurrentGame()
	{
		return (Game) currentGame;
	}
	
	public Game()
	{
		currentGame = this;
	}

	public void init(String path)
	{
		// Manager setup
		super.init(path);

		
		this.renderManager = new RenderManager(this.entityManager);

		this.ui = new UserInterface();

		// RenderManager
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(8f, 10f, 56f);
		camera.lookAt(8f, 0f, 53f);
		//camera.near = 0.1f;
		//camera.far = 300f;
		this.renderManager.setCamera(camera);

		// Stuff // Tilemap

		activePlayer = new Player("localplayer", 0, Player.PlayerType.Attacker);

		int robo = this.blueprintManager.createEntityFromBlueprint("robocop.json");
		this.entityManager.getComponent(robo, PositionC.class).setPos(8, 0, 53);

		this.activePlayer.setHero(robo);

		// Input
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ui);
		ingameinput = new InGameInput();
		inputMultiplexer.addProcessor(ingameinput);
		Gdx.input.setInputProcessor(inputMultiplexer);

		// Renderer
		tilemapRenderer = new TilemapRenderer(this.tilemap, this.renderManager);

		renderS = new RenderSystem(this.systemManager, this.renderManager, this);
	}

	public RenderManager getRenderManager()
	{
		return renderManager;
	}

	public PerspectiveCamera getCamera()
	{
		return camera;
	}

	public Player getActivePlayer()
	{
		return activePlayer;
	}

	

	public RenderSystem getRenderSystem()
	{
		return renderS;
	}

	public UserInterface getUI()
	{
		return ui;
	}

	public void toggleEditMode()
	{
		this.editMode = !editMode;
	}

	public boolean isEditMode()
	{
		return editMode;
	}

	public void update(double deltaTime)
	{
		this.ingameinput.update(deltaTime);
		super.update(deltaTime);
	}
	
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		this.getRenderManager().render();
		this.getRenderManager().spriteBatch.begin();

		if (this.isEditMode())
		{
			font.draw(this.getRenderManager().spriteBatch, "EDIT MODE - (F10) to calculate Navigation (F11) to save (F12) to close", 10,
					Gdx.graphics.getHeight()-10);
			font.draw(this.getRenderManager().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight()-26);
			font.draw(this.getRenderManager().spriteBatch, "(s) place/remove halfwall", 10, Gdx.graphics.getHeight()-42);
			font.draw(this.getRenderManager().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight()-58);
			font.draw(this.getRenderManager().spriteBatch, "(a) place/remove halfdoor", 10, Gdx.graphics.getHeight()-74);
		}

		this.getRenderManager().spriteBatch.end();
	}
}
