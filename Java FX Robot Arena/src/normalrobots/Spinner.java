package normalrobots;

import java.util.Random;

import abstractobjects.Robot;
import javafx.scene.paint.Color;

public class Spinner extends Robot {

	double spin;
	Random r = new Random();

	/**
	 * Constructor for a spinner robot
	 * 
	 * @param data
	 *            robot data
	 * @param spin
	 *            how much the angle should change every tick
	 */
	public Spinner(double[] data, double spin) {
		super(data);
		this.ID = 2;
		this.spin = spin;
		maincolor = Color.BLUE;
		altcolor = Color.DODGERBLUE;
		backcolor = Color.BLACK;
	}

	/**
	 * Overridden to handle exporting spin
	 */
	public String exportRobot(char c) {
		return super.exportRobot(c) + separate(c, spin);
	}

	/**
	 * Override, which adds spin to the robot
	 */
	public void Calculate() {
		direction += spin;
		super.Calculate();
	}
}
