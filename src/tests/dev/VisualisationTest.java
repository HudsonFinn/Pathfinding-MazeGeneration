// Version 1.1, Thursday 2nd April @ 2:40pm
package tests.dev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class VisualisationTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~
    private static Path findVisualisationDir() {
        String[] spellingVariants = {
            "visualisation", "vizualisation", "visualization", "vizualization",
            "visualisations", "vizualisations", "visualizations", "vizualizations"
        };
        for (int i = 0; i < spellingVariants.length; ++i) {
            Path dir = Paths.get(
                String.format("maze/%s", spellingVariants[i])
            ).toAbsolutePath();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                return dir;
            } catch (IOException e){}
        }
        return null;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureVisualisationPackage() {
        assertNotEquals(findVisualisationDir(), null);
    }

}
