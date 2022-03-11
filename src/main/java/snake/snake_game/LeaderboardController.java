package snake.snake_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Scanner;

class sortByScoreDescend implements Comparator<Leaderboard>
{
    public int compare(Leaderboard a, Leaderboard b)
    {
        return b.score() - a.score();
    }
}

public class LeaderboardController implements Initializable
{
    private final Scanner sc = new Scanner(Leaderboard.file);

    @FXML
    private TableView<Leaderboard> table;
    @FXML
    private TableColumn<Leaderboard, String> date;
    @FXML
    private TableColumn<Leaderboard, String> name;
    @FXML
    private TableColumn<Leaderboard, Integer> score;

    private final ObservableList<Leaderboard> list = FXCollections.observableArrayList();

    public LeaderboardController() throws IOException {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Load leaderboard
        while (sc.hasNext() && !sc.next().isEmpty())
        {
            String[] data = sc.nextLine().split(",");
            list.add(new Leaderboard(data[0],data[1],Integer.parseInt(data[2])));
        }
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        list.sort(new sortByScoreDescend());
        table.setItems(list);
    }

    @FXML
    private void switchToMenu()
    {
        SceneController.switchTo("menu");
    }
}
