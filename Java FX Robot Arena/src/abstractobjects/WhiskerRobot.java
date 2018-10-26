package abstractobjects;

import javafx.scene.canvas.GraphicsContext;

public class WhiskerRobot extends Robot {
	protected double[] xleft = new double[4];
	protected double[] yleft = new double[4];
	protected double[] xright = new double[4];
	protected double[] yright = new double[4];

	/**
	 * Constructor for whiskerrobot
	 * 
	 * @param data
	 *            robot data
	 */
	public WhiskerRobot(double[] data) {
		super(data);
		this.ID = 0;
		calculateWhiskers();
	}

	/**
	 * Draw the whiskers, then the robot over the top
	 */
	public void Draw(GraphicsContext gc) {
		setColor(gc);
		Whiskers(gc);
		super.Draw(gc);
	}

	/**
	 * Calculate whiskers, then draw them.
	 * 
	 * @param gc
	 *            Graphics Context
	 */
	private void Whiskers(GraphicsContext gc) {
		calculateWhiskers();
		gc.fillPolygon(xleft, yleft, 4);
		gc.fillPolygon(xright, yright, 4);
	}

	/**
	 * Calculate each whisker, x then y.
	 */
	private void calculateWhiskers() {
		xleft[0] = x + 0.4 * size * sin(direction - 45);
		yleft[0] = y + 0.4 * size * cos(direction - 45);

		xleft[1] = x + 0.4 * size * sin(direction - 35);
		yleft[1] = y + 0.4 * size * cos(direction - 35);

		xleft[2] = x + 1.2 * size * sin(direction - 35);
		yleft[2] = y + 1.2 * size * cos(direction - 35);

		xleft[3] = x + 1.2 * size * sin(direction - 45);
		yleft[3] = y + 1.2 * size * cos(direction - 45);

		xright[0] = x + 0.4 * size * sin(direction + 45);
		yright[0] = y + 0.4 * size * cos(direction + 45);

		xright[1] = x + 0.4 * size * sin(direction + 35);
		yright[1] = y + 0.4 * size * cos(direction + 35);

		xright[2] = x + 1.2 * size * sin(direction + 35);
		yright[2] = y + 1.2 * size * cos(direction + 35);

		xright[3] = x + 1.2 * size * sin(direction + 45);
		yright[3] = y + 1.2 * size * cos(direction + 45);
	}

	/**
	 * 
	 * @param canvaswidth
	 *            0 to width
	 * @param canvasheight
	 *            0 to height
	 * @param x
	 *            point on whisker
	 * @param y
	 *            point on whisker
	 * @return true if point is outside canvas, false if not.
	 */
	public boolean pointOutside(double canvaswidth, double canvasheight, double x, double y) {
		boolean turn = (x < size || x > canvaswidth - size || y < size || y > canvasheight - size);
		active = active || turn;
		return turn;
	}
}