package evolution;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that contains and manages the functionality of all of the birds for the
 * smart game. Loops through an array of all of the birds and makes them fall,
 * jump, and check for death. Keeps track of stats for the overall generation and
 * decides when to pass on weights to the next generation, and which weights to
 * pass on.
 */
public class Population implements Flappable {
    private SmartBird[] _birds;
    private Pane _gamePane;
    private ArrayList<Pipe> _pipes;
    private int _generation, _numAlive, _currFit, _lastAvgFit, _lastBestFit, _bestFitEver;
    private Label _genLabel, _aliveLabel, _currFitLabel, _lastAvgLabel, _lastBestLabel, _bestEverLabel;

    /**
     * Constructor takes the game pane and list of pipes as a parameter used to
     * instantiate all of the birds. Instantiates all of the initial birds with
     * randomized weights. Creates all of the labels for the stats.
     */
    public Population(Pane gamePane, ArrayList<Pipe> pipes) {
        _birds = new SmartBird[Constants.POPULATION_SIZE];

        _gamePane = gamePane;
        _pipes = pipes;

        for (int i = 0; i < _birds.length; i++) {
            _birds[i] = new SmartBird(gamePane, pipes);
        }

        _generation = 1;
        _genLabel = new Label("Generation: " + _generation);
        _numAlive = Constants.POPULATION_SIZE;
        _aliveLabel = new Label("Alive: " + _numAlive);
        _currFit = 0;
        _currFitLabel = new Label("Current fitness " + _currFit);
        _lastAvgFit = 0;
        _lastAvgLabel = new Label("Last gen avg fitness: " + _lastAvgFit);
        _lastBestFit = 0;
        _lastBestLabel = new Label("Last gen best fitness: " + _lastBestFit);
        _bestFitEver = 0;
        _bestEverLabel = new Label("Best fitness all time: " + _bestFitEver);
    }

    /**
     * Loops through all of the birds and makes them fall if they aren't dead.
     */
    @Override
    public void moveY() {
        for (SmartBird bird: _birds) {
            if (!bird.isDead()) {
                bird.moveY();
            }
        }
    }

    /**
     * Loops through all of the birds and makes them jump if they aren't dead.
     */
    @Override
    public void jump() {
        for (SmartBird bird: _birds) {
            if (!bird.isDead()) {
                bird.jump();
            }
        }
    }

    /**
     * Returns a boolean of whether or not the whole population has died. Loops
     * through all of the birds, checks for their deaths, and updates the count
     * of living birds. If a bird is alive its fitness and the current fitness of
     * the generation is updated. If a bird reaches the max fitness goal it is killed
     * so that the game will reset and the next generation can begin.
     */
    @Override
    public boolean isDead() {
        boolean allDead = true;
        _numAlive = 0;
        for (SmartBird bird: _birds) {
            if (!bird.isDead()) {
                allDead = false;
                bird.updateFitness();
                _numAlive++;
                _currFit = bird.getFitness();
                if (_currFit >= Constants.MAX_FITNESS) {
                    bird.kill();
                }
            }
        }
        _aliveLabel.setText("Alive: " + _numAlive);
        _currFitLabel.setText("Current fitness " + _currFit);
        return allDead;
    }

    /**
     * Resets all of the birds and begins the new generation. Updates the stats
     * based on the generation that just died and retrieves the elite birds. If
     * any elite birds were selected, their weights are passed on to the new SmartBirds.
     * If not, new SmartBirds with random weights are generated.
     */
    @Override
    public void reset() {

        this.updateStats();

        ArrayList<SmartBird> bestBirds = this.getBestBirds();

        for (int i = 0; i < _birds.length; i++) {
            if (bestBirds.size() != 0) {
                SmartBird selected = bestBirds.get(i % bestBirds.size());
                _birds[i] = new SmartBird(_gamePane, _pipes, selected.getSyn0(), selected.getSyn1());
            }
            else {
                _birds[i] = new SmartBird(_gamePane, _pipes);
            }
        }
    }

    /**
     * Returns an ArrayList of the best performing birds whose weights should get
     * passed on to the next generation. Loops through the list of birds ordered
     * from greatest to least fitness and picks the first few. If those best fitness
     * birds made it past the minimum fitness they are selected to pass on their weights.
     */
    private ArrayList<SmartBird> getBestBirds() {
        int numBestBirds = (int) (_birds.length * Constants.SELECTION_RATE);

        ArrayList<SmartBird> bestBirds = new ArrayList<SmartBird>();

        for (int i = 0; i < numBestBirds; i++) {
            if (_birds[i].getFitness() > Constants.MIN_FITNESS) {
                bestBirds.add(_birds[i]);
            }
        }

        return bestBirds;
    }

    /**
     * Called at the end of each generation to sort the birds by fitness and update
     * the stats for the generation.
     */
    private void updateStats() {
        Arrays.sort(_birds);

        _lastBestFit = _birds[0].getFitness();
        _lastBestLabel.setText("Last gen best fitness: " + _lastBestFit);

        if (_lastBestFit > _bestFitEver) {
            _bestFitEver = _lastBestFit;
            _bestEverLabel.setText("Best fitness all time: " + _bestFitEver);
        }

        int totalFitness = 0;

        for (SmartBird bird: _birds) {
            totalFitness += bird.getFitness();
        }
        _lastAvgFit = totalFitness / _birds.length;
        _lastAvgLabel.setText("Last gen avg fitness: " + _lastAvgFit);

        _generation++;
        _genLabel.setText("Generation: " + _generation);
    }

    /**
     * Returns a pane with all of the relevant stats for the smart game to the
     * game class.
     */
    @Override
    public VBox getStats() {
        VBox statsPane = new VBox();

        statsPane.getChildren().addAll(_genLabel, _aliveLabel, _currFitLabel, _lastAvgLabel, _lastBestLabel, _bestEverLabel);
        return statsPane;
    }
}
