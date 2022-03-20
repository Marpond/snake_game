module snake.snake_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens snake.snake_game to javafx.fxml;
    exports snake.snake_game;
}