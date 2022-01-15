package evolution;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Main game class that handles most of the logic. Generates the bird(s) and pipes,
 * moves them according to the timeline, manages keyboard and button input, checks
 * for game over and resets accordingly.
 */
public class FlappyBird {
    private Pane _gamePane;
    private Flappable _bird;
    private ArrayList<Pipe> _pipes;
    private boolean _isSmart;
    private Timeline _timeline;

    /**
     * Game constructor takes the game pane and the bottom control pane as parameters
     * to add graphic game elements, stats, and buttons to them. Takes a boolean
     * parameter which tells whether or not the manual or smart game was selected.
     * Generates the first pipe and the initial bird(s). Sets up the timeline
     * and the KeyHandler or speed buttons depending on isSmart. Adds stats to the
     * control pane.
     */
    public FlappyBird(Pane gamePane, HBox controlPane, boolean isSmart) {
        _gamePane = gamePane;
        _isSmart = isSmart;

        _pipes = new ArrayList<Pipe>();

        _pipes.add(new Pipe(Constants.SCENE_WIDTH, Math.random() * Constants.GAME_HEIGHT, _gamePane));

        this.instantiateBirds();
        controlPane.getChildren().add(_bird.getStats());

        this.setupTimeline();
        if (!isSmart) {
            _gamePane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
            _gamePane.setFocusTraversable(true);
        }
        else {
            this.makeSpeedButtons(controlPane);
        }
    }

    /**
     * Helper method to instantiate the right type of bird depending on the game
     * type. If it's smart then a population, if not then a regular bird.
     */
    private void instantiateBirds() {
        if (_isSmart) {
            _bird = new Population(_gamePane, _pipes);
        }
        else {
            _bird = new Bird(_gamePane, _pipes);
        }
    }

    /**
     * Moves the pipes across the screen according to the timeline. Generates new
     * pipes and removes the ones off screen.
     */
    private void scroll() {
        for (Pipe pipe: _pipes) {
            pipe.scroll();
        }
        this.generatePipes();
        this.removePipes();
    }

    /**
     * Generates a new pipe to the right every time the rightmost pipe has made
     * it onto the screen.
     */
    private void generatePipes() {
        Pipe lastPipe = _pipes.get(_pipes.size() - 1);
        if (lastPipe.getX() < Constants.SCENE_WIDTH) {
            Pipe newPipe = new Pipe(lastPipe.getX() + Constants.PIPE_WIDTH + Constants.PIPE_X_SPACE, lastPipe.getGapY(), _gamePane);
            _pipes.add(newPipe);
        }
    }

    /**
     * Removes a pipe when it leaves the screen.
     */
    private void removePipes() {
        if (_pipes.get(0).getX() < -Constants.PIPE_WIDTH) {
            _pipes.get(0).removeGraphic();
            _pipes.remove(_pipes.get(0));
        }
    }

    /**
     * Sets up the keyframe and timeline.
     */
    private void setupTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    /**
     * Checks if the bird/population is dead. If so, resets the game by removing
     * the existing pipes, adding a new first pipe, and resetting the bird/population.
     */
    private void checkGameOver() {
        if (_bird.isDead()) {
            for (int i = _pipes.size() - 1; i >= 0; i--) {
                _pipes.get(i).removeGraphic();
                _pipes.remove(_pipes.get(i));
            }
            _pipes.add(new Pipe(Constants.SCENE_WIDTH, Math.random() * Constants.GAME_HEIGHT, _gamePane));
            _bird.reset();
        }
    }

    /**
     * Makes the speed buttons that adjust the timeline rate for the smart game.
     * Takes the bottom controlPane as a parameter to add the buttons to.
     */
    private void makeSpeedButtons(HBox controlPane) {
        Button rateOne = new Button("1x");
        rateOne.setOnAction(new SpeedHandler(1));
        Button rateTwo = new Button("2x");
        rateTwo.setOnAction(new SpeedHandler(2));
        Button rateFive = new Button("5x");
        rateFive.setOnAction(new SpeedHandler(5));
        Button rateMax = new Button("Max");
        rateMax.setOnAction(new SpeedHandler(25));
        controlPane.getChildren().addAll(rateOne, rateTwo, rateFive, rateMax);
    }

    /**
     * Called on every timeline tick to move the birds, scroll the pipes along
     * the screen, and check if the game needs to be reset. If it's a smart game
     * also calls on the population to have each SmartBird decide whether or
     * not to jump.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            _bird.moveY();
            if (_isSmart) {
                _bird.jump();
            }
            scroll();
            checkGameOver();
        }
    }

    /**
     * Instantiated for the manual game. Causes the bird to jump when the space
     * bar is pressed.
     */
    private class KeyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                _bird.jump();
            }

            event.consume();
        }
    }

    /**
     * Instantiated for the smart game. Takes a parameter of the timeline rate for
     * each speed button. When each button is pressed sets the timeline to the
     * corresponding rate.
     */
    private class SpeedHandler implements EventHandler<ActionEvent> {
        private double _rate;

        public SpeedHandler(double rate) {
            _rate = rate;
        }

        @Override
        public void handle(ActionEvent event) {
            _timeline.setRate(_rate);
        }
    }
}
