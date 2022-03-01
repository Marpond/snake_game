package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food {
    private final Rectangle rectangle;
    private final Random random = new Random();
    private final int entitySize;

    public Food(double x, double y, AnchorPane pane, double entitySize)
    {
        this.entitySize = (int) entitySize;
        Color color = Color.RED;
        rectangle = new Rectangle(x,y,entitySize,entitySize);
        rectangle.setFill(color);
        pane.getChildren().add(rectangle);
    }

    public Rectangle getPosition()
    {
        return rectangle;
    }

    public void move()
    {
        int gridSize = 12;
        int positionX = random.nextInt(gridSize);
        int positionY = random.nextInt(gridSize);
        rectangle.setLayoutX(positionX * entitySize);
        rectangle.setLayoutY(positionY * entitySize);
    }
}
