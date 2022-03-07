package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food
{
    private final Rectangle food;
    private final Random random = new Random();

    public Food(double x, double y, AnchorPane anchorPane)
    {
        food = new Rectangle(x,y,GameController.entitySize,GameController.entitySize);
        GameController.setImage(food,"src/main/java/snake/snake_game/images/apple.png");
        anchorPane.getChildren().add(food);
    }

    public Rectangle getRectangle()
    {
        return food;
    }

    public void move(AnchorPane anchorPane)
    {
        food.setLayoutX(random.nextInt((int) (anchorPane.getPrefWidth()/GameController.entitySize)) * GameController.entitySize);
        food.setLayoutY(random.nextInt((int) (anchorPane.getPrefHeight()/GameController.entitySize)) * GameController.entitySize);
    }
}
