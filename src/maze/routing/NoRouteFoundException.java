package maze.routing;

/**
 * Exception to be thrown when the route finder cannot find a route
 */
public class NoRouteFoundException extends RuntimeException {
  public NoRouteFoundException(String error) {
    super(error);
  }
}
