# Machine Learning Flappy Bird Game

For a full description of the project visit my website at mehekj.github.io/projects/flappybird.html.

## Run Instructions
Either clone this repo or download the flappybird.jar file (located in out/artifacts/FlappyBird).  
In a command line navigate to the folder where the flappybird.jar file is located.  
Run `java -jar flappybird.jar`.

## Usage Instructions
#### Manual Mode
The game will automatically begin. Press the space bar to make the bird jump. When you lose the game will automatically restart.
You can view your current score and high score (resets each time you run the program) in the bottom left corner.

#### Smart Mode
The game will run itself, originally at the default speed. You can speed up the game by selecting one of the buttons in the bottom left corner.
(I would recommend running on Max at first as it often takes many generations for the birds to start making significant progress.)
You can also view the stats of how the bird population is doing where fitness is a measure of how good the birds are at the game (i.e. how far they have traveled).
The fitness does cap off at 10000 at which point any remaining birds are killed off and the next generation is started.
