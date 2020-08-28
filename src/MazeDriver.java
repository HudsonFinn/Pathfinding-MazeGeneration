import maze.Maze;
import maze.Tile;
import maze.routing.RouteFinder;
import java.io.FileNotFoundException;

import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class MazeDriver {
  public static void main(String[] args) {
    try {
      Maze file = Maze.fromTxt("../mazes/maze2.txt");
      System.out.println(file.toString());
      RouteFinder finder = new RouteFinder(file);
      while (!(finder.step())) {
        System.out.println(file.getTileLocation(finder.peek()));
      }
      System.out.println(finder.toString());
    } catch (FileNotFoundException e){

    }
  }
}
