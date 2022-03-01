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

    private final ArrayList<Rectangle> body = new ArrayList<>();

    private int score;

    private final double cycleMultiplier = 1.1;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button startButton;
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
    }

    void setBody()
    {
        for (Rectangle segment: body)
        {
            anchorPane.getChildren().remove(segment);
        }
        body.clear();
        head = new Rectangle(0, 0, entitySize, entitySize);

        direction = Direction.RIGHT;
        canChangeDirection = true;

        head.setFill(headColor);
        body.add(head);
        anchorPane.getChildren().add(head);
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
            }
            canChangeDirection = true;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(1);
        timeline.play();
    }

    @FXML
    void changeDirection(KeyEvent event)
    {
        if(canChangeDirection)
        {
            if      (event.getCode().equals(KeyCode.W) && direction != Direction.DOWN)  {direction = Direction.UP;}
            else if (event.getCode().equals(KeyCode.S) && direction != Direction.UP)    {direction = Direction.DOWN;}
            else if (event.getCode().equals(KeyCode.A) && direction != Direction.RIGHT) {direction = Direction.LEFT;}
            else if (event.getCode().equals(KeyCode.D) && direction != Direction.LEFT)  {direction = Direction.RIGHT;}
            canChangeDirection = false;
        }
    }

    void moveHead()
    {
        switch (direction)
        {
            case RIGHT -> head.setLayoutX(head.getLayoutX() + entitySize);
            case LEFT  -> head.setLayoutX(head.getLayoutX() - entitySize);
            case UP    -> head.setLayoutY(head.getLayoutY() - entitySize);
            case DOWN  -> head.setLayoutY(head.getLayoutY() + entitySize);
        }
    }

    void moveTail()
    {
        if (body.size()>1)
        {
            for (int i = body.size()-1;i > 0;i--)
            {
                body.get(i).setLayoutX(body.get(i-1).getLayoutX());
                body.get(i).setLayoutY(body.get(i-1).getLayoutY());
            }
        }
    }

    void addTail()
    {
        Rectangle tail = new Rectangle(0,0,entitySize,entitySize);
        body.add(tail);
        tail.setFill(tailColor);
        anchorPane.getChildren().add(tail);
    }

    boolean isFoodEaten()
    {
        return food.getPosition().getLayoutX() == head.getLayoutX() && food.getPosition().getLayoutY() == head.getLayoutY();
    }

    boolean isFoodInSnake()
    {
        for (Rectangle segment:body)
        {
            if (food.getPosition().getLayoutX() == segment.getLayoutX() && food.getPosition().getLayoutY() == segment.getLayoutY())
            {
                return true;
            }
        }
        return false;
    }

    boolean isSelfCollision()
    {
        for (Rectangle tail:body.subList(1,body.size()))
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
        if (head.getLayoutX() > 550 || head.getLayoutX() < 0 || head.getLayoutY() < 0 || head.getLayoutY() > 550)
        {
            return true;
        }
        else return isSelfCollision();
    }
}
