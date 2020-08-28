package tests.dev;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;

public class ExceptionTest {

    @Test
    public void ensureMultipleEntranceExceptionIsInvalidMazeException() {
        assertTrue(InvalidMazeException.class.isAssignableFrom(
            MultipleEntranceException.class
        ));
    }

    @Test
    public void ensureMultipleExitExceptionIsInvalidMazeException() {
        assertTrue(InvalidMazeException.class.isAssignableFrom(
            MultipleExitException.class
        ));
    }

    @Test
    public void ensureNoEntranceExceptionIsInvalidMazeException() {
        assertTrue(InvalidMazeException.class.isAssignableFrom(
            NoEntranceException.class
        ));
    }

    @Test
    public void ensureNoExitExceptionIsInvalidMazeException() {
        assertTrue(InvalidMazeException.class.isAssignableFrom(
            NoExitException.class
        ));
    }

}
