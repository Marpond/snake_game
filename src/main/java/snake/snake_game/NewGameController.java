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
    private TextField entitySizeTextField;
    @FXML
    private Text validUsernameText;
    @FXML
    private Text validEntitySizeText;
    @FXML
    private Button startBtn;

    private final int ANCHOR_PANE_SIZE = 600;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        GameController.entitySize = 50;
        Food.wantSpeed = false;
        Food.wantSize = false;
        GameController.isCursed = false;
        GameController.wantObstacles = false;
        startBtn.setDisable(true);
        setListeners();
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

    private void setListeners()
    {
        // Username text-field
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            Sound.play("type");
            // Disable or enable the button whether everything is correct
            startBtn.setDisable(newValue.isEmpty() ||
                                usernameTextField.getText().length()>15 ||
                                usernameTextField.getText().contains(",") ||
                                validEntitySizeText.getFill() == Color.RED);
            // If the input is invalid
            if (newValue.isEmpty() ||
                usernameTextField.getText().length()>15 ||
                usernameTextField.getText().contains(","))
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
        // Entity size text-field
        entitySizeTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            Sound.play("type");
            try
            {
                if (newValue.isEmpty())
                {
                    GameController.entitySize = 50;

                    validEntitySizeText.setText("OK");
                    validEntitySizeText.setFill(Color.LIMEGREEN);
                    startBtn.setDisable(!validUsernameText.getText().equals("OK"));
                }
                else if (ANCHOR_PANE_SIZE %Integer.parseInt(newValue)==0 && Integer.parseInt(newValue) <= 200)
                {
                    GameController.entitySize = Integer.parseInt(newValue);

                    validEntitySizeText.setText("OK");
                    validEntitySizeText.setFill(Color.LIMEGREEN);
                    startBtn.setDisable(!validUsernameText.getText().equals("OK"));
                }
                else
                {
                    validEntitySizeText.setText("Invalid entity size");
                    validEntitySizeText.setFill(Color.RED);
                    startBtn.setDisable(true);
                }
            }
            catch (Exception e)
            {
                validEntitySizeText.setText("Invalid entity size");
                validEntitySizeText.setFill(Color.RED);
                startBtn.setDisable(true);
            }
        });
        // Listeners for checkboxes
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
            GameController.isCursed = newValue;
            Sound.play("select");
        });
    }
}
