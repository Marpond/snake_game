package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGameController implements Initializable
{
    @FXML
    private AnchorPane ngAnchorPane;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        GameController.entitySize = 50;
        Food.wantSpeed = false;
        Food.wantSize = false;
        GameController.isCursed = false;
        GameController.wantObstacles = false;
        startBtn.setDisable(true);
        setListeners(ngAnchorPane);
    }

    @FXML
    private void switchToGame()
    {
        SceneController.switchTo("game");
        SoundController.play("start");
        GameController.currentUsername = usernameTextField.getText();
    }

    @FXML
    private void switchToMenu()
    {
        SceneController.switchTo("menu");
        SoundController.play("select");
    }

    /**
     * Sets multiple listeners inside the scene
     * @param anchorPane the listened items are on
     */
    private void setListeners(AnchorPane anchorPane)
    {
        // Username text-field
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            SoundController.play("type");
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
                setTextFill(validUsernameText,"Invalid username",Color.RED);
            }
            else
            {
                setTextFill(validUsernameText,"OK",Color.LIMEGREEN);
            }
        });
        // Entity size text-field
        entitySizeTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            SoundController.play("type");
            try
            {
                // If it's empty
                if (newValue.isEmpty())
                {
                    // Reset entitySize to default value
                    GameController.entitySize = 50;

                    setTextFill(validEntitySizeText,"OK",Color.LIMEGREEN);
                    startBtn.setDisable(!validUsernameText.getText().equals("OK"));
                }
                // If it can divide the anchor-pane-size and is less than 200
                else if (anchorPane.getPrefWidth()%Integer.parseInt(newValue)==0 && Integer.parseInt(newValue) <= 200)
                {
                    GameController.entitySize = Integer.parseInt(newValue);

                    setTextFill(validEntitySizeText,"OK",Color.LIMEGREEN);
                    startBtn.setDisable(!validUsernameText.getText().equals("OK"));
                }
                else
                {
                    setTextFill(validEntitySizeText,"Invalid entity size",Color.RED);
                    startBtn.setDisable(true);
                }
            }
            // If it's not an integer
            catch (Exception e)
            {
                setTextFill(validEntitySizeText,"Invalid entity size",Color.RED);
                startBtn.setDisable(true);
            }
        });
        // Listeners for checkboxes
        speedCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            Food.wantSpeed = newValue;
            SoundController.play("select");
        });
        sizeCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            Food.wantSize = newValue;
            SoundController.play("select");
        });
        obstacleCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            GameController.wantObstacles = newValue;
            SoundController.play("select");
        });
        cursedCheck.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            GameController.isCursed = newValue;
            SoundController.play("select");
        });
    }

    /**
     * Changes the message and color of a Text
     * @param text to change
     * @param message to prompt
     * @param color to change to
     */
    private void setTextFill(Text text, String message, Color color)
    {
        text.setText(message);
        text.setFill(color);
    }
}
