package snake.snake_game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class SceneController
{
    public static void switchTo(String fxml)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxml+".fxml")));
            Main.stage.setScene(new Scene(root));
            root.requestFocus();
        }
        catch (IOException e){e.printStackTrace();}
    }
}
