package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.pathfinder.Path;


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
public class AStar {

	ArrayList<Node> openlist = new ArrayList<Node>();
	ArrayList<Node> closedlist = new ArrayList<Node>();

	
	/**
	 * Generates a T-typed path with from 'first' to 'last' using the A*-Algorithm.
	 * @param first
	 * @param last
	 * @return
	 */
	public ArrayList<Node> generatePath(Node first, Node last) {
		openlist.clear();
		closedlist.clear();
		Node.prev.clear();
		Node.value.clear();
		
		first.setValue(0f);
		openlist.add(first);

		Node current = null;
		while (!openlist.isEmpty()) {
			current = getMin(openlist,last);
			if (current == last) break;
			openlist.remove(current);
			closedlist.add(current);
			expandNode(current,last, openlist,closedlist);
		}
		
		ArrayList<Node> path = new ArrayList<Node>();
		
//		if (current != last) {
//			System.out.println("current != last");
//			return path;
//		}
		//path.add(last);
		
		while (current != first) {
			path.add(current);
			current = Node.prev.get(current);
		}
		path.add(first);
		Collections.reverse(path);
		return path;
	}

	private void expandNode(Node current, Node last, ArrayList<Node> openlist, ArrayList<Node> closedlist){
		for (Node node : current.getNeighbours((NavPoint) last)) {
			if (closedlist.contains(node)) continue;
			float temp_g = current.getValue() + current.getDistance(node);
			if (openlist.contains(node) && temp_g >= node.getValue()) continue;
			Node.prev.put(node, current);
			node.setValue(temp_g);
			if (!openlist.contains(node)) openlist.add(node);
		}
	}
	
	private Node getMin(ArrayList<Node> list, Node last) {
		Node min = list.get(0);
		for (Node node : list) {
			if (node.getValue() + node.getPos().dst(last.getPos()) < min.getValue() + min.getPos().dst(last.getPos())) {
				min = node;
			}
		}
		return min;
	}
}
