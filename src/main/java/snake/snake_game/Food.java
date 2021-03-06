package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Food {
    // Boolean values for different food types
    public static boolean wantSpeed;
    public static boolean wantSize;

    private final Rectangle RECTANGLE;
    private final Random RANDOM = new Random();
    private final ArrayList<FoodType> FOOD_TYPES = new ArrayList<>();

    private FoodType foodType;

    /**
     * Constructor
     *
     * @param anchorPane to add it to
     */
    public Food(AnchorPane anchorPane) {
        // Create new rectangle
        this.RECTANGLE = new Rectangle(0, 0, GameController.entitySize, GameController.entitySize);
        anchorPane.getChildren().add(this.RECTANGLE);
        // Set food type
        this.FOOD_TYPES.add(FoodType.NORMAL);
        if (wantSpeed && wantSize) {
            this.FOOD_TYPES.add(FoodType.SPEED);
            this.FOOD_TYPES.add(FoodType.SIZE);
        } else if (wantSpeed) {
            this.FOOD_TYPES.add(FoodType.SPEED);
        } else if (wantSize) {
            this.FOOD_TYPES.add(FoodType.SIZE);
        }
    }

    public Rectangle getRECTANGLE() {
        return this.RECTANGLE;
    }

    public FoodType getFoodType() {
        return this.foodType;
    }

    /**
     * Moves the rectangle to a random position
     * Sets food type
     * Sets graphics
     *
     * @param anchorPane to move the food on
     */
    public void move(AnchorPane anchorPane) {
        // Move the food
        this.RECTANGLE.setLayoutX(RANDOM.nextInt((int) (anchorPane.getPrefWidth() / GameController.entitySize)) * GameController.entitySize);
        this.RECTANGLE.setLayoutY(RANDOM.nextInt((int) (anchorPane.getPrefHeight() / GameController.entitySize)) * GameController.entitySize);

        this.foodType = randomType();

        setGraphics();
    }

    /**
     * Method used to set current food's type
     *
     * @return random FoodType value from FOOD_TYPES arraylist
     */
    private FoodType randomType() {
        return FOOD_TYPES.get(RANDOM.nextInt(FOOD_TYPES.size()));
    }

    /**
     * Sets the graphics of the food based on it's type
     */
    private void setGraphics() {
        switch (this.foodType) {
            case NORMAL -> GameController.setImage(this.RECTANGLE, "src/main/java/snake/snake_game/images/normalfood.png");
            case SPEED -> GameController.setImage(this.RECTANGLE, "src/main/java/snake/snake_game/images/speedfood.png");
            case SIZE -> GameController.setImage(this.RECTANGLE, "src/main/java/snake/snake_game/images/sizefood.png");
        }
    }
}
