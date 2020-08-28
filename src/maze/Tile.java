package maze;

// Object serialization
import java.io.Serializable;

/**
 * Tile class represents an indiviaual tile in the maze
 * @version 10/04/2020
 * @author Finlay Hudson
 */
public class Tile implements java.io.Serializable {
  // Attributes
  private Type type;
  private char tileChar;

  /**
   * Constructor for tile which sets its type
   * @param tileType The type that the tile should be set as
   */
  private Tile(Type tileType) {
    type = tileType;
  }

  /**
   * Enum to represent the 4 different types of tile
   */
  public enum Type {
    CORRIDOR, ENTRANCE, EXIT, WALL
  }

  /**
   * Constructor for a tile from a given charecter can only be called from the
   * package
   * @param charecter The character which the tile should be constructed
   *                  represents the type of file it should be
   * @return Returns a tile with the correct type
   */
  protected static Tile fromChar(char charecter) {
    Tile tile;
    Type tileType = null;
    switch(charecter) {
      case '#':
        tileType = Type.WALL;
        break;
      case 'e':
        tileType = Type.ENTRANCE;
        break;
      case '.':
        tileType = Type.CORRIDOR;
        break;
      case 'x':
        tileType = Type.EXIT;
        break;
    }
    tile = new Tile(tileType);
    tile.tileChar = charecter;
    return tile;
  }

  /**
   * Gets the type of the tile
   * @return Returns the tiles type
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Checks if the tile is navigatable
   * @return Returns a boolean value representing if the tile is navigatable
   */
  public boolean isNavigable() {
    if (this.type == type.WALL) {
      return false;
    }
    return true;
  }

  /**
   * Overrides the toString to in-built method to output the character as a
   * string representing the tile
   */
  @Override
  public String toString() {
    String tileString = String.valueOf(this.tileChar);
    return tileString;
  }
}
