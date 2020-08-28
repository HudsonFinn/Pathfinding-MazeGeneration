package maze.visualisation;

// Packaged imports
import maze.Maze.Coordinate;
import maze.Maze;
import maze.Tile;
import maze.routing.RouteFinder;

// Javafx imports
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Class to create a javafx GridPane to represent the current maze state
 * @version 10/04/2020
 * @author Finlay Hudson
 */
public class MazeVisualiser {
  // Attributes
  private Maze maze;
  private RouteFinder route;

  /**
   * Empty constructor method
   */
  public MazeVisualiser() {
  }

  /**
   * Method to construct the gridpane to represent the maze state
   * @param maze The maze to be represented
   * @param route The maze to be represented
   * @param width The maze to be represented
   * @param height The maze to be represented
   * @return Returns a GripPane element containing Rectangles to represent tiles
   */
  public GridPane UpdateGrid(Maze maze, RouteFinder route, int width, int height) {
    // Sets width and gap between rectangles
    width = width - 40;
    GridPane grid = new GridPane();
    int nodeSize;
    grid.setHgap(3);
    grid.setVgap(3);

    // Sets size to fit on screen
    if (maze.getTiles().size() >= maze.getTiles().get(0).size()) {
      nodeSize = ((width-(3*maze.getTiles().size()-3))/maze.getTiles().size());
    } else {
      nodeSize = ((width-(3*maze.getTiles().get(0).size() -3))/maze.getTiles().get(0).size());
    }

    // Loops through maze and sets rectangle colour based on the type of tile
    for (int j = 0; j < maze.getTiles().size(); j++) {
      for (int i = 0; i < maze.getTiles().get(j).size(); i++) {
        if (maze.getTiles().get(j).get(i).toString().equals("#")) {
          grid.add(new Rectangle(0, 0, nodeSize, nodeSize), i, j, 1, 1);
        }else if (route.search(route.getRouteStack(), maze.getTileAtLocation(maze.new Coordinate(i, maze.getTiles().size() - (j+1)))) != -1) {
          grid.add(new Rectangle(nodeSize, nodeSize, Color.web("#5b5996", 1.0)), i, j, 1, 1);
        }else if (route.search(route.getChecked(), maze.getTileAtLocation(maze.new Coordinate(i, maze.getTiles().size() - (j+1)))) != -1) {
          grid.add(new Rectangle(nodeSize, nodeSize, Color.web("#af4745", 1.0)), i, j, 1, 1);
        } else if (maze.getTiles().get(j).get(i).toString().equals("x")) {
          grid.add(new Rectangle(nodeSize, nodeSize, Color.web("#4b45af", 1.0)), i, j, 1, 1);
        } else {
          grid.add(new Rectangle(nodeSize, nodeSize, Color.WHITE), i, j, 1, 1);
        }
      }
    }
    return grid;
  }
}
