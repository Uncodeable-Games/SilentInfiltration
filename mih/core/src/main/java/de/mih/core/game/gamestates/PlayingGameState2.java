package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.render.visualisation.Heatmap;
import de.mih.core.engine.render.visualisation.MarchingSquares;
import de.mih.core.engine.render.visualisation.MarchingSquares.Cell;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

public class PlayingGameState2 extends GameState
{

	public PlayingGameState2(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	public Game game;
	BitmapFont font;

	@Override
	public void onEnter()
	{
		FileHandle logFile = Gdx.files.local("level2_log.txt");
//		heatmap = new Heatmap(120, 80);
//		if(logFile.exists())
//		{
//			String read = logFile.readString();
//			String[] lines = read.split("\n");
//			for (String line : lines)
//			{
//				if (line.startsWith("de.mih.core.engine.ecs.events.BaseEvent$LocalEvent"))
//				{
//					String[] splitted = line.split(" ");
//					if(!splitted[2].equals("PLAYER_DETECTED"))
//					{
//						System.out.println(splitted[2]);
//						continue;
//					}
//					String position = splitted[3] + " " + splitted[4] + " " + splitted[5];
//					System.out.println(position);
//					String[] floats = position.substring(1, position.length() - 1).split(",");
//
//					float x = Float.parseFloat(floats[0]);
//					float z = Float.parseFloat(floats[2]);
//					x *= 2;
//					z *= 2;
//					heatmap.events[(int) x][(int) z]++;
//
//				}
//			}
//		}

		game = new Game("level2");
		game.init("assets/maps/map2.xml");
		font = new BitmapFont();

	}

	// TODO: reorganize!
	@Override
	public void update(double deltaTime)
	{
		game.update(deltaTime);
	}

	Heatmap heatmap;// = new Heatmap(0,0);

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

	MarchingSquares ms = new MarchingSquares();
	Cell[][] cells = null;

	void debug()
	{
//		if (heatmap != null)
//			heatmap.render();

		if (true) // DEBUG
		{
			ShapeRenderer sr = new ShapeRenderer();
			OrthographicCamera camera = new OrthographicCamera(100,80);
			// PerspectiveCamera camera = game.getRenderManager().getCamera();
			sr.setProjectionMatrix(camera.combined);

			sr.begin(ShapeType.Line);
			sr.setColor(Color.WHITE);

			if (game.getEntityManager().hasComponent(game.robo, PositionC.class))
			{
				Vector3 position = game.getEntityManager().getComponent(game.robo, PositionC.class).getPos();
				sr.circle(position.x, position.z, 0.5f);
			}
			sr.setColor(Color.RED);

			if (game.getEntityManager().hasComponent(game.guard, PositionC.class))
			{
				PositionC posc = game.getEntityManager().getComponent(game.guard, PositionC.class);
				Vector3 position = posc.getPos();
				Vector3 facing = position.cpy().add(posc.facing.cpy().scl(5));//.scl(10);
				sr.circle(position.x, position.z, 0.5f);
				sr.line(position.x, position.z , facing.x, facing.z);
//				System.out.println(posc.facing.cpy());

			}
			if (game.getEntityManager().hasComponent(game.guard2, PositionC.class))
			{
				PositionC posc = game.getEntityManager().getComponent(game.guard2, PositionC.class);
				Vector3 position = posc.getPos();
				Vector3 facing = position.cpy().add(posc.facing.cpy().scl(5));//.scl(10);
				sr.circle(position.x, position.z, 0.5f);
				sr.line(position.x, position.z , facing.x, facing.z);
//				System.out.println(posc.facing.cpy());

			}
			if (game.sight != null)
			{
				sr.line(game.sight.from.x, game.sight.from.y, game.sight.to.x, game.sight.to.y);

			}
			sr.setColor(Color.YELLOW);

			Tilemap map = game.getTilemap();
			for (Line line : map.colLines)
			{
				sr.line(line.from, line.to);
			}
			sr.end();

			int x = heatmap.events.length - 1;
			int y = heatmap.events[0].length - 1;
			if (cells == null)
			{
				cells = new Cell[x][y];
				for (int i = 0; i < x; i++)
				{
					for (int j = 0; j < y; j++)
					{
						Cell current = ms.newCell();
						current.isoLT = heatmap.events[i][j];
						current.isoRT = heatmap.events[i + 1][j];
						current.isoLB = heatmap.events[i][j + 1];
						current.isoRB = heatmap.events[i + 1][j + 1];
						current.lt = new Vector3(i * 0.5f, 0, j * 0.5f);
						current.rt = new Vector3((i + 1) * 0.5f, 0, j * 0.5f);
						current.lb = new Vector3(i * 0.5f, 0, (j + 1) * 0.5f);
						current.rb = new Vector3((i + 1) * 0.5f, 0, (j + 1) * 0.5f);
						cells[i][j] = current;
					}
				}
				float step = heatmap.max_events / 5.0f;
				ms.cells = cells;

				for (int i = 0; i < heatmap.max_events; i += step)
				{
					ms.calculateIsoline(i);
				}
			}
			ms.sr = sr;

			sr.setProjectionMatrix(game.getRenderManager().getCamera().combined);

			sr.begin(ShapeType.Line);

			sr.setColor(Color.WHITE);

			ms.render();
			sr.end();
		}
	}
}
