find . -name "*.class" -type f -delete

javac -d ./bin tests/ModifierChecker.java
isStatic=$(java -cp bin tests/ModifierChecker)

javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/CoordinateTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/DirectionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/ExceptionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/MazeTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/RouteFinderTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/TileTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/VisualisationTest.java

if [ "$isStatic" == "true" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/MazeCoordinateStaticTest.java
elif [ "$isStatic" == "false" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar ./tests/*/MazeCoordinateNotStaticTest.java
else
    echo "--------- ERROR ---------"
fi

java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests
