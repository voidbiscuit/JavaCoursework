package normalrobots;

import java.util.Random;

import abstractobjects.Robot;
import javafx.scene.paint.Color;

public class WallRunner extends Robot {
	Random r = new Random();
	Boolean collided;

	/**
	 * Constructor for a wall runner
	 * 
	 * @param data
	 *            robot data
	 */
	public WallRunner(double[] data) {
		super(data);
		this.ID = 3;
		maincolor = Color.DARKGREEN;
		altcolor = Color.LIMEGREEN;
		backcolor = Color.BLACK;
	}

	/**
	 * Override for the wall bounce code. If the robot meets a wall, it will travel
	 * 90 degrees to the wall and come 1 pixel away from the wall. This is so the
	 * robot does not keep colliding into the wall over and over and to make sure
	 * that if the robot hits 2 walls, it does not get rekt.
	 */
	public void OnWallBounce(double canvaswidth, double canvasheight) {
		direction = fixangle(direction);
		if (x < size) // Left Wall
		{
			x++;
			direction = 0;
		}
		if (x > canvaswidth - size) // Right Wall
		{
			x--;
			direction = 180;
		}
		if (y < size) // Top Wall
		{
			y++;
			direction = 270;
		}
		if (y > canvasheight - size) // Bottom Wall
		{
			y--;
			direction = 90;
		}
		active = (direction % 90 == 0);
		direction = fixangle(direction);
	}

	/**
	 * Robots like this are better when they don't follow collision, otherwise they
	 * tend to end up outside the arena and need to be saved by a section of code
	 * which checks if robots have run off
	 */
	public void Collide(double reflectionaxis) {

	}

}
