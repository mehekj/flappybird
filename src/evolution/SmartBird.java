package evolution;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Subclass of Bird used for the smart game. Includes most of the same functionality
 * except it jumps according to its contained NeuralNetwork and keeps track of fitness.
 */
public class SmartBird extends Bird implements Comparable<SmartBird> {
    private NeuralNetwork _nn;
    private int _fitness;

    /**
     * Constructor used to instantiate SmartBirds with random weights. Takes only
     * the game pane and the pipes as parameters just like parent class. Instantiates
     * a NeuralNetwork with the default constructor and makes the bird translucent.
     */
    public SmartBird(Pane gamePane, ArrayList<Pipe> pipes) {
        super(gamePane, pipes);

        _body.setOpacity(Constants.BIRD_OPACITY);
        _nn = new NeuralNetwork();
        _fitness = 0;
    }

    /**
     * Constructor used to instantiate SmartBirds that inherit weights from the previous
     * generation. Does the same as the previous constructor but instantiates a neural
     * network with the weights passed in from the previous bird.
     */
    public SmartBird(Pane gamePane, ArrayList<Pipe> pipes, double[][] syn0, double[][] syn1) {
        super(gamePane, pipes);

        _body.setOpacity(Constants.BIRD_OPACITY);
        _nn = new NeuralNetwork(syn0, syn1);
        _fitness = 0;
    }

    /**
     * Overrides parent class method. Retrieves neural network output based on inputs
     * about the bird's location at that timeline instant and calls the parent class
     * method if the output is above the specified threshold.
     */
    @Override
    public void jump() {
        Pipe nearestPipe = this.getNearestPipe();
        double output = _nn.forwardProp(this.getY(), nearestPipe.getGapY(), nearestPipe.getX());
        if (output >= Constants.JUMP_FREQUENCY) {
            super.jump();
        }
    }

    /**
     * Increments the fitness of the bird.
     */
    public void updateFitness() {
        _fitness++;
    }

    /**
     * Returns the fitness of the bird.
     */
    public int getFitness() {
        return _fitness;
    }

    /**
     * Returns syn0 the first set of weights of the bird.
     */
    public double[][] getSyn0() {
        return _nn.getSyn0();
    }

    /**
     * Returns syn1 the second set of weights of the bird.
     */
    public double[][] getSyn1() {
        return _nn.getSyn1();
    }

    /**
     * Kills the bird.
     */
    public void kill() {
        _isDead = true;
    }

    /**
     * Uses the Comparable compareTo method to compare this bird to another using
     * its fitness value so that the Population class can sort an array of SmartBirds
     * from greatest fitness to least.
     */
    @Override
    public int compareTo(SmartBird other) {
        return other.getFitness() - this.getFitness();
    }
}
