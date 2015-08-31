package problem;

import java.awt.geom.Rectangle2D;
import java.util.Scanner;

/**
 * This class represents one of the rectangular obstacles in Assignment 1.
 * 
 * @author lackofcheese
 */
public class Obstacle {
	/** Stores the obstacle as a Rectangle2D */
	private Rectangle2D rect;

	/**
	 * Constructs an obstacle with the given (x,y) coordinates of the
	 * bottom-left corner, as well as the width and height.
	 * 
	 * @param x
	 *            the minimum x-value.
	 * @param y
	 *            the minimum y-value.
	 * @param w
	 *            the width of the obstacle.
	 * @param h
	 *            the height of the obstacle.
	 */
	public Obstacle(double x, double y, double w, double h) {
		this.rect = new Rectangle2D.Double(x, y, w, h);
	}

	/**
	 * Constructs an obstacle from the representation used in the input file:
	 * that is, the x- and y- coordinates of the upper-left vertex followed by
	 * the lower-right vertex
	 * 
	 * @param str
	 */
	public Obstacle(String str) {
		Scanner s = new Scanner(str);
		double xMin = s.nextDouble();
		double yMax = s.nextDouble();
		double xMax = s.nextDouble();
		double yMin = s.nextDouble();
		this.rect = new Rectangle2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);
		s.close();
	}

	/**
	 * Returns a copy of the Rectangle2D representing this obstacle.
	 * 
	 * @return a copy of the Rectangle2D representing this obstacle.
	 */
	public Rectangle2D getRect() {
		return (Rectangle2D) rect.clone();
	}

	/**
	 * Returns a String representation of this obstacle.
	 * 
	 * @return a String representation of this obstacle.
	 */
	public String toString() {
		return rect.toString();
	}
}
