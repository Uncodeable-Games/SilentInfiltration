package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.VelocityC;

public class NavPoint
{

	public static class Tuple
	{
		public NavPoint nav;
		public float dist;

		public Tuple(NavPoint n, float d)
		{
			nav = n;
			dist = d;
		}
	}

	public Vector2 pos = new Vector2();

	public HashMap<NavPoint, Float> visibleNavPoints = new HashMap<NavPoint, Float>();

	public HashMap<NavPoint, Tuple> router = new HashMap<NavPoint, Tuple>();

	private EntityManager entityManager;

	public NavPoint()
	{
		this(0, 0);
	}

	public NavPoint(float x, float y)
	{
		pos.x = x;
		pos.y = y;
		this.entityManager = Game.getCurrentGame().getEntityManager();
	}

	ArrayList<ColliderC> allcolliders = new ArrayList<ColliderC>();
	boolean intersects = false;

	public void calculateVisibility(Room r)
	{
		allcolliders.clear();
		visibleNavPoints.clear();
		for (Integer i : r.entitiesInRoom)
		{
			if (entityManager.hasComponent(i, ColliderC.class) && !entityManager.hasComponent(i, VelocityC.class))
			{
				allcolliders.add(entityManager.getComponent(i, ColliderC.class));
			}
		}

		for (ColliderC col1 : allcolliders)
		{
			for (NavPoint nav : col1.navpoints)
			{
				if (nav == this)
				{
					continue;
				}
				// System.out.print("checking:
				// "+nav+"("+nav.pos.x+","+nav.pos.y+")");
				intersects = false;
				for (ColliderC col2 : allcolliders)
				{
					if (LineIntersectsRect(pos, nav.pos, col2.getNavCollider()))
					{
						intersects = true;
						// System.out.println("intersecting with
						// "+col2.entityID);
						break;
					}
				}
				if (!intersects)
				{
					// System.out.println("visble!");
					visibleNavPoints.put(nav, (float) Math.sqrt(
							((pos.x - nav.pos.x) * (pos.x - nav.pos.x) + (pos.y - nav.pos.y) * (pos.y - nav.pos.y))));
				}
			}
		}
	}

	static Vector2 r1 = new Vector2();
	static Vector2 r2 = new Vector2();
	static Vector2 r3 = new Vector2();
	static Vector2 r4 = new Vector2();

	public static boolean LineIntersectsRect(Vector2 p1, Vector2 p2, Rectangle r)
	{
		r1.set(r.x, r.y);
		r2.set(r.x, r.y + r.height);
		r3.set(r.x + r.width, r.y + r.height);
		r4.set(r.x + r.width, r.y);

		return LineIntersectsLine(p1, p2, r1, r2) || LineIntersectsLine(p1, p2, r1, r4)
				|| LineIntersectsLine(p1, p2, r3, r2) || LineIntersectsLine(p1, p2, r3, r4)
				|| (r.contains(p1) && r.contains(p2));
	}

	private static boolean LineIntersectsLine(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2)
	{
		float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
		float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);

		if (d == 0)
		{
			return false;
		}

		float r = q / d;

		q = (l1p1.y - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
		float s = q / d;

		if (r < 0 || r > 1 || s < 0 || s > 1)
		{
			return false;
		}

		return true;
	}

	public void route()
	{
		router.put(this, new Tuple(this, 0f));
		for (NavPoint nav : visibleNavPoints.keySet())
		{
			Tuple t;
			if (!nav.router.containsKey(this))
			{
				t = new Tuple(this, visibleNavPoints.get(nav));
				nav.router.put(this, t);
			}
			t = nav.router.get(this);
			if (t.dist > visibleNavPoints.get(nav))
			{
				t.dist = visibleNavPoints.get(nav);
				t.nav = this;
			}
			nav.route(this);
		}

	}

	public void route(NavPoint start)
	{
		for (NavPoint nav : visibleNavPoints.keySet())
		{
			if (nav == start)
				continue;
			Tuple t;
			if (!nav.router.containsKey(start))
			{
				t = new Tuple(this, visibleNavPoints.get(nav) + router.get(start).dist);
				nav.router.put(start, t);
				nav.route(start);
				continue;
			}
			t = nav.router.get(start);
			if (t.dist > visibleNavPoints.get(nav) + router.get(start).dist)
			{
				t.dist = visibleNavPoints.get(nav) + router.get(start).dist;
				t.nav = this;
				nav.route(start);
				continue;
			}
		}
	}
}
