package snake.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable
{
    public static final Double entitySize = 50.;

    public static final int gridSize = 12;

    Snake snake;
    Food food;

    private Direction direction;

    private int score;

    private final double cycleRate = 1;

    private final double cycleMultiplier = 1.01;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private Text scoreText;

    Timeline graphicsTimeline;
    Timeline gameTimeline;

    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        direction = Direction.RIGHT;
        snake = new Snake(0,0, anchorPane);
        snake.addDirectionLog(direction);
        food = new Food(0,0, anchorPane);
        food.move();
        setGameTimeline();
        setGraphicsTimeline();
    }

    @FXML
    void reset()
    {
        gameTimeline.stop();
        graphicsTimeline.stop();
        anchorPane.getChildren().removeAll(snake.getBody());
        direction = Direction.RIGHT;
        snake.addDirectionLog(direction);
        snake = new Snake(0,0, anchorPane);
        food.move();
        setGameTimeline();
        setGraphicsTimeline();
        scoreText.setText("0");
    }

    void setGraphicsTimeline()
    {
        graphicsTimeline = new Timeline(new KeyFrame(Duration.millis(1), e -> snake.setTailGraphics()));
        graphicsTimeline.setCycleCount(Animation.INDEFINITE);
        graphicsTimeline.setRate(1);
        graphicsTimeline.play();
    }

    void setGameTimeline()
    {
        gameTimeline = new Timeline(new KeyFrame(Duration.seconds(cycleRate), e ->
        {
            if (isFoodEaten())
            {
                // Update score
                score = Integer.parseInt(scoreText.getText());
                scoreText.setText(String.valueOf(score+1));
                // New tail
                snake.addTail(anchorPane);
                // New food
                food.move();
                while (isFoodInSnake())
                {
                    food.move();
                }
                // Speed up
                gameTimeline.setRate(gameTimeline.getRate()*cycleMultiplier);
            }
            snake.moveTail();
            snake.moveHead(direction);

            if (isGameOver())
            {
                gameTimeline.stop();
                double delay = 500;
                for (int i = snake.getBody().size()-1;i>-1;i--)
                {
                    fadeSnake(i, Duration.millis(delay));
                    delay *= 0.9;
                }
            }
            canChangeDirection = true;
        }));
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.setRate(1);
        gameTimeline.play();
    }

    void fadeSnake(int index, Duration delay)
    {
        Timeline fade = new Timeline(new KeyFrame(Duration.millis(1000),
                new KeyValue(snake.getBody().get(index).opacityProperty(), 0.0)));
        fade.setRate(2);
        fade.setDelay(delay);
        fade.play();
    }

    @FXML
    void changeDirection(KeyEvent event)
    {
        if(canChangeDirection)
        {
            if      (event.getCode().equals(KeyCode.W) && direction != Direction.DOWN)  {direction = Direction.UP;snake.addDirectionLog(direction);}
            else if (event.getCode().equals(KeyCode.A) && direction != Direction.RIGHT) {direction = Direction.LEFT;snake.addDirectionLog(direction);}
            else if (event.getCode().equals(KeyCode.S) && direction != Direction.UP)    {direction = Direction.DOWN;snake.addDirectionLog(direction);}
            else if (event.getCode().equals(KeyCode.D) && direction != Direction.LEFT)  {direction = Direction.RIGHT;snake.addDirectionLog(direction);}
            canChangeDirection = false;
        }
    }

    boolean isFoodEaten()
    {
        return food.getRectangle().getLayoutX() == snake.getBody().get(0).getLayoutX() && food.getRectangle().getLayoutY() == snake.getBody().get(0).getLayoutY();
    }

    boolean isFoodInSnake()
    {
        for (Rectangle segment: snake.getBody())
        {
            if (food.getRectangle().getLayoutX() == segment.getLayoutX() && food.getRectangle().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    boolean isHitWall()
    {
        return snake.getBody().get(0).getLayoutX() > anchorPane.getPrefWidth() - entitySize || snake.getBody().get(0).getLayoutX() < 0 ||
                snake.getBody().get(0).getLayoutY() < 0 || snake.getBody().get(0).getLayoutY() > anchorPane.getPrefHeight() - entitySize;
    }

    boolean isSelfCollision()
    {
        for (Rectangle tail: snake.getBody().subList(1, snake.getBody().size()))
        {
            if (tail.getLayoutX() == snake.getBody().get(0).getLayoutX() && tail.getLayoutY() == snake.getBody().get(0).getLayoutY())
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
