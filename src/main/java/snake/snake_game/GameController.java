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


public class GameController implements Initializable
{

    public static String currentUsername;
    public static final Double ENTITY_SIZE = 40.;
    public static int score;

    private Direction direction;
    private final double SNAKE_SPEED = 0.15;
    private final double SPEED_MULTIPLIER = 1.2;
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
        score = 0;

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
        setFoodReset();

        setTimeline();
    }

    private void setTimeline()
    {
        gameTick = new Timeline(new KeyFrame(Duration.seconds(SNAKE_SPEED), gt ->
        {
            if (isFoodEaten())
            {
                // Update score
                score++;
                scoreText.setText(String.valueOf(score));
                // New tail
                snake.addTail(snakePane);
                // Food effects
                switch (food.getFoodType())
                {
                    case SPEED ->
                            {
                                try {speedReset.stop();} catch (Exception ignored){}
                                // Speed up
                                gameTick.setRate(gameTick.getRate()* SPEED_MULTIPLIER);
                                // Normal speed after 5 seconds
                                speedReset = new Timeline(new KeyFrame(Duration.seconds(5), sr -> gameTick.setRate(1)));
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
                setFoodReset();
            }

            snake.followHead();
            snake.moveHead(direction);

            if (isGameOver())
            {
                gameTick.stop();
                // Fade snake
                for (int i = snake.getBODY().size()-1; i>-1; i--)
                {
                    fadeSnake(i);
                }

                updateLeaderboard();
                // Switch to game-over scene after 2 seconds
                new Timeline(new KeyFrame(Duration.seconds(2), e -> SceneController.switchTo("gameover"))).play();
            }
            canChangeDirection = true;
        }));
        gameTick.setCycleCount(Animation.INDEFINITE);
        gameTick.setRate(1);
        gameTick.play();
    }

    private void fadeSnake(int index)
    {
        Timeline fade = new Timeline(new KeyFrame(Duration.seconds(1.5),
                new KeyValue(snake.getBODY().get(index).opacityProperty(), 0.0)));
        fade.setRate(1);
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

    private void updateLeaderboard()
    {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(Leaderboard.FILE,true)))
        {
            Date d = new Date();
            pw.println(String.join(",",
                    new String[]{
                            String.valueOf(d),
                            currentUsername,
                            scoreText.getText()}));
        }
        catch (FileNotFoundException ex) {ex.printStackTrace();}
    }

    private boolean isFoodEaten()
    {
        return food.getRECTANGLE().getLayoutX() == snake.getBODY().get(0).getLayoutX() &&
                food.getRECTANGLE().getLayoutY() == snake.getBODY().get(0).getLayoutY();
    }

    private boolean isFoodInSnake()
    {
        for (Rectangle segment: snake.getBODY())
        {
            if (food.getRECTANGLE().getLayoutX() == segment.getLayoutX() &&
                    food.getRECTANGLE().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    private void setFoodReset()
    {
        try {foodReset.stop();} catch (Exception ignored){}
        foodReset = new Timeline(new KeyFrame(Duration.seconds(10), fr ->
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

    private boolean isHitWall()
    {
        return snake.getBODY().get(0).getLayoutX() > fieldPane.getPrefWidth() - ENTITY_SIZE ||
                snake.getBODY().get(0).getLayoutY() > fieldPane.getPrefHeight() - ENTITY_SIZE ||
                snake.getBODY().get(0).getLayoutX() < 0 ||
                snake.getBODY().get(0).getLayoutY() < 0;
    }

    private boolean isSelfCollision()
    {
        for (Rectangle tail: snake.getBODY().subList(1, snake.getBODY().size()))
        {
            if (tail.getLayoutX() == snake.getBODY().get(0).getLayoutX() && tail.getLayoutY() == snake.getBODY().get(0).getLayoutY())
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
