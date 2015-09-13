package de.mih.core.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
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
import de.mih.core.game.player.Interaction;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;


public class MiH extends ApplicationAdapter implements InputProcessor {
	RenderSystem rs;
	ControllerSystem cs;
	OrderSystem os;
	MoveSystem ms;
	Pathfinder pf;
	TilemapParser tr;
	InputMultiplexer input;
	Tilemap tilemap;
	InGameInput inGameInput;
	CircularContextMenu contextMenu;
	AdvancedAssetManager assetManager;
	BitmapFont font;

	public Player activePlayer;
	int cam_target = -1;

	Map<Tile, Tile> path;
	private TilemapParser tilemapParser;
	private TilemapRenderer tilemapRenderer;

	private SpriteBatch spriteBatch;
	public boolean editMode;

	public void create() {

		activePlayer = new Player("localplayer", 0, EntityManager.getInstance());

		assetManager = AdvancedAssetManager.getInstance();
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.assetManager.load("assets/icons/sit.png", Texture.class);
//		assetManager.assetManager.load("assets/models/wall.obj", Model.class);
//		assetManager.assetManager.load("assets/models/door.obj", Model.class);
//		assetManager.assetManager.load("assets/models/selectioncircle.obj", Model.class);
//		assetManager.assetManager.load("assets/models/chair.obj", Model.class);

		PerspectiveCamera camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;

		assetManager.assetManager.finishLoading();
		this.font = new BitmapFont(); 
		rs = new RenderSystem(SystemManager.getInstance(), EntityManager.getInstance(), EventManager.getInstance(), camera);
		input = new InputMultiplexer();

		cs = new ControllerSystem(rs, Gdx.input);

		new PlayerSystem(rs);
		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);
		BlueprintManager.getInstance().registerComponentType(OrderableC.name,OrderableC.class);
		BlueprintManager.getInstance().registerComponentType(InteractableC.name, InteractableC.class);
		BlueprintManager.getInstance().registerComponentType(StatsC.name, StatsC.class);

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/objects/chair.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/wall.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/door.xml");

		RenderManager.getInstance().setCamera(camera);
		contextMenu = new CircularContextMenu();
		inGameInput = new InGameInput(activePlayer, contextMenu, EntityManager.getInstance(), camera);
		inGameInput.mih = this;

		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);
		input.addProcessor(this);

		Gdx.input.setInputProcessor(input);

		tilemapParser = new TilemapParser();

		pf = new Pathfinder();

		tilemap = tilemapParser.readMap("assets/maps/map1.xml");

		os = new OrderSystem(pf, tilemap);

		tilemapRenderer = new TilemapRenderer(tilemap);

		new CircularContextMenuRenderer(this.contextMenu);


		ms = new MoveSystem(tilemap);


		

		int chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.x = 2f;
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.z = 2f;

		EntityManager.getInstance().getComponent(chair, VisualC.class).setScale(0.5f, 0.5f, 0.5f);

		PositionC tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 2;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 2;
		
		System.out.println(EntityManager.getInstance().getComponent(tmp.entityID,StatsC.class).candisguise+" ; "+EntityManager.getInstance().getComponent(tmp.entityID,StatsC.class).canko+" ; "+EntityManager.getInstance().getComponent(tmp.entityID,StatsC.class).canclimbvent+" ; "+EntityManager.getInstance().getComponent(tmp.entityID,StatsC.class).walkspeed+" ; "+EntityManager.getInstance().getComponent(tmp.entityID,StatsC.class).runspeed);

	}

	Map<Tile,Integer> pathToEntity = new HashMap<>();
	private Tile start;
	private Tile end = null;

	public void render() {
		SystemManager.getInstance().update(Gdx.graphics.getDeltaTime());
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

	public void toggleEditMode() {
		this.editMode = !editMode;
		EntityManager.getInstance();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.F12)
		{
			this.toggleEditMode();
		}
		if(this.editMode)
		{
			if(keycode == Keys.W)
			{
				Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = this.tilemap.getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for(TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if(tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if(closest.hasColliderEntity())
				{
					closest.removeColliderEntity();
				}
				else
				{
					closest.setColliderEntity(BlueprintManager.getInstance().createEntityFromBlueprint("wall"));
				}
			}
			else if(keycode == Keys.D)
			{
				Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);
				List<TileBorder> borders = this.tilemap.getBorders();
				TileBorder closest = borders.get(0);
				float closestDist = closest.getCenter().dst(mouseTarget);
				for(TileBorder border : borders)
				{
					float tmp = mouseTarget.dst(border.getCenter());
					if(tmp < closestDist)
					{
						closestDist = tmp;
						closest = border;
					}
				}
				if(closest.hasColliderEntity())
				{
					closest.removeColliderEntity();
				}
				else
				{
					closest.setColliderEntity(BlueprintManager.getInstance().createEntityFromBlueprint("door"));
				}
			}
			else if (keycode == Keys.F11)
			{
				try {
					this.tilemapParser.writeTilemap(Gdx.files.internal("assets/maps/map1.xml").path(), this.tilemap);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
