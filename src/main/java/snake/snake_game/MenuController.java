package snake.snake_game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class MenuController
{
    public void switchToUsername() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("username.fxml")));
        Main.stage.setScene(new Scene(root));
    }

    public void switchToLeaderboard() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("leaderboard.fxml")));
        Main.stage.setScene(new Scene(root));
    }
}