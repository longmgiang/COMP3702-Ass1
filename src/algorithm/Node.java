package algorithm;

import java.awt.geom.*;

public class Node extends Point2D {
	//defining components of node
	public double xPos;
	public double yPos;
	public double gCost;
	public double hCost;
	public double fCost;
	public double distance; //distance
	
	//Create node with x and y values
	public Node(double x, double y){
		this.xPos = x;
		this.yPos = y;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = fCost;
	}
	
	//Create node with x and y pos of a point
	public Node(Point2D.Double point) {
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
	
	 //Returns gCost.
	public double getGCost() {
		return this.gCost;
	}
	
	 //Returns hCost.
	public double getHCost() {
		return this.hCost;
	}
	
	 //Returns fCost.
	public double getFCost() {
		return this.fCost;
	}

	 //Returns distance.
	public double getDistance() {
		return this.distance;
	}
	
	
	public Point2D.Double convert() {
		return new Point2D.Double(this.xPos, this.yPos);
	}
	
	public double calcDistance(Node n) {
		double dx = (this.getX() - n.getX());
		double dy = (this.getY() - n.getY());
		this.distance = Math.sqrt(dx * dx + dy * dy);
		return this.distance;
	}
	
	public double calcCost(Node n) {
		this.fCost = n.getFCost() + n.getDistance();
		return this.fCost;
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
