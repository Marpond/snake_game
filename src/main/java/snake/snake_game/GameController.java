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
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;


public class GameController implements Initializable {

    public static String currentUsername;
    public static final Double entitySize = 40.;

    private Direction direction;
    private int score;
    private final double snakeSpeed = 0.15;
    @FXML
    private AnchorPane fieldPane;
    @FXML
    private AnchorPane snakePane;
    @FXML
    private Text scoreText;

    Snake snake;
    Food food;
    Timeline gameTick;
    Timeline foodReset;
    Timeline speedReset;

    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

    public static void setImage(Rectangle rectangle, String directory)
    {
        rectangle.setFill(new ImagePattern(new Image(new File(directory).toURI().toString())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        fieldPane.setBackground(new Background(new BackgroundImage(new Image(new File(
                "src/main/java/snake/snake_game/images/bg.png").toURI().toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        direction = Direction.RIGHT;

        snake = new Snake(0, 0, snakePane);
        food = new Food(0, 0, fieldPane);

        food.move(fieldPane);

        setTimeline();
    }

    void setTimeline()
    {
        gameTick = new Timeline(new KeyFrame(Duration.seconds(snakeSpeed), e ->
        {
            if (isFoodEaten())
            {
                try {foodReset.stop();} catch (Exception ignored){}
                // Update score
                score = Integer.parseInt(scoreText.getText());
                scoreText.setText(String.valueOf(score+1));
                // New tail
                snake.addTail(snakePane);
                // Food effects
                switch (food.getFoodType())
                {
                    case SPEED ->
                            {
                                try {speedReset.stop();} catch (Exception ignored){}
                                // Speed up
                                gameTick.setRate(gameTick.getRate()*1.15);
                                // Normal speed after 5 seconds
                                speedReset = new Timeline(new KeyFrame(Duration.seconds(5), s -> gameTick.setRate(1)));
                                speedReset.play();
                            }
                    // TODO: Increase head hit-box and visual
                    case SIZE -> System.out.println("size");
                }
                // New food
                do
                {
                    food.move(fieldPane);
                }
                while (isFoodInSnake());
                // New food location every 10 secs
                foodReset = new Timeline(new KeyFrame(Duration.seconds(10), f ->
                {
                    do
                    {
                        food.move(fieldPane);
                    }
                    while (isFoodInSnake());
                }));
                foodReset.setCycleCount(Animation.INDEFINITE);
                foodReset.play();
            }

            snake.followHead();
            snake.moveHead(direction);

            if (isGameOver())
            {
                gameTick.stop();
                // Fade snake
                for (int i = snake.getBody().size()-1;i>-1;i--)
                {
                    fadeSnake(i);
                }
                // Update leaderboard.csv
                try (PrintWriter pw = new PrintWriter(new FileOutputStream(Leaderboard.file,true)))
                {
                    Date d = new Date();
                    pw.println(String.join(",",
                            new String[]{
                                    String.valueOf(d),
                                    currentUsername,
                                    scoreText.getText()}));
                }
                catch (FileNotFoundException ex) {ex.printStackTrace();}
                // Switch to gameover scene
                SceneController.switchTo("gameover");
            }
            canChangeDirection = true;
        }));
        gameTick.setCycleCount(Animation.INDEFINITE);
        gameTick.setRate(1);
        gameTick.play();
    }

    private void fadeSnake(int index)
    {
        Timeline fade = new Timeline(new KeyFrame(Duration.millis(1000),
                new KeyValue(snake.getBody().get(index).opacityProperty(), 0.0)));
        fade.setRate(2);
        fade.play();
    }

    @FXML
    private void changeDirection(KeyEvent event)
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

    private boolean isFoodEaten()
    {
        return food.getRectangle().getLayoutX() == snake.getBody().get(0).getLayoutX() &&
                food.getRectangle().getLayoutY() == snake.getBody().get(0).getLayoutY();
    }

    private boolean isFoodInSnake()
    {
        for (Rectangle segment: snake.getBody())
        {
            if (food.getRectangle().getLayoutX() == segment.getLayoutX() &&
                    food.getRectangle().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    private boolean isHitWall()
    {
        return snake.getBody().get(0).getLayoutX() > fieldPane.getPrefWidth() - entitySize ||
                snake.getBody().get(0).getLayoutY() > fieldPane.getPrefHeight() - entitySize ||
                snake.getBody().get(0).getLayoutX() < 0 ||
                snake.getBody().get(0).getLayoutY() < 0;
    }

    private boolean isSelfCollision()
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

    private boolean isGameOver()
    {
        return isHitWall() || isSelfCollision();
    }
}
