package snake.snake_game;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.ArrayList;

public class Snake
{
    private final ArrayList<Rectangle> body = new ArrayList<>();

    public Snake(double x, double y, AnchorPane anchorPane)
    {
        // Create head
        Rectangle head = new Rectangle(x, y, GameController.entitySize, GameController.entitySize);
        setImage(head,"src/main/java/snake/snake_game/images/head.png");
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
        setImage(tail,"src/main/java/snake/snake_game/images/tail.png");
        anchorPane.getChildren().add(tail);
    }

    public ArrayList<Rectangle> getBody()
    {
        return this.body;
    }

    public void setImage(Rectangle rectangle, String directory)
    {
        Image image = new Image(new File(directory).toURI().toString());
        ImagePattern imagePattern = new ImagePattern(image);
        rectangle.setFill(imagePattern);
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

    void moveTail()
    {
        for (int i = this.body.size()-1; i > 0; i--)
        {
            this.body.get(i).setLayoutX(this.body.get(i-1).getLayoutX());
            this.body.get(i).setLayoutY(this.body.get(i-1).getLayoutY());
            this.body.get(i).setRotate(this.body.get(i - 1).getRotate());
        }
        setTailGraphics();
    }

    void setTailGraphics()
    {
        for (int i = this.body.size()-1; i > -1; i--)
        {
            if (i>0)
            {
                if (i == this.body.size()-1)
                {
                    setImage(this.body.get(i),"src/main/java/snake/snake_game/images/end.png");
                    this.body.get(i).setRotate(this.body.get(i-1).getRotate());
                }
                else if (this.body.get(i+1).getRotate() - this.body.get(i-1).getRotate() == 90 ||
                                this.body.get(i+1).getRotate() - this.body.get(i-1).getRotate() == -90 ||
                                this.body.get(i+1).getRotate() - this.body.get(i-1).getRotate() == 270 ||
                                this.body.get(i+1).getRotate() - this.body.get(i-1).getRotate() == -270)
                {
                    setImage(this.body.get(i),"src/main/java/snake/snake_game/images/corner.png");
                }
                else
                {
                    setImage(this.body.get(i), "src/main/java/snake/snake_game/images/tail.png");
                }
            }
        }
    }
}
