package de.mih.core.engine.navigation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;

import de.mih.core.engine.ecs.RenderManager;

public class Pathgenerator
{

	static Pathgenerator instance;

	public static Pathgenerator getInstance()
	{
		if (instance == null)
		{
			instance = new Pathgenerator();
		}
		return instance;
	}
	// public class VisabilityGraph<N> implements Graph<N>
	// {
	//
	// @Override
	// public Array getConnections(N fromNode) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// }

	// Polygon roomBounds;
	// List<Shape2D> colliders;
	List<Polygon> polygons;
	BalancedBinaryTree<Edge> T;

	private class DRay
	{
		public Vector2 origin, direction;

		public DRay(Vector2 origin, Vector2 direction)
		{
			this.origin = origin;
			this.direction = direction;
		}
	}

	int edgeCount = 0;

	public VisabilityGraph generateVisabilityGraph(List<PolygonGraph> polygons)
	{
		VisabilityGraph vG = new VisabilityGraph();
		int connections = 0;
		for (PolygonGraph polygon : polygons)
		{
			for (Vertex v : polygon.vertices)
			{
				List<Vertex> visible = getVisibleVertizes(polygons, v.position);
				for (Vertex w : visible)
				{
					if (w == v)
						continue;
					connections++;
					vG.addConnection(v, w);
				}
			}
		}
		return vG;
	}

	public List<Vertex> getVisibleVertizes(List<PolygonGraph> polygons, Vector2 point)
	{
		edgeCount = 0;
		DRay ray = new DRay(point, new Vector2(1, 0));
		PriorityQueue<Vertex> orderedVertices = new PriorityQueue<>(new Comparator<Vertex>() {

			@Override
			public int compare(Vertex o1, Vertex o2)
			{
				return Intersector.pointLineSide(point, o2.position, o1.position);
//				if(o1.position.y > o2.position.y)
//				{
//					Vertex tmp = o1;
//					o1 = o2;
//					o2 = tmp;
//				}
//				boolean isless = isLess(point, o1.position, o2.position);
//				if (o1.position.equals(o2.position))
//					return 0;
//				return isless ? -1 : 1;
			}
		});
		// Vertex last = null;
		for (PolygonGraph polygon : polygons)
		{
			for (Vertex v : polygon.vertices)
			{
				// if (last != null)
				// {
				// v.prior = last;
				// }
				// last = v;
				orderedVertices.add(v);
			}
		}

		// orderedVertices.

		STEP2(polygons, point, ray, orderedVertices);
		T.printL();
		return STEP3(point, orderedVertices);
	}

	public void STEP2(List<PolygonGraph> polygons, Vector2 point, DRay ray, PriorityQueue<Vertex> orderedVertices)
	{
		T = new BalancedBinaryTree<>();
		BalancedBinaryTree<Edge> tree = new BalancedBinaryTree<>();

		for (PolygonGraph polygon : polygons)
		{
			for (Edge edge : polygon.edges)
			{
				float distToPoint = Intersector.distanceSegmentPoint(edge.from.position, edge.to.position, point);
				Vector2 tmp = new Vector2(ray.direction).scl(1000);
				tmp.y = ray.origin.y;
				tmp.x += ray.origin.x;
				RenderManager.getInstance().shapeRenderer.begin(ShapeType.Line);
				RenderManager.getInstance().shapeRenderer.line(ray.origin, tmp);
				RenderManager.getInstance().shapeRenderer.end();

				Vector2 intersector = new Vector2();
				if (Intersector.intersectLines(ray.origin, tmp, edge.from.position, edge.to.position, intersector) )//&& !intersector.equals(point))
				{
					tree.insert(distToPoint, edge);
				}
			}
		}
		tree.balanceTree();
		//tree.printL();
		while (tree.getMin() != null)
		{
			Node<Edge> tmp = tree.getMin();
			T.insert(++edgeCount, tmp.getValue());
			T.searchNode(edgeCount).getValue().key = edgeCount;
			tree.remove(tmp);
			// tree.balanceTree();
			// tree.printL();
		}
		T.balanceTree();
		//T.printL();
	}

