package tests.dev;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;

public class DirectionTest {

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~
    
    @Test
    public void ensureEnum() {
        for (Class cls: Maze.class.getDeclaredClasses()) {
            if (cls.getName().equals("maze.Maze$Direction")) {
                assertTrue(cls.isEnum());
                return;
            }
        }
        fail();
    }

}
