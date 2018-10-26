package normalrobots;

import abstractobjects.Robot;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Light extends Robot {
	public double brightness;

	/**
	 * Constructor for Light
	 * 
	 * @param data
	 *            of Robot
	 */
	public Light(double data[]) {
		super(data);
		this.ID = -2;
		this.direction = 0;
		this.speed = 0;
		this.brightness = size * 2.5;
		this.mass = 0;

		maincolor = Color.ORANGE;
		altcolor = Color.YELLOW;
		backcolor = Color.BLACK;

	}

	/**
	 * Overridden to draw the outside of the light, then the inside, no arrow
	 * required
	 */
	public void Draw(GraphicsContext gc) {
		setColor(gc);
		DrawLight(gc);
		setBG(gc);
		DrawCircleBorder(gc);
		setColor(gc);
		DrawCircle(gc);
	}

	/**
	 * Function for drawing a light around the light robot, with a radius of
	 * brightness
	 * 
	 * @param gc
	 *            Graphics Context
	 */
	public void DrawLight(GraphicsContext gc) {
		gc.fillArc(x - brightness / 2, y - brightness / 2, brightness, brightness, 0, 360, ArcType.ROUND);
	}

	/**
	 * Override for collide so lights do not collide
	 */
	public void Collide(double reflectionaxis) {

	}

	/**
	 * Lights never move, overridden as no calculations are needed
	 */
	public void Calculate() {

	}

	/**
	 * This robot has a brightness, so its brightness is returned, this is used for
	 * calculating light robots.
	 */
	public double getBrightness() {
		return brightness;
	}
}
