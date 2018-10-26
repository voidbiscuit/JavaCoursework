package normalrobots;

import java.util.Random;

import abstractobjects.Robot;
import javafx.scene.paint.Color;

public class RoamDot extends Robot {
	Random r = new Random();

	/**
	 * Constructor for my first robot :) These ones specifically were made to
	 * demonstrate conservation of momentum cause I was bored hehe Their colour will
	 * reflect depending on how much mass and velocity they have
	 * 
	 * @param data
	 *            robot data
	 */
	public RoamDot(double[] data) {
		super(data);
		this.ID = 1;
		maincolor = Color.BROWN;
		altcolor = Color.GOLDENROD;
		backcolor = Color.BLACK;
	}

	/**
	 * Set active based on their momentum
	 */
	public void Calculate() {
		active = active || (speed * size > 250);
		super.Calculate();
	}

}
