package tests;

import java.lang.reflect.Modifier;

import maze.Maze;

public class ModifierChecker {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public static void main(String args[]) {
        try {
            Class cls = Class.forName("maze.Maze");
            for (Class innerClass: cls.getDeclaredClasses()) {
                if (innerClass.getName().equals("maze.Maze$Coordinate")) {
                    System.out.println(Modifier.isStatic(
                        innerClass.getModifiers()
                    ));
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("error");
        }
    }

}
