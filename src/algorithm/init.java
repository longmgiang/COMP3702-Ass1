package algorithm;

import java.io.IOException;
import java.awt.geom.Point2D;
import problem.*;
import algorithm.*;

public class init {
	public static void main(String[] args){
		ProblemSpec problem = new ProblemSpec();
		try {
			double a = 1;
			double b = 2;
			problem.loadProblem("testcases/4_joints.txt");
			Point2D point = problem.getInitialState().getBase();
			
			Node n = new Node(a, b);
			System.out.println(n);
		} catch (IOException e) {
			System.err.println("File cannot be found (IOException): "
					+ e.getMessage());
		}
		
		
	}
	
}
