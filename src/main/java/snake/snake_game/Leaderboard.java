package snake.snake_game;

import java.io.File;

public record Leaderboard(String date, String name, int score) {
    public static final File file = new File("src/main/java/snake/snake_game/leaderboard.csv");

    public String getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }
}
