//Michael Dobrzanski
import java.awt.Graphics2D;
// DO NOT MODIFY
// UPDATE 3/8/2018: Added setCenterCoordinates method
public class Circle {
	private double radius;
	// X and Y are in Java graphics coordinate system
	// (0, 0 in upper left corner)
	private double centerX, centerY;
	
	public Circle(double radius, double centerX, double centerY) {
		if (radius < 0) {
			throw new IllegalArgumentException();
		}
		this.radius = radius;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getCenterX() {
		return centerX;
	}
	
	public double getCenterY() {
		return centerY;
	}
	
	public void setRadius(double radius) {
		if (radius < 0) {
			throw new IllegalArgumentException();
		}
		this.radius = radius;
	}
	
	public void setCenterCoordinates(double centerX, double centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public double area() {
		return Math.PI * radius * radius;
	}
	
	public boolean overlaps(Circle c) {
		// First, calculate distance
		double dx = this.centerX - c.centerX;
		double dy = this.centerY - c.centerY;
		double dist = Math.sqrt(dx*dx + dy*dy);
		return dist <= this.radius + c.radius;
	}

	public void draw(Graphics2D g) {
		g.fillOval((int)(centerX-radius),
				(int)(centerY-radius),
				(int)(2*radius),
				(int)(2*radius));
		
	}
	
}
