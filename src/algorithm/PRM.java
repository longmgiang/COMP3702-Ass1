package algorithm;

import java.util.*;
import java.awt.geom.*;
import problem.*;
import java.io.IOException;

public class PRM {
	public static HashMap<Node, HashMap<Node, Double>> bestPath;
	
	public static HashMap<Node, HashMap<Node, Double>> createMap(Node start, Node end, List<Obstacle> obstacles, int numPoints) {
		ProblemSpec problem = new ProblemSpec();
		bestPath = new HashMap<Node, HashMap<Node, Double>>();
		bestPath.put(start, new HashMap<Node, Double>());
		bestPath.put(end, new HashMap<Node, Double>());
			if (obstacles.size() == 0) {
				for (Node n : bestPath.keySet()) {
						bestPath.get(start).put(end,start.calcDistance(end));
						bestPath.get(end).put(start,start.calcDistance(end));
				}
			} else {
				generatePoints(obstacles, numPoints);
			}
		return bestPath;
	}
	
	
	private static void generatePoints(List<Obstacle> obstacles, int numPoints){
		Random rand = new Random(); 
		int counter = 0;
		int collisions = 0;
		int actual = 0;
		for (int i = 0; i < numPoints; i++) {
			Node newNode = new Node(rand.nextDouble(), rand.nextDouble());
			counter++;
			for (Obstacle o : obstacles) {
				if (!o.getRect().contains(newNode)) {
					bestPath.put(newNode, new HashMap<Node, Double>());
					for (Node n : bestPath.keySet()) {
						if (new Line2D.Double(newNode.convert(),n.convert()).intersects(o.getRect())) {
							//System.out.println(newNode+ "to " + n + "at rectangle: " + o.getRect());
						} else {
							bestPath.get(newNode).put(n,newNode.calcDistance(n));
							bestPath.get(n).put(newNode,newNode.calcDistance(n));	
							actual++;
							//System.out.println(newNode);
						}
					}
				}else{
					collisions++;
				}
			}
		}
//		System.out.println(counter);
//		System.out.println(collisions);
//		System.out.println(actual);
	}

}
