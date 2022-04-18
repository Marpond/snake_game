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

/**
 * Class used to sort the leaderboard values by descending scores
 */
class sortByScoreDescend implements Comparator<Leaderboard> {
    public int compare(Leaderboard a, Leaderboard b) {
        return b.score() - a.score();
    }
}

public class LeaderboardController implements Initializable {
    private final Scanner SCANNER = new Scanner(Leaderboard.FILE);

    @FXML
    private TableView<Leaderboard> table;
    @FXML
    private TableColumn<Leaderboard, String> date;
    @FXML
    private TableColumn<Leaderboard, String> name;
    @FXML
    private TableColumn<Leaderboard, Integer> score;

    private final ObservableList<Leaderboard> LIST = FXCollections.observableArrayList();

    public LeaderboardController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load leaderboard
        while (SCANNER.hasNext() && !SCANNER.next().isEmpty()) {
            String[] data = SCANNER.nextLine().split(",");
            LIST.add(new Leaderboard(data[0], data[1], Integer.parseInt(data[2])));
        }
        // Bind stuff
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        // Sort list with method
        LIST.sort(new sortByScoreDescend());

        table.setItems(LIST);

        // Make the leaderboard untouchable
        for (TableColumn tableColumn : table.getColumns()) {
            tableColumn.setResizable(false);
            tableColumn.setSortable(false);
            tableColumn.setEditable(false);
            tableColumn.setReorderable(false);
        }
    }

    @FXML
    private void switchToMenu() {
        SceneController.switchTo("menu");
        SoundController.play("select");
    }
}
