package snake.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable
{
    private final Double entitySize = 50.;
    private Rectangle head;

    Food food;

    private Direction direction;

    private final ArrayList<Rectangle> snakeBody = new ArrayList<>();

    private int score;

    private final double cycleMultiplier = 1.01;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Text scoreText;

    Timeline timeline;

    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

    Color headColor = Color.web("ffffff");
    Color tailColor = Color.web("000000");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        food = new Food(0,0, anchorPane, entitySize);
        food.move();
        setBody();
        setTimeline();
    }

    @FXML
    void reset()
    {
        timeline.stop();
        food.move();
        setBody();
        setTimeline();
        scoreText.setText("0");
    }

    void setRectangleImage(Rectangle rectangle, String directory)
    {
        Image image = new Image(directory);
        ImagePattern imagePattern = new ImagePattern(image);
        rectangle.setFill(imagePattern);
    }

    void setBody()
    {
        // Remove snake from anchorpane, clear arraylist
        for (Rectangle segment: snakeBody)
        {
            anchorPane.getChildren().remove(segment);
        }
        snakeBody.clear();
        // Initial direction
        direction = Direction.RIGHT;
        // Create head
        head = new Rectangle(0, 0, entitySize, entitySize);
        head.setFill(headColor);
        // Add to body and anchorpane
        snakeBody.add(head);
        anchorPane.getChildren().add(head);
        // Add first tail
        addTail();
    }

    void setTimeline()
    {
        double cycleRate = 0.25;
        timeline = new Timeline(new KeyFrame(Duration.seconds(cycleRate), e ->
        {

            System.out.println("Food: "+food.getPosition().getLayoutX()+" "+food.getPosition().getLayoutY());
            System.out.println("Head: "+head.getLayoutX()+" "+head.getLayoutY());

            if (isFoodEaten())
            {
                // Update score
                score = Integer.parseInt(scoreText.getText());
                scoreText.setText(String.valueOf(score+1));
                // New tail
                addTail();
                // New food
                food.move();
                while (isFoodInSnake())
                {
                    food.move();
                }
                // Speed up
                timeline.setRate(timeline.getRate()*cycleMultiplier);
            }
            moveTail();
            moveHead();

            if (isGameOver())
            {
                timeline.stop();
                double delay = 500;
                for (int i = snakeBody.size()-1;i>-1;i--)
                {
                    fadeSnake(i, Duration.millis(delay));
                    delay /= 1.1;
                }
            }
            canChangeDirection = true;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(1);
        timeline.play();
    }

    void fadeSnake(int index, Duration delay)
    {
        Timeline fade = new Timeline(new KeyFrame(Duration.millis(1000),
                new KeyValue(snakeBody.get(index).opacityProperty(), 0.0)));
        fade.setRate(2);
        fade.setDelay(delay);
        fade.play();
    }

    @FXML
    void changeDirection(KeyEvent event)
    {
        if(canChangeDirection)
        {
            if      (event.getCode().equals(KeyCode.W) && direction != Direction.DOWN)  {direction = Direction.UP;}
            else if (event.getCode().equals(KeyCode.A) && direction != Direction.RIGHT) {direction = Direction.LEFT;}
            else if (event.getCode().equals(KeyCode.S) && direction != Direction.UP)    {direction = Direction.DOWN;}
            else if (event.getCode().equals(KeyCode.D) && direction != Direction.LEFT)  {direction = Direction.RIGHT;}
            canChangeDirection = false;
        }
    }

    void moveHead()
    {
        switch (direction)
        {
            case DOWN  -> head.setLayoutY(head.getLayoutY() + entitySize);
            case RIGHT -> head.setLayoutX(head.getLayoutX() + entitySize);
            case UP    -> head.setLayoutY(head.getLayoutY() - entitySize);
            case LEFT  -> head.setLayoutX(head.getLayoutX() - entitySize);
        }
    }

    void moveTail()
    {
        if (snakeBody.size()>1)
        {
            // First time I've done a reverse loop o.o
            for (int i = snakeBody.size()-1; i > 0; i--)
            {
                snakeBody.get(i).setLayoutX(snakeBody.get(i-1).getLayoutX());
                snakeBody.get(i).setLayoutY(snakeBody.get(i-1).getLayoutY());
            }
        }
    }

    void addTail()
    {
        Rectangle tail = new Rectangle(0,0,entitySize,entitySize);
        snakeBody.add(tail);
        tail.setFill(tailColor);
        anchorPane.getChildren().add(tail);
    }

    boolean isFoodEaten()
    {
        return food.getPosition().getLayoutX() == head.getLayoutX() && food.getPosition().getLayoutY() == head.getLayoutY();
    }

    boolean isFoodInSnake()
    {
        for (Rectangle segment: snakeBody)
        {
            if (food.getPosition().getLayoutX() == segment.getLayoutX() && food.getPosition().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    boolean isHitWall()
    {
        return head.getLayoutX() > anchorPane.getPrefWidth() - entitySize || head.getLayoutX() < 0 ||
                head.getLayoutY() < 0 || head.getLayoutY() > anchorPane.getPrefHeight() - entitySize;
    }

    boolean isSelfCollision()
    {
        for (Rectangle tail: snakeBody.subList(1, snakeBody.size()))
        {
            if (tail.getLayoutX() == head.getLayoutX() && tail.getLayoutY() == head.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    boolean isGameOver()
    {
        if (isHitWall())
        {
            return true;
        }
        else return isSelfCollision();
    }
}
