package snake.snake_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TxtHandler {
    private String directory;
    private String line = null;

    void getText() {
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(this.directory);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line);
            }

            // Always close files.
            bufferedReader.close();
        } catch (IOException ignored) {}
    }
}
