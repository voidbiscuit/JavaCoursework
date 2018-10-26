package ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import java.awt.Dimension;
import java.awt.MouseInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import abstractobjects.Robot;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Tim
 */
public class Window extends Application {

	// User Interface Objects

	private Stage window; // Stage which is updated with all Objects
	private BorderPane area; // Main Window
	private Group root; // Container for all Window items
	private Canvas canvas; // The container for drawing objects
	private GraphicsContext gc; // Drawing object which populates the canvas
	private VBox debug; // Text area for printing information
	private AnimationTimer timer; // Animates Arena
	private RobotArena robotarena; // Arena Object
	private String title; // Title of the Window
	private double width, height, canvaswidth, canvasheight; // Constraints of objects in window
	private int type; // Type of robot to be placed
	private JToggleButton toggledebug; // Button which checks if debug should be shown
	private Scene scene;
	private HBox buttons;

	/**
	 * Main function, starts the program
	 * 
	 * @param args
	 *            arguments for Application launch
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * Start of the program all variables are initialised here.
	 */
	@Override
	public void start(Stage window) throws Exception {
		window.setOnCloseRequest(e -> System.exit(0));
		window.setOpacity(.93);
		// Set Variables
		title = "Tim's Robot Arena";
		width = Screen.getPrimary().getBounds().getWidth();
		height = Screen.getPrimary().getBounds().getHeight();
		canvaswidth = width;
		canvasheight = height;
		type = 1;
		toggledebug = new JToggleButton("Toggle Debug");
		this.window = window;
		area = new BorderPane();
		root = new Group();
		canvas = new Canvas(canvaswidth, canvasheight);
		gc = canvas.getGraphicsContext2D();
		debug = new VBox();
		area.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setMouseEvents();

		// Placing of UI components

		area.setTop(root);
		root.getChildren().add(canvas);
		root.getChildren().add(debug);
		debug.setTranslateX(canvaswidth - 200);
		debug.setTranslateY(20);
		buttons = mainmenu();
		area.setCenter(buttons); // set bottom pane with buttons

		timer = new AnimationTimer() { // set up timer
			public void handle(long currentNanoTime) { // and its action when on
				if (window.getWidth() != width)
					resizeWidth();
				if (window.getHeight() != height)
					resizeHeight();
				drawSystem();
				calculateSystem();
				window.setTitle(title + "   (Canvas [" + canvaswidth + ", " + canvasheight + "], Mouse [" + getMouseX()
						+ " , " + getMouseY() + "])");
				if (toggledebug.isSelected())
					drawInfo(getMouseX(), getMouseY());
				else
					debug.getChildren().clear();
				drawPointer();

			}
		};
		timer.start();
		scene = new Scene(area, width, height); // set overall scene

		// Window
		window.setTitle(title);
		window.setScene(scene);
		window.show();
		robotarena = new RobotArena(gc, debug);
		window.setMaximized(true);
	}

	/**
	 * Resize Arena width with Window
	 */
	private void resizeWidth() {
		width = window.getWidth();
		canvaswidth = width;
		canvas.setWidth(canvaswidth);
		robotarena.canvaswidth = canvaswidth;
	}

	/**
	 * Resize Arena height with Window
	 */
	private void resizeHeight() {
		height = window.getHeight();
		canvasheight = height - 100;
		canvas.setHeight(canvasheight);
		robotarena.canvasheight = canvasheight;
	}

	/**
	 * @return offset X Coordinate to match window location.
	 */
	private double getMouseX() {
		return MouseInfo.getPointerInfo().getLocation().getX() - 6 - window.getX();
	}

	/**
	 * @return offset Y Coordinate to match window location.
	 */
	private double getMouseY() {
		return MouseInfo.getPointerInfo().getLocation().getY() - 30 - window.getY();
	}

	/**
	 * Draw the arena
	 */
	private void drawSystem() {
		robotarena.drawSystem();
	}

