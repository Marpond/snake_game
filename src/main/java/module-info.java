module snake.snake_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens snake.snake_game to javafx.fxml;
    exports snake.snake_game;
}