package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Obstacle
{
    private final Rectangle rectangle;
    private final Random random = new Random();

    /**
     * Constructor
     * @param x position
     * @param y position
     * @param anchorPane to add it to
     * @param snakeBody body of Snake
     * @param food Food
     * @param obstacles Arraylist of already existing obstacles
     */
    public Obstacle(double x, double y, AnchorPane anchorPane, ArrayList<Rectangle> snakeBody, Food food, ArrayList<Rectangle> obstacles)
    {
        this.rectangle = new Rectangle(x,y,GameController.entitySize,GameController.entitySize);
        GameController.setImage(this.rectangle,"src/main/java/snake/snake_game/images/obstacle.png");
        anchorPane.getChildren().add(this.rectangle);
        // Set random position
        do {move(anchorPane);}
        while ((this.rectangle.getLayoutX() == snakeBody.get(0).getLayoutX() &&         // If it's inside the snake
                this.rectangle.getLayoutY() == snakeBody.get(0).getLayoutY()) ||
                (this.rectangle.getLayoutX() == food.getRECTANGLE().getLayoutX() &&     // If it's inside the food
                this.rectangle.getLayoutY() == food.getRECTANGLE().getLayoutY()) ||
                (this.rectangle.getLayoutX()-snakeBody.get(0).getLayoutX() < 200) ||    // If it's within 4 blocks relative to the head
                isStacked(this.rectangle,obstacles));                                   // If it's inside an obstacle
        obstacles.add(this.rectangle);
    }

    /**
     * Moves the rectangle to a random position
     * @param anchorPane the pane to move it on
     */
    private void move(AnchorPane anchorPane)
    {
        this.rectangle.setLayoutX(random.nextInt((int) (anchorPane.getPrefWidth()/GameController.entitySize)) * GameController.entitySize);
        this.rectangle.setLayoutY(random.nextInt((int) (anchorPane.getPrefHeight()/GameController.entitySize)) * GameController.entitySize);
    }

    /**
     * Checks if an obstacle is inside an already existing obstacle
     * @param obstacle to check
     * @param obstacles already existing ones
     * @return boolean value
     */
    private boolean isStacked(Rectangle obstacle,ArrayList<Rectangle> obstacles)
    {
        return obstacles.stream().anyMatch(rectangle ->
               obstacle.getLayoutX()==rectangle.getLayoutX() &&
               obstacle.getLayoutY()==rectangle.getLayoutY());
    }
}