	/**
	 * Display Info
	 * 
	 * @param mousex
	 *            mouse x
	 * @param mousey
	 *            mouse y
	 */
	private void drawInfo(double mousex, double mousey) {
		robotarena.drawInfo(getMouseX(), getMouseY());
	}

	/**
	 * Draw a circle on the pointer
	 */
	private void drawPointer() {
		gc.setFill(Color.CORNFLOWERBLUE);
		double size = 10;
		gc.fillArc(getMouseX() - size / 2, getMouseY() - size / 2, size, size, 0, 360, ArcType.ROUND);
	}

	/**
	 * Calculate System
	 */
	private void calculateSystem() {
		robotarena.calculateSystem(canvaswidth, canvasheight);
	}

	/**
	 * Get the Mouse X and Y, fixed for the arena
	 * 
	 * Check if the mouse is colliding with any robots Delete any robots which are
	 * colliding with the mouse
	 * 
	 * If there are no robots colliding, create a new roamdot robot in that location
	 * 
	 * When mouse is released, draw the system
	 * 
	 */
	private void setMouseEvents() {
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				double mousex = getMouseX();
				double mousey = getMouseY();
				boolean flag = false;
				for (int i = 0; i < robotarena.robots.size(); i++) {
					Robot r = robotarena.robots.get(i);
					if (Math.sqrt(
							((r.x - mousex) * (r.x - mousex)) + ((r.y - mousey) * (r.y - mousey))) < (r.size / 2)) {
						robotarena.robots.remove(i);
						flag = true;
					}
				}
				if (!flag)
					robotarena.addRobot(type, mousex, mousey);
				drawSystem();
			}
		});

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				drawSystem();
			}
		});

	}

	/**
	 * Creates a number of buttons which have different functions. Not all buttons
	 * have to be returned so some redundant or inactive buttons coding can stay
	 * here.
	 * 
	 * @return the buttons that have been created in this section
	 */
	private HBox mainmenu() {

		// Button Initialisation

		Button menu = new Button("Menu");
		Button info = new Button("Info");

		Button start = new Button("Start");
		Button stop = new Button("Stop");
		Button clear = new Button("Clear");

		Button save = new Button("Save");
		Button load = new Button("Load");

		// Robot Buttons

		Button roamdot = new Button("RoamDot");
		Button spinner = new Button("Spinner");
		Button wallrunner = new Button("WallRunner");
		Button wallphobicbot = new Button("WhiskerRobot");
		Button lightbot = new Button("LightBot");

		Button counter = new Button("Counter");
		Button light = new Button("Light");

		// Menu Frame

		JFrame menubox = new JFrame("Menu");

		menubox.setPreferredSize(new Dimension(500, 200));
		menubox.setDefaultCloseOperation(2);
		menubox.setLocation(200, 200);
		menubox.setAlwaysOnTop(true);
		toggledebug.setPreferredSize(new Dimension(300, 50));

		menubox.add(new java.awt.Label("Info : Tim's Robot Arena Program"));

		menubox.add(toggledebug);
		menubox.pack();

		// Button Handlers

		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\n\n: Showing Menu");
				menubox.setVisible(true);
			}
		});

		info.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\n\n: Displaying Info");
				JOptionPane.showMessageDialog(null, "Tim's Robot Arena Program");
			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\n\n: Starting Arena");
				timer.start();
			}
		});

		stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\n\n: Stopping Arena");
				timer.stop();
			}
		});
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\n\n: Clearing Arena");
				robotarena.robots.clear();
				drawSystem();
			}
		});

		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				char c = '§';
				PrintWriter savewriter;
				String dir = System.getProperty("user.dir") + "/data/";
				File file = new File(dir);
				if (!file.exists())
					file.mkdir();
				String filename;
				String outputfile = "";
				for (int i = 0; i < robotarena.robots.size(); i++)
					outputfile += robotarena.robots.get(i).robotInfo(c);
				System.out.println("\n\n: Saving File to directory " + dir);
				filename = "" + JOptionPane.showInputDialog("Enter File Name") + ".robotdata";
				System.out.println(": File " + filename);
				if (!(filename.equals("null.robotdata") || filename.equals(".robotdata")))
					file = new File(dir + filename);
				if (file.exists())
					file.delete();
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println(": Necessary files have been created.");
				try {
					savewriter = new PrintWriter(dir + filename);
					savewriter.write(outputfile);
					savewriter.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println(
						": File written\n\n--- Start of File ---\n\n" + outputfile + "\n\n---  End of File  ---");

			}
		});

		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				char c = '§';
				robotarena.robots.clear();
				int numlines = -1;
				String dir = System.getProperty("user.dir") + "/data/";
				File file = new File(dir);
				if (!file.exists())
					file.mkdir();
				String filename = "";
				filename = JOptionPane.showInputDialog("Open which file?") + ".robotdata";
				file = new File(dir + filename);
				if (!file.exists() || filename == "" || filename == null)
					JOptionPane.showMessageDialog(null, "THE FILE DOES NOT EXIST");
				else {
					BufferedReader reader;
					String inputstring = "";
					try {
						reader = new BufferedReader(new FileReader(dir + filename));
						String line = "";
						while (line != null) {
							numlines++;
							line = reader.readLine();
							inputstring += line + "\n";
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();

					} catch (IOException e) {
						e.printStackTrace();
					}
					String currentrobot = "";
					String temp = "";
					ArrayList<Double> params;
					int split;
					for (int i = 0; i < numlines; i++) {
						split = splitByChar('\n', inputstring);
						currentrobot = inputstring.substring(0, split - 1);
						inputstring = inputstring.substring(split, inputstring.length());
						params = new ArrayList<Double>();
						while (currentrobot.length() > 0 && currentrobot != null) {
							split = splitByChar(c, currentrobot);
							temp = currentrobot.substring(0, split - 1);
							currentrobot = currentrobot.substring(split, currentrobot.length());
							params.add(Double.parseDouble(temp));
						}
						System.out.println("x : " + params.get(1) + "\ny : " + params.get(2) + "\nm : " + params.get(3)
								+ "\nv : " + params.get(4) + "\nd : " + params.get(5));
						double[] paramsarray = new double[params.size()];
						for (int j = 0; j < params.size() - 1; j++)
							paramsarray[j] = params.get(j + 1);
						robotarena.addRobotArray(params.get(0).intValue(), paramsarray);
					}
				}
			}
		});

		roamdot.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (type == 1)
					robotarena.addRobot(1, canvaswidth / 2, canvasheight / 2);
				type = 1;
			}
		});

		spinner.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == 2)
					robotarena.addRobot(2, canvaswidth / 2, canvasheight / 2);
				type = 2;
			}
		});
		wallrunner.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == 3)
					robotarena.addRobot(3, canvaswidth / 2, canvasheight / 2);
				type = 3;
			}
		});
		wallphobicbot.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == 4)
					robotarena.addRobot(4, canvaswidth / 2, canvasheight / 2);
				type = 4;
			}
		});

		lightbot.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == 5)
					robotarena.addRobot(5, canvaswidth / 2, canvasheight / 2);
				type = 5;
			}
		});

		counter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == -1)
					robotarena.addRobot(-1, canvaswidth / 2, canvasheight / 2);
				type = -1;
			}
		});

		light.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (type == -2)
					robotarena.addRobot(-2, canvaswidth / 2, canvasheight / 2);
				type = -2;
			}
		});

		// Add the buttons to the button box, REMEMBER TO ADD THEM HERE
		HBox buttons = new HBox(new Label("UI : "), info, menu, start, stop, clear, new Label("Files : "), save, load,
				new Label("Robots : "), roamdot, spinner, wallrunner, wallphobicbot, lightbot, counter, light);
		buttons.setSpacing(10);
		return buttons;
	}

	/**
	 * 
	 * @param c
	 *            is the character to split at
	 * @param split
	 *            is the string which will be split
	 * @return an integer value based on where the next split is
	 */
	int splitByChar(char c, String split) {
		int count = 0;
		while (count < split.length()) {
			if (split.charAt(count++) == c)
				return count;
		}
		return 0;
	}
}
