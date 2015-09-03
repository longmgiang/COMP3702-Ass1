package algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.List;
import java.awt.geom.Point2D;
import problem.*;
import algorithm.*;

public class init {
	public static void main(String[] args){
		ProblemSpec problem = new ProblemSpec();
		try {
			
			problem.loadProblem("testcases/4_joints.txt");
			
			Node nodeStart = new Node(problem.getInitialState().getBase().getX(), 
					problem.getInitialState().getBase().getY());
			Node nodeEnd = new Node(problem.getGoalState().getBase().getX(), 
					problem.getGoalState().getBase().getY());
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>(problem.getObstacles());
			
			long startTimeMap = System.nanoTime();
			HashMap<Node, HashMap<Node, Double>> newMap = PRM.createMap(nodeStart, nodeEnd, obstacles, 5000);
			long endTimeMap = System.nanoTime();
			//System.out.println(newMap);
			System.out.println("time: " + (endTimeMap-startTimeMap)/1000000000);
//			System.out.println(newMap.get(nodeStart).keySet());
			long startTimeSearch = System.nanoTime();
			ArrayList<Node> travelPath = Astar.InformedSearch(nodeStart, nodeEnd, newMap);
			long endTimeSearch = System.nanoTime();
			System.out.println("time: " + (endTimeSearch-startTimeSearch)/1000000000);
			System.out.println(travelPath);
		} catch (IOException e) {
			System.err.println("File cannot be found (IOException): "
					+ e.getMessage());
		}
		
		
	}
	
}
