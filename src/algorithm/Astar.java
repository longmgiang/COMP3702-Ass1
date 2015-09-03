package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import algorithm.Node;

public class Astar {

	public static ArrayList<Node> InformedSearch(Node start, Node end, HashMap<Node, HashMap<Node, Double>> map) {

		ArrayList<Node> history = new ArrayList<Node>();
		ArrayList<Node> finalPath = new ArrayList<Node>();
		HashMap<Node, Node> travelPath = new HashMap<Node, Node>();
		PriorityQueue<Node> priority = new PriorityQueue(1, new NodePrioritize());
		start.fCost = start.calcDistance(end);
		priority.add(start);
		double tempg;
		while (priority.size() != 0) {
			Node current = priority.poll();
			// current = priority.remove();

			if (current.equals(end)) {
				finalPath.add(end);
				System.out.println("GOAL!");
				return finalPath;
			}

			history.add(current);
			for (Node n : map.get(current).keySet()) {
				if (!history.contains(n)) {
					tempg = current.getGCost() + map.get(current).get(n);
					if (!(priority.contains(n)) || tempg < n.getGCost()) {
						n.gCost = tempg;
						n.fCost = n.gCost + n.calcDistance(end);
					}

					priority.add(n);
				}

			}
			if (!current.equals(end)) {
				finalPath.add(current);
			}

		}

		return finalPath;
	}

}
