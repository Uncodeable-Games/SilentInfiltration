package de.mih.core.game.gamestates;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.network.GameClient;

public class LobbyState extends GameState
{

	public LobbyState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
		// TODO Auto-generated constructor stub
	}

	public Game game;
	BitmapFont font;
	GameClient client;

	@Override
	public void onEnter()
	{
		game = new Game();
		game.init("assets/maps/map1.xml");
		try
		{
			client = new GameClient(this.game, "127.0.0.1", 13337);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		font = new BitmapFont();

	}

	@Override
	public void update()
	{
		game.getSystemManager().update(Gdx.graphics.getDeltaTime());
		game.update();
	}


	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		debug();
		game.getRenderManager().render();
		game.getRenderManager().spriteBatch.begin();

		if (game.isEditMode())
		{
			font.draw(game.getRenderManager().spriteBatch, "EDIT MODE - (F11) to save (F12) to close", 10,
					Gdx.graphics.getHeight() - 10);
			font.draw(game.getRenderManager().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight() - 26);
			font.draw(game.getRenderManager().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight() - 42);
		}

		game.getRenderManager().spriteBatch.end();

		if (game.isGameOver)
		{
			gamestateManager.changeGameState("MAIN_MENU");
		}
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

	void debug()
	{

//		if (true) // DEBUG
//		{
//			ShapeRenderer sr = new ShapeRenderer();
//			OrthographicCamera camera = new OrthographicCamera(60, 100);
//			// PerspectiveCamera camera = game.getRenderManager().getCamera();
//			sr.setProjectionMatrix(camera.combined);
//
//			sr.begin(ShapeType.Line);
//			sr.setColor(Color.WHITE);
//
//			if (game.getEntityManager().hasComponent(game.robo, PositionC.class))
//			{
//				Vector3 position = game.getEntityManager().getComponent(game.robo, PositionC.class).getPos();
//				sr.circle(position.x, position.z, 0.5f);
//			}
//			sr.setColor(Color.RED);
//			sr.setColor(Color.YELLOW);
//
//			Tilemap map = game.getTilemap();
//			for (Line line : map.colLines)
//			{
//				sr.line(line.from, line.to);
//			}
//			sr.end();
//		}
	}

}
