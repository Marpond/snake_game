package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food
{
    private final Rectangle rectangle;
    private final Random random = new Random();
    private FoodType foodType;

    public Food(double x, double y, AnchorPane anchorPane)
    {
        this.rectangle = new Rectangle(x,y,GameController.entitySize,GameController.entitySize);
        anchorPane.getChildren().add(this.rectangle);
    }

    public Rectangle getRectangle()
    {
        return this.rectangle;
    }

    public FoodType getFoodType()
    {
        return this.foodType;
    }

    public void move(AnchorPane anchorPane)
    {
        this.rectangle.setLayoutX(random.nextInt((int) (anchorPane.getPrefWidth()/GameController.entitySize)) * GameController.entitySize);
        this.rectangle.setLayoutY(random.nextInt((int) (anchorPane.getPrefHeight()/GameController.entitySize)) * GameController.entitySize);
        this.foodType = randomType();
        setGraphics();
    }

    private FoodType randomType()
    {
        int x = random.nextInt(FoodType.class.getEnumConstants().length);
        return FoodType.class.getEnumConstants()[x];
    }
    
    private void setGraphics()
    {
        switch (this.foodType)
        {
            // TODO: Different graphics for each food
            case NORMAL -> GameController.setImage(this.rectangle,"src/main/java/snake/snake_game/images/normalfood.png");
            case SPEED -> GameController.setImage(this.rectangle,"src/main/java/snake/snake_game/images/speedfood.png");
            case SIZE -> GameController.setImage(this.rectangle,"src/main/java/snake/snake_game/images/normalfood.png");
        }
    }
}
