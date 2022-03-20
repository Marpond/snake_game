package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGameController implements Initializable
{
    @FXML
    private CheckBox speedCheck;
    @FXML
    private CheckBox sizeCheck;
    @FXML
    private CheckBox obstacleCheck;
    @FXML
    private CheckBox cursedCheck;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Text validUsernameText;
    @FXML
    private Button startBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Food.wantSpeed = false;
        Food.wantSize = false;
        GameController.cursed = false;
        GameController.wantObstacles = false;
        startBtn.setDisable(true);
        setTextListener();
        setCheckListener();
    }

    @FXML
    private void switchToGame()
    {
        SceneController.switchTo("game");
        Sound.play("start");
        GameController.currentUsername = usernameTextField.getText();
    }

    @FXML
    private void switchToMenu()
    {
        SceneController.switchTo("menu");
        Sound.play("select");
    }

    private void setTextListener()
    {
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->
                {
                    Sound.play("type");
                    startBtn.setDisable(newValue.isEmpty() || usernameTextField.getText().length()>15);
                    if (newValue.isEmpty() || usernameTextField.getText().length()>15 || usernameTextField.getText().contains(","))
                    {
                        validUsernameText.setText("Invalid username");
                        validUsernameText.setFill(Color.RED);
                    }
                    else
                    {
                        validUsernameText.setText("OK");
                        validUsernameText.setFill(Color.LIMEGREEN);
                    }
                });
    }

    private void setCheckListener()
    {
        speedCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
            {
                Food.wantSpeed = newValue;
                Sound.play("select");
            });
        sizeCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
            {
                Food.wantSize = newValue;
                Sound.play("select");
            });
        obstacleCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
                {
                    GameController.wantObstacles = newValue;
                    Sound.play("select");
                });
        cursedCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
                {
                    GameController.cursed = newValue;
                    Sound.play("select");
                });
    }
}
