package algorithm;

import java.util.*;
import java.awt.geom.*;
import problem.*;
import java.io.IOException;

public class PRM {
	public static HashMap<Node, HashMap<Node, Double>> bestPath;

	public static HashMap<Node, HashMap<Node, Double>> createMap(Node start, Node end, List<Obstacle> obstacles,
			int numPoints) {
		ProblemSpec problem = new ProblemSpec();
		bestPath = new HashMap<Node, HashMap<Node, Double>>();
		bestPath.put(start, new HashMap<Node, Double>());
		bestPath.put(end, new HashMap<Node, Double>());
		generatePoints(obstacles, numPoints);
		return bestPath;
	}

	private static void generatePoints(List<Obstacle> obstacles, int numPoints) {
		Random rand = new Random();

		for (int i = 0; i < numPoints; i++) {
			Node newNode = new Node(rand.nextDouble(), rand.nextDouble());
			if (goodPoint(newNode, obstacles)) {
				bestPath.put(newNode, new HashMap<Node, Double>());

				for (Node n : bestPath.keySet()) {
					if (checkPathCollision(newNode, n, obstacles)) {
						bestPath.get(newNode).put(n, newNode.calcDistance(n));
						bestPath.get(n).put(newNode, newNode.calcDistance(n));
					}
				}
			}
		}
	}

	private static boolean goodPoint(Node newNode, List<Obstacle> obstacles) {
		for (Obstacle o : obstacles) {
			if (o.getRect().contains(newNode)) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkPathCollision(Node n1, Node n2, List<Obstacle> obstacles) {
		Line2D.Double line = new Line2D.Double(n1.convert(), n2.convert());
		for (Obstacle o : obstacles) {
			if (line.intersects(o.getRect())) {
				return false;
			}
		}
		return true;
	}

}
