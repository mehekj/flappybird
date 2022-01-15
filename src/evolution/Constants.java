package evolution;

/**
 * Class that stores important constants for the game.
 */
public class Constants {
    public static final double SCENE_WIDTH = 600;
    public static final double SCENE_HEIGHT = 800;
    public static final double CONTROL_PANE_HEIGHT = 120;
    public static final double GAME_HEIGHT = SCENE_HEIGHT - CONTROL_PANE_HEIGHT;
    // spacing for buttons in start menu
    public static final double BUTTON_SPACING = 50;

    public static final int GRAVITY = 1000;
    public static final int REBOUND_VELOCITY = -400;
    public static final double DURATION = 0.016;
    public static final double SCROLL_SPEED = -3;

    public static final double BIRD_X = 100;
    public static final double BIRD_START_Y = 300;
    public static final double BIRD_R = 20;
    public static final double BIRD_OPACITY = 0.3;
    public static final double EYE_R = 3;
    public static final double EYE_X = BIRD_X + 5;
    public static final double BEAK_X = BIRD_X + BIRD_R - 3;
    public static final double BEAK_THICKNESS = 5;
    public static final double BEAK_LENGTH = 10;

    public static final double PIPE_WIDTH = 60;
    public static final double PIPE_GAP = 200;
    public static final double PIPE_X_SPACE = 250;
    // minimum distance between pipe gap and the edge of the pane
    public static final double PIPE_GAP_BUFFER = 100;
    // maximum distance new gap can be from previous pipe gap
    public static final double NEXT_GAP_RANGE = 200;

    public static final double WEIGHTS_MAX = 1;
    public static final double WEIGHTS_MIN = -1;
    public static final int INPUT_NODES = 3;
    public static final int HIDDEN_NODES = 6;
    public static final int OUTPUT_NODES = 1;

    public static final int POPULATION_SIZE = 50;
    public static final double JUMP_FREQUENCY = 0.5;
    public static final double SELECTION_RATE = 0.04;
    // rate at which weights are selected to be mutated
    public static final double MUTATION_RATE = 0.03;
    // maximum value by which mutated weight can change
    public static final double MUTATION_CHANGE = 0.03;
    // minimum fitness elite bird must reach to be selected to pass on weights
    public static final int MIN_FITNESS = (int) ((SCENE_WIDTH - BIRD_X - BIRD_R) / -SCROLL_SPEED);
    // fitness at which population resets and next generation begins
    public static final int MAX_FITNESS = 10000;
}
