package maze;

import maze.InvalidMazeException;

/**
 * Exception to be thrown when maze contains multiple exits
 */
public class MultipleExitException extends InvalidMazeException {
  public MultipleExitException(String error) {
    super(error);
  }
}
