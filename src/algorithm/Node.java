package algorithm;

import java.awt.geom.*;

public class Node extends Point2D {
	//defining components of node
	public double xPos;
	public double yPos;
	public double distance; //distance
	
	//Create node with x and y values
	public Node(double x, double y){
		this.xPos = x;
		this.yPos = y;
	}
	
	//Create node with x and y pos of a point
	public Node(Point2D point) {
		this.xPos = point.getX();
		this.yPos = point.getY();
	}
	
	 //Returns x-value.
	public double getX() {
		return this.xPos;
	}

	 //Returns y-value.
	public double getY() {
		return this.yPos;
	}
	
	//String print node pos
	public String toString() {
		return "Node: " + this.xPos + ", " + this.yPos;
	}

	@Override
	public void setLocation(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	
}
