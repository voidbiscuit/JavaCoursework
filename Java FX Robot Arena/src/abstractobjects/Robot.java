package abstractobjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Robot {
	// Main Constructors
	public double x, y, size, speed, direction;

	// Momentum Values
	public double mass, momentum;

	// Colour Coding
	public Color maincolor, altcolor, backcolor;
	public boolean active;

	// Identification
	public int ID;
	public String name;

	/**
	 * Constructor for the abstract class Robot
	 * 
	 * @param data
	 *            double[] storing robot data
	 */
	public Robot(double[] data) {
		this.ID = 0;
		int count = 0;
		this.x = data[count++];
		this.y = data[count++];
		this.size = data[count++];
		this.speed = data[count++];
		this.direction = data[count++];
		this.mass = Math.PI * size * size; // Mass = pi r^2 assuming all robots have the same density and are 2D
		this.momentum = mass * size;
		maincolor = Color.WHITE;
		altcolor = Color.WHITE;
		backcolor = Color.BLACK;
		active = false;

	}

	/**
	 * Run by user to get robot information. Only used in this class, and will
	 * always run exportrobot.
	 * 
	 * @param c
	 *            Character to split robot info by.
	 * @return robot information
	 */
	public String robotInfo(char c) {
		return exportRobot(c) + "\n";
	}

	/**
	 * Returns data for a robot, there are multiple overloads of this function.
	 * 
	 * @param c
	 *            character to split by
	 * @return robot data
	 */
	protected String exportRobot(char c) {
		return separate(c, ID, x, y, size, speed, direction);
	}

	/**
	 * Separate robot data by C
	 * 
	 * @param c
	 *            separate by this character
	 * @param data
	 *            robot data
	 * @return any number of objects, in string form, separated by char c
	 */
	protected String separate(char c, Object... data) {
		String out = "";
		for (int i = 0; i < data.length; i++)
			out += data[i] + "" + c;
		return out;
	}

	/**
	 * Sets the color depending on if the robot is active
	 * 
	 * @param gc
	 *            Graphics Context which the robot is drawn to
	 */

	protected void setColor(GraphicsContext gc) {
		gc.setFill(active ? altcolor : maincolor);
	}

	/**
	 * Set robot background to the background color of the specific robot
	 * 
	 * @param gc
	 *            Graphics Context
	 */
	protected void setBG(GraphicsContext gc) {
		gc.setFill(backcolor);
	}

	/**
	 * Draw border, then inner circle, then arrow, in the right colours
	 * 
	 * @param gc
	 *            GraphicsContext
	 */
	public void Draw(GraphicsContext gc) {
		setColor(gc);
		DrawCircleBorder(gc);
		setBG(gc);
		DrawCircle(gc);
		setColor(gc);
		DrawDirectionArrow(gc);

	}

	/**
	 * Draw inside of Circle
	 * 
	 * @param gc
	 *            GraphicsContext
	 */
	protected void DrawCircle(GraphicsContext gc) {
		double scale = size - 3;
		gc.fillArc(x - scale / 2, y - scale / 2, scale, scale, 0, 360, ArcType.ROUND);

	}

	/**
	 * Draw outside of Circle
	 * 
	 * @param gc
	 *            GraphicsContext
	 */
	protected void DrawCircleBorder(GraphicsContext gc) {
		gc.fillArc(x - size / 2, y - size / 2, size, size, 0, 360, ArcType.ROUND);
	}

	/**
	 * Direction Arrow Maths
	 * 
	 * @param gc
	 *            GraphicsContext
	 */
	protected void DrawDirectionArrow(GraphicsContext gc) {
		gc.fillPolygon(
				new double[] { x + 0.35 * size * Math.sin(Math.toRadians(direction)),
						x + 0.3 * size * Math.sin(Math.toRadians(direction + 270)),
						x + 0.3 * size * Math.sin(Math.toRadians(direction + 90)) },
				new double[] { y + 0.35 * size * Math.cos(Math.toRadians(direction)),
						y + 0.3 * size * Math.cos(Math.toRadians(direction + 270)),
						y + 0.3 * size * Math.cos(Math.toRadians(direction + 90)) },
				3);
	}

	/**
	 * Most robots have no need for a Left Right function, as they do not have
	 * whiskers, but if it gets called, then they will return false
	 * 
	 * @param x
	 *            x coordinate of the bump point
	 * @param y
	 *            y coordinate of the bump point
	 * @param size
	 *            the size of the light or object which needs to be checked
	 * @return false
	 */
	public boolean Left(double x, double y, double size) {
		return false;
	}

	/**
	 * Most robots have no need for a Left Right function, as they do not have
	 * whiskers, but if it gets called, then they will return false
	 * 
	 * @param x
	 *            x coordinate of the bump point
	 * @param y
	 *            y coordinate of the bump point
	 * @param size
	 *            the size of the light or object which needs to be checked
	 * @return false
	 */

	public boolean Right(double x, double y, double size) {
		return false;
	}

	/**
	 * Update the location of the robot, based on its speed and direction. Also,
	 * conservation of momentum rules apply here.
	 */
	public void Calculate() {
		direction = fixangle(direction);
		x += speed * sin(direction);
		y += speed * cos(direction);
		momentum = mass * speed;
	}

	/**
	 * Keeps angles between 0 and 360, should be used with setAngle();
	 * 
	 * @param angle
	 *            angle to be fixed
	 * @return fixed angle
	 */
	protected double fixangle(double angle) {
		while (angle < 0)
			angle += 360;
		angle %= 360;
		return angle;
	}

	/**
	 * short math.cos function
	 * 
	 * @param angle
	 *            angle
	 * @return cos angle
	 */
	protected double cos(double angle) {
		angle = fixangle(angle);
		return Math.cos(Math.toRadians(angle));
	}

	/**
	 * short math.sin function
	 * 
	 * @param angle
	 *            angle
	 * @return sin angle
	 */
	protected double sin(double angle) {
		angle = fixangle(angle);
		return Math.sin(Math.toRadians(angle));
	}

	/**
	 * Bounce on wall
	 * 
	 * @param canvaswidth
	 *            0 to right wall
	 * @param canvasheight
	 *            0 to bottom wall
	 */
	public void OnWallBounce(double canvaswidth, double canvasheight) {
		direction = fixangle(direction);
		if (x < size / 2) // Left Wall
			direction = (direction < 360 && direction > 180) ? 360 - direction : direction;
		if (x > canvaswidth - size / 2) // Right Wall
			direction = (direction < 180 && direction > 0) ? 360 - direction : direction;
		if (y < size / 2) // Top Wall
			direction = (direction < 270 && direction > 90) ? 180 - direction : direction;
		if (y > canvasheight - size / 2) // Bottom Wall
			direction = (direction < 90 || direction > 270) ? 180 - direction : direction;
		direction = fixangle(direction);
	}

	/**
	 * If robots collide, make them bounce off eachother using reflection axis.
	 * 
	 * @param reflectionaxis
	 *            the angle at which they collide
	 */
	public void Collide(double reflectionaxis) {
		direction = fixangle(reflectionaxis - direction);
	}

	/**
	 * Robots do not have a brightness generally
	 * 
	 * @return 0 for generic robots
	 */
	public double getBrightness() {
		return 0;
	}
}
