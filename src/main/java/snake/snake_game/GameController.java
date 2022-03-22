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
    // Public variables used across many classes
    public static int entitySize = 50;
    public static String currentUsername;
    public static int score;
    // To check if the player wants to play cursed mode
    public static boolean isCursed;
    // To check if the player wants obstacles
    public static boolean wantObstacles;

    private Direction direction;
    private final double SNAKE_SPEED = 0.15;
    private final double MAX_OBSTACLES = 2000/entitySize;
    private final double SPEED_MULTIPLIER = 1.3;
    private final int[] ROTATION = {0,90,180,270};
    private final Random RANDOM = new Random();
    private final ArrayList<Rectangle> OBSTACLES = new ArrayList<>();
    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

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
    // Timeline for the game itself
    private Timeline gameTick;
    // Timeline to reset the food
    private Timeline foodReset;
    // Timeline to reset the speed of the snake after eating a speed food
    private Timeline speedReset;

    /**
     * Sets the fill of a rectangle to an image pattern
     * @param rectangle to set the fill of
     * @param directory of the image
     */
    public static void setImage(Rectangle rectangle, String directory)
    {
        rectangle.setFill(new ImagePattern(new Image(new File(directory).toURI().toString())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Set the username text
        // Without the timeline usernameText is null
        new Timeline(new KeyFrame(Duration.millis(1), setUsername -> usernameText.setText(usernameText.getText()+currentUsername))).play();
        // Start playing the game music
        soundtrack = new MediaPlayer(new Media(new File(
                "src/main/java/snake/snake_game/sounds/soundtrack.mp3").toURI().toString()));
        soundtrack.play();
        soundtrack.setCycleCount(MediaPlayer.INDEFINITE);

        score = 0;
        // Initial direction of the snake
        direction = Direction.RIGHT;

        snake = new Snake(snakePane);
        food = new Food(fieldPane);

        // Add obstacles
        if (wantObstacles)
        {
            for (int i = 0;i<RANDOM.nextInt((int) MAX_OBSTACLES);i++)
            {
                new Obstacle(0,0,obstaclePane,snake.getBODY(),food,OBSTACLES);
            }
        }
        // Move the food once and repeat till it's not colliding with other rectangles
        do
        {
            food.move(fieldPane);
        }
        while (isFoodColliding());

        setFoodReset();
        setGameTick(SNAKE_SPEED);
    }

    /**
     * Sets a timeline that is responsible for the game's ticks
     * @param duration cycle rate of the timeline
     */
    private void setGameTick(double duration)
    {
        if (isCursed)
        {
            // Set a new timeline that changes the rotation of the play-field every second
            Timeline cursedTimeline = new Timeline(new KeyFrame(Duration.seconds(1), c ->
                    obstaclePane.setRotate(ROTATION[RANDOM.nextInt(4)])));
            cursedTimeline.setCycleCount(Animation.INDEFINITE);
            cursedTimeline.setRate(1);
            cursedTimeline.play();
        }
        // Main timeline
        gameTick = new Timeline(new KeyFrame(Duration.seconds(duration), gt ->
        {
            if (isFoodEaten())
            {
                // Play sound
                SoundController.play("get");
                // Update score
                score++;
                scoreText.setText(String.valueOf(score));
                // New tail
                snake.addTail(snakePane);
                // Food effects
                switch (food.getFoodType())
                {
                    // Speed food
                    case SPEED ->
                            {
                                // Try to stop the timeline in case it already exists
                                try {speedReset.stop();} catch (Exception ignored){}
                                // Speed up the main timeline
                                gameTick.setRate(gameTick.getRate() * SPEED_MULTIPLIER);
                                // Normal speed after 3 seconds
                                speedReset = new Timeline(new KeyFrame(Duration.seconds(3), sr -> gameTick.setRate(1)));
                                speedReset.play();
                            }
                    // TODO: Increase head hit-box
                    // Size food
                    case SIZE ->
                            {
                                System.out.println("size");
                            }
                }
                // Move food to new location
                do
                {
                    food.move(fieldPane);
                }
                while (isFoodColliding());

                setFoodReset();
            }
            // Move the snake
            snake.followHead();
            snake.moveHead(direction);

            if (isGameOver())
            {
                // Play the game-over sound
                SoundController.play("gameover");
                // Stop the game
                soundtrack.stop();
                gameTick.stop();

                updateLeaderboard();
                // Switch to game-over scene after 2 seconds
                new Timeline(new KeyFrame(Duration.seconds(2), e -> SceneController.switchTo("gameover"))).play();
            }
            canChangeDirection = true;
        }));
        // Start main timeline
        gameTick.setCycleCount(Animation.INDEFINITE);
        gameTick.setRate(1);
        gameTick.play();
    }

    /**
     * Changes direction of the snake
     * @param event keyboard input
     */
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

    /**
     * Updates leaderboard.csv
     */
    private void updateLeaderboard()
    {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(Leaderboard.FILE,true)))
        {
            // Only "vanilla" runs are saved
            if (!Food.wantSpeed &&
                !Food.wantSize &&
                !GameController.wantObstacles &&
                !GameController.isCursed &&
                GameController.entitySize == 50)
            {
                Date d = new Date();
                pw.println(String.join(",",
                        new String[]{String.valueOf(d),
                                    currentUsername,
                                    scoreText.getText()}));
            }
        }
        catch (FileNotFoundException ex) {ex.printStackTrace();}
    }

    /**
     * Checks if the snake head coordinates match the food's
     * @return boolean value
     */
    private boolean isFoodEaten()
    {
        return food.getRECTANGLE().getLayoutX() == snake.getBODY().get(0).getLayoutX() &&
                food.getRECTANGLE().getLayoutY() == snake.getBODY().get(0).getLayoutY();
    }

    /**
     * Checks for multiple occasions where the food could be colliding with other rectangles
     * @return boolean value
     */
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

    /**
     * Sets a timeline that resets the food's location every x seconds
     */
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

    /**
     * Checks if the snake head's coordinates match an obstacle's
     * @return boolean value
     */
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

    /**
     * Checks if the snake head is outside the boundary
     * @return boolean value
     */
    private boolean isHitWall()
    {
        return snake.getBODY().get(0).getLayoutX() > fieldPane.getPrefWidth() - entitySize ||
                snake.getBODY().get(0).getLayoutY() > fieldPane.getPrefHeight() - entitySize ||
                snake.getBODY().get(0).getLayoutX() < 0 ||
                snake.getBODY().get(0).getLayoutY() < 0;
    }

    /**
     * Checks if the snake head's coordinates match with any of the other body's parts
     * @return boolean value
     */
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

    /**
     * Checks if the returned boolean value of any game-ending methods are true
     * @return boolean value
     */
    private boolean isGameOver()
    {
        return isHitWall() || isSelfCollision() || isHitObstacle();
    }
}
