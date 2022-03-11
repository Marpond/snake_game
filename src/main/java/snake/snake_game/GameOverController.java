package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GameOverController implements Initializable
{
    private final ArrayList<String> LEADERBOARD_DATA = new ArrayList<>();
    @FXML
    Text scoreText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try (Scanner sc = new Scanner(new File("src/main/java/snake/snake_game/leaderboard.csv")))
        {
            while (sc.hasNextLine() && !sc.next().isEmpty())
            {
                LEADERBOARD_DATA.add(sc.nextLine());
            }
        }
        catch (IOException e){e.printStackTrace();}
        scoreText.setText(LEADERBOARD_DATA.get(LEADERBOARD_DATA.size()-1).split(",")[2]);
    }

    @FXML
    private void switchToMenu()
    {
        SceneController.switchTo("menu");
    }

    @FXML
    private void switchToGame()
    {
        SceneController.switchTo("game");
    }
}
