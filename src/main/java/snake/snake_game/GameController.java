package snake.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable
{
    //A snake body part is 50x50
    private final Double entitySize = 50.;
    //The head of the snake is created, at position (250,250)
    private Rectangle snakeHead;
    //First snake tail created behind the head of the snake
    private Rectangle firstTail;
    //x and y position of the snake head different from starting position
    double xPos;
    double yPos;

    //Food
    Food food;

    //Direction snake is moving at start
    private Direction direction;

    //List of all position of the snake head
    private final List<Position> positions = new ArrayList<>();

    //List of all snake body parts
    private final ArrayList<Rectangle> snakeBody = new ArrayList<>();

    //How many times the snake has moved
    private int snakeMovement;

    //Speed of the game
    private final double baseSpeed = 0.3;

    // Speed multiplier
    private final double speedMultiplier = 1.2;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button startButton;

    Timeline timeline;

    private boolean canChangeDirection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        createTimeline();
        food = new Food(-50,-50, anchorPane, entitySize);
        start();
    }

    void createTimeline()
    {
        timeline = new Timeline(new KeyFrame(Duration.seconds(baseSpeed), e ->
        {
            positions.add(new Position(snakeHead.getX() + xPos, snakeHead.getY() + yPos));

            for (int i = 1; i < snakeBody.size(); i++)
            {
                moveTail(snakeBody.get(i), i);
            }
            canChangeDirection = true;
            snakeMovement++;
            if (isFoodEaten())
            {
                while (isFoodInSnake())
                {
                    food.spawnFood();
                }
                addTail();
                // Spawn new food
                food.spawnFood();
                // Speed things up
                timeline.setRate(timeline.getRate()*speedMultiplier);
            }
            moveHead(snakeHead);
            if(gameOver())
            {
                timeline.stop();
            }
        }));
    }
    @FXML
    void start()
    {
        for (Rectangle bodyPart : snakeBody)
        {
            anchorPane.getChildren().remove(bodyPart);
        }

        snakeMovement = 0;
        positions.clear();
        snakeBody.clear();
        snakeHead = new Rectangle(250, 250, entitySize, entitySize);
        firstTail = new Rectangle(snakeHead.getX() - entitySize, snakeHead.getY(), entitySize, entitySize);
        xPos = snakeHead.getLayoutX();
        yPos = snakeHead.getLayoutY();
        direction = Direction.RIGHT;
        canChangeDirection = true;
        food.spawnFood();

        snakeBody.add(snakeHead);
        snakeHead.setFill(Color.web("d2ffba"));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(1);
        timeline.play();

        firstTail.setFill(Color.web("9abf87"));
        snakeBody.add(firstTail);

        anchorPane.getChildren().addAll(snakeHead, firstTail);
    }

    //Change position with key pressed
    @FXML
    void changeDirection(KeyEvent event)
    {
        if(canChangeDirection){
            if (event.getCode().equals(KeyCode.W) && direction != Direction.DOWN)
            {
                direction = Direction.UP;
            }
            else if (event.getCode().equals(KeyCode.S) && direction != Direction.UP)
            {
                direction = Direction.DOWN;
            }
            else if (event.getCode().equals(KeyCode.A) && direction != Direction.RIGHT)
            {
                direction = Direction.LEFT;
            }
            else if (event.getCode().equals(KeyCode.D) && direction != Direction.LEFT)
            {
                direction = Direction.RIGHT;
            }
            canChangeDirection = false;
        }
    }

    //Snake head is moved in the direction specified
    private void moveHead(Rectangle snakeHead)
    {
        switch (direction)
        {
            case RIGHT -> {xPos = xPos + entitySize;snakeHead.setTranslateX(xPos);}
            case LEFT -> {xPos = xPos - entitySize;snakeHead.setTranslateX(xPos);}
            case UP -> {yPos = yPos - entitySize;snakeHead.setTranslateY(yPos);}
            case DOWN -> {yPos = yPos + entitySize;snakeHead.setTranslateY(yPos);}
        }
    }

    //A specific tail is moved to the position of the head x game ticks after the head was there
    private void moveTail(Rectangle snakeTail, int tailNumber) {
        double yPos = positions.get(snakeMovement - tailNumber + 1).getYPos() - snakeTail.getY();
        double xPos = positions.get(snakeMovement - tailNumber + 1).getXPos() - snakeTail.getX();
        snakeTail.setTranslateX(xPos);
        snakeTail.setTranslateY(yPos);
    }

    //New snake tail is created and added to the snake and the anchor pane
    private void addTail()
    {
        Rectangle snakeTail = new Rectangle
                (
                snakeBody.get(1).getX() + xPos + entitySize,
                snakeBody.get(1).getY() + yPos, entitySize, entitySize
                );
        snakeTail.setFill(Color.web("9abf87"));
        snakeBody.add(snakeTail);
        anchorPane.getChildren().add(snakeTail);
    }

    public boolean gameOver()
    {
        // Hit wall
        if (xPos > 300 || xPos < -250 ||yPos < -250 || yPos > 300)
        {
            return true;
        }
        // Hit itself
        else return selfCollision();
    }

    public boolean selfCollision()
    {
        int size = positions.size() - 1;
        if(size > 2){
            for (int i = size - snakeBody.size(); i < size; i++) {
                if(positions.get(size).getXPos() == (positions.get(i).getXPos()) && positions.get(size).getYPos() == (positions.get(i).getYPos()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFoodEaten()
    {
        // If x and y coordinates of the food and snake head are equal
        if(xPos + snakeHead.getX() == food.getPosition().getXPos() && yPos + snakeHead.getY() == food.getPosition().getYPos())
        {
            return true;
        }
        return false;
    }

    private boolean isFoodInSnake()
    {
        int size = positions.size();
        if(size > 2){
            for (int i = size - snakeBody.size(); i < size; i++)
            {
                if(food.getPosition().getXPos() == (positions.get(i).getXPos()) && food.getPosition().getYPos() == (positions.get(i).getYPos()))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
