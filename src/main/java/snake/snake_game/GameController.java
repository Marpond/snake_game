package snake.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;


public class GameController implements Initializable
{

    public static String currentUsername;
    public static final Double ENTITY_SIZE = 40.;
    public static int score;
    public static boolean cursed;
    public static boolean wantObstacles;

    private Direction direction;
    private final double SNAKE_SPEED = 0.15;
    private final int MAX_OBSTACLES = 25;
    private final double SPEED_MULTIPLIER = 1.2;
    private final int[] ROTATION = {0,90,180,270};
    private final Random RANDOM = new Random();
    private final ArrayList<Rectangle> OBSTACLES = new ArrayList<>();

    @FXML
    private AnchorPane fieldPane;
    @FXML
    private AnchorPane snakePane;
    @FXML
    private AnchorPane obstaclePane;
    @FXML
    private Text scoreText;
    @FXML
    private Text usernameText;

    private MediaPlayer soundtrack;
    private Snake snake;
    private Food food;
    private Timeline gameTick;
    private Timeline foodReset;
    private Timeline speedReset;

    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

    public static void setImage(Rectangle rectangle, String directory)
    {
        rectangle.setFill(new ImagePattern(new Image(new File(directory).toURI().toString())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Without the timeline usernameText is null
        new Timeline(new KeyFrame(Duration.millis(1), setUsername -> usernameText.setText(usernameText.getText()+currentUsername))).play();

        soundtrack = new MediaPlayer(new Media(new File(
                "src/main/java/snake/snake_game/sounds/soundtrack.mp3").toURI().toString()));
        soundtrack.play();
        soundtrack.setCycleCount(MediaPlayer.INDEFINITE);

        score = 0;

        direction = Direction.RIGHT;

        snake = new Snake(0, 0, snakePane);
        food = new Food(0, 0, fieldPane);

        // Add obstacles
        if (wantObstacles)
        {
            for (int i = 0;i<RANDOM.nextInt(MAX_OBSTACLES);i++)
            {
                new Obstacle(0,0,obstaclePane,snake.getBODY(),food,OBSTACLES);
            }
        }


        do
        {
            food.move(fieldPane);
        }
        while (isFoodColliding());

        setFoodReset();
        setGameTick();
    }

    private void setGameTick()
    {
        if (cursed)
        {
            Timeline cursedTimeline = new Timeline(new KeyFrame(Duration.seconds(1), c ->
                    obstaclePane.setRotate(ROTATION[RANDOM.nextInt(4)])));
            cursedTimeline.setCycleCount(Animation.INDEFINITE);
            cursedTimeline.setRate(1);
            cursedTimeline.play();
        }


        gameTick = new Timeline(new KeyFrame(Duration.seconds(SNAKE_SPEED), gt ->
        {

            if (isFoodEaten())
            {
                Sound.play("get");
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
                                gameTick.setRate(gameTick.getRate() * SPEED_MULTIPLIER);
                                // Normal speed after 3 seconds
                                speedReset = new Timeline(new KeyFrame(Duration.seconds(3), sr -> gameTick.setRate(1)));
                                speedReset.play();
                            }
                    // TODO: Increase head hit-box
                    case SIZE ->
                            {
                                if (snake.getBODY().get(0).getHeight() != ENTITY_SIZE*2)
                                {
                                    snake.getBODY().get(0).setHeight(ENTITY_SIZE*2);
                                    snake.getBODY().get(0).setWidth(ENTITY_SIZE*2);
                                    new Timeline(new KeyFrame(Duration.seconds(2), e ->
                                    {
                                        snake.getBODY().get(0).setHeight(ENTITY_SIZE);
                                        snake.getBODY().get(0).setWidth(ENTITY_SIZE);
                                    })).play();
                                }
                            }
                }
                // New food
                do
                {
                    food.move(fieldPane);
                }
                while (isFoodColliding());
                // New food location every 10 secs
                setFoodReset();
            }

            snake.followHead();
            snake.moveHead(direction);

            if (isGameOver())
            {
                Sound.play("gameover");
                soundtrack.stop();
                gameTick.stop();
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

    private boolean isFoodColliding()
    {
        // Snake
        for (Rectangle segment:snake.getBODY())
        {
            if (food.getRECTANGLE().getLayoutX() == segment.getLayoutX() &&
                    food.getRECTANGLE().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        // Obstacle
        for (Rectangle obstacle:OBSTACLES)
        {
            if (food.getRECTANGLE().getLayoutX() == obstacle.getLayoutX() &&
            food.getRECTANGLE().getLayoutY() == obstacle.getLayoutY())
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
            while (isFoodColliding());
        }));
        foodReset.setCycleCount(Animation.INDEFINITE);
        foodReset.play();
    }

    private boolean isHitObstacle()
    {
        for (Rectangle obstacle:OBSTACLES)
        {
            if (obstacle.getLayoutX() == snake.getBODY().get(0).getLayoutX() &&
            obstacle.getLayoutY() == snake.getBODY().get(0).getLayoutY())
            {
                return true;
            }
        }
        return false;
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
            if (tail.getLayoutX() == snake.getBODY().get(0).getLayoutX() &&
                    tail.getLayoutY() == snake.getBODY().get(0).getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    private boolean isGameOver()
    {
        return isHitWall() || isSelfCollision() || isHitObstacle();
    }
}
