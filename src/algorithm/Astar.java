package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import algorithm.Node;

public class Astar {

	public static ArrayList<Node> InformedSearch(Node start, Node end, HashMap<Node, HashMap<Node, Double>> map) {	
		
		ArrayList<Node> closedList = new ArrayList<Node>();
		HashMap<Node, Node> openList = new HashMap<Node, Node>(); 
		PriorityQueue<Node> priority = new PriorityQueue(20, new NodePrioritize());		
		
		start.fCost = start.calcDistance(end);;
		priority.add(start);
		
		while (!priority.isEmpty()) {
			
			Node current = priority.remove();
			
			if (current.equals(end)) {
				System.out.println("GOAL!");
				return finalPath(openList,start, end);
			}

			closedList.add(current);
			for (Node n : map.get(current).keySet()) {
				if (!closedList.contains(n)) {
					double tempG = current.getGCost() + map.get(current).get(n);
					if (!(priority.contains(n)) || tempG < n.getGCost()) {
						n.gCost = tempG;
						n.calcCost(end);
						openList.put(n, current);
						if (!priority.contains(n)){
							priority.add(n);
						}
					}
				}
			}
			
		}
		System.out.println("GG");
		return null;
	}
	
	public static ArrayList<Node> finalPath(HashMap<Node, Node> openList, Node start, Node current){
		ArrayList<Node> finalPath = new ArrayList<Node>();
		Node newNode = current;
		boolean done = false;
		
		while (!done) {
			if (newNode.equals(start)){
				done = true;
			}
			finalPath.add(0, newNode);
			newNode = openList.get(newNode);
		}
		return finalPath;
	}
	
}
