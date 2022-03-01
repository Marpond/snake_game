package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class UsernameController
{
    @FXML
    TextField usernameTextField;

    public void switchToGame() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        Main.stage.setScene(new Scene(root));

        

    }
}
