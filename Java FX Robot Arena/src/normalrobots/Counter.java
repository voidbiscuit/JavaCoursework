package normalrobots;

import abstractobjects.Robot;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Counter extends Robot {
	int count;

	/**
	 * Constructor for static counter robot
	 * 
	 * @param data
	 *            of robot
	 * @param count
	 *            current count that the counter should start on, normally 0
	 */
	public Counter(double[] data, int count) {
		super(data);
		this.ID = -1;
		this.count = count;
		this.direction = 0;
		this.speed = 0;
		this.size = data.length > 2 ? data[2] : 40;
		this.mass = 0;

		maincolor = Color.GREEN;
		altcolor = Color.LIMEGREEN;
		backcolor = Color.BLACK;
	}

	/**
	 * Overload needed to return extra count data as it is stored in the constructor
	 */
	public String exportRobot(char c) {
		return super.exportRobot(c) + separate(c, count);
	}

	/**
	 * Draw overridden to draw the count number inside the circle
	 */
	public void Draw(GraphicsContext gc) {
		setColor(gc);
		DrawCircleBorder(gc);
		setBG(gc);
		DrawCircle(gc);
		setColor(gc);
		gc.fillText("" + count, x, y);
	}

	/**
	 * Robot never moves, so no calculation required
	 */
	public void Calculate() {

	}

	/**
	 * When robots collide, count is incremented, and redrawn
	 */
	public void Collide(double reflectionaxis) {
		count++;
	}

}
