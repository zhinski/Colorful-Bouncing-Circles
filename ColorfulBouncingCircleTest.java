import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Test of CS 142 Assignment 6 by Martin Hock (Version of 8:15 PM 11/25/2018)
 * 
 * This code is for personal educational use only and may not be redistributed
 * without permission of the author.
 */

public class ColorfulBouncingCircleTest {

	private static ArrayList<Circle> allCircles = new ArrayList<>();
	private static ArrayList<ColorfulBouncingCircle> bouncingCircles = new ArrayList<>();
	
	public static void checkFile(String file, byte[] goodDigest) {
		try {
			Scanner s = new Scanner(new File(file));
			String text = s.useDelimiter("\\Z").next();
			s.close();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(text.replaceAll("//.*", "").replaceAll("\\s+", "").getBytes());
			if (!Arrays.equals(digest, goodDigest)) {
				System.out.println("WARNING: Your copy of " + file + " is different than the one found on Canvas!");
				System.out.println("You may not receive any points!!");
				System.exit(-1);
			}
			System.out.println(file+" looks OK.");
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find "+file+". Did you correctly set up this assignment?");
			System.out.println("You may not receive any points!!");
			System.exit(-1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Please check with your instructor regarding this error. There may be problems with your program or Java setup!");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		checkFile("src/Circle.java", new byte[] {75, 103, 78, 83, -105, 12, -29, 71, 2, -44, 92, -40, 25, 68, -96, -36});
		checkFile("src/ColorfulCircle.java", new byte[] {-81, 50, -64, -92, 48, 92, 116, 15, -54, 66, 74, 26, -31, 69, -78, 4});
		JFrame window = new JFrame("Colorful Bouncing Circles Test");
		{
			BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, 64, 64);
			g.setColor(Color.RED);
			g.fillOval(-32, -32, 64, 64);
			g.setColor(Color.GREEN);
			g.fillOval(32, -32, 64, 64);
			g.setColor(Color.BLUE);
			g.fillOval(-32, 32, 64, 64);
			g.setColor(Color.YELLOW);
			g.fillOval(32, 32, 64, 64);
			window.setIconImage(image);
		}
		window.setLocationByPlatform(true);
		@SuppressWarnings("serial")
		final JPanel panel = new JPanel() {

			protected void paintComponent(Graphics gx) {
				Graphics2D g = (Graphics2D) gx;
				int width = getWidth();
				int height = getHeight();
				g.clearRect(0, 0, width, height);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setBackground(Color.WHITE);
				g.setColor(Color.BLACK);
				ColorfulBouncingCircle.setPlayingFieldSize(width, height);
				for (Circle c : allCircles) {
					c.draw(g);
				}
			}

		};
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		window.setSize(d.width / 2, d.height / 2);
		window.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
		window.setContentPane(panel);
		for (int i = 0; i < 100; i++) {
			int radius = (int) (Math.random() * 15) + 5;
			int x = (int) (Math.random() * window.getWidth());
			int y = (int) (Math.random() * window.getHeight());
			if (Math.random() > 0.25) {
				bouncingCircles.add(new ColorfulBouncingCircle(radius,
						x, y,
						Color.getHSBColor((float) Math.random(), 1.0f, 1.0f), Math.random() * radius, Math.random() * radius));
				allCircles.add(bouncingCircles.get(bouncingCircles.size()-1));
			} else if (Math.random() > 0.75) {
				allCircles.add(new ColorfulCircle(radius,
						x, y,
						Color.getHSBColor((float) Math.random(), 1.0f, 1.0f)));
			} else {
				allCircles.add(new Circle(radius, x, y));
			}
		}
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Timer t = new Timer(1000 / 60, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ColorfulBouncingCircle c : bouncingCircles) {
					c.tick();
				}
				for (Circle c1 : allCircles) {
					for (Circle c2 : allCircles) {
						if (c1 != c2) {
							c1.overlaps(c2);
						}
					}
				}
				window.getContentPane().repaint();
			}
		});
		t.start();
		Scanner s = new Scanner(System.in);
		System.out.print("Without closing the window, press Enter in the console to run tests...");
		s.nextLine();
		s.close();
		t.stop();
		window.setVisible(false);
		window.dispose();
		System.out.println("Running tests, please wait...");
		int score = 28; // For getting it to compile, etc.
		try {
			System.out.println("Setting playing field size to width of 50 and height of 20 by calling the static method ColorfulBouncingCircle.setPlayingFieldSize(50, 20).");
			ColorfulBouncingCircle.setPlayingFieldSize(50, 20);
			System.out.println("At this point, all circles should know to bounce if their center is less than 0 in either axis, greater than 50 in the x dimension, or 20 in the y dimension.");
			System.out.println("Creating ColorfulBouncingCircle c1 with radius 3 at (10, 20) with x velocity 6 and y velocity -5.");
			ColorfulBouncingCircle c1 = new ColorfulBouncingCircle(3, 10, 20, null, 6, -5);
			if (c1.getCenterX() != 10 || c1.getCenterY() != 20) {
				System.out.println("ColorfulBouncingCircle c1 does not have the center coordinate given to the constructor!");
			} else score += 5;
			System.out.println("After each call to the tick() method, the center coordinate should change unless it would cause the circle to pass through the wall.");
			c1.tick();
			if (c1.getCenterX() != 16 || c1.getCenterY() != 15) {
				System.out.println("ColorfulBouncingCircle c1 does not have the correct center after calling tick()!");
				System.out.println("Should be (16, 15) but is ("+c1.getCenterX()+", "+c1.getCenterY()+")");
			} else score += 5;
			System.out.println("Creating ColorfulBouncingCircle c2 with radius 10 at (3, 15) with x velocity -2.5 and y velocity 2.");
			System.out.println("Now, Tick Phases 1 through 27 will occur. In each Tick Phase, the tick() method will be called on each circle (c1 and c2), then the circles will be checked for overlap.");
			ColorfulBouncingCircle c2 = new ColorfulBouncingCircle(10, 3, 15, null, -2.5, 2);
			int[] c1xs = {22, 28, 34, 34, 40, 46, 46, 40, 34, 34, 28, 34, 34, 40};
			int[] c1ys = {10,  5,  0,  0,  5, 10, 10, 15, 20, 20, 15, 20,  20,15};
			double[] c2xs={3,0.5,0.5,  3,  3, 5.5,  8,10.5,13,15.5,18,20.5,18,15.5};
			int[] c2ys = {15, 17, 17, 19, 19, 17, 15, 13, 11,  9,  7,  5,  7,  9};
			for (int i = 0; i < c1xs.length; i++) {
				boolean ok = true;
				{
					double c2x = c2.getCenterX(), c2y = c2.getCenterY();
					if (c2.getCenterX() != c2x || c2.getCenterY() != c2y) {
						System.out.println("Getting c2 center twice in a row got different results!");
						ok = false;
					}
					if (c2x != c2xs[i] || c2y != c2ys[i]) {
						if (i > 0 && c2xs[i] == c2xs[i-1] && c2ys[i] == c2ys[i-1]) {
							System.out.println("c2 should not have moved but went to ("+c2x+", "+c2y+")!");
							if (c2x == c2xs[i] || c2y == c2ys[i]) {
								System.out.println("It looks like one coordinate didn't change, but if the circle is stopped by the wall, neither coordinate should change.");
							}
						} else {
							System.out.println("c2 center should be ("+c2xs[i]+", "+c2ys[i]+") but was ("+c2x+", "+c2y+")!");
						}
						ok = false;
					}
				}
				c2.tick();
				c1.tick();
				System.out.println("Tick Phase "+(i+1)+" occurs.");
				System.out.println("After tick() called, c1: ("+c1.getCenterX()+", "+c1.getCenterY()+") c2: ("+c2.getCenterX()+", "+c2.getCenterY()+")");
				boolean badlap = false;
				if (c1.overlaps(c2)) {
					if (i == 10) {
						System.out.println("c1.overlaps(c2). Good!");
						score += 2;
					}
					else System.out.println("c1.overlaps(c2) returning true at tick "+(i+1)+" unexpected!");
				} else if (i == 10) {
					System.out.println("c1.overlaps(c2) should return true at tick 11!");
					badlap = true;
				}
				if (c2.overlaps(c1)) {
					if (i == 10) {
						System.out.println("c2.overlaps(c1). Good!");
						score += 2;
					}
					else {
						System.out.println("c2.overlaps(c1) returning true at tick "+(i+1)+" unexpected!");
						if (i == 11 && score == 64) {
							System.out.println("Did you override the overlap method? The velocity should have been flipped when the overlap occurred above in the Tick 11 phase.");
						}
					}
				} else if (i == 10 && !badlap) {
					System.out.println("c2.overlaps(c1) should return true at tick 11!");
				}
				{
					double c1x = c1.getCenterX(), c1y = c1.getCenterY();
					if (c1.getCenterX() != c1x || c1.getCenterY() != c1y) {
						System.out.println("Getting c1 center twice in a row got different results!");
						ok = false;
					}
					if (c1x != c1xs[i] || c1y != c1ys[i]) {
						if (i > 0 && c1xs[i] == c1xs[i-1] && c1ys[i] == c1ys[i-1]) {
							System.out.println("c1 should not have moved but went to ("+c1x+", "+c1y+")!");
							if (c1x == c1xs[i] || c1y == c1ys[i]) {
								System.out.println("It looks like one coordinate didn't change, but if the circle is stopped by the wall, neither coordinate should change.");
							}
						} else {
							System.out.println("c1 center should be ("+c1xs[i]+", "+c1ys[i]+") but was ("+c1x+", "+c1y+")!");
						}
						ok = false;
					}
				}
				if (ok) {
					System.out.println("Tick Phase "+(i+1)+" OK.");
					score += 2;
				}
			}
			System.out.println("Changing playing field size to width of 60 and height of 30. This changes where the walls are for all circles.");
			ColorfulBouncingCircle.setPlayingFieldSize(60, 30);
			int oldTicks = c1xs.length;
			c1xs = new int[]{  46, 52, 58, 58, 52, 46, 40, 34, 28, 22, 28, 34, 40};
			c1ys = new int[]{  10,  5,  0,  0,  5, 10, 15, 20, 25, 30, 25,  20,15};
			c2xs =new double[]{13, 10.5,8, 5.5, 3, 0.5,0.5,  3,5.5,  8,10.5,8,5.5};
			c2ys = new int[]{  11, 13, 15, 17, 19, 21, 21,  23, 25, 27, 29, 27,25};
			for (int i = 0; i < c1xs.length; i++) {
				boolean ok = true;
				{
					double c2x = c2.getCenterX(), c2y = c2.getCenterY();
					if (c2.getCenterX() != c2x || c2.getCenterY() != c2y) {
						System.out.println("Getting c2 center twice in a row got different results!");
						ok = false;
					}
					if (c2x != c2xs[i] || c2y != c2ys[i]) {
						if (i > 0 && c2xs[i] == c2xs[i-1] && c2ys[i] == c2ys[i-1]) {
							System.out.println("c2 should not have moved but went to ("+c2x+", "+c2y+")!");
							if (c2x == c2xs[i] || c2y == c2ys[i]) {
								System.out.println("It looks like one coordinate didn't change, but if the circle is stopped by the wall, neither coordinate should change.");
							}
						} else {
							System.out.println("c2 center should be ("+c2xs[i]+", "+c2ys[i]+") but was ("+c2x+", "+c2y+")!");
						}
						ok = false;
					}
				}
				c2.tick();
				c1.tick();
				System.out.println("Tick Phase "+(i+oldTicks+1)+" occurs.");
				System.out.println("After tick() called, c1: ("+c1.getCenterX()+", "+c1.getCenterY()+") c2: ("+c2.getCenterX()+", "+c2.getCenterY()+")");
				boolean badlap = false;
				if (c1.overlaps(c2)) {
					if (i == 9) {
						System.out.println("c1.overlaps(c2). Good!");
						score += 2;
					}
					else System.out.println("c1.overlaps(c2) returning true at "+(i+oldTicks+1)+" unexpected!");
				} else if (i == 9) {
					System.out.println("c1.overlaps(c2) should return true at tick "+(i+oldTicks+1)+"!");
					badlap = true;
				}
				if (c2.overlaps(c1)) {
					if (i == 9) {
						System.out.println("c2.overlaps(c1). Good!");
						score += 2;
					}
					else System.out.println("c2.overlaps(c1) returning true at "+(i+oldTicks+1)+" unexpected!");
				} else if (i == 9 && !badlap) {
					System.out.println("c2.overlaps(c1) should return true at tick "+(i+oldTicks+1)+"!");
				}
				{
					double c1x = c1.getCenterX(), c1y = c1.getCenterY();
					if (c1.getCenterX() != c1x || c1.getCenterY() != c1y) {
						System.out.println("Getting c1 center twice in a row got different results!");
						ok = false;
					}
					if (c1x != c1xs[i] || c1y != c1ys[i]) {
						if (i > 0 && c1xs[i] == c1xs[i-1] && c1ys[i] == c1ys[i-1]) {
							System.out.println("c1 should not have moved but went to ("+c1x+", "+c1y+")!");
							if (c1x == c1xs[i] || c1y == c1ys[i]) {
								System.out.println("It looks like one coordinate didn't change, but if the circle is stopped by the wall, neither coordinate should change.");
							}
						} else {
							System.out.println("c1 center should be ("+c1xs[i]+", "+c1ys[i]+") but was ("+c1x+", "+c1y+")!");
						}
						ok = false;
					}
				}
				if (ok) {
					System.out.println("Tick Phase "+(i+oldTicks+1)+" OK.");
					score += 2;
				}
			}
		} finally {	
			if (score < 100) {
				System.out.println("Please scroll back to the top and fix the first error that occurs, then run this test again. Later errors can accumulate due to earlier problems.");
			}
			System.out.println("Tentative score: "+score+" / 100");
			System.out.println("Score may be affected by the academic dishonesty policy!");
		}
	}
}
