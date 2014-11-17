
/**
 *
 * @author Linnea Dahl and Axel Bostrom 
 */

/**
 * The PongModel keeps track of the bars, the ball and the game state. The Model describes the data of an application. In the case of this assignment, the Model implements
 * the “physics” of the game: where the two bars are, where the ball is, how the ball moves, and so on.
 */
import java.util.Set;
import java.awt.Point;
import java.awt.Dimension;

public class PongModel {
    
    /**
     * Takes the inputs and applies them to the model, computing one
     * simulation step. delta_t is the time that has passed since the
     * last compute step -- use this in your time integration to have
     * the items move at the same speed, regardless of the framerate.
     */
    public void compute(Set<Input> input, long delta_t);

    /**
     * getters that take a BarKey LEFT or RIGHT
     * and return positions of the various items on the board
     */
    public int getBarPos(BarKey k);
    public int getBarHeight(BarKey k);
    public Point getBallPos();

    /**
     * Will output information about the state of the game to be
     * displayed to the players
     */
    public String getMessage();

    /**
     * getter for the scores.
     */
    public String getScore(BarKey k);
    
    /**
     * a valid implementation of the model will keep the field size
     * will remain constant forever.
     */
    public Dimension getFieldSize();
}
