package whiskerrobots;

import abstractobjects.WhiskerRobot;
import javafx.scene.paint.Color;

public class WallPhobicBot extends WhiskerRobot {

	/**
	 * Constructor for WallPhobicBot which is a type of whiskerrobot
	 * 
	 * @param data
	 *            robot data
	 */
	public WallPhobicBot(double[] data) {
		super(data);
		this.ID = 4;
		maincolor = Color.BLUEVIOLET;
		altcolor = Color.VIOLET;
		backcolor = Color.BLACK;
	}

	/**
	 * When checking if the robot has hit a wall, also check that the whiskers are
	 * not through a wall. If they are the robot will turn away from the wall
	 */
	public void OnWallBounce(double canvaswidth, double canvasheight) {
		direction = pointOutside(canvaswidth, canvasheight, xleft[3], yleft[3]) ? direction + 10 : direction;
		direction = pointOutside(canvaswidth, canvasheight, xright[3], yright[3]) ? direction - 10 : direction;
		super.OnWallBounce(canvaswidth, canvasheight);

	}
}
