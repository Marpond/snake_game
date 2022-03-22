package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Snake
{
    // Arraylist every body segment
    private final ArrayList<Rectangle> BODY = new ArrayList<>();

    /**
     * Constructor
     * @param anchorPane to add it to
     */
    public Snake(AnchorPane anchorPane)
    {
        // Create head
        Rectangle head = new Rectangle(0, 0, GameController.entitySize, GameController.entitySize);
        GameController.setImage(head,"src/main/java/snake/snake_game/images/head.png");
        // Add to body and anchor-pane
        this.BODY.add(head);
        anchorPane.getChildren().add(head);
        // Add first 2 tails
        addTail(anchorPane);
        addTail(anchorPane);
    }

    /**
     * Creates a new rectangle as a new tail
     * @param anchorPane to add it to
     */
    public void addTail(AnchorPane anchorPane)
    {
        // Create tail
        Rectangle tail = new Rectangle(0,0,GameController.entitySize,GameController.entitySize);
        GameController.setImage(tail,"src/main/java/snake/snake_game/images/tail.png");
        // Add to body and anchor-pane
        this.BODY.add(tail);
        anchorPane.getChildren().add(tail);
    }

    public ArrayList<Rectangle> getBODY() {return this.BODY;}

    /**
     * Moves the head coordinates and changes the rotation based on the current direction inside GameController
     * @param direction of the head
     */
    public void moveHead(Direction direction)
    {
        switch (direction)
        {
            case DOWN  ->
                    {
                        getBODY().get(0).setLayoutY(getBODY().get(0).getLayoutY() + GameController.entitySize);
                        this.BODY.get(0).setRotate(180);
                    }
            case RIGHT ->
                    {
                        getBODY().get(0).setLayoutX(getBODY().get(0).getLayoutX() + GameController.entitySize);
                        this.BODY.get(0).setRotate(90);
                    }
            case UP    ->
                    {
                        getBODY().get(0).setLayoutY(getBODY().get(0).getLayoutY() - GameController.entitySize);
                        this.BODY.get(0).setRotate(0);
                    }
            case LEFT  ->
                    {
                        getBODY().get(0).setLayoutX(getBODY().get(0).getLayoutX() - GameController.entitySize);
                        this.BODY.get(0).setRotate(270);
                    }
        }
    }

    /**
     * Makes every other body segment except the head move to the coordinates of the previous segment
     */
    public void followHead()
    {
        for (int i = this.BODY.size()-1; i > 0; i--)
        {
            this.BODY.get(i).setLayoutX(this.BODY.get(i-1).getLayoutX());
            this.BODY.get(i).setLayoutY(this.BODY.get(i-1).getLayoutY());
            this.BODY.get(i).setRotate(this.BODY.get(i-1).getRotate());
        }
        changeEndTail();
    }

    /**
     * Changes the graphical values of the last segment and the one before that
     */
    private void changeEndTail()
    {
        GameController.setImage(this.BODY.get(this.BODY.size()-1), "src/main/java/snake/snake_game/images/end.png");
        GameController.setImage(this.BODY.get(this.BODY.size()-2), "src/main/java/snake/snake_game/images/tail.png");
    }
}
