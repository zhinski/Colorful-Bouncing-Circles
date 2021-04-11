//Michael Dobrzanski
import java.awt.Color;
import java.awt.Graphics2D;
//DO NOT MODIFY
public class ColorfulCircle extends Circle {

	private Color color;
	
	public ColorfulCircle(double radius, double centerX, double centerY, Color color) {
		super(radius, centerX, centerY);
		this.color = color;
	}

	// The Override annotation below is not necessary, but it ensures that this method
	// correctly matches up with a method in Circle or another superclass.	
	@Override
	public void draw(Graphics2D g) {
		Color c = g.getColor();
		g.setColor(color);
		super.draw(g);
		g.setColor(c);
	}

}
