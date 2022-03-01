package snake.snake_game;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;

public class Snake
{
    private final ArrayList<Rectangle> body = new ArrayList<>();

    public Snake(double x, double y, AnchorPane anchorPane)
    {
        // Create head
        Rectangle head = new Rectangle(x, y, GameController.entitySize, GameController.entitySize);
        setImage(head, getClass().getResource("head.png"));
        // Add to body and anchor-pane
        this.body.add(head);
        anchorPane.getChildren().add(head);
        // Add first tail
        addTail(anchorPane);
    }

    public void addTail(AnchorPane anchorPane)
    {
        Rectangle tail = new Rectangle(0,0,GameController.entitySize,GameController.entitySize);
        this.body.add(tail);
        setImage(tail,getClass().getResource("tail.png"));
        anchorPane.getChildren().add(tail);
    }

    public ArrayList<Rectangle> getBody()
    {
        return this.body;
    }

    public void setImage(Rectangle rectangle, URL directory)
    {
        Image image = new Image(String.valueOf(directory));
        ImagePattern imagePattern = new ImagePattern(image);
        rectangle.setFill(imagePattern);
    }

    void moveHead(Direction direction)
    {
        switch (direction)
        {
            case DOWN  -> getBody().get(0).setLayoutY(getBody().get(0).getLayoutY() + GameController.entitySize);
            case RIGHT -> getBody().get(0).setLayoutX(getBody().get(0).getLayoutX() + GameController.entitySize);
            case UP    -> getBody().get(0).setLayoutY(getBody().get(0).getLayoutY() - GameController.entitySize);
            case LEFT  -> getBody().get(0).setLayoutX(getBody().get(0).getLayoutX() - GameController.entitySize);
        }
    }

    void moveTail()
    {
        if (this.body.size()>1)
        {
            for (int i = this.body.size()-1; i > 0; i--)
            {
                this.body.get(i).setLayoutX(this.body.get(i-1).getLayoutX());
                this.body.get(i).setLayoutY(this.body.get(i-1).getLayoutY());
            }
        }
    }

}
