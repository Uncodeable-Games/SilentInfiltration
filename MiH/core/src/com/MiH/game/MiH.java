package com.MiH.game;

import com.MiH.engine.ecs.EntityManager;
import com.MiH.engine.ecs.EventManager;
import com.MiH.engine.ecs.SystemManager;
import com.MiH.engine.exceptions.ComponentNotFoundEx;
import com.MiH.engine.io.TilemapReader;
import com.MiH.engine.tilemap.TileMapRenderer;
import com.MiH.engine.tilemap.Tilemap;
import com.MiH.game.components.Control;
import com.MiH.game.components.PositionC;
import com.MiH.game.components.VelocityC;
import com.MiH.game.components.Visual;
import com.MiH.game.systems.ControllerSystem;
import com.MiH.game.systems.MoveSystem;
import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class MiH extends ApplicationAdapter {
	
	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static RenderSystem rs;
	static ControllerSystem cs;
	static MoveSystem ms;
	
	static TilemapReader tr;
	static Tilemap map;
	static TileMapRenderer trd = new TileMapRenderer();
	
	int cam_target = -1;
	
	public void create() {
		entityM = new EntityManager();
		systemM = new SystemManager(entityM, 5);
		eventM = new EventManager();
		
		rs = new RenderSystem(systemM,entityM,eventM,new PerspectiveCamera(75,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		cs = new ControllerSystem(systemM, entityM, eventM, rs);
		ms = new MoveSystem(systemM, entityM, eventM);
		
		tr = new TilemapReader(rs);
		
		map = tr.readMap("map1.xml");
		trd.tilemap = map;
		
		cam_target = entityM.createEntity();
		entityM.addComponent(cam_target, new PositionC(new Vector3(0f,0f,0f)), new VelocityC(new Vector3()), new Control());
		
		int hero = entityM.createEntity();
		entityM.addComponent(hero, new Visual(rs.box, rs), new PositionC(new Vector3(0f, 0f, 0f)), new VelocityC(new Vector3()), new Control());
		try {
			entityM.getComponent(hero, VelocityC.class).drag = 0.5f;
			entityM.getComponent(hero, VelocityC.class).maxspeed = 5f;
			entityM.getComponent(hero, Control.class).withwasd = true;
			
		} catch (ComponentNotFoundEx e) {e.printStackTrace();}
		
	}

	public void render () {	
		try {
			systemM.update(Gdx.graphics.getDeltaTime());
			systemM.render(Gdx.graphics.getDeltaTime());
			trd.render();
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}	
	}
}
