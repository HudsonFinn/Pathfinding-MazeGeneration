package maze;

/**
 * The base exception class for all maze related exceptions.
 */ 
public class InvalidMazeException extends RuntimeException {
  public InvalidMazeException(String error) {
    super(error);
  }
}
