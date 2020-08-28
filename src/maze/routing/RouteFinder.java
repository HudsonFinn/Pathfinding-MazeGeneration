package maze.routing;

// Utils
import java.util.*;

// Package imports
import maze.Maze;
import maze.Maze.Direction;
import maze.Maze.Coordinate;
import maze.Tile;
import maze.routing.NoRouteFoundException;

// Input and output imports
import java.io.IOException;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Route find contains the current route the checked tiles and the methods to
 * search the maze for an exit from the entrance
 */
public class RouteFinder implements java.io.Serializable {

  // Attributes
  private Maze maze;
  private Stack<Tile> route;
  private boolean finished;
  private Stack<Tile> checked;

  /**
   * Constructor to initalise the route and checked lists and set the maze
   * @param inputMaze The maze to be searched
   */
  public RouteFinder (Maze inputMaze) {
    maze = inputMaze;
    route = new Stack<Tile>();
    checked = new Stack<Tile>();
    route.push(maze.getEntrance());
    checked.push(maze.getEntrance());
  }

  /**
   * Gets the maze
   * @return Returns the maze being checked
   */
  public Maze getMaze() {
    return this.maze;
  }

  /**
   * Gets the current tiles in the route in a 1d list
   * @return Returns a list of all current tiles in the route
   */
  public List<Tile> getRoute() {
    return this.route;
  }

  /**
   * Gets the current tiles in the route in a stack format
   * @return Returns a stack containing all current tiles in the route
   */
  public Stack<Tile> getRouteStack() {
    return this.route;
  }

  /**
   * Gets all the tiles that have already been checked by the finder
   * @return Returns a stack of all the tiles that have already been checked
   */
  public Stack<Tile> getChecked() {
    return this.checked;
  }

  /**
   * Returns the status of the routefinder
   * @return Returns a boolean depending on if the maze has finnished
   */
  public boolean isFinished() {
    return this.finished;
  }

  /**
   * Represents a single step in the route finding proccess
   * @return Returns a boolean depending on if the solution has been found
   * @throws NoRouteFoundException Thrown if no route is found
   */
  public boolean step() {
    boolean found = false;

    // If the route is empty all tiles have been popped so no route is possible
    if (route.isEmpty()) {
      throw new NoRouteFoundException("No route");
    }
    /* Checks if the current end of the route is the exit if it is a route
    has been found. */
    if (route.peek() == maze.getExit()) {
      finished = true;
      return true;
    } else {
      // Loops through the maze to fine the next avaliable adjacent tile
      Tile currentTile = route.peek();
      Direction[] directions = Direction.values();
      for (int i = 0; i < directions.length; i++) {
        if (maze.getAdjacentTile(currentTile, directions[i]) != null) {
          if (maze.getAdjacentTile(currentTile, directions[i]).isNavigable()) {
            if (checked.search(maze.getAdjacentTile(
                            currentTile, directions[i])) == -1) {
              // Add chosen tile to the route and checked tiles
              route.push(maze.getAdjacentTile(currentTile, directions[i]));
              checked.push(maze.getAdjacentTile(currentTile, directions[i]));
              found = true;
              break;
            }
          }
        }
      }
      // If no avaliable tiles its a dead end so pop current tile
      if (found == false) {
        route.pop();
      }
    }
    return false;
  }

  /**
   * Saves the current route finder to a file
   * @param outputFile The name of the file the object will be saved to
   * @throws IOException Thrown if error with input file name
   */
  public void save(String outputFile) throws IOException {
    try {
      writeObject(outputFile);
    } catch (IOException e) {
      throw new IOException("Incorrect file");
    }
  }

  /**
   * Writes the object to a given file
   * @param file The file to be saved to
   * @throws IOException Thrown if there is an error with the file name
   */
  public void writeObject(String file) throws IOException {
    try (FileOutputStream FOS = new FileOutputStream(file)) {
      ObjectOutputStream OOS = new ObjectOutputStream(FOS);
      OOS.writeObject(this);
      OOS.close();
      FOS.close();
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Loads a route and maze from a file
   * @param inputFile The file which the route will be loaded from
   * @return Returns the loaded route
   */
  public static RouteFinder load(String inputFile) {
    FileInputStream FIS;
    ObjectInputStream OIS;
    try {
      FIS = new FileInputStream(inputFile);
      OIS = new ObjectInputStream(FIS);
      RouteFinder newRoute = (RouteFinder) OIS.readObject();
      return newRoute;
    } catch (IOException e) {
      System.out.println("Class not found");
      return null;
    } catch (ClassNotFoundException e) {
      System.out.println("Class not found");
      return null;
    }
  }

  /**
   * Checks if the given stack is empty
   * @param thisRoute The stack of tiles to be checked
   * @return Returns true if the stack is empty and false if it contains tiles
   */
  public boolean isEmpty(Stack<Tile> thisRoute) {
     if (thisRoute.empty()) {
       return true;
     } else {
       return false;
     }
   }

   /**
    * Removes and returns a tile from the top of the route
    * @return Returns the removed tile if it has left or null if empty
    */
   public Tile pop() {
     if (!isEmpty(route)) {
       Tile popValue = route.pop();
       return popValue;
    } else {
      return null;
      }
    }

  /**
   * Adds a tile to the top of the stack
   * @param value The tile to be added
   */
  public void push(Tile value) {
    route.push(value);
  }

  /**
   * Returns the tile at the top of the stack
   * @return Tile at top of stack
   */
  public Tile peek() {
    return route.peek();
  }

  /**
   * Finds the position of a tile in a stack
   * @param stack The stack to be searched
   * @param find The tile to be found
   * @return Returns the ingeger postion of the tile from the top of the stack
   *         or -1 if its not in the stack.
   */
  public int search(Stack<Tile> stack, Tile find) {
    return stack.search(find);
  }

  /**
   * Checks if a tile is in a stack
   * @param stack The stack to be searched
   * @param find The tile to be checked
   * @return Returns true if the tile is in the stack and false if not
   */
  public boolean exists(Stack<Tile> stack, Tile find) {
    int index =  stack.search(find);
    if (index == -1) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Converts the route and maze into a string. A maze with the current path
   * and route drawn through it
   * @return String representing the maze and current path through it
   */
  public String toString() {
    String output = "";
    List<List<Tile>> tiles = maze.getTiles();
    for (int i = 0; i < tiles.size(); i++) {
      output = output + (tiles.size()- (i + 1)) + " ";
      for (int j = 0; j < tiles.get(i).size(); j++) {
        if (this.search(this.getRouteStack(), maze.getTileAtLocation(
                        maze.new Coordinate(j, tiles.size() - (i+1)))) != -1) {
          output = output + "*" + " ";
        } else if (this.search(this.getChecked(), maze.getTileAtLocation(
                        maze.new Coordinate(j, tiles.size() - (i+1)))) != -1) {
          output = output + "-" + " ";
        } else {
          output = output + (tiles.get(i).get(j).toString()) + " ";
        }
      }
      output = output + "\n";
    }
    output = output + "\n  ";
    for (int k = 0; k < tiles.get(0).size(); k++) {
      output = output + k + " ";
    }
    return output;
  }
}
