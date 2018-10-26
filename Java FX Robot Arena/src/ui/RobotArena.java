package ui;

import java.util.ArrayList;
import java.util.Random;

import abstractobjects.Robot;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import normalrobots.Counter;
import normalrobots.Light;
import normalrobots.RoamDot;
import normalrobots.Spinner;
import normalrobots.WallRunner;
import whiskerrobots.LightBot;
import whiskerrobots.WallPhobicBot;

public class RobotArena {

	public double canvaswidth;
	public double canvasheight;

	private double maxspeed = 15;

	private VBox debug;
	private GraphicsContext gc;
	private Random random = new Random();

	public ArrayList<Robot> robots = new ArrayList<Robot>();

	public RobotArena(GraphicsContext gc, VBox debug) {
		this.gc = gc;
		this.debug = debug;
	}

	/**
	 * @param canvaswidth
	 *            width of canvas for checking outside of arena
	 * @param canvasheight
	 *            height of canvas for checking outside of arena
	 */
	public void calculateSystem(double canvaswidth, double canvasheight) {
		for (int i = 0; i < robots.size(); i++)
			robots.get(i).active = false;
		for (int i = 0; i < robots.size(); i++) {
			Robot r = robots.get(i);
			r.Calculate();
			r.OnWallBounce(canvaswidth, canvasheight);
			for (int j = i + 1; j < robots.size(); j++) {
				Robot s = robots.get(j);

				// Check Light Robots
				if (r.ID == -2 && s.ID == 5) // if r is a light, and s is a lightbot
				{
					if (s.Left(r.x, r.y, r.getBrightness())) {
						r.active = true;
						s.active = true;
						s.direction -= 10;
					}
					if (s.Right(r.x, r.y, r.getBrightness())) {
						r.active = true;
						s.active = true;
						s.direction += 10;
					}
				} else

				if (s.ID == -2 && r.ID == 5) // if r is a light, and s is a lightbot
				{
					if (r.Left(s.x, s.y, s.size)) {
						r.active = true;
						s.active = true;
						r.direction -= 10;
					}
					if (r.Right(s.x, s.y, s.size)) {
						r.active = true;
						s.active = true;
						r.direction += 10;
					}
				} else

				// Collision Code
				// This code above and below, checks that light robots are not colliding with
				// eachother, and that different code is run when light robots interact

				if (s.ID != 5 || r.ID != 5)
					if (Math.sqrt(
							((r.x - s.x) * (r.x - s.x)) + ((r.y - s.y) * (r.y - s.y))) < (r.size / 2 + s.size / 2)) {
						if (r.ID != -2)
							r.active = true;
						if (s.ID != -2)
							s.active = true;
						double reflectionaxis = 180 - Math.toDegrees(Math.atan((r.x - s.x) / (r.y - s.y)));
						while (reflectionaxis < 0)
							reflectionaxis += 360;
						reflectionaxis %= 360;
						double massa = (r.mass > 0 ? r.mass : s.mass);
						double massb = (s.mass > 0 ? s.mass : r.mass);
						double momentum = (massa * r.speed + massb * s.speed);
						r.speed = momentum / (2 * massa) < maxspeed ? momentum / (2 * massa) : maxspeed;
						s.speed = momentum / (2 * massb) < maxspeed ? momentum / (2 * massb) : maxspeed;
						r.Collide(reflectionaxis);
						s.Collide(reflectionaxis);
						r.Calculate();
						s.Calculate();
					}
			}
		}
		for (int i = 0; i < robots.size(); i++) {
			Robot r = robots.get(i);
			if (r.x < -300 || r.x > canvaswidth + 300 || r.y < -300 || r.y > canvasheight + 300) {
				System.out.println("Oops, Robot " + i + " has gone out of bounds!");
				r.x = canvaswidth / 2;
				r.y = canvasheight / 2;
				r.direction = random.nextInt(360) % 360;
			}
		}
	}

	/**
	 * Draws the line upon which two robots collide.
	 * 
	 * @param r
	 *            Robot R
	 * @param s
	 *            Robot S
	 * @param reflectionaxis
	 *            The angle at which they collide, and at which the line should be
	 *            drawn
	 */

