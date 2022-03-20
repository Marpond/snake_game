package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Obstacle
{
    private final Rectangle RECTANGLE;
    private final Random RANDOM = new Random();

    public Obstacle(double x, double y, AnchorPane anchorPane, ArrayList<Rectangle> snakeBody, Food food, ArrayList<Rectangle> obstacles)
    {
        this.RECTANGLE = new Rectangle(x,y,GameController.entitySize,GameController.entitySize);
        GameController.setImage(this.RECTANGLE,"src/main/java/snake/snake_game/images/obstacle.png");
        obstacles.add(this.RECTANGLE);
        anchorPane.getChildren().add(this.RECTANGLE);
        do
        {
            move(anchorPane);
        }
        while ((this.RECTANGLE.getLayoutX() == snakeBody.get(0).getLayoutX() &&     // If it's inside the snake
                this.RECTANGLE.getLayoutY() == snakeBody.get(0).getLayoutY())
                ||
                (this.RECTANGLE.getLayoutX() == food.getRECTANGLE().getLayoutX() && // If it's inside the food
                this.RECTANGLE.getLayoutY() == food.getRECTANGLE().getLayoutY())
                ||
                (this.RECTANGLE.getLayoutX()-snakeBody.get(0).getLayoutX() < 200) // If it's within 4 blocks relative to the head
                ||
                isStacked(this.RECTANGLE,obstacles)); // If it's inside an obstacle
    }

    private void move(AnchorPane anchorPane)
    {
        this.RECTANGLE.setLayoutX(RANDOM.nextInt((int) (anchorPane.getPrefWidth()/GameController.entitySize)) * GameController.entitySize);
        this.RECTANGLE.setLayoutY(RANDOM.nextInt((int) (anchorPane.getPrefHeight()/GameController.entitySize)) * GameController.entitySize);
    }

    private boolean isStacked(Rectangle obstacle,ArrayList<Rectangle> obstacles)
    {
        for (Rectangle rectangle:obstacles)
        {
            if (obstacle.getLayoutX()==rectangle.getLayoutX()&&
                obstacle.getLayoutY()==rectangle.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }
}
