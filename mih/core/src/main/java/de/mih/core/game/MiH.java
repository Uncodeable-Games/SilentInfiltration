package de.mih.core.game;

import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.exceptions.ComponentNotFoundEx;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.Visual;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.RenderSystem;
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
	static Pathfinder pf;
	static TilemapReader tr;
	static int map;

	int cam_target = -1;

	Map<Integer, Integer> path;

	public void create() {
		entityM = new EntityManager();
		systemM = new SystemManager(entityM, 5);
		eventM = new EventManager();

		rs = new RenderSystem(systemM, entityM, eventM,
				new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		cs = new ControllerSystem(systemM, entityM, eventM, rs);
		ms = new MoveSystem(systemM, entityM, eventM);

		tr = new TilemapReader(rs, entityM);

		pf = new Pathfinder(entityM);
		
		map = tr.readMap("assets/map1.xml");
		
		// Robocop!!!111elf
		System.out.println("create");
		int hero = entityM.createEntity();
		entityM.addComponent(hero, new Visual(rs.robocop, rs), new PositionC(new Vector3(0f, 0f, 0f)),
				new VelocityC(new Vector3()), new Control());
		try {
			entityM.getComponent(hero, Visual.class).pos.y = -.5f;
			entityM.getComponent(hero, VelocityC.class).drag = 0.5f;
			entityM.getComponent(hero, VelocityC.class).maxspeed = 5f;
			entityM.getComponent(hero, Control.class).withwasd = true;
			entityM.getComponent(hero, Visual.class).angle = 90;
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
		//

		// Pathfinder test
		try {
			end = entityM.getComponent(map, TilemapC.class).getTileAt(1, 1);
		} catch (ComponentNotFoundEx e) {e.printStackTrace();}
	}

	
	//TODO: Delete! (Pathfinder-Test)
	int start = -1;
	int end = -1;

	public void render() {

		try {
			
			//TODO: Delete! (Pathfinder-Test)
			for (int i = 0; i < entityM.entityCount; i++) {
				if (entityM.hasComponent(i, NodeC.class)) {
					if (!entityM.getComponent(i, NodeC.class).blocked && entityM.hasComponent(i, Visual.class)) {
						entityM.removeComponent(i, entityM.getComponent(i, Visual.class));
					}
				}
			}
			int x = entityM.getComponent(map, TilemapC.class).cordToIndex_x(rs.getMouseTarget(0, Gdx.input).x);
			int z = entityM.getComponent(map, TilemapC.class).cordToIndex_z(rs.getMouseTarget(0, Gdx.input).z);
			try {
				start = entityM.getComponent(map, TilemapC.class).getTileAt(0, 0);
				if (x >= 0 && x < 10 && z >= 0 && z < 10) {
					if (!entityM.getComponent(entityM.getComponent(map, TilemapC.class).getTileAt(x, z), NodeC.class).blocked)
						end = entityM.getComponent(map, TilemapC.class).getTileAt(x, z);
				}
			} catch (ComponentNotFoundEx e1) {e1.printStackTrace();}

			path = pf.findShortesPath(start, end);
			int tmp = end;
			while (path.get(tmp) != null) {
				entityM.addComponent(tmp, new Visual(rs.redbox, rs));
				tmp = path.get(tmp);
			}
			entityM.addComponent(tmp, new Visual(rs.redbox, rs));
			//

			systemM.update(Gdx.graphics.getDeltaTime());
			systemM.render(Gdx.graphics.getDeltaTime());
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
	}
}
