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
    @FXML
    Text scoreText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        scoreText.setText(String.valueOf(GameController.score));
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
