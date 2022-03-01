package snake.snake_game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class SceneController
{

    public void switchToGame() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        Main.stage.setScene(new Scene(root));
    }

    public void switchToLeaderboard() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("leaderboard.fxml")));
        Main.stage.setScene(new Scene(root));
    }
}