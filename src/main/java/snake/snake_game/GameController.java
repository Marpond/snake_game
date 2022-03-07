package snake.snake_game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable
{
    public static String currentUsername;
    public static final Double entitySize = 40.;

    private Direction direction;
    private int score;
    private final double cycleRate = 0.15;
    private final double cycleMultiplier = 1.01;
    @FXML
    private AnchorPane fieldPane;
    @FXML
    private AnchorPane snakePane;
    @FXML
    private Text scoreText;

    Snake snake;
    Food food;
    Timeline gameTimeline;

    // Without this the user would be able to change direction multiple times between timeline cycles
    private boolean canChangeDirection;

    public static void setImage(Rectangle rectangle, String directory)
    {
        rectangle.setFill(new ImagePattern(new Image(new File(directory).toURI().toString())));
    }

    @FXML
    private void switchToMenu() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Main.stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        snakePane.prefWidthProperty().bind(fieldPane.widthProperty());
        snakePane.prefHeightProperty().bind(fieldPane.heightProperty());

        fieldPane.setBackground(new Background(new BackgroundImage(new Image(new File(
                "src/main/java/snake/snake_game/images/bg.png").toURI().toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        direction = Direction.RIGHT;

        snake = new Snake(0,0, snakePane);
        food = new Food(0,0, fieldPane);

        food.move(fieldPane);

        setGameTimeline();
    }

    @FXML
    void reset()
    {
        gameTimeline.stop();
        snakePane.getChildren().removeAll(snake.getBody());
        direction = Direction.RIGHT;
        snake = new Snake(0,0, snakePane);
        food.move(fieldPane);
        setGameTimeline();
        scoreText.setText("0");
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
                snake.addTail(snakePane);
                // New food
                food.move(fieldPane);
                while (isFoodInSnake())
                {
                    food.move(fieldPane);
                }
                // Speed up
                gameTimeline.setRate(gameTimeline.getRate()*cycleMultiplier);
            }

            snake.followHead();
            snake.moveHead(direction);

            if (isGameOver())
            {
                gameTimeline.stop();
                // Fade snake
                for (int i = snake.getBody().size()-1;i>-1;i--)
                {
                    fadeSnake(i);
                }
                // Update leaderboard.csv
                try (PrintWriter pw = new PrintWriter(new FileOutputStream(Leaderboard.file,true)))
                {
                    Date d = new Date();
                    pw.println(String.join(",",new String[]{String.valueOf(d),currentUsername,scoreText.getText()}));
                }
                catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            }
            canChangeDirection = true;
        }));
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.setRate(1);
        gameTimeline.play();
    }



    void fadeSnake(int index)
    {
        Timeline fade = new Timeline(new KeyFrame(Duration.millis(1000),
                new KeyValue(snake.getBody().get(index).opacityProperty(), 0.0)));
        fade.setRate(2);
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

    boolean isFoodEaten()
    {
        return food.getRectangle().getLayoutX() == snake.getBody().get(0).getLayoutX() &&
                food.getRectangle().getLayoutY() == snake.getBody().get(0).getLayoutY();
    }

    boolean isFoodInSnake()
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

    boolean isHitWall()
    {
        return snake.getBody().get(0).getLayoutX() > fieldPane.getPrefWidth() - entitySize ||
                snake.getBody().get(0).getLayoutY() > fieldPane.getPrefHeight() - entitySize ||
                snake.getBody().get(0).getLayoutX() < 0 ||
                snake.getBody().get(0).getLayoutY() < 0;
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
        return isHitWall() || isSelfCollision();
    }
}
