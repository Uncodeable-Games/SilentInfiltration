package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Node;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Door;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Wall;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;

public class NavPoint implements Node {

	public static class Tuple {
		public NavPoint nav;
		public float dist;

		public Tuple(NavPoint n, float d) {
			nav = n;
			dist = d;
		}
	}

	private Vector2 pos = new Vector2();
	private Room room;

	private HashMap<NavPoint, Float> visibleNavPoints = new HashMap<NavPoint, Float>();
	private HashMap<NavPoint, Tuple> router = new HashMap<NavPoint, Tuple>();

	private EntityManager entityManager;

	public NavPoint(float x, float y) {
		pos.x = x;
		pos.y = y;
		this.entityManager = Game.getCurrentGame().getEntityManager();
		int nav = Game.getCurrentGame().getCurrentGame().getBlueprintManager().createEntityFromBlueprint("nav");
		this.entityManager.getComponent(nav, PositionC.class).setPos(x, 0, y);
		this.entityManager.getComponent(nav, VisualC.class).setScale(0.5f, 0.5f, 0.5f);
		room = Game.getCurrentGame().getTilemap().getRoomAt(x, y);
	}

	public Vector2 getPos() {
		return pos;
	}

	public boolean isVisibleBy(NavPoint nav) {
		return nav.visibleNavPoints.containsKey(this);
	}

	public boolean isReachableBy(NavPoint nav) {
		return nav.router.keySet().contains(this);
	}

	public NavPoint getNextNavPoint(NavPoint target) {
		return router.get(target).nav;
	}

	public ArrayList<NavPoint> getVisibleNavPoints() {
		return new ArrayList<NavPoint>(visibleNavPoints.keySet());
	}

	public ArrayList<NavPoint> getReachableNavPoints() {
		return new ArrayList<NavPoint>(router.keySet());
	}

	public void flushRouter() {
		router.clear();
	}

	public void addVisibleNavPoint(NavPoint nav, float dist) {
		visibleNavPoints.put(nav, dist);
	}

	public void addVisibleNavPoint(NavPoint nav) {
		visibleNavPoints.put(nav, this.getPos().dst(nav.getPos()));
	}

	public void addToRouter(NavPoint nav, Tuple tuple) {
		router.put(nav, tuple);
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

	public void calculateVisibility() {
		Room r = this.getRoom();
		allcolliders.clear();
		visibleNavPoints.clear();
		for (Integer i : r.entitiesInRoom) {
			if (entityManager.hasComponent(i, ColliderC.class) && !entityManager.hasComponent(i, VelocityC.class)) {
				allcolliders.put(entityManager.getComponent(i, ColliderC.class), i);
			}
		}
		for (Wall border : r.allWalls) {
			allcolliders.put(entityManager.getComponent(border.getColliderEntity(), ColliderC.class),
					border.getColliderEntity());
		}

		for (Door door : r.allDoors) {
			if (r.allDoors.contains(door)) {
				if (entityManager.getComponent(door.getColliderEntity(), BorderC.class).isclosed) {
					allcolliders.put(entityManager.getComponent(door.getColliderEntity(), ColliderC.class),
							door.getColliderEntity());
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
				visibleNavPoints.put(nav, pos.dst(nav.pos));
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

	@Override
	public float getDistance(Node target) {
		if (target instanceof Door) {
			return getDistanceToNavPoint(
					Game.getCurrentGame().getNavigationManager().getDoorNavPointByRoom((Door) target, this.getRoom()));
		} else if (target instanceof NavPoint) {
			return getDistanceToNavPoint((NavPoint) target);
		}
		return Float.MAX_VALUE;
	}

	public float getDistanceToNavPoint(NavPoint nav) {
		if (this == nav)
			return 0;
		if (nav.isVisibleBy(this))
			return visibleNavPoints.get(nav);
		if (nav.isReachableBy(this))
			return router.get(nav).dist;
		System.out.println(nav + "is not reachable by " + this);
		return Float.MAX_VALUE;
	}

	@Override
	public ArrayList<Node> getNeighbours(NavPoint last) {
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (Door door : getRoom().allDoors){
			neighbours.add(door);
		}
		return neighbours;
	}
}
