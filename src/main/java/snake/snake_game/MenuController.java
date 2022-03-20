package snake.snake_game;

import javafx.fxml.FXML;

public class MenuController
{
    @FXML
    private void switchToNewGame()
    {
        SceneController.switchTo("newgame");
        Sound.play("select");
    }

    @FXML
    private void switchToLeaderboard()
    {
        SceneController.switchTo("leaderboard");
        Sound.play("select");
    }
}