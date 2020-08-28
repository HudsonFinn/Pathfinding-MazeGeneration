package tests.dev;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;

public class CoordinateTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Class setupForClassMembers() {
        try {
            Class cls = Class.forName("maze.Maze");
            for (Class innerClass: cls.getDeclaredClasses()) {
                if (innerClass.getName().equals("maze.Maze$Coordinate")) {
                    return innerClass;
                }
            }
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Maze");
        }

        fail("ClassNotFoundException: maze.Maze$Coordinate");
        return null;
    }

    public Object setupForInstanceMembers(int x, int y) {
        Class cls = setupForClassMembers();
        try {
              Constructor constructor = cls.getDeclaredConstructor(int.class, int.class);
              assertTrue(Modifier.isPublic(constructor.getModifiers()));
              if (Modifier.isStatic(constructor.getModifiers())) {
                  return constructor.newInstance(x, y);
              } else {
                  Maze maze = setupForMaze1();
                  return constructor.newInstance(maze, x, y);
              }
        } catch (
            NoSuchMethodException | SecurityException | InvocationTargetException |
            InstantiationException | IllegalAccessException e
        ) {
            fail(e.getClass().getName() + ": maze.Maze$Coordinate(int, int)");
        }
        return null;
    }

    public Maze setupForMaze1() {
        Maze rtn = null;
        try {
            rtn = Maze.fromTxt("../mazes/maze1.txt");
        } catch (FileNotFoundException e) { fail(); }
        return rtn;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureIntAttributeX() {
        Class cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("x");
            assertSame(typeAttribute.getType(), int.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: x");
        }
    }

    @Test
    public void ensureIntAttributeY() {
        Class cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("y");
            assertSame(typeAttribute.getType(), int.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: y");
        }
    }

    @Test
    public void ensurePublicMethodGetX() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getX");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getX()");
        }
    }

    @Test
    public void ensureGetXNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getX");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getX()");
        }
    }

    @Test
    public void ensureGetXReturnsInt() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getX");
            assertSame(method.getReturnType(), int.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getX(): int");
        }
    }

    @Test
    public void ensurePublicMethodGetY() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getY");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getY()");
        }
    }

    @Test
    public void ensureGetYNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getY");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getY()");
        }
    }

    @Test
    public void ensureGetYReturnsInt() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getY");
            assertSame(method.getReturnType(), int.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getY(): int");
        }
    }

}
