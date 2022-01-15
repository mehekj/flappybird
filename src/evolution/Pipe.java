package evolution;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Class for the game's pipe obstacle. Consists of two rectangles that are generated
 * in the class based on the previous pipe.
 */
public class Pipe {
    private Rectangle _top;
    private Rectangle _bottom;
    private Pane _gamePane;

    /**
     * Pipe constructor. Takes the game pane as a parameter to add graphic elements
     * to. Takes the x value of this new pipe as a parameter and the y value of the
     * top of the previous pipe's gap. Uses these values to generate the two rectangles
     * of the pipe.
     */
    public Pipe(double x, double prevY, Pane gamePane) {
        _gamePane = gamePane;

        this.createRects(x, prevY);
    }

    /**
     * Helper method that creates the two rectangles. Takes the x value of the pipe
     * as a parameter and the previous pipe's gap y value. Generates the y value
     * of the new gap randomly within a specific range from the previous one.
     */
    private void createRects(double x, double prevY) {
        // prevents bug where prevY value sometimes is larger than what the max value should be
        if (prevY > Constants.GAME_HEIGHT - Constants.PIPE_GAP_BUFFER - Constants.PIPE_GAP) {
            prevY = Constants.GAME_HEIGHT - Constants.PIPE_GAP_BUFFER - Constants.PIPE_GAP;
        }
        double lowBound = Math.max(Constants.PIPE_GAP_BUFFER, prevY - Constants.NEXT_GAP_RANGE);
        double highBound = Math.min(prevY + Constants.NEXT_GAP_RANGE, Constants.GAME_HEIGHT - Constants.PIPE_GAP_BUFFER - Constants.PIPE_GAP);
        double gapTop = Math.random() * (highBound - lowBound) + lowBound;

        _top = new Rectangle(x, 0, Constants.PIPE_WIDTH, gapTop);
        _bottom = new Rectangle(x, gapTop + Constants.PIPE_GAP, Constants.PIPE_WIDTH, Constants.GAME_HEIGHT - gapTop - Constants.PIPE_GAP);

        _top.setFill(Color.OLIVEDRAB);
        _bottom.setFill(Color.OLIVEDRAB);
        _gamePane.getChildren().addAll(_top, _bottom);
    }

    /**
     * Called on each timeline tick to move both of the pipe's rectangles across
     * the game window.
     */
    public void scroll() {
        _top.setX(_top.getX() + Constants.SCROLL_SPEED);
        _bottom.setX(_bottom.getX() + Constants.SCROLL_SPEED);
    }

    /**
     * Returns the x value of the pipe.
     */
    public double getX() {
        return _top.getX();
    }

    /**
     * Returns the y value of the top of the pipe gap.
     */
    public double getGapY() {
        return _top.getY() + _top.getHeight();
    }

    /**
     * Removes the pipe from the pane graphically.
     */
    public void removeGraphic() {
        _gamePane.getChildren().removeAll(_top, _bottom);
    }

    /**
     * Takes the body of the bird being checked for collision as a parameter and
     * checks whether the bird has hit either the top or bottom of the pipe.
     */
    public boolean hits(Shape bird) {
        return bird.intersects(_top.getX(), _top.getY(), _top.getWidth(), _top.getHeight())
                || bird.intersects(_bottom.getX(), _bottom.getY(), _bottom.getWidth(), _bottom.getHeight());
    }
}
