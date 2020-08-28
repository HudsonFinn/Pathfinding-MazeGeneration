package maze;

//Input, output, file handling and utils
import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Serializable;

//Exception handling
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

/**
 * Class to construct and represent the maze
 * @version 10/04/2020
 * @author Finlay Hudson
 */
public class Maze implements java.io.Serializable {

  // attributes
  private Tile entrance;
  private Tile exit;
  private List<List<Tile>> tiles;
  private String originFile;

  /**
   * Overrides the constructor making it private to stop it from being called
   * outside the object. Called by fromRecursiveDevision and fromTxt.
   */
  private Maze() {
  }

  /**
   * Attempts to set the entrance for the maze
   * @param tile The tile to be set as the entrance
   * @throws IllegalArgumentException Throws illegal argument if the
   * entance given is not in the maze.
   * @throws MultipleEntranceException If there is an entrance already set
   */
  private void setEntrance(Tile tile) {
    if (this.getEntrance() == null && tile.toString().equals("e")) {
      if (this.getTileLocation(tile) != null) {
        this.entrance = tile;
      } else {
        throw new IllegalArgumentException("Entrance not in maze");
      }
    } else {
      throw new MultipleEntranceException("Multiple Entrances");
    }
  }

  /**
   * Attempts to set the exit for the maze.
   * @param tile The tile to be set as the exit.
   * @throws IllegalArgumentException Throws illegal argument if the
   * exit given is not in the maze.
   * @throws MultipleExitException If there is an exit already set.
   */
  private void setExit(Tile tile) {
    if (this.getExit() == null && tile.toString().equals("x")) {
      if (this.getTileLocation(tile) != null) {
        this.exit = tile;
      } else {
        throw new IllegalArgumentException("Exit not in maze");
      }
    } else {
      throw new MultipleExitException("Multiple Exits");
    }
  }

  /**
   * Gets the entrance tile for the maze
   * @return A tile representing the entrance of the maze.
   */
  public Tile getEntrance() {
    return this.entrance;
  }

  /**
   * Gets the exit tile for the maze
   * @return A tile representing the exit of the maze.
   */
  public Tile getExit() {
    return this.exit;
  }

  /**
   * Gets the list of all tiles contained in the maze.
   * @return a 2d list of tiles which are contained in the maze
   */
  public List<List<Tile>> getTiles() {
    return this.tiles;
  }

