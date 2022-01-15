package evolution;

/**
 * Neural network class that uses inputs from the SmartBird to output a value
 * that allows it to decide whether or not to jump at each timeline step using either
 * randomized weights or weights inherited from a previous generation bird.
 */
public class NeuralNetwork {
    private double[][] _syn0;
    private double[][] _syn1;

    /**
     * Default constructor that creates random weights.
     */
    public NeuralNetwork() {
        _syn0 = this.randomizeWeights(Constants.HIDDEN_NODES, Constants.INPUT_NODES);
        _syn1 = this.randomizeWeights(Constants.OUTPUT_NODES, Constants.HIDDEN_NODES);
    }

    /**
     * Constructor that inherits weights from a previous generation bird. Sets
     * this birds weights to copies of the previous weights and then mutates them.
     */
    public NeuralNetwork(double[][] syn0, double[][] syn1) {
        _syn0 = this.copy(syn0);
        _syn1 = this.copy(syn1);
        this.mutate(syn0);
        this.mutate(syn1);
    }

    /**
     * Takes the dimensions of a matrix and returns a 2D array that represents
     * the resulting matrix filled with random weights.
     */
    private double[][] randomizeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = Math.random() * (Constants.WEIGHTS_MAX - Constants.WEIGHTS_MIN) + Constants.WEIGHTS_MIN;
            }
        }

        return weights;
    }

    /**
     * Method that is called by the SmartBird that takes in the inputs from the bird
     * (the bird location and the pipe location) and then completes the full
     * forward propagation process and returns the output.
     */
    public double forwardProp(double birdY, double pipeY, double pipeX) {
        double[] inputs = this.normalizeInputs(birdY, pipeY, pipeX);

        double[] hiddenLayer = this.sigmoid(this.dotProduct(_syn0, inputs));
        double[] output = this.sigmoid(this.dotProduct(_syn1, hiddenLayer));

        if (output == null) {
            output = new double[Constants.OUTPUT_NODES];
            output[0] = 0;
        }

        return output[0];
    }

    /**
     * Takes in the inputs passed from the SmartBird and returns their normalized
     * values as an array.
     */
    private double[] normalizeInputs(double birdY, double pipeY, double pipeX) {
        double[] inputs = new double[Constants.INPUT_NODES];
        // range of birdy is the game height
        inputs[0] = (birdY - Constants.BIRD_R) / Constants.GAME_HEIGHT;
        // range of pipeY is from min value of buffer to max value of game height - buffer and gap
        inputs[1] = (pipeY - Constants.PIPE_GAP_BUFFER) / (Constants.GAME_HEIGHT - 2 * Constants.PIPE_GAP_BUFFER - Constants.PIPE_GAP);
        // range of pipeX is scene width
        inputs[2] = pipeX / Constants.SCENE_WIDTH;
        return inputs;
    }

    /**
     * Calculates the product of two matrices. Takes in a left 2D array which is
     * the weights and a right array that is the layer of nodes being multiplied.
     * Returns the product as a 1D array.
     */
    private double[] dotProduct(double[][] left, double[] right) {
        if (right != null && left != null) {
            int leftRows = left.length;
            int leftCols = left[0].length;
            int rightRows = right.length;

            if (leftCols == rightRows) {
                double[] product = new double[leftRows];

                for (int row = 0; row < leftRows; row++) {
                    for (int i = 0; i < leftCols; i++) {
                        product[row] += left[row][i] * right[i];
                    }
                }
                return product;
            }
        }
        return null;
    }

    /**
     * Sigmoid activation function. Takes the layer being processed in and performs
     * the sigmoid function on each value and returns the result.
     */
    private double[] sigmoid(double[] layer) {
        if (layer != null) {
            double[] activatedLayer = new double[layer.length];
            for (int i = 0; i < layer.length; i++) {
                activatedLayer[i] = 1 / (1 + Math.exp(-layer[i]));
            }
            return activatedLayer;
        }
        return null;
    }

    /**
     * Returns syn0
     */
    public double[][] getSyn0() {
        return _syn0;
    }

    /**
     * Returns syn1
     */
    public double[][] getSyn1() {
        return _syn1;
    }

    /**
     * Takes a 2D array and returns a copy of it.
     */
    public double[][] copy(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    /**
     * Takes a matrix of weights and mutates them. Loops through all the entries
     * and chooses which ones to alter based randomly on the mutation rate.
     * Then changes it to a new random value within a certain range from the previous.
     */
    public void mutate(double[][] weights) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                if (Math.random() < Constants.MUTATION_RATE) {
                    double currWeight = weights[i][j];
                    double upperBound = Math.min(currWeight + Constants.MUTATION_CHANGE, Constants.WEIGHTS_MAX);
                    double lowerBound = Math.max(currWeight - Constants.MUTATION_CHANGE, Constants.WEIGHTS_MIN);
                    weights[i][j] = Math.random() * (upperBound - lowerBound) + lowerBound;
                }
            }
        }
    }
}
