package snake.snake_game;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Snake
{
    private final ArrayList<Rectangle> body = new ArrayList<>();

    public Snake(double x, double y, AnchorPane anchorPane)
    {
        // Create head
        Rectangle head = new Rectangle(x, y, GameController.entitySize, GameController.entitySize);
        GameController.setImage(head,"src/main/java/snake/snake_game/images/head.png");
        // Add to body and anchor-pane
        this.body.add(head);
        anchorPane.getChildren().add(head);
        // Add first 2 tails
        addTail(anchorPane);
        addTail(anchorPane);
    }

    public void addTail(AnchorPane anchorPane)
    {
        Rectangle tail = new Rectangle(0,0,GameController.entitySize,GameController.entitySize);
        this.body.add(tail);
        GameController.setImage(tail,"src/main/java/snake/snake_game/images/tail.png");
        anchorPane.getChildren().add(tail);
    }

    public ArrayList<Rectangle> getBody()
    {
        return this.body;
    }

    void moveHead(Direction direction)
    {
        switch (direction)
        {
            case DOWN  ->
                    {
                        getBody().get(0).setLayoutY(getBody().get(0).getLayoutY() + GameController.entitySize);
                        this.body.get(0).setRotate(180);
                    }
            case RIGHT ->
                    {
                        getBody().get(0).setLayoutX(getBody().get(0).getLayoutX() + GameController.entitySize);
                        this.body.get(0).setRotate(90);
                    }
            case UP    ->
                    {
                        getBody().get(0).setLayoutY(getBody().get(0).getLayoutY() - GameController.entitySize);
                        this.body.get(0).setRotate(0);
                    }
            case LEFT  ->
                    {
                        getBody().get(0).setLayoutX(getBody().get(0).getLayoutX() - GameController.entitySize);
                        this.body.get(0).setRotate(270);
                    }
        }
    }

    void followHead()
    {
        for (int i = this.body.size()-1; i > 0; i--)
        {
            this.body.get(i).setLayoutX(this.body.get(i-1).getLayoutX());
            this.body.get(i).setLayoutY(this.body.get(i-1).getLayoutY());
            this.body.get(i).setRotate(this.body.get(i-1).getRotate());
        }
        changeEndTail();
    }

    void changeEndTail()
    {
        GameController.setImage(this.body.get(this.body.size()-1), "src/main/java/snake/snake_game/images/end.png");
        GameController.setImage(this.body.get(this.body.size()-2), "src/main/java/snake/snake_game/images/tail.png");
    }
}
