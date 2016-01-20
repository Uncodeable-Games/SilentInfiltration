package de.mih.core.engine.ai.navigation;

import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;

public class NavPoint {

	public static class Tuple {
		public NavPoint nav;
		public float dist;

		public Tuple(NavPoint n, float d) {
			nav = n;
			dist = d;
		}
	}

	public Vector2 pos = new Vector2();
	public Room room;

	public HashMap<NavPoint, Float> visibleNavPoints = new HashMap<NavPoint, Float>();
	public HashMap<NavPoint, Tuple> router = new HashMap<NavPoint, Tuple>();
	public int vis;

	private EntityManager entityManager;

	public NavPoint() {
		this(0, 0);
	}

	public NavPoint(float x, float y) {
		pos.x = x;
		pos.y = y;
		this.entityManager = Game.getCurrentGame().getEntityManager();
		vis = Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("nav");
		this.entityManager.getComponent(vis, PositionC.class).setPos(x, 0, y);
		this.entityManager.getComponent(vis, VisualC.class).setScale(0.5f, 0.5f, 0.5f);
	}

	public void setRoom(Room r) {
		if (r == room)
			return;
		room = r;
	}

	public Room getRoom() {
		return room;
	}

	HashMap<ColliderC, Integer> allcolliders = new HashMap<ColliderC, Integer>();
	boolean intersects = false;

	public void calculateVisibility(Room r) {
		allcolliders.clear();
		visibleNavPoints.clear();
		for (Integer i : r.entitiesInRoom) {
			if (entityManager.hasComponent(i, ColliderC.class) && !entityManager.hasComponent(i, VelocityC.class)) {
				allcolliders.put(entityManager.getComponent(i, ColliderC.class), i);
			}
		}
		for (TileBorder border : r.allBorders) {
			if (border.hasColliderEntity()) {
				if (r.allDoors.contains(border)) {
					if (entityManager.getComponent(border.getColliderEntity(), BorderC.class).isclosed) {
						allcolliders.put(entityManager.getComponent(border.getColliderEntity(), ColliderC.class),
								border.getColliderEntity());
					}
				} else {
					allcolliders.put(entityManager.getComponent(border.getColliderEntity(), ColliderC.class),
							border.getColliderEntity());
				}
			}
		}

		for (NavPoint nav : Game.getCurrentGame().getNavigationManager().get(r)) {
			if (nav == this) {
				continue;
			}
			intersects = false;
			for (ColliderC col : allcolliders.keySet()) {
				if (NavigationManager.LineIntersectsCollider(pos, nav.pos, col,
						entityManager.getComponent(allcolliders.get(col), PositionC.class))) {
					intersects = true;
					break;
				}
			}
			if (!intersects) {
				visibleNavPoints.put(nav, (float) Math
						.sqrt(((pos.x - nav.pos.x) * (pos.x - nav.pos.x) + (pos.y - nav.pos.y) * (pos.y - nav.pos.y))));
			}
		}
	}

	public void route() {
		router.put(this, new Tuple(this, 0f));
		for (NavPoint nav : visibleNavPoints.keySet()) {
			Tuple t;
			if (!nav.router.containsKey(this)) {
				t = new Tuple(this, visibleNavPoints.get(nav));
				nav.router.put(this, t);
			}
			t = nav.router.get(this);
			if (t.dist > visibleNavPoints.get(nav)) {
				t.dist = visibleNavPoints.get(nav);
				t.nav = this;
			}
			nav.route(this);
		}

	}

	public void route(NavPoint start) {
		for (NavPoint nav : visibleNavPoints.keySet()) {
			if (nav == start)
				continue;
			Tuple t;
			if (!nav.router.containsKey(start)) {
				t = new Tuple(this, visibleNavPoints.get(nav) + router.get(start).dist);
				nav.router.put(start, t);
				nav.route(start);
				continue;
			}
			t = nav.router.get(start);
			if (t.dist > visibleNavPoints.get(nav) + router.get(start).dist) {
				t.dist = visibleNavPoints.get(nav) + router.get(start).dist;
				t.nav = this;
				nav.route(start);
				continue;
			}
		}
	}
}
