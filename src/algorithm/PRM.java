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
		generatePoints(obstacles, numPoints);
		
		
		return bestPath;
	}
	
	
	private static void generatePoints(List<Obstacle> obstacles, int numPoints){
		Random rand = new Random(); 
		double distanceCost = 1;
		for (int i = 0; i < numPoints; i++) {
			Node newNode = new Node(rand.nextDouble(), rand.nextDouble());
			for (Obstacle o : obstacles) {
				if (!o.getRect().contains(newNode)) {
					bestPath.put(newNode, new HashMap<Node, Double>());
					for (Node n : bestPath.keySet()) {
						if (new Line2D.Double(newNode.convert(),n.convert()).intersects(o.getRect())) {
							System.out.println(newNode+ "to " + n + "at rectangle: " + o.getRect());
						} else {
							bestPath.get(newNode).put(n,distanceCost);
							bestPath.get(n).put(newNode,distanceCost);	
							//System.out.println(newNode);
						}
					}
				}				
			}
		}	
	}

}
