// Version 1.1, Tuesday 7th April @ 2:40pm
package tests.dev;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;
import maze.Maze.Coordinate;
import maze.Tile;

public class MazeCoordinateNotStaticTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Maze setupForMaze1() {
        Maze rtn = null;
        try {
            rtn = Maze.fromTxt("../mazes/maze1.txt");
        } catch (Exception e) { fail(); }
        return rtn;
    }


    // ~~~~~~~~~~ Functionality tests : Coordinate ~~~~~~~~~~

    @Test
    public void ensureGetX() {
      Maze maze = setupForMaze1();
      Maze.Coordinate coords = maze.new Coordinate(1, 2);
      assertSame(coords.getX(), 1);
    }

    @Test
    public void ensureGetY() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(1, 2);
        assertSame(coords.getY(), 2);
    }

    @Test
    public void ensureToString() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(1, 2);
        assertEquals("(1, 2)", coords.toString());
    }


    // ~~~~~~~~~~ Functionality tests : Maze ~~~~~~~~~~

    @Test
    public void ensureAdjacentTileNorthFromCentre() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(2, 2);
        Tile tile = maze.getTileAtLocation(coords);
        assertSame(
            maze.getAdjacentTile(tile, Maze.Direction.NORTH).getType(),
            Tile.Type.WALL
        );
    }

    @Test
    public void ensureAdjacentTileEastFromCentre() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(2, 2);
        Tile tile = maze.getTileAtLocation(coords);
        assertSame(
            maze.getAdjacentTile(tile, Maze.Direction.EAST).getType(),
            Tile.Type.WALL
        );
    }

    @Test
    public void ensureAdjacentTileSouthFromCentre() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(2, 2);
        Tile tile = maze.getTileAtLocation(coords);
        assertSame(
            maze.getAdjacentTile(tile, Maze.Direction.SOUTH).getType(),
            Tile.Type.CORRIDOR
        );
    }

    @Test
    public void ensureAdjacentTileWestFromCentre() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(2, 2);
        Tile tile = maze.getTileAtLocation(coords);
        assertSame(
            maze.getAdjacentTile(tile, Maze.Direction.WEST).getType(),
            Tile.Type.CORRIDOR
        );
    }

    @Test
    public void ensureGetTileAtLocationTopLeft() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(0, 5);
        assertSame(
            maze.getTileAtLocation(coords).getType(),
            Tile.Type.ENTRANCE
        );
    }

    @Test
    public void ensureGetTileAtLocationTopRight() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(5, 5);
        assertSame(
            maze.getTileAtLocation(coords).getType(),
            Tile.Type.WALL
        );
    }

    @Test
    public void ensureGetTileAtLocationBottomLeft() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(0, 0);
        assertSame(
            maze.getTileAtLocation(coords).getType(),
            Tile.Type.WALL
        );
    }

    @Test
    public void ensureGetTileAtLocationBottomRight() {
        Maze maze = setupForMaze1();
        Maze.Coordinate coords = maze.new Coordinate(5, 0);
        assertSame(
            maze.getTileAtLocation(coords).getType(),
            Tile.Type.WALL
        );
    }

    @Test
    public void ensureGetTileLocationOKForTopLeft() {
        Maze maze = setupForMaze1();
        Maze.Coordinate orig = maze.new Coordinate(0, 5);
        Maze.Coordinate result = maze.getTileLocation(maze.getTileAtLocation(orig));
        assertEquals(orig.getX(), result.getX());
        assertEquals(orig.getY(), result.getY());

    }

    @Test
    public void ensureGetTileLocationOKForTopRight() {
        Maze maze = setupForMaze1();
        Maze.Coordinate orig = maze.new Coordinate(5, 5);
        Maze.Coordinate result = maze.getTileLocation(maze.getTileAtLocation(orig));
        assertEquals(orig.getX(), result.getX());
        assertEquals(orig.getY(), result.getY());

    }

    @Test
    public void ensureGetTileLocationOKForBottomLeft() {
        Maze maze = setupForMaze1();
        Maze.Coordinate orig = maze.new Coordinate(0, 0);
        Maze.Coordinate result = maze.getTileLocation(maze.getTileAtLocation(orig));
        assertEquals(orig.getX(), result.getX());
        assertEquals(orig.getY(), result.getY());

    }

    @Test
    public void ensureGetTileLocationOKForBottomRight() {
        Maze maze = setupForMaze1();
        Maze.Coordinate orig = maze.new Coordinate(5, 0);
        Maze.Coordinate result = maze.getTileLocation(maze.getTileAtLocation(orig));
        assertEquals(orig.getX(), result.getX());
        assertEquals(orig.getY(), result.getY());
    }

}
