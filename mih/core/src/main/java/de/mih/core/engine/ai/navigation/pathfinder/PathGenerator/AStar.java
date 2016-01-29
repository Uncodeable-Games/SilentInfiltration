package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator;

import java.util.ArrayList;
import java.util.Collections;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths.BasePath;

/**
 * A generic implementation of the A*-Algorithm.
 * 
 * @author Cataract
 *
 * @param <T>
 *            A path-type the A*-Algorithm should create.
 * @param <K>
 *            The Node-type the path uses.
 */
public class AStar<T extends BasePath<K>, K> {

	class Node {
		private K item;
		private Node prev;
		private float value;

		public Node(K item, Node prev, float value) {
			this.item = item;
			this.prev = prev;
			this.value = value;
		}
	}

	final Class<T> CLASS;

	/**
	 * The class of the path-type needs to be saved because you can't create
	 * instances of generic types
	 * 
	 * @param type
	 *            The class of the path-type.
	 */
	public AStar(Class<T> type) {
		CLASS = type;
	}

	ArrayList<Node> openlist = new ArrayList<Node>();
	ArrayList<Node> closedlist = new ArrayList<Node>();;

	
	/**
	 * Generates a T-typed path with from 'first' to 'last' using the A*-Algorithm.
	 * @param first
	 * @param last
	 * @return
	 */
	public T generatePath(K first, K last) {
		openlist.clear();
		closedlist.clear();
		T path = newInstance(CLASS);

		Node start = new Node(first, null, path.getPos(first).dst(path.getPos(last)));
		openlist.add(start);

		Node current = null;
		while (!openlist.isEmpty()) {
			current = getMin(openlist);
			if (current.item == last) {
				break;
			}
			openlist.remove(current);
			closedlist.add(current);

			for (K item : path.getNeighbours(current.item)) {
				if (!contains(closedlist, item) && !contains(openlist, item) && current.item != item) {
					openlist.add(new Node(item, current, current.value + path.getDistance(current.item, item)
							+ path.getPos(first).dst(path.getPos(last))));
				}
			}
		}
		if (current.item != last) {
			return (T) path.getNoPath();
		}

		path.distance = current.value;

		while (current != start) {
			path.add(current.item);
			current = current.prev;
		}
		path.add(start.item);
		Collections.reverse(path);
		return path;
	}

	private Node getMin(ArrayList<Node> list) {
		Node min = list.get(0);
		for (Node node : list) {
			if (node.value < min.value) {
				min = node;
			}
		}
		return min;
	}

	private boolean contains(ArrayList<Node> list, K item) {
		for (Node n : list) {
			if (n.item == item)
				return true;
		}
		return false;
	}

	private T newInstance(Class<T> type) {
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
