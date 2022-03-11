package snake.snake_game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GameOverController implements Initializable
{
    private final ArrayList<String> data = new ArrayList<>();
    @FXML
    Text scoreText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try (Scanner sc = new Scanner(new File("src/main/java/snake/snake_game/leaderboard.csv")))
        {
            while (sc.hasNextLine() && !sc.next().isEmpty())
            {
                data.add(sc.nextLine());
            }
        }
        catch (IOException e){e.printStackTrace();}
        scoreText.setText(data.get(data.size()-1).split(",")[2]);
    }

    @FXML
    private void switchToMenu() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    private void switchToGame() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        Main.stage.setScene(new Scene(root));
        root.requestFocus();
    }
}
