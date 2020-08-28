package maze;

import maze.InvalidMazeException;

/**
 * Exception to be thrown when maze contains multiple entrances
 */
public class MultipleEntranceException extends InvalidMazeException {
  public MultipleEntranceException(String error) {
    super(error);
  }
}
