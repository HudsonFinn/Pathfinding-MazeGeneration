package maze;

import maze.InvalidMazeException;


/**
 * Exception to be thrown when the mazes rows or columns are uneven
 */
public class RaggedMazeException extends InvalidMazeException {
  public RaggedMazeException(String error) {
    super(error);
  }
}
