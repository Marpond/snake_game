package snake.snake_game;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.ArrayList;

public class Snake
{
    private boolean isCorner;
    private final ArrayList<Direction> directionLog = new ArrayList<>();
    private final ArrayList<Rectangle> body = new ArrayList<>();

    public Snake(double x, double y, AnchorPane anchorPane)
    {
        // Create head
        Rectangle head = new Rectangle(x, y, GameController.entitySize, GameController.entitySize);
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

    public void addDirectionLog(Direction direction)
    {
        directionLog.add(direction);
        if (directionLog.size() > 2)
        {
            directionLog.remove(0);
        }
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
        }
        setTailGraphics();
    }

    void setTailGraphics()
    {
        for (int i = this.body.size()-1; i > -1; i--)
        {
            // If segment is head
            if (i == 0)
            {
                isCorner = false;
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/head.png");
            }
            // If segment is end tail
            else if (i == this.body.size()-1)
            {
                isCorner = false;
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/end.png");
                this.body.get(i).setRotate(this.body.get(i-2).getRotate());
            }
            // If segment is corner - 8 total scenarios

            // Y coordinates same
            // X coords go to right
            else if (!(this.body.get(i).getLayoutY() - this.body.get(i+1).getLayoutY() == 0)&&
                    this.body.get(i).getLayoutY() - this.body.get(i-1).getLayoutY() == 0 &&
                    this.body.get(i).getLayoutX() - this.body.get(i-1).getLayoutX() == -50.0 &&
                    i-1 == 0)
            {
                isCorner = true;
                System.out.println("Y coordinates same // X coords go to right");
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/corner.png");
                switch (directionLog.get(0))
                {
                    /*
                    o > >
                    ^
                    ^
                    */
                    case UP -> {this.body.get(i).setRotate(0);}
                    /*
                    ˇ
                    ˇ
                    o > >
                    */
                    case DOWN -> {this.body.get(i).setRotate(270);}
                }
            }
            // X coords same
            // Y coords go up
            else if (!(this.body.get(i).getLayoutX() - this.body.get(i+1).getLayoutX() == 0)&&
                    this.body.get(i).getLayoutY() - this.body.get(i-1).getLayoutY() == 50 &&
                    this.body.get(i).getLayoutX() - this.body.get(i-1).getLayoutX() == 0 &&
                    i-1 == 0)
            {
                isCorner = true;
                System.out.println("X coords same // Y coords go up");
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/corner.png");
                switch (directionLog.get(0))
                {
                    /*
                        ^
                        ^
                    > > o
                     */
                    case RIGHT -> {this.body.get(i).setRotate(180);}
                    /*
                    ^
                    ^
                    o < <
                     */
                    case LEFT -> {this.body.get(i).setRotate(270);}
                }
            }
            // Y coords same
            // X coords go to left
            else if (!(this.body.get(i).getLayoutY() - this.body.get(i+1).getLayoutY() == 0)&&
                    this.body.get(i).getLayoutY() - this.body.get(i-1).getLayoutY() == 0 &&
                    this.body.get(i).getLayoutX() - this.body.get(i-1).getLayoutX() == 50.0 &&
                    i-1 == 0)
            {
                isCorner = true;
                System.out.println("Y coords same // X coords go to left");
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/corner.png");
                switch (directionLog.get(0))
                {
                    /*
                        ˇ
                        ˇ
                    < < o
                    */
                    case DOWN -> {this.body.get(i).setRotate(180);}
                    /*
                    < < o
                        ^
                        ^
                     */
                    case UP -> {this.body.get(i).setRotate(90);}
                }
            }
            // X coords same
            // Y coords go down
            else if (!(this.body.get(i).getLayoutX() - this.body.get(i+1).getLayoutX() == 0)&&
                    this.body.get(i).getLayoutY() - this.body.get(i-1).getLayoutY() == -50 &&
                    this.body.get(i).getLayoutX() - this.body.get(i-1).getLayoutX() == 0 &&
                    i-1 == 0)
            {
                isCorner = true;
                System.out.println("X coords same // Y coords go down");
                setImage(this.body.get(i), "src/main/java/snake/snake_game/images/corner.png");
                switch (directionLog.get(0))
                {
                    /*
                    > > o
                        ˇ
                        ˇ
                     */
                    case RIGHT -> {this.body.get(i).setRotate(90);}
                    /*
                    o < <
                    ˇ
                    ˇ
                     */
                    case LEFT -> {this.body.get(i).setRotate(0);}
                }
            }
            // If segment is normal tail
            else
            {
                if (isCorner)
                {
                    setImage(this.body.get(i), "src/main/java/snake/snake_game/images/corner.png");
                    switch ((int)this.body.get(i-1).getRotate())
                    {
                        // If corner segment is 0 degrees
                        case 0 ->
                                {
                                    switch (directionLog.get(0))
                                    {
                                    case UP -> this.body.get(i).setRotate(0);
                                    case LEFT -> this.body.get(i).setRotate(270);
                                    }
                                }
                        case 90 ->
                                {
                                    switch (directionLog.get(0))
                                    {
                                        case UP -> this.body.get(i).setRotate(0);
                                        case RIGHT -> this.body.get(i).setRotate(90);
                                    }
                                }
                        case 180 ->
                                {
                                    switch (directionLog.get(0))
                                    {
                                        case DOWN -> this.body.get(i).setRotate(180);
                                        case RIGHT -> this.body.get(i).setRotate(90);
                                    }
                                }
                        case 270 ->
                                {
                                    switch (directionLog.get(0))
                                    {
                                        case DOWN -> this.body.get(i).setRotate(180);
                                        case LEFT -> this.body.get(i).setRotate(270);
                                    }
                                }
                    }
                    isCorner = false;
                }
                else
                {
                    setImage(this.body.get(i), "src/main/java/snake/snake_game/images/tail.png");
                    this.body.get(i).setRotate(this.body.get(i-1).getRotate());
                }
            }
        }
    }
}
