package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food
{
    private final Rectangle RECTANGLE;
    private final Random RANDOM = new Random();
    private FoodType foodType;

    public Food(double x, double y, AnchorPane anchorPane)
    {
        this.RECTANGLE = new Rectangle(x,y,GameController.ENTITY_SIZE,GameController.ENTITY_SIZE);
        anchorPane.getChildren().add(this.RECTANGLE);
    }

    public Rectangle getRECTANGLE()
    {
        return this.RECTANGLE;
    }

    public FoodType getFoodType()
    {
        return this.foodType;
    }

    public void move(AnchorPane anchorPane)
    {
        this.RECTANGLE.setLayoutX(RANDOM.nextInt((int) (anchorPane.getPrefWidth()/GameController.ENTITY_SIZE)) * GameController.ENTITY_SIZE);
        this.RECTANGLE.setLayoutY(RANDOM.nextInt((int) (anchorPane.getPrefHeight()/GameController.ENTITY_SIZE)) * GameController.ENTITY_SIZE);
        this.foodType = randomType();
        setGraphics();
    }

    private FoodType randomType()
    {
        int x = RANDOM.nextInt(FoodType.class.getEnumConstants().length);
        return FoodType.class.getEnumConstants()[x];
    }
    
    private void setGraphics()
    {
        switch (this.foodType)
        {
            // TODO: Different graphics for each food
            case NORMAL -> GameController.setImage(this.RECTANGLE,"src/main/java/snake/snake_game/images/normalfood.png");
            case SPEED -> GameController.setImage(this.RECTANGLE,"src/main/java/snake/snake_game/images/speedfood.png");
            case SIZE -> GameController.setImage(this.RECTANGLE,"src/main/java/snake/snake_game/images/normalfood.png");
        }
    }
}
