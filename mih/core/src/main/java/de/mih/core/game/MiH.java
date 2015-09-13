package de.mih.core.game;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.CircularContextMenu;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.RenderManager;
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
import com.badlogic.gdx.math.Vector3;

public class MiH extends ApplicationAdapter implements InputProcessor {

	EntityManager entityM = EntityManager.getInstance();
	SystemManager systemM = SystemManager.getInstance();
	EventManager eventM = EventManager.getInstance();
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

	//public static AssetManager assetManager;

	public static Player activePlayer;
	int cam_target = -1;

	Map<Tile, Tile> path;
	private TilemapParser tilemapParser;
	private TilemapRenderer tilemapRenderer;
	private SpriteBatch spriteBatch;
	
	public boolean editMode;

	public void create() {
		activePlayer = new Player("localplayer", 0, entityM);
		
		assetManager = AdvancedAssetManager.getInstance();
		// Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		PerspectiveCamera camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		assetManager.assetManager.finishLoading();
		this.font = new BitmapFont(); 
		rs = new RenderSystem(systemM, entityM, eventM, camera);
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

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/wall.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/door.xml");

		
		RenderManager.getInstance().setCamera(camera);
		contextMenu = new CircularContextMenu(50, assetManager.assetManager.get("assets/textures/contextmenu_bg.png",Texture.class));
		

		contextMenu.getButton(0).addClickListener(() -> {
			PositionC pos = entityM.getComponent(activePlayer.selectedunits.get(0), PositionC.class);
			//TilemapC tilemap = entityM.getComponent(map, TilemapC.class);
			Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);

			Tile start = tilemap.getTileAt(tilemap.coordToIndex_x((int)pos.position.x), tilemap.coordToIndex_z((int)pos.position.z));
			Tile end = tilemap.getTileAt(tilemap.coordToIndex_x(mouseTarget.x),tilemap.coordToIndex_z(mouseTarget.z));
			MoveOrder order = new MoveOrder(RenderManager.getInstance().getMouseTarget(0f, Gdx.input),
					start,
					end,
					pf.findShortesPath(start, end),
					tilemap);
			
			entityM.getComponent(activePlayer.selectedunits.get(0), OrderableC.class).newOrder(order);
		});
		contextMenu.getButton(1).addClickListener(() -> System.out.println("Button 2 pressed!"));
		contextMenu.getButton(2).addClickListener(() -> System.out.println("Button 3 pressed!"));
		contextMenu.getButton(3).addClickListener(() -> System.out.println("Button 4 pressed!"));
		contextMenu.getButton(4).addClickListener(() -> System.out.println("Button 5 pressed!"));
		contextMenu.getButton(5).addClickListener(() -> System.out.println("Button 6 pressed!"));

		inGameInput = new InGameInput(activePlayer, contextMenu, entityM, rs.camera);
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
		
		ms = new MoveSystem(tilemap);

	
		PositionC tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 2;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 2;


		this.spriteBatch = new SpriteBatch();
	}

	Map<Tile,Integer> pathToEntity = new HashMap<>();
	private Tile start;
	private Tile end = null;
	
	
	public void render() {
		
		// TODO: Delete! (Pathfinder-Test)
//		for (int i = 0; i < entityM.entityCount; i++) {
//			if (entityM.hasComponent(i, NodeC.class)) {
//				if (!entityM.getComponent(i, NodeC.class).blocked && entityM.hasComponent(i, VisualC.class)) {
//					entityM.removeComponent(i, entityM.getComponent(i, VisualC.class));
//				}
//			}
//		}
		systemM.update(Gdx.graphics.getDeltaTime());
		
		RenderManager.getInstance().startRender();
		tilemapRenderer.render();
		systemM.render(Gdx.graphics.getDeltaTime());
		RenderManager.getInstance().endRender();
		
	//	this.contextMenu.update();
		this.spriteBatch.begin();
		this.contextMenu.render(spriteBatch);
		
		if(this.editMode)
		{
			font.draw(this.spriteBatch, "EDIT MODE", 10, Gdx.graphics.getHeight() - 10);
			font.draw(this.spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight() - 26);
			font.draw(this.spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight() - 42);
		}
		
		this.spriteBatch.end();

		//test
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
