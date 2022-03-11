package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewGameController implements Initializable
{
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button startBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        startBtn.setDisable(true);
        setTextListener();
    }

    @FXML
    private void switchToGame() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        Main.stage.setScene(new Scene(root));
        root.requestFocus();

        GameController.currentUsername = usernameTextField.getText();
    }

    @FXML
    private void switchToMenu() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Main.stage.setScene(new Scene(root));
    }

    private void setTextListener()
    {
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                startBtn.setDisable(newValue.isEmpty() || usernameTextField.getText().length()>20));
    }
}
