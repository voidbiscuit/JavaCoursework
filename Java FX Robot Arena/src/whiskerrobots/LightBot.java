package whiskerrobots;

import abstractobjects.WhiskerRobot;
import javafx.scene.paint.Color;

public class LightBot extends WhiskerRobot {

	/**
	 * Constructor for Lightbot
	 * 
	 * @param data
	 *            robot data
	 */
	public LightBot(double[] data) {
		super(data);
		this.ID = 5;
		maincolor = Color.DIMGRAY;
		altcolor = Color.YELLOW;
		backcolor = Color.BLACK;
	}

	/**
	 * @param x
	 *            center of the light
	 * @param y
	 *            center of the light
	 * @param radius
	 *            radius of the light
	 * @return whether the whisker is inside the light, true if so, false if not.
	 */

	public boolean Left(double x, double y, double radius) {
		if (((x - xleft[3]) * (x - xleft[3])) + ((y - yleft[3]) * (y - yleft[3])) < (radius * radius))
			return true;
		return false;
	}

	/**
	 * @param x
	 *            center of the light
	 * @param y
	 *            center of the light
	 * @param radius
	 *            radius of the light
	 * @return whether the whisker is inside the light, true if so, false if not.
	 */

	public boolean Right(double x, double y, double radius) {
		if (((x - xright[3]) * (x - xright[3])) + ((y - yright[3]) * (y - yright[3])) < (radius * radius))
			return true;
		return false;
	}

	/**
	 * Override for collide so the light robots do not collide
	 */
	public void Collide() {

	}
}