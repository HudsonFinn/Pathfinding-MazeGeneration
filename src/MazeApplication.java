// Package Imports
import maze.Maze;
import maze.Tile;
import maze.visualisation.MazeVisualiser;
import maze.routing.RouteFinder;
import maze.routing.NoRouteFoundException;
import maze.Maze.Coordinate;
// Exception imports
import java.io.IOException;
import java.util.*;
// FileHandling
import java.io.File;
import java.io.FileInputStream;
// Core JavaFX
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
// Layout
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
// Elements
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//Events
import javafx.scene.input.MouseEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
//Input Elements
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * The application class which creates a GUI and controls the flow and calls to
 * the other classes
 */
public class MazeApplication extends Application {
  // Attributes
  private Maze maze;
  private MazeVisualiser visualiser;
  private RouteFinder route;
  private int width = 650;
  private int height = 750;
  private GridPane grid;
  private Scene dialogScene;

  /**
   * Inital method for the application, sets up all variables, loads an inital
   * maze and sets up event listeners to allow the application to be interactive
   */
  @Override
  public void start(Stage stage) {
    // Creates HBoxes to contain the main 3 sections of the GUI
    HBox top = new HBox();
    top.setAlignment(Pos.TOP_LEFT);
    HBox mid = new HBox();
    mid.setAlignment(Pos.CENTER);
    HBox bottom = new HBox();
    bottom.setAlignment(Pos.CENTER);

    // Loads the logo image into the top container
    Image image = null;
    try {
      image = new Image(new FileInputStream("./maze/visualisation/MazeLogo.jpg"));
    } catch (IOException NoImage) {
      System.out.println("Please readd the logo file");
    }
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(50);
    imageView.setFitWidth(50);

    // Loads the 4 buttons into the top container
    Button loadFile = new Button("Load Maze");
    Button genMaze = new Button("Generate Maze");
    Button loadSavedMaze = new Button("Load Route");
    Button SaveRoute = new Button("Save Route");
    top.getChildren().addAll(imageView, genMaze, loadFile, loadSavedMaze, SaveRoute);
    top.setId("NavBar");

    // Creates the grid which will contain the visualised maze
    GridPane grid = new GridPane();
    visualiser = new MazeVisualiser();
    try {
      maze = Maze.fromTxt("../mazes/maze3.txt");
      route = new RouteFinder(maze);
      grid.setHgap(3);
      grid.setVgap(3);
      mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
      mid.setId("MazeContainer");
    } catch (IOException t){
      System.out.println("Invalid maze");
    }

    // Creates the step button and adds it to the bottom container
    Button step = new Button("Step");
    bottom.getChildren().addAll(step);

    // Creates the master VBox which contains the top, middle and bottom HBoxes
    VBox master = new VBox();
    master.getChildren().addAll(top,mid,bottom);
    master.setId("WholeContainer");

    // Creates the scene for the application and adds a stylesheet
    Scene scene = new Scene(master, width, height, Color.BLUE);
    scene.getStylesheets().add("./maze/visualisation/stylesheet.css");

    // EVENTS

    /* The step event, triggered when the step button is clicked. Finds the next
    tile in the maze and visualises that change. */
    step.setOnAction(e -> {
      // If the route isn't finnished step and update the visualisation.
      if (!(route.isFinished())) {
        try {
          this.route.step();
        } catch (NoRouteFoundException NoRoute) {
          System.out.println("No route avaliable");
        }
        mid.getChildren().clear();
        mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
      // If the route is done bring up a dialog to sumarise the route and tiles.
      } else {
        System.out.println("Finnished");
        final Stage finnished = new Stage();
        finnished.initModality(Modality.APPLICATION_MODAL);
        finnished.initOwner(stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        Label text = new Label(String.format("Finnished! It took %s steps!", route.getRoute().size()));
        Button submit = new Button("Confirm");
        dialogVbox.getChildren().addAll(text, submit);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        // Close the dialog when the user clocks the submit button
        submit.setOnAction((z) -> {
          finnished.close();
        });
        finnished.setScene(dialogScene);
        finnished.show();
      }
    });

    /* Save route event, opens a file chooser dialog and then saves the current
    route and maze to the selected file */
    SaveRoute.setOnAction(saveRoute -> {
      System.out.println("Saved");
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Object Files", "*.obj")
        );
      fileChooser.setInitialDirectory(new File("../routes"));
      String selectedFileName = fileChooser.showSaveDialog(stage).toString();
      System.out.println(selectedFileName);
      try {
        route.save(selectedFileName);
      } catch (IOException e) {
        System.out.println("Invalid File");
      }
    });

    /* Load route event, opens a file chooser dialog and then loads the chosen
    file updating the route and maze attributes and the visualisation. */
    loadSavedMaze.setOnAction(loadRoute -> {
      FileChooser fileChooser = new FileChooser();
      String selectedFileName = null;
      fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Object Files", "*.obj")
        );
      fileChooser.setInitialDirectory(new File("../routes"));
      try {
        selectedFileName = fileChooser.showOpenDialog(stage).toString();
        System.out.println(selectedFileName);
        this.route = RouteFinder.load(selectedFileName);
        this.maze = route.getMaze();
        System.out.println(this.route.toString());
        mid.getChildren().clear();
        mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
      } catch (NullPointerException NoSelected) {
        System.out.println("No maze");
      }
    });

    /* Generate maze event, Creates an empty maze and visualises it */
    genMaze.setOnAction(e -> {
      Random random = new Random();
      long startTime = System.currentTimeMillis();
      maze = Maze.fromRecursiveDevision(random.nextInt(80)+20, random.nextInt(80)+20);
      //maze = Maze.fromRecursiveDevision(1000, 1000);
      route = new RouteFinder(maze);
      long endTime = System.currentTimeMillis();
      System.out.println("Took "+(endTime - startTime) + " ms to create maze");
      startTime = System.currentTimeMillis();
      if (!(route.isFinished())) {
          mid.getChildren().clear();
          mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
      }
      endTime = System.currentTimeMillis();
      System.out.println("Took "+(endTime - startTime) + " ms to visualise");
      System.out.println("Finnished");
    });

    /* Load maze event, Creates an empty maze from a file and visualises it */
    loadFile.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
      fileChooser.setInitialDirectory(new File("../mazes"));
      try {
        String selectedFileName = fileChooser.showOpenDialog(stage).toString();
        maze = Maze.fromTxt(selectedFileName);
        route = new RouteFinder(maze);
        if (!(route.isFinished())) {
          mid.getChildren().clear();
          mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
        }
      } catch (IOException t){
        System.out.println("Invalid maze");
      } catch (RuntimeException x) {
        System.out.println("Invalid maze for reason: " + x.getMessage());
      }
    });

    /* The step event, triggered when the "D" key is pressed. Finds the next
    tile in the maze and visualises that change. */
    scene.setOnKeyPressed((KeyEvent key) -> {
      if (key.getCode() == KeyCode.D) {
        if (!(route.isFinished())) {
          try {
            this.route.step();
          } catch (NoRouteFoundException NoRoute) {
            System.out.println("No route avaliable");
          }
          mid.getChildren().clear();
          mid.getChildren().addAll(visualiser.UpdateGrid(maze, route, width, height));
        } else {
          System.out.println("Finnished");
          final Stage finnished = new Stage();
          finnished.initModality(Modality.APPLICATION_MODAL);
          finnished.initOwner(stage);
          VBox dialogVbox = new VBox(20);
          dialogVbox.setAlignment(Pos.CENTER);
          Label text = new Label(String.format("Finnished! The route takes %s steps! \n %d tiles were checked!", route.getRoute().size(), route.getChecked().size()));
          Button submit = new Button("Confirm");
          dialogVbox.getChildren().addAll(text, submit);
          Scene dialogScene = new Scene(dialogVbox, 300, 200);
          submit.setOnAction((z) -> {
            finnished.close();
          });
          finnished.setScene(dialogScene);
          finnished.show();
        }
      }
    });

    // Sets the scene and the title for the application and then shows the scene
    stage.setScene(scene);
    stage.setTitle("Maze");
    stage.show();
  }

  /**
   * The main method for the application. Launches the JavaFX application.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
