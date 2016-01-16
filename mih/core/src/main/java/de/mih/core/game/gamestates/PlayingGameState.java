package de.mih.core.game.gamestates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
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
import de.mih.core.engine.physic.Geometry;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.render.visualisation.Heatmap;
import de.mih.core.engine.render.visualisation.MarchingSquares;
import de.mih.core.engine.render.visualisation.MarchingSquares.Cell;
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
		FileHandle logFile = Gdx.files.local("log.txt");
		String read = logFile.readString();
		String[] lines = read.split("\n");
		for(String line : lines)
		{
			if(line.startsWith("de.mih.core.engine.ecs.events.BaseEvent$LocalEvent"))
			{
				String[] splitted = line.split(" ");
				String position = splitted[3] + " " + splitted[4] + " " + splitted[5];
				System.out.println(position);
//				System.out.print(line.split(" ")[3] + " ");
//				System.out.print(line.split(" ")[4] + " ");
//				System.out.println(line.split(" ")[5]);
				

			}
		}
		//System.out.println("first line: " +read.split("\n")[0]);
		
		game = new Game();
		game.init("assets/maps/map1.xml");
		font = new BitmapFont();
		

	}

	// TODO: reorganize!
	@Override
	public void update()
	{
		game.getSystemManager().update(Gdx.graphics.getDeltaTime());
		game.update();
	}
	Heatmap heatmap = new Heatmap(0,0);
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
	
	void debug ()
	{
		heatmap.render();

		if (true) // DEBUG
		{
			ShapeRenderer sr = new ShapeRenderer();
			 OrthographicCamera camera = new OrthographicCamera(60,100);
			//PerspectiveCamera camera = game.getRenderManager().getCamera();
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
				Vector3 position = game.getEntityManager().getComponent(game.guard, PositionC.class).getPos();
				sr.circle(position.x, position.z, 0.5f);
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
			
			
			int x = heatmap.events.length -1;
			int y = heatmap.events[0].length -1;
			if(cells == null)
			{
			 cells = new Cell[x][y];
			for(int i = 0; i < x; i++)
			{
				for(int j = 0; j < y; j++)
				{
					Cell current = ms.newCell();
					current.isoLT = heatmap.events[i][j];
					current.isoRT = heatmap.events[i+1][j];
					current.isoLB = heatmap.events[i][j+1];
					current.isoRB = heatmap.events[i+1][j+1];
					current.lt = new Vector3(i * 0.5f , 0, j * 0.5f);
					current.rt = new Vector3((i+1) *0.5f, 0, j * 0.5f) ;
					current.lb = new Vector3(i * 0.5f, 0, (j+1) *0.5f);
					current.rb = new Vector3((i+1) *0.5f, 0,  (j+1) *0.5f);
					cells[i][j] = current;
				}
			}
			ms.cells = cells;
			ms.calculateIsoline(3.5f);
			ms.calculateIsoline(2.5f);
			ms.calculateIsoline(1.5f);
			ms.calculateIsoline(0.5f);

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
