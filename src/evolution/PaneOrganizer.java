package evolution;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Creates and organizes all of the panes for the scene and instantiates the game class.
 * Creates the initial start menu when opening the program and handles the process
 * of choosing the game mode and instantiating the game accordingly.
 */
public class PaneOrganizer {
    private BorderPane _root;
    private HBox _startPane;
    private Pane _gamePane;
    private HBox _controlPane;

    /**
     * Constructor instantiates all of the panes, styles them, and sets up the initial
     * start menu.
     */
    public PaneOrganizer() {
        _root = new BorderPane();
        _startPane = new HBox(Constants.BUTTON_SPACING);
        _gamePane = new Pane();
        _controlPane = new HBox();

        BackgroundFill fill = new BackgroundFill(Color.POWDERBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        _root.setBackground(background);
        _controlPane.setBackground(background);
        _controlPane.setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        _gamePane.setMaxHeight(Constants.GAME_HEIGHT);
        _controlPane.setPrefHeight(Constants.CONTROL_PANE_HEIGHT);

        this.makeStartMenu();
    }

    /**
     * Makes the initial start menu with buttons allowing user to choose between
     * the manual game and the smart game.
     */
    private void makeStartMenu() {
        Button manual = new Button("Manual Flappy Bird");
        Button smart = new Button("Smart Flappy Bird");

        manual.setOnAction(new ClickHandler(false));
        smart.setOnAction(new ClickHandler(true));

        _startPane.getChildren().addAll(manual, smart);
        _startPane.setAlignment(Pos.CENTER);

        _root.setCenter(_startPane);
    }

    /**
     * Called when one of the start menu buttons is pressed. Takes boolean isSmart
     * parameter from ClickHandler which tells which button is clicked and passes
     * that to the game so it starts either the smart or manual. Switches the panes
     * to hide the menu and show the game and stats.
     */
    private void startGame(boolean isSmart) {
        _root.setCenter(null);
        _root.setTop(_gamePane);
        _root.setBottom(_controlPane);

        new FlappyBird(_gamePane, _controlPane, isSmart);
    }

    /**
     * Returns the root pane.
     */
    public BorderPane getRoot() {
        return _root;
    }

    /**
     * Handles the clicking of both start menu buttons. Takes a boolean parameter
     * which tells whether or not the smart button was clicked so that the correct
     * game mode can be set up in the game class.
     */
    private class ClickHandler implements EventHandler<ActionEvent> {
        private boolean _isSmart;

        public ClickHandler(boolean isSmart) {
            _isSmart = isSmart;
        }

        @Override
        public void handle(ActionEvent event) {
            startGame(_isSmart);
        }
    }
}
