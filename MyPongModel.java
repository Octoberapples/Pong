
package model;

import java.util.Set;
import java.awt.Point;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import view.*;

/**
 * The PongModel keeps track of the bars, the ball and the game state. 
 * The Model describes the data of an application. In the case of this assignment, 
 * the Model implements the “physics” of the game: where the two bars are, 
 * where the ball is, how the ball moves, and so on.
 */

//view tar in från model get.fieldsize(), get.height()/(int) model.getFieldSize().getWidth

public class MyPongModel implements PongModel {
    
    /*switch fett bra att använda
    for loop som frågar efter inputs i compute 
    */
    
   /* private Input key; 
    private Input dir; */
    
    private final String leftPlayer;
    private final String rightPlayer;
    private Point ball;
    private final Dimension field;
    private final int barPos; 
    
    
    public MyPongModel(String leftPlayer, String rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;   
        this.field = new Dimension(1200,800);  
        this.barPos = (int) (this.field.getHeight()/2);
        this.ball = new Point((int) (this.field.getWidth()/2),(int) (this.field.getHeight()/2));
        
    }
    

     /**
     * Takes the inputs and applies them to the model, computing one
     * simulation step. delta_t is the time that has passed since the
     * last compute step -- use this in your time integration to have
     * the items move at the same speed, regardless of the framerate.
     * 
     * Used in PongController
     */
    @Override
    public void compute(Set<Input> input, long delta_t) {
//input tar barkey och direction, delta_t är ba en tidsenhet typ, irrelevant iaf
      /*  
        input = new HashSet<>();
        input.add(key);
        input.add(dir); 
        //input.add(ball); //men bläh behöver positionerna på bollen etc 
        
        for (Input eachinput : input) {
           
        }
        
        */
        
    }
   /**
     * getters that take a BarKey LEFT or RIGHT
     * and return positions of the various items on the board
     * 
     * Used in PongView
     */  
    @Override
    public int getBarPos(BarKey k) {
        return this.barPos;
    }

    @Override
    public int getBarHeight(BarKey k) {
        return (int) (this.field.getHeight()/4);
    }

    @Override
    public Point getBallPos() {
        return this.ball; 
    }
    
  /**
     * Will output information about the state of the game to be
     * displayed to the players
     * 
     * Used in PongView
     */
    @Override
    public String getMessage() {
        return "Toby is fucker";    
    }
   /**
     * getter for the scores.
     * 
     * Used in PongView
     */
    @Override
    public String getScore(BarKey k) {
        return "10";    
    }
   /**
     * a valid implementation of the model will keep the field size
     * will remain constant forever.
     */
    @Override
    public Dimension getFieldSize() {
        return this.field;
    }
    
}
