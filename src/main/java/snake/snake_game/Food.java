package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food
{
    private final Rectangle rectangle;
    private final Random random = new Random();

    public Food(double x, double y, AnchorPane anchorPane)
    {
        Color color = Color.RED;
        rectangle = new Rectangle(x,y,GameController.entitySize,GameController.entitySize);
        rectangle.setFill(color);
        anchorPane.getChildren().add(rectangle);
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public void move()
    {
        int positionX = random.nextInt(GameController.gridSize);
        int positionY = random.nextInt(GameController.gridSize);
        rectangle.setLayoutX(positionX * GameController.entitySize);
        rectangle.setLayoutY(positionY * GameController.entitySize);
    }


}
