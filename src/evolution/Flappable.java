package evolution;

import javafx.scene.layout.VBox;

/**
 * Interface used to minimize clutter in game class. Provides common method calls
 * for both the Bird and Population classes so that management of each for either
 * the manual or smart game can be done using almost entirely the same method
 * calls regardless of the type of game. Includes methods for falling, jumping,
 * checking for deaths, resetting, and retrieving stats which is all of the bird
 * functionality that the game class needs to handle.
 */
public interface Flappable {
    public void moveY();

    public void jump();

    public boolean isDead();

    public void reset();

    public VBox getStats();
}
