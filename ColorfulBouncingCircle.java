//Michael Dobrzanski
import java.awt.Color;
public class ColorfulBouncingCircle extends ColorfulCircle {

	private double xVelocity, yVelocity;
	private static double width, height;
	public static void setPlayingFieldSize(double newWidth, double newHeight) {
		width = newWidth;
		height = newHeight;
	}
	
	public ColorfulBouncingCircle(double radius, double centerX, double centerY, Color color, double xVelocity, double yVelocity) {
		super(radius, centerX, centerY, color);
		//set velocity
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	public void tick() {
		//is it out of bounds? if so, change velo
		if (getCenterX() + xVelocity < 0 || getCenterX() + xVelocity > width || getCenterY() + yVelocity < 0 || getCenterY() + yVelocity > height) {
			if (getCenterX() + xVelocity < 0 || getCenterX() + xVelocity > width) {
				xVelocity = -xVelocity;
			}
			if (getCenterY() + yVelocity < 0 || getCenterY() + yVelocity > height) {
				yVelocity = -yVelocity;
			}
		}
		else {
		//move circles
		double xShift = (getCenterX() + xVelocity);
		double yShift = (getCenterY() + yVelocity);
		setCenterCoordinates(xShift, yShift);
		}
	}	
	
		@Override
		//does it overlap? if so, change velo
		//else keep moving
	public boolean overlaps (Circle c) {
		if (super.overlaps(c)) {
			if (this.getCenterX() != c.getCenterX()) {
				xVelocity = -xVelocity;
			}
			if (this.getCenterY() != c.getCenterY()) {
				yVelocity = -yVelocity;
				return true;
			}
		}
		return false;
}
}
