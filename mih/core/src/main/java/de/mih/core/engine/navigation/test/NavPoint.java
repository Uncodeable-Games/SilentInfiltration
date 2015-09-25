package de.mih.core.engine.navigation.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.css.Rect;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.VelocityC;

public class NavPoint {

	public Vector2 pos = new Vector2();

	public HashMap<NavPoint, Float> visibleNavPoints = new HashMap<NavPoint, Float>();

	public NavPoint() {
	}

	public NavPoint(float x, float y) {
		pos.x = x;
		pos.y = y;
	}

	public NavPoint(int x, int y) {
		pos.x = x;
		pos.y = y;
	}

	ArrayList<ColliderC> allcolliders = new ArrayList<ColliderC>();
	Vector2 tmpvec = new Vector2();

	boolean intersects = false;

	ArrayList<ColliderC> tmplist = new ArrayList<ColliderC>();
	public void calculateVisibility(Room r) {
		allcolliders.clear();
		visibleNavPoints.clear();
		System.out.println(r);
		for (Integer i : r.entitiesInRoom) {
			if (EntityManager.getInstance().hasComponent(i, ColliderC.class)
					&& !EntityManager.getInstance().hasComponent(i, VelocityC.class)) {
				allcolliders.add(EntityManager.getInstance().getComponent(i, ColliderC.class));
			}
		}

		System.out.println("\n"+this+"("+pos.x+","+pos.y+") with "+allcolliders.size()+" of "+r.entitiesInRoom+" colliders: ");
		for (ColliderC col1 : allcolliders) {
			for (NavPoint nav : col1.navpoints) {
				System.out.print("Checking "+nav+": ");
				if (nav == this){
					System.out.println("same point");
					continue;
				}
				intersects = false;
				for (ColliderC col2 : allcolliders) {
					if (LineIntersectsRect(pos, nav.pos, col2.getNavCollider())) {
						System.out.println("intersecting with "+col2.entityID);
						intersects = true;
						break;
					}
				}
				if (!intersects) {
					visibleNavPoints.put(nav, (float) (pos.x-nav.pos.x)*(pos.x-nav.pos.x)+(pos.y-nav.pos.y)*(pos.y-nav.pos.y));
					System.out.println("not intersecting");
				}
			}
		}
		System.out.println("Navpoints: "+visibleNavPoints);
	}

	public static boolean LineIntersectsRect(Vector2 p1, Vector2 p2, Rectangle r) {
		return LineIntersectsLine(p1, p2, new Vector2(r.x, r.y), new Vector2(r.x, r.y + r.height))
				|| LineIntersectsLine(p1, p2, new Vector2(r.x, r.y), new Vector2(r.x + r.width, r.y))
				|| LineIntersectsLine(p1, p2, new Vector2(r.x + r.width, r.y + r.height),
						new Vector2(r.x, r.y + r.height))
				|| LineIntersectsLine(p1, p2, new Vector2(r.x + r.width, r.y + r.height),
						new Vector2(r.x + r.width, r.y))
				|| (r.contains(p1) && r.contains(p2));
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
}
