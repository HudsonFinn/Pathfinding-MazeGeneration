package maze;

import maze.InvalidMazeException;

/**
 * Exception to be thrown when maze contains no exits
 */
public class NoExitException extends InvalidMazeException {
  public NoExitException(String error) {
    super(error);
  }
}
