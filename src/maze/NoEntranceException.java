package maze;

import maze.InvalidMazeException;

/**
 * Exception to be thrown when maze contains no entrances
 */
public class NoEntranceException extends InvalidMazeException {
  public NoEntranceException(String error) {
    super(error);
  }
}
