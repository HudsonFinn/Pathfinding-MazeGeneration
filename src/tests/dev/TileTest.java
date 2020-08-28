package tests.dev;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Tile;

public class TileTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Object constructTypeObject(String typeValue) {
        Class cls = setupForClassMembers();
        try {
    	      Constructor constructor = cls.getDeclaredConstructor(
                setupForInnerClassMembers()
            );
            constructor.setAccessible(true);
            return constructor.newInstance(Tile.Type.valueOf(typeValue));
        } catch (
            NoSuchMethodException | InstantiationException |
            IllegalAccessException | InvocationTargetException e
        ) {
            fail(e.getClass().getName() + ": Tile(Type)");
        }
        return null;
    }

    public Tile fromChar(char c) {
        try {
            Method method = setupForFromChar();
            return (Tile)method.invoke(null, c);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e.getClass().getName() + ": Tile.fromChar");
        }
        return null;
    }

    public Class setupForClassMembers() {
        try {
            return Class.forName("maze.Tile");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
        return null;
    }

    public Method setupForFromChar() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException: Tile.fromChar");
        }
        return null;
    }

    public Class setupForInnerClassMembers() {
        Class cls = setupForClassMembers();
        for (Class innerClass: cls.getDeclaredClasses()) {
            if (innerClass.getName().equals("maze.Tile$Type")) {
                return innerClass;
            }
        }
        fail("ClassNotFoundException: maze.Tile$Type");
        return null;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureTypeAttributeIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("type");
            assertSame(typeAttribute.getType(), setupForInnerClassMembers());
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: type");
        }
    }

    @Test
    public void ensureConstructorArgumentIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Constructor constructor = cls.getDeclaredConstructor(
                setupForInnerClassMembers()
            );
        } catch (NoSuchMethodException e) {
            fail("No constructor with signature: Tile(Type)");
        }
    }

    @Test
    public void ensureFromCharArgumentIsChar() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromChar(char)");
        }
    }

    @Test
    public void ensureFromCharReturnsTile() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            assertSame(method.getReturnType(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromChar(char): Tile");
        }
    }

    @Test
    public void ensurePublicMethodIsNavigable() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("isNavigable");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: isNavigable()");
        }
    }

    @Test
    public void ensureIsNavigableNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("isNavigable");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: isNavigable()");
        }
    }

    @Test
    public void ensureIsNavigableReturnsBoolean() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("isNavigable");
            assertSame(method.getReturnType(), boolean.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: isNavigable(): boolean");
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
            fail("No method with signature: toString(): String");
        }
    }


    // ~~~~~~~~~~ Functionality tests ~~~~~~~~~~

    @Test
    public void ensureConstructorEntrance() {
        constructTypeObject("ENTRANCE");
    }

    @Test
    public void ensureConstructorWall() {
        constructTypeObject("WALL");
    }

    @Test
    public void ensureFromCharCorridorType() {
        Tile tile = fromChar('.');
        assertEquals(tile.getType(), Tile.Type.valueOf("CORRIDOR"));
    }

    @Test
    public void ensureFromCharEntranceType() {
        Tile tile = fromChar('e');
        assertEquals(tile.getType(), Tile.Type.valueOf("ENTRANCE"));
    }

    @Test
    public void ensureFromCharExitType() {
        Tile tile = fromChar('x');
        assertEquals(tile.getType(), Tile.Type.valueOf("EXIT"));
    }

    @Test
    public void ensureFromCharWallType() {
        Tile tile = fromChar('#');
        assertEquals(tile.getType(), Tile.Type.valueOf("WALL"));
    }

    @Test
    public void ensureGetTypeReturnsTypeVariable() {
        Tile tile = fromChar('#');
        Class cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("type");
            typeAttribute.setAccessible(true);
            assertSame(typeAttribute.get(tile), tile.getType());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": type");
        }
    }

    @Test
    public void ensureCorridorNavigable() {
        Tile tile = fromChar('.');
        assertTrue(tile.isNavigable());
    }

    @Test
    public void ensureEntranceNavigable() {
        Tile tile = fromChar('e');
        assertTrue(tile.isNavigable());
    }

    @Test
    public void ensureExitNavigable() {
        Tile tile = fromChar('x');
        assertTrue(tile.isNavigable());
    }

    @Test
    public void ensureWallNotNavigable() {
        Tile tile = fromChar('#');
        assertFalse(tile.isNavigable());
    }

}
