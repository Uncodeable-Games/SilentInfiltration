package de.mih.core.engine.physic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.game.components.ColliderC;

public class Geometry
{
	ArrayList<ColliderC> allcolliders = new ArrayList<ColliderC>();
	boolean intersects = false;

	public static List<Integer> getVisibleEntities(Vector2 position, List<ColliderC> colliders)
	{
		List<Integer> visibleEntities = new ArrayList<>();


		for (ColliderC collider : colliders)
		{
			for (NavPoint nav : collider.navpoints)
			{
				boolean intersects = false;
				for (ColliderC col2 : colliders)
				{
					if (LineIntersectsRect(position, nav.pos, col2.getCollider()))
					{
						intersects = true;
						break;
					}
				}
				if (!intersects)
				{
					visibleEntities.add(collider.entityID);
				}
			}
		}
		return visibleEntities;
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
	
	public static boolean isVisible(int from, int to)
	{
		
		return false;
	}
}