	public void collisionline(Robot r, Robot s, double reflectionaxis) {
		double x = (r.x + s.x) / 2;
		double y = (r.y + s.y) / 2;

		double xdiff = 2 * Math.sin(Math.toRadians(reflectionaxis));
		double ydiff = 2 * Math.cos(Math.toRadians(reflectionaxis));
		x -= 50 * xdiff;
		y -= 50 * ydiff;
		gc.setFill(Color.BLUE);
		for (int k = 0; k < 100; k++) {
			x += xdiff;
			y += ydiff;
			gc.fillArc(x - 2, y - 2, 5, 5, 0, 360, ArcType.ROUND);
		}
	}

	/**
	 * Writes the information of all robots in the arena to Debug
	 * 
	 * @param mousex
	 *            Current mouse x, in relation to the Window
	 * @param mousey
	 *            Current mouse y, in relation to the Window
	 */
	public void drawInfo(double mousex, double mousey) {
		if (!debug.getChildren().isEmpty())
			debug.getChildren().clear();
		for (int i = 0; i < robots.size(); i++) {
			Robot r = robots.get(i);
			String out = "";
			out += "Robot " + r.ID;
			out += " [";
			out += String.format("%4.0f", r.x);
			out += ",";
			out += String.format("%4.0f", r.y);
			out += "] ,";
			out += String.format("%3.0f", r.size);
			out += ", ";
			out += String.format("%3.0f", r.speed);
			out += ", ";
			out += String.format("%3.0f", r.direction);

			debug.getChildren().add(new Label(out));
		}
		debug.getChildren().add(new Label(" "));
		debug.getChildren().add(new Label("Canvas [" + canvaswidth + ", " + canvasheight + "]"));
		debug.getChildren().add(new Label("Mouse [" + mousex + ", " + mousey + "]"));
		debug.autosize();
	}

	/**
	 * Display the System The canvas is cleared. Lights are sent to the start of the
	 * array so they are drawn first, which stops robots hiding behind them. Next
	 * each robot is drawn.
	 */
	public void drawSystem() {
		gc.clearRect(0, 0, canvaswidth, canvasheight);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvaswidth, canvasheight);

		// Rearrange Lights to Behind

		int switchbot;
		int start = 0;
		for (int i = 0; i < robots.size(); i++)
			if (robots.get(i).ID != -2 && start < 1)
				start = i;
		for (int i = start; i < robots.size(); i++) {
			Robot r = robots.get(i);
			if (r.ID == -2) {
				switchbot = -1;
				for (int j = 0; j < robots.size(); j++)
					if (switchbot < 0 && robots.get(j).ID != -2)
						switchbot = j;
				if (switchbot > -1) {
					robots.set(i, robots.get(switchbot));
					robots.set(switchbot, r);
				}
			}
		}

		// Draw System

		for (int i = 0; i < robots.size(); i++) {
			Robot r = robots.get(i);
			r.Draw(gc);
		}
	}

	/**
	 * Overload for AddRobot to take infinite parameters
	 * 
	 * @param type
	 *            robot type
	 * @param data
	 *            array of data, written as individual doubles separated by commas
	 */
	public void addRobot(int type, double... data) {
		addRobotArray(type, data);
	}

	/**
	 * Main function for AddRobot, as a Double[] so data can be parsed in a
	 * different format.
	 * 
	 * @param type
	 *            robot type
	 * @param data
	 *            array of data.
	 */
	public void addRobotArray(int type, double[] data) {
		if (robots.size() >= 40)
			return;
		int params = data.length;
		int current = -1;
		double[] inputdata = new double[5];

		inputdata[++current] = params > current ? data[current] : canvaswidth / 2;
		inputdata[++current] = params > current ? data[current] : canvasheight / 2;
		inputdata[++current] = params > current ? data[current] : 30 + random.nextInt(30);
		inputdata[++current] = params > current ? data[current] : 2 + random.nextInt(8);
		inputdata[++current] = params > current ? data[current] : random.nextInt(360) % 360;

		data = inputdata;

		switch (type) {
		case 1:
			robots.add(new RoamDot(data));
			break;
		case 2:
			robots.add(new Spinner(data, 5));
			break;
		case 3:
			robots.add(new WallRunner(data));
			break;
		case 4:
			robots.add(new WallPhobicBot(data));
			break;
		case 5:
			robots.add(new LightBot(data));
			break;
		case -1:
			robots.add(new Counter(data, 0));
			break;
		case -2:
			robots.add(new Light(data));
			break;

		default:
			System.out.println("Debug : No Robot Found");
		}

	}
}