package evolution;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Main bird class. Used in the manual game, but includes overall common functionality
 * required for both the regular bird and the smart bird subclass: creates shapes
 * for the bird graphic, falls, jumps, gets the nearest pipe, checks for collisions,
 * checks for death, and resets. For the manual game it also needs to keep track
 * of the score and high score and return those stats to the game.
 */
public class Bird implements Flappable {
    protected Circle _body;
    private ArrayList<Shape> _shapes;
    private Pane _gamePane;
    private double _vy;
    protected boolean _isDead;
    private ArrayList<Pipe> _pipes;
    private int _score, _highScore;
    private Label _scoreLabel, _HSLabel;

    /**
     * Constructor takes the game pane as a parameter to add shapes to. It also takes
     * the list of pipes from the game class so the bird can get the nearest pipe,
     * check for collisions, and update the score within the bird class.
     */
    public Bird(Pane gamePane, ArrayList<Pipe> pipes) {
        _gamePane = gamePane;
        this.createShapes();

        _pipes = pipes;

        _highScore = 0;
        _score = 0;
        _vy = 0;
        _isDead = false;

        _scoreLabel = new Label("Score: " + _score);
        _HSLabel = new Label("High score: " + _highScore);
    }

    /**
     * Helper method that creates all of the birds shapes. Keeps the main circle
     * of the body as a separate instance variable so that it's y location can be
     * easily retrieved in absolute terms instead of having to retrieve the
     * translation relative to the starting position.
     */
    public void createShapes() {
        _shapes = new ArrayList<Shape>();

        _body = new Circle(Constants.BIRD_X, Constants.BIRD_START_Y, Constants.BIRD_R);
        _body.setFill(Color.GOLD);

        Circle eye = new Circle(Constants.EYE_X, Constants.BIRD_START_Y, Constants.EYE_R);
        eye.setFill(Color.BLACK);
        _shapes.add(eye);

        Rectangle beak = new Rectangle(Constants.BEAK_X, Constants.BIRD_START_Y - Constants.BEAK_THICKNESS / 2,
                Constants.BEAK_LENGTH, Constants.BEAK_THICKNESS);
        beak.setFill(Color.ORANGE);
        _shapes.add(beak);

        _gamePane.getChildren().add(_body);
        _gamePane.getChildren().addAll(_shapes);
    }

    /**
     * Called on every timeline tick to make the bird fall with gravity. Stops
     * the bird from going up further when it hits the top of the window.
     */
    @Override
    public void moveY() {
        _vy = _vy + Constants.GRAVITY * Constants.DURATION;
        double newY = _body.getCenterY() + _vy * Constants.DURATION;

        if (newY <= Constants.BIRD_R) {
            _vy = 0;
            newY = Constants.BIRD_R;
        }

        for (Shape shape: _shapes) {
            shape.setTranslateY(newY - Constants.BIRD_START_Y);
        }
        _body.setCenterY(newY);
    }

    /**
     * Returns the y value of the bird's body.
     */
    public double getY() {
        return _body.getCenterY();
    }

    /**
     * For the manual game called when space is pressed and for the smart game
     * called from the subclass if the neural network of the SmartBird tells it to
     * jump. Sets the bird's velocity to the rebound velocity.
     */
    @Override
    public void jump() {
        _vy = Constants.REBOUND_VELOCITY;
    }

    /**
     * Returns the leftmost pipe on the screen that the bird has not already passed.
     */
    protected Pipe getNearestPipe() {
        for (Pipe pipe: _pipes) {
            if (pipe.getX() >= Constants.BIRD_X - Constants.PIPE_WIDTH - Constants.BIRD_R) {
                return pipe;
            }
        }
        return _pipes.get(0);
    }

    /**
     * Checks for collision with the nearest pipe.
     */
    public boolean hits() {
        return this.getNearestPipe().hits(_body);
    }

    /**
     * Checks if the bird is dead if it has collided with a pipe or fallen off the screen
     * and returns a boolean accordingly. If the bird is not dead, it updates its
     * score and if it is, it is removed graphically from the pane.
     */
    @Override
    public boolean isDead() {
        if (!_isDead) {
            this.updateScore();
            if (this.hits() || this.getY() > Constants.GAME_HEIGHT + Constants.BIRD_R) {
                _isDead = true;
            }
        }
        if (_isDead) {
            _gamePane.getChildren().remove(_body);
            _gamePane.getChildren().removeAll(_shapes);
        }
        return _isDead;
    }

    /**
     * Updates the score of the bird if it is passing the next nearest pipe.
     * Updates the high score if it has been beaten.
     */
    private void updateScore() {
        // Two conditions ensure that score is updated even if bird location never exactly equals the edge of the pipe
        if (Constants.BIRD_X <= this.getNearestPipe().getX() + Constants.PIPE_WIDTH
        && Constants.BIRD_X > this.getNearestPipe().getX() + Constants.PIPE_WIDTH + Constants.SCROLL_SPEED) {
            _score++;
            _scoreLabel.setText("Score: " + _score);
        }
        if (_score > _highScore) {
            _highScore = _score;
            _HSLabel.setText("High score: " + _highScore);
        }
    }

    /**
     * Resets the bird by setting it's position to it's initial y value and
     * resetting score and velocity.
     */
    @Override
    public void reset() {
        _gamePane.getChildren().add(_body);
        _gamePane.getChildren().addAll(_shapes);
        _body.setCenterY(Constants.BIRD_START_Y);
        for (Shape shape: _shapes) {
            shape.setTranslateY(0);
        }
        _score = 0;
        _scoreLabel.setText("Score: " + _score);
        _vy = 0;
        _isDead = false;
    }

    /**
     * Returns a pane with all of the relevant stats for the manual game.
     */
    @Override
    public VBox getStats() {
        VBox statsPane = new VBox();
        statsPane.getChildren().addAll(_scoreLabel, _HSLabel);
        return statsPane;
    }
}
