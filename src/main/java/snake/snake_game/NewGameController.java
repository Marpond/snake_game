package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewGameController implements Initializable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private CheckBox cursedCheck;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button startBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        anchorPane.setBackground(new Background(new BackgroundImage(new Image(new File(
                "src/main/java/snake/snake_game/images/gridbg.png").toURI().toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        GameController.cursed = false;
        startBtn.setDisable(true);
        setTextListener();
        setCheckListener();
    }

    @FXML
    private void switchToGame()
    {
        SceneController.switchTo("game");

        GameController.currentUsername = usernameTextField.getText();
    }

    @FXML
    private void switchToMenu()
    {
        SceneController.switchTo("menu");
    }

    private void setTextListener()
    {
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                startBtn.setDisable(newValue.isEmpty() || usernameTextField.getText().length()>20));
    }

    private void setCheckListener()
    {
        cursedCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
                GameController.cursed = newValue);
    }
}