  /**
   * Enum to represent the 4 different directions that a the route can travel
   */
  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }


  /**
   * Gets the tile in the given direction relative to the given tiles position
   * @param originTile The base tile used for return tiles postion
   * @param dir The enum direction for the offset from the origin tile
   * @return Returns the tile adjacent to the origin tile in a given direction
   *         if its an invalid coord (out of bounds) it will return null.
   */
  public Tile getAdjacentTile(Tile originTile, Direction dir) {
    //x and y are the coords of the return tile
    int x;
    int y;
    Maze.Coordinate originCoords = this.getTileLocation(originTile);
    // Initialises them to the origin coords location
    x = originCoords.getX();
    y = originCoords.getY();
    //Depending which direction, changes the x and y to match
    switch(dir) {
      case NORTH:
        y = y + 1;
        break;
      case SOUTH:
        y = y - 1;
        break;
      case EAST:
        x = x + 1;
        break;
      case WEST:
        x = x - 1;
        break;
      }
    //Ensures there are no out of bounds errors
    if ((y < 0) | (y >= tiles.size())) {
      return null;
    }
    if ((x < 0) | (x >= tiles.get(0).size())) {
      return null;
    }
    /* Creates the coord for the target location and gets the tile at that
    location */
    Maze.Coordinate targetCoord = new Maze.Coordinate(x, y);
    Tile targetTile = this.getTileAtLocation(targetCoord);
    return targetTile;
  }

  /**
   * Gets the coordinates of a passed tile
   * @param targetTile The tile that you want to find the coords for.
   * @return Returns a Coordinate initialised to the tiles x and y.
   *         Returns null if tile not in maze.
   */
  public Maze.Coordinate getTileLocation(Tile targetTile) {
    //Loops through the entire maze.
    for (int i = 0; i < this.tiles.size(); i++) {
      for (int j = 0; j < this.tiles.get(i).size(); j++) {
        // Checks if the current loop tile is the same as the target tile
        if (targetTile == this.tiles.get(i).get(j)){
          // Returns a tile with coords x and y (y changed to index backwards)
          return new Coordinate(j, this.tiles.size() - (i+1));
        }
      }
    }
    return null;
  }

  /**
   * Gets the tile at a given Coordinates location
   * @param targetCoord The target coord with x and y initialised for the
   *                    location
   * @return Returns a tile a at the location of a given Coordinate
   */
  public Tile getTileAtLocation(Maze.Coordinate targetCoord) {
    Tile tileAtLocation;
    try {
      tileAtLocation = (tiles.get(tiles.size()
        - (targetCoord.getY()+1)).get(targetCoord.getX()));
    } catch (NullPointerException e) {
      return null;
    }
    return tileAtLocation;
  }

  /**
   * Creates a maze by reading in a txt file and converting the charecters
   * to a 2d list of tiles.
   * @param file A string containing the full path of the file to be converted.
   * @return Returns a maze object containing a list of tiles representing the
   *         maze, a set entrance and a set exit.
   * @throws InvalidMazeException When there is an invalid character in maze.
   * @throws MultipleEntranceException When there are multiple entances.
   * @throws MultipleExitException When there are multiple exits.
   * @throws RaggedMazeException When row or column lenght are uneven.
   * @throws NoExitException When no exit is found.
   * @throws NoEntranceException When no entrance is found.
   * @throws FileNotFoundException When the file string is invalid.
   */
  public static Maze fromTxt (String file) throws FileNotFoundException {
    String mazeRow;
    Tile tempTile = null;
    Maze newMaze = new Maze();
    Tile tempEntrance = null;
    Tile tempExit = null;
    try (
      // Reads in file
      FileReader mazeFile = new FileReader(file);
      BufferedReader mazeStream = new BufferedReader(mazeFile);
      ) {
        // Reads first line
        mazeRow = mazeStream.readLine();
        // Constructs 2d tile container
        List<List<Tile>> tiles = new ArrayList<List<Tile>>(mazeRow.length());
        // Loops as long as the line is not empty
        while (mazeRow != null) {
          // Constructs a 1d array for the line
          List<Tile> RowOfTiles = new ArrayList<Tile>(mazeRow.length());
          // Loops through individual line
          for (int i = 0; i < mazeRow.length(); i++) {
            // Single tile
            tempTile = Tile.fromChar(mazeRow.charAt(i));
            //Check if invalid char
            if (mazeRow.charAt(i) != 'e' && mazeRow.charAt(i) != 'x' && mazeRow.charAt(i) != '.' && mazeRow.charAt(i) != '#') {
              throw new InvalidMazeException("Invalid Character");
            }
            // Adds to the line and checks what type of tile it is
            RowOfTiles.add(i, (tempTile));
            if (mazeRow.charAt(i) == 'e') {
              if (tempEntrance == null) {
                tempEntrance = tempTile;
              } else {
                throw new MultipleEntranceException("Multiple Entrances");
              }
            }
            if (mazeRow.charAt(i) == 'x') {
              if (tempExit == null) {
                tempExit = tempTile;
              } else {
                throw new MultipleExitException("Multiple Exits");
              }
            }
          }
          // Reads next line
          mazeRow = mazeStream.readLine();
          // Adds 1d array to 2d array (Adds row to list of rows)
          tiles.add(RowOfTiles);
          // Checks if previous row is of same length as next row
          if (mazeRow != null) {
            if (mazeRow.length() != RowOfTiles.size()) {
              throw new RaggedMazeException("Variable length");
            }
          }
        }
        newMaze.tiles = tiles;
        // Ensures both exit and entrance were set
        if (tempExit != null) {
          try {
            newMaze.setExit(tempExit);
          } catch (MultipleExitException MulExit) {
            throw new MultipleExitException("Multiple Exits");
          } catch (IllegalArgumentException InvalidExit) {
            System.out.println("Invalid Exit");
          }
        } else {
          throw new NoExitException("No Exit");
        }
        if (tempEntrance != null) {
          try {
            newMaze.setEntrance(tempEntrance);
          } catch (MultipleEntranceException MulEntrance) {
            throw new MultipleEntranceException("Multiple Entrances");
          } catch (IllegalArgumentException InvalidEntrance) {
            System.out.println("Invalid Entrance");
          }
        } else {
          throw new NoEntranceException("No Entance");
        }
      }
      catch (IOException e) {
        throw new FileNotFoundException("Not a valid maze");
      }
    return newMaze;
  }

  /**
   * Custom maze builder which uses recursive division to construct a random
   * maze.
   * @param widthMaze The width of the required maze
   * @param heightMaze The height of the required maze
   * @return Returns the constructed maze.
   */
  public static Maze fromRecursiveDevision(int widthMaze, int heightMaze) {
    Maze newMaze = new Maze();
    Random random = new Random();
    //Constructs an emptyMaze of all entrances
    List<List<Tile>> tiles = new ArrayList<List<Tile>>(widthMaze);
    for (int j = 0; j < heightMaze; j++) {
      List<Tile> RowOfTiles = new ArrayList<Tile>(widthMaze);
      for (int i = 0; i < widthMaze; i++) {
        Tile tempTile = Tile.fromChar('e');
        RowOfTiles.add(i, (tempTile));
      }
      tiles.add(RowOfTiles);
    }
    newMaze.tiles = tiles;

    // Starts recursive function to break down maze and create walls
    newMaze = newMaze.divide(newMaze, 0, 0, widthMaze, heightMaze);

    //  Creates an entrance at the coord (0, 0)
    Tile emptyTile = Tile.fromChar('e');
    newMaze.getTiles().get(0).set(0, emptyTile);
    newMaze.setEntrance(newMaze.getTileAtLocation(
                  newMaze.new Coordinate(0, heightMaze - 1)));
    // Creates exit at (width of maze, height of maze)
    emptyTile = Tile.fromChar('x');
    newMaze.getTiles().get(heightMaze-1).set(widthMaze-1, emptyTile);
    newMaze.setExit(newMaze.getTileAtLocation(
                  newMaze.new Coordinate(widthMaze - 1, 0)));

    return newMaze;
  }

  /**
   * Recursive fucntion which divides the maze into two parts places a wall
   * down the centre and then calls itself on the to parts it creates until
   * there is a gap of 2 inbetween (1 creates adjacent walls)
   * @param mazeToSplit The maze that needs to be broken down
   * @param x The leftmost x value of the section of maze to be split
   * @param y The uppermost y value of the section of maze to be split
   * @param width The width of the section to be split
   * @param height The height of the section to be split
   * @return Returns the maze split maze.
   */
  public static Maze divide(Maze mazeToSplit, int x, int y, int width, int height) {
    // If the space to place is 3 or less it cannot place a split so just returns
    if (width < 3 || height < 3) {
      return mazeToSplit;
    }
    // Picks the corrent orientation to split it
    String orientation = chooseOrientation(width, height);
    if (orientation.equals("Vertical")) {
      Random random = new Random();

      // Chooses where to split the maze
      // Can be changed a set divider for less randomness
      int splitX = random.nextInt(width-2) + 1;
      //int splitX = (width/3);
      //splitX = splitX + x;

      // Chooses where to place the opening for this wall
      // Could also be changed for a set place along all walls
      int openingY = random.nextInt(height);
      openingY = openingY + y;
      // Changes tiles to walls except for the opening and any other openings
      for (int i = y; i < y + height; i++) {
        if (i != openingY && !mazeToSplit.getTiles().get(i).get(
                                        splitX + x).toString().equals(".")) {
          Tile emptyTile = Tile.fromChar('#');
          mazeToSplit.getTiles().get(i).set(splitX + x, emptyTile);
        } else {
          // Sets opening to an empty tile a the two parallel adjacent tiles.
          Tile emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(i).set(splitX + x, emptyTile);
          emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(i).set(splitX + x + 1, emptyTile);
          emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(i).set(splitX + x - 1, emptyTile);
        }
      }
      // Calls itself on the section to the right and the left of the split
      mazeToSplit = divide(mazeToSplit, x, y, splitX, height);
      mazeToSplit = mazeToSplit.divide(
                mazeToSplit, x + splitX + 1, y, width - splitX - 1, height);
    } else if (orientation.equals("Horizontal")) {
      Random random = new Random();
      int splitY = random.nextInt(height - 2) + 1;
      //int splitY = (width/3);
      //splitY = splitY;
      int openingX = random.nextInt(width);
      openingX = openingX + x;
      for (int i = x; i < x + width; i++) {
        if (i != openingX && !mazeToSplit.getTiles().get(
                                splitY + y).get(i).toString().equals(".")) {
          Tile emptyTile = Tile.fromChar('#');
          mazeToSplit.getTiles().get(splitY + y).set(i, emptyTile);
        } else {
          Tile emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(splitY + y).set(i, emptyTile);
          emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(splitY + y + 1).set(i, emptyTile);
          emptyTile = Tile.fromChar('.');
          mazeToSplit.getTiles().get(splitY + y - 1).set(i, emptyTile);
        }
      }
      mazeToSplit = divide(mazeToSplit, x, y, width, splitY);
      mazeToSplit = mazeToSplit.divide(
                mazeToSplit, x, y + splitY + 1, width, height - splitY - 1);
    }
    return mazeToSplit;
  }

  /**
   * Picks the orientation that will to split it perpendicular to its longest
   * dimension based on height and width
   * @param width The width of the section
   * @param height The height of the section
   * @return Returns the orientation default is "Vertical"
   */
  private static String chooseOrientation(int width, int height) {
    //If they are of equal length will pick vertically
    String returnDirection = "Vertical";
    if (width < height) {
      returnDirection = "Horizontal";
    } else if (height > width) {
      returnDirection = "Vertical";
    }
    return returnDirection;
  }

  /**
   * Overrides the default toString method to output the maze with numbered axis
   * @return Returns the maze as a string
   */
  @Override
  public String toString() {
    String output = "";
    for (int i = 0; i < tiles.size(); i++) {
      output = output + (tiles.size()- (i + 1)) + " ";
      for (int j = 0; j < tiles.get(i).size(); j++) {
        output = output + (tiles.get(i).get(j).toString()) + " ";
      }
      output = output + "\n";
    }
    output = output + "\n  ";
    for (int k = 0; k < tiles.get(0).size(); k++) {
      output = output + k + " ";
    }
    return output;
  }

  /**
   * Subclass to represent any coordinate in the maze
   * @version 04/04/2020
   */
  public class Coordinate {

    //attributes
    private int x;
    private int y;

    /**
     * Constructor sets the location of the coordinate
     * @param coordx coordinate x
     * @param coordy coordinate y
     */
    public Coordinate(int coordx, int coordy) {
      this.x = coordx;
      this.y = coordy;
    }

    /**
     * Gets the coordinates x value
     * @return Returns the coordinates current x location
     */
    public int getX() {
      return this.x;
    }

    /**
     * Gets the coordinates y value
     * @return Returns the coordinates current y location
     */
    public int getY() {
      return this.y;
    }

    /**
     * Overrides the implicit to string method and makes the coordinates
     * x and y value into a formated coordinate string.
     * @return Returns a string containing the formated x and y (x, y)
     */
    @Override
    public String toString() {
      String theString = ("(" + this.getX() + ", " + this.getY() + ")");
      return theString;
    }
  }
}
