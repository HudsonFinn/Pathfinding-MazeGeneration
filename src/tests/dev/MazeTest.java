// Version 1.2, Friday 17th April @ 9:10pm
package tests.dev;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;
import maze.Tile;

public class MazeTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public boolean checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(Maze maze) {
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();

        // Modify tiles to contain another entrance tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newEntrance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (InvocationTargetException e) {
            assertSame(e.getCause().getClass(), MultipleEntranceException.class);
            return true; // return true only if MultipleEntranceException thrown
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setEntrance");
        }
        return false; // no MultipleEntranceException thrown
    }

    public boolean checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(Maze maze) {
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();

        // Modify tiles to contain another exit tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newExit);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (InvocationTargetException e) {
            assertSame(e.getCause().getClass(), MultipleExitException.class);
            return true; // return true only if MultipleExitException thrown
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setExit");
        }
        return false; // no MultipleExitException thrown
    }

    public Tile fromChar(char c) {
        try {
            Class cls = Class.forName("maze.Tile");
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            method.setAccessible(true);
            return (Tile)method.invoke(null, c);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": Tile.fromChar");
        } catch (InvocationTargetException e) {
          fail(
              e.getClass().getName() + ": Tile.fromChar\nCause: " +
              e.getCause().getClass() + ": " + e.getCause().getMessage()
          );
        }
        return null;
    }

    public Class setupForClassMembers() {
        try {
            return Class.forName("maze.Maze");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Maze");
        }
        return null;
    }

    public Maze setupForMaze1() {
        Maze rtn = null;
        try {
            rtn = Maze.fromTxt("../mazes/maze1.txt");
        } catch (Exception e) { fail(); }
        return rtn;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureEntranceAttributeIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Field entranceAttribute = cls.getDeclaredField("entrance");
            assertSame(entranceAttribute.getType(), Class.forName("maze.Tile"));
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: entrance");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
    }

    @Test
    public void ensureExitAttributeIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Field exitAttribute = cls.getDeclaredField("exit");
            assertSame(exitAttribute.getType(), Class.forName("maze.Tile"));
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: exit");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
    }

    @Test
    public void ensureTilesAttributeIsListType() {
        Class cls = setupForClassMembers();
        try {
    	      Field tilesAttribute = cls.getDeclaredField("tiles");
            assertSame(tilesAttribute.getType(), List.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: tiles");
        }
    }

    @Test
    public void ensureOnlyPrivateConstructors() {
        Class cls = setupForClassMembers();
        Constructor[] allConstructors = cls.getDeclaredConstructors();
        for (Constructor constructor : allConstructors) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
        assertEquals(cls.getConstructors().length, 0);
    }

    @Test
    public void ensurePrivateConstructorNoArguments() {
        Class cls = setupForClassMembers();
        try {
    	      Constructor constructor = cls.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("No constructor with signature: Maze()");
        }
    }

    @Test
    public void ensureFromTxtReturnsMaze() {
        boolean declared = false;
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromTxt", String.class);
            assertSame(method.getReturnType(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromTxt(String)");
        }
    }

    @Test
    public void ensurePublicMethodGetEntrance() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getEntrance");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getEntrance()");
        }
    }

    @Test
    public void ensureGetEntranceNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getEntrance");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getEntrance()");
        }
    }

    @Test
    public void ensureGetEntranceReturnsTile() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getEntrance");
            assertSame(method.getReturnType(), Tile.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getEntrance()");
        }
    }

    @Test
    public void ensurePublicMethodGetExit() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getExit");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getExit()");
        }
    }

    @Test
    public void ensureGetExitNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getExit");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getExit()");
        }
    }

    @Test
    public void ensureGetExitReturnsTile() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getExit");
            assertSame(method.getReturnType(), Tile.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getExit()");
        }
    }

    @Test
    public void ensurePublicMethodGetTiles() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getTiles");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getTiles()");
        }
    }

    @Test
    public void ensureGetTilesNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getTiles");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getTiles()");
        }
    }

    @Test
    public void ensurePrivateMethodSetEntrance() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            assertTrue(Modifier.isPrivate(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setEntrance()");
        }
    }

    @Test
    public void ensureSetEntranceReturnsVoid() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            assertSame(method.getReturnType(), Void.TYPE);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setEntrance()");
        }
    }

    @Test
    public void ensurePrivateMethodSetExit() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            assertTrue(Modifier.isPrivate(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setExit()");
        }
    }

    @Test
    public void ensureSetExitReturnsVoid() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            assertSame(method.getReturnType(), Void.TYPE);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setExit()");
        }
    }

    @Test
    public void ensurePublicMethodToString() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringReturnsString() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
            assertSame(method.getReturnType(), String.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString()");
        }
    }


    // ~~~~~~~~~~ Functionality tests ~~~~~~~~~~

    @Test
    public void ensureFromTxt() {
        setupForMaze1();
    }

    @Test
    public void ensureFromTxtWithInvalidCharThrowsInvalidMazeException() {
        try {
            Maze.fromTxt("../mazes/invalid/invalidChar.txt");
        } catch (
            NoEntranceException | NoExitException |
            MultipleEntranceException | MultipleExitException |
            RaggedMazeException e
        ) {
            fail();
        } catch (InvalidMazeException e) {
            // OK
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void ensureFromTxtWithRaggedMazeThrowsRaggedMazeException() {
        Exception exception = assertThrows(RaggedMazeException.class, () -> {
            Maze.fromTxt("../mazes/invalid/ragged.txt");
        });
    }

    @Test
    public void ensureFromTxtWithNoEntranceThrowsNoEntranceException() {
        Exception exception = assertThrows(NoEntranceException.class, () -> {
            Maze.fromTxt("../mazes/invalid/noEntrance.txt");
        });
    }

    @Test
    public void ensureFromTxtWithNoExitThrowsNoExitException() {
        Exception exception = assertThrows(NoExitException.class, () -> {
            Maze.fromTxt("../mazes/invalid/noExit.txt");
        });
    }

    @Test
    public void ensureGetEntranceIsEntranceType() {
        Maze maze = setupForMaze1();
        assertSame(maze.getEntrance().getType(), Tile.Type.ENTRANCE);
    }

    @Test
    public void ensureGetExitIsExitType() {
        Maze maze = setupForMaze1();
        assertSame(maze.getExit().getType(), Tile.Type.EXIT);
    }

    @Test
    public void ensureGetTilesReturns2DList() {
        Maze maze = setupForMaze1();
        List<List<Tile>> tiles = maze.getTiles();
    }

    @Test
    public void ensureGetTilesReturnsCorrectDimensions() {
        Maze maze = setupForMaze1();
        List<List<Tile>> tiles = maze.getTiles();
        assertSame(tiles.size(), 6);
        for (List<Tile> row : tiles) { assertSame(row.size(), 6); }
    }

    @Test
    public void ensureSetEntranceOK() {
        Maze maze = setupForMaze1();
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;

        // Modify entrance to be null
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            entranceAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        // Modify tiles to contain another entrance tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newEntrance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (
            NoSuchMethodException | IllegalAccessException e
        ) {
            fail(e.getClass().getName() + ": setEntrance");
        } catch (InvocationTargetException e) {
            fail(
                e.getClass().getName() + ": setEntrance\nCause: " +
                e.getCause().getClass() + ": " + e.getCause().getMessage()
            );
        }

        // Check the new value of entrance
        try {
            assertSame((Tile)entranceAttribute.get(maze), newEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }
    }

    @Test
    public void ensureSetEntranceFailsIfEntranceNotEmpty() {
        Maze maze = setupForMaze1();
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;
        Tile origEntrance = null;

        // Save original entrance state
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            origEntrance = (Tile)entranceAttribute.get(maze);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(maze);

        // Compare the old and new values of entrance
        try {
            assertEquals((Tile)entranceAttribute.get(maze), origEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

    }

    @Test
    public void ensureSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty() {
        Maze maze = setupForMaze1();
        assertTrue(checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(maze));
    }

    @Test
    public void ensureSetEntranceFailsIfTileNotInMaze() {
        Maze maze = setupForMaze1();
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;

        // Modify entrance to be null
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            entranceAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setEntrance");

        // If an exception is thrown in the setEntrance method, it must be an
        // IllegalArgumentException (or something that extends IllegalArgumentException).
        } catch (InvocationTargetException e) {
            assertTrue(
                IllegalArgumentException.class.isAssignableFrom(e.getCause().getClass())
            );
        }

        // Compare the new value of entrance, make sure that setEntrance()
        // has had no effect.
        try {
            assertNotSame((Tile)entranceAttribute.get(maze), newEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }
    }

    @Test
    public void ensureSetExitOK() {
        Maze maze = setupForMaze1();
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();
        Field exitAttribute = null;

        // Modify exit to be null
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            exitAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        // Modify tiles to contain another exit tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newExit);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (
            NoSuchMethodException | IllegalAccessException e
        ) {
            fail(e.getClass().getName() + ": setExit");
        } catch (InvocationTargetException e) {
            fail(
                e.getClass().getName() + ": setExit\nCause: " +
                e.getCause().getClass() + ": " + e.getCause().getMessage()
            );
        }

        // Check the new value of exit
        try {
            assertSame((Tile)exitAttribute.get(maze), newExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }
    }

    @Test
    public void ensureSetExitFailsIfExitNotEmpty() {
        Maze maze = setupForMaze1();
        Class cls = setupForClassMembers();
        Field exitAttribute = null;
        Tile origExit = null;

        // Save original exit state
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            origExit = (Tile)exitAttribute.get(maze);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(maze);

        // Compare the old and new values of exit
        try {
            assertEquals((Tile)exitAttribute.get(maze), origExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

    }

    @Test
    public void ensureSetExitThrowsMultipleExitExceptionIfExitNotEmpty() {
        Maze maze = setupForMaze1();
        assertTrue(checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(maze));
    }

    @Test
    public void ensureSetExitFailsIfTileNotInMaze() {
        Maze maze = setupForMaze1();
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();
        Field exitAttribute = null;

        // Modify exit to be null
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            exitAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setExit");

        // If an exception is thrown in the setExit method, it must be an
        // IllegalArgumentException (or something that extends IllegalArgumentException).
        } catch (InvocationTargetException e) {
            assertTrue(
                IllegalArgumentException.class.isAssignableFrom(e.getCause().getClass())
            );
        }

        // Compare the new value of exit, make sure that setExit()
        // has had no effect.
        try {
            assertNotSame((Tile)exitAttribute.get(maze), newExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }
    }

    @Test
    public void ensureToStringMeetsMinimumDimensions() {
        Maze maze = setupForMaze1();
        String[] lines = maze.toString().split("\r\n|\r|\n");
        Set<Integer> set = new HashSet<Integer>();
        assertTrue(lines.length >= 6);
        for (int i=0; i < lines.length; i++) {
            if (lines[i].length() != 0) set.add(lines[i].length());
        }
        List<Integer> lst = set.stream().collect(Collectors.toList());
        assertTrue(set.size() <= 2);
        assertTrue(lst.get(0).intValue() >= 6);
    }

}
