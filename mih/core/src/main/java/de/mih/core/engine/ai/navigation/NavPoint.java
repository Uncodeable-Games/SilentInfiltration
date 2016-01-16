package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.Position;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
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
		if (room != null) {
			room.removeNavPoint(this);
		}
		room = r;
		room.addNavPoint(this);
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
			if (border.hasColliderEntity() && !r.allDoors.contains(border)) {
				if (r.allDoors.contains(border)) {
					if (!entityManager.getComponent(border.getColliderEntity(), BorderC.class).isclosed) {
						allcolliders.put(entityManager.getComponent(border.getColliderEntity(), ColliderC.class),
								border.getColliderEntity());
					}
				} else {
					allcolliders.put(entityManager.getComponent(border.getColliderEntity(), ColliderC.class),
							border.getColliderEntity());
				}
			}
		}

		for (NavPoint nav : r.allNavPoints) {
			if (nav == this) {
				continue;
			}
			intersects = false;
			for (ColliderC col : allcolliders.keySet()) {
				if (LineIntersectsCollider(pos, nav.pos, col,
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

	static Vector2 r1 = new Vector2();
	static Vector2 r2 = new Vector2();
	static Vector2 r3 = new Vector2();
	static Vector2 r4 = new Vector2();

	static Vector2 rp1 = new Vector2(), rp2 = new Vector2(), rpos = new Vector2(0, 0);

	public static boolean LineIntersectsCollider(Vector2 p1, Vector2 p2, ColliderC col, PositionC pos) {
		rpos.set(pos.getX(), pos.getZ());
		rp1.set(p1).sub(rpos).rotate(pos.getAngle());
		rp2.set(p2).sub(rpos).rotate(pos.getAngle());
		r1.set(-(col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				-(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r2.set(-(col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r3.set((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r4.set((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				-(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);

		return LineIntersectsLine(rp1, rp2, r1, r2) || LineIntersectsLine(rp1, rp2, r1, r4)
				|| LineIntersectsLine(rp1, rp2, r3, r2) || LineIntersectsLine(rp1, rp2, r3, r4)
				|| (rectContainsPoint(rp1, r1, r3) && rectContainsPoint(rp2, r1, r3));
	}

	private static boolean rectContainsPoint(Vector2 point, Vector2 min, Vector2 max) {
		return (min.x <= point.x && point.x <= max.x && min.y <= point.y && point.y <= max.y);
	}

	private static boolean LineIntersectsLine(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2) {
		float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
		float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);

		if (d == 0) {
			return false;
		}

		float r = q / d;

		q = (l1p1.y - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
		float s = q / d;

		if (r < 0 || r > 1 || s < 0 || s > 1) {
			return false;
		}

		return true;
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