	public List<Vertex> STEP3(Vector2 point, PriorityQueue<Vertex> ordererdVertices)
	{
		List<Vertex> visible = new ArrayList<>();
		// int wc = 0;
		Vertex prior = null;
		while (!ordererdVertices.isEmpty())
		{
			Vertex current = ordererdVertices.poll();
			current.prior = prior;
			prior = current;

			// wc++;
			if (isVisible(point, current))
			{
				visible.add(current);
				List<Edge> incidentEdges = current.polygon.getIncidentEdges(current);
				for (Edge e : incidentEdges)
				{
					if (e.from == current)
					{
						if (isLess(point, e.from.position, current.position))
						{
							T.remove(e.key);
							T.balanceTree();
						}
						else
						{
							// float distToPoint =
							// Intersector.distanceSegmentPoint(e.from.position,
							// e.to.position, point);
							edgeCount++;
							T.insert(edgeCount, e);
							T.balanceTree();
							T.searchNode(edgeCount).getValue().key = edgeCount;
							// T.insert(distToPoint,e);
						}
					}
					else if (e.to == current)
					{
						if (isLess(point, e.to.position, current.position))
						{
							T.remove(e.key);
							T.balanceTree();
						}
						else
						{
							// float distToPoint =
							// Intersector.distanceSegmentPoint(e.from.position,
							// e.to.position, point);
							// T.insert(distToPoint,e);
							T.insert(++edgeCount, e);
							T.searchNode(edgeCount).getValue().key = edgeCount;
							T.balanceTree();
						}
					}
				}
			}

		}
		return visible;
	}

	public boolean isVisible(Vector2 point, Vertex w)
	{
//		if (w.polygon.intersectsLine(point, w)) //Intersector.intersectLinePolygon(point, w.position, w.polygon.polygon))
//		{
//			System.out.println(point + " -> " + w.position + " cuts polygon of w!");
//			return false;
//		}
		if (w.prior == null || Intersector.pointLineSide(point, w.position, w.prior.position) != 0)
		{
			if(w.prior != null)
				System.out.println("on line: " + point + ", " + w.position + ": " + w.prior.position);
			if (T.getMostLeft() == null)
				return true;
			Edge e = T.getMostLeft().getValue();
			Vector2 intersector = new Vector2();
			if (e != null && Intersector.intersectLines(point, w.position, e.from.position, e.to.position, intersector)  
					&& !intersector.equals(point) && !intersector.equals(w.position) && !intersector.equals(e.from.position) && !intersector.equals( e.to.position))
				return false;
			else
				return true;

		}
		else
		{
			if (!isVisible(point, w.prior))
			{
				return false;
			}
			else
			{
				for (int i = 0; i < this.edgeCount; i++)
				{
					Node<Edge> n = T.searchNode(i);
					if(n == null)
						continue;
					Edge tmp = n.getValue();
					Vector2 intersector = new Vector2();
					if (Intersector.intersectLines(tmp.from.position, tmp.to.position, w.position, w.prior.position,
							intersector) && !intersector.equals(point) && !intersector.equals(w.position))
					{
						return false;
					}
				}
				return true;
			}
		}
	}

	public boolean isLess(Vector2 center, Vector2 a, Vector2 b)
	{
		if (a.x - center.x >= 0 && b.x - center.x < 0)
			return true;
		if (a.x - center.x < 0 && b.x - center.x >= 0)
			return false;
		if (a.x - center.x == 0 && b.x - center.x == 0)
		{
			if (a.y - center.y >= 0 || b.y - center.y >= 0)
				return a.y > b.y;
			return b.y > a.y;
		}

		// compute the cross product of vectors (center -> a) x (center -> b)
		float det = (a.x - center.x) * (b.y - center.y) - (b.x - center.x) * (a.y - center.y);
		if (det < 0)
			return true;
		if (det > 0)
			return false;

		// points a and b are on the same line from the center
		// check which point is closer to the center
		float d1 = (a.x - center.x) * (a.x - center.x) + (a.y - center.y) * (a.y - center.y);
		float d2 = (b.x - center.x) * (b.x - center.x) + (b.y - center.y) * (b.y - center.y);
		return d1 > d2;
	}

}
