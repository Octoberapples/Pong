
package model;

import java.util.Set;
import java.awt.Point;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Axel Bostrom and Linnea Dahl
 */


public class MyPongModel implements PongModel {

    private final String left;
    private final String right;
    private String message;
    private int leftScore;
    private int rightScore;
    private final Point ball;
    private final int ballSize;
    private final int barWidth;
    private final Dimension field;
    private int leftBarPos;
    private int rightBarPos;
    private int leftBarSize;
    private int rightBarSize;
    private double xSpeed;
    private double ySpeed;

    /**
     *
     * @param left
     * @param right
     */
    public MyPongModel(String left, String right) {
        this.left = left;
        this.right = right;   
        this.message = "Welcome to Pong!";
        this.leftScore = 0;
        this.rightScore = 0;
        this.field = new Dimension(1200,800);
        this.leftBarPos = (int) (this.field.getHeight()/2);
        this.rightBarPos = (int) (this.field.getHeight()/2);
        this.leftBarSize = (int) this.field.getHeight()/4;
        this.rightBarSize = (int) this.field.getHeight()/4;
        this.ball = new Point((int)(this.field.getWidth()/2), (int)(this.field.getHeight()/2));
        this.ballSize = 20;
        this.barWidth = 10;
        this.xSpeed = 0;
    }
    

     /**
     * Takes the inputs and applies them to the model, computing one
     * simulation step. delta_t is the time that has passed since the
     * last compute step -- use this in your time integration to have
     * the items move at the same speed, regardless of the frame rate.
     * 
     * Used in PongController
     * @param input takes the input
     * @param delta_t the framerate 
     */
    @Override
    public void compute(Set<Input> input, long delta_t) {
        int ms =(int)delta_t/2;  
        for(Input i : input) {
            switch(i.key){ //field.height växer uppifrån och ner, så längst upp är 0 och längstner är max
                case LEFT: 
                    switch(i.dir) {
                        case UP: 
                            if(checkBarUp(BarKey.LEFT)){
                                this.leftBarPos = getBarPos(BarKey.LEFT) - ms;
                                break;
                            }
                        case DOWN:
                            if(checkBarDown(BarKey.LEFT)){
                                this.leftBarPos = getBarPos(BarKey.LEFT) + ms;
                                break;
                            }
                    }
                break;
                case RIGHT: 
                    switch(i.dir) {
                        case UP: 
                            if(checkBarUp(BarKey.RIGHT)){
                                this.rightBarPos = getBarPos(BarKey.RIGHT) - ms;
                                break;
                            }
                        case DOWN: 
                            if (checkBarDown(BarKey.RIGHT)){
                                this.rightBarPos = getBarPos(BarKey.RIGHT) + ms;
                                break;
                            }
                    }
                break;
            }
        }   
        moveBall(ms);
        if (scoreQuery()) { 
            try { 
                score(ms);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyPongModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }
 /**
  * Keeps track over whether the player scored or not.
  */
    private boolean scoreQuery() {
        int aOffset = ballSize * 5; //this
        int bOffset = ballSize;     //and this is used to know if someone has scored and shit
        return ((isBetween(-aOffset, (int)getBallPos().getX(), -bOffset)) || 
               (isBetween((int)this.field.getWidth()+bOffset, (int)getBallPos().getX(), (int)this.field.getWidth()+aOffset)));
    }
   
/**
 * Keeps track of score 
 * @param ms speed of movement
 * @throws InterruptedException 
 */
    
    private void score(int ms) throws InterruptedException {
        int rTime;
        int rScore;
        if (getBallPos().getX() < this.field.getWidth()/2) { //ball on the left side of field
            if (rightScore < 9) {
                rScore = 0;
                rTime = 1000;
                ++rightScore;
                shrink(BarKey.LEFT);
                this.message = this.right + " scored!";
                reset(ms, rTime, rScore);
            } else {
                rScore = 1;
                rTime = 3000;
                this.message = this.right + " totally won!";
                reset(ms, rTime, rScore);
            }
        } else { //right side of field
            if (leftScore < 9) {
                rScore = 0;
                rTime = 1000;
                ++leftScore;
                shrink(BarKey.RIGHT);
                this.message = this.left + " scored!";
                reset(ms, rTime, rScore);
            } else {
                rScore = 1;
                rTime = 3000;
                this.message = this.left + " totally won!";
                reset(ms, rTime, rScore);
            }
        }
        
    }  
    
   /**
    * Resets the game
    * @param ms speed of movement 
    * @param time
    * @param resetScore
    * @throws InterruptedException 
    */
    
    private void reset(int ms, int time, int resetScore) throws InterruptedException {
        this.ball.setLocation((int)(this.field.getWidth()/2), (int)(this.field.getHeight()/2));
        if (resetScore == 0) {
            if (this.xSpeed < 0) this.xSpeed = ms;
            else this.xSpeed = -ms;
            this.ySpeed = 0;
            Thread.sleep(time);
        } else {
            this.xSpeed = ms;
            this.ySpeed = 0;
            this.leftScore = 0;
            this.rightScore = 0;
            this.leftBarSize = (int) this.field.getHeight()/4;
            this.rightBarSize = (int) this.field.getHeight()/4;
            Thread.sleep(time);
        }
    }
    
    /**
     * Shrinks the bar key 
     * @param k the bar key
     */
    
    private void shrink(BarKey k) {
        if (k == BarKey.LEFT && this.leftBarSize >= this.field.getHeight()/6) this.leftBarSize -= this.leftBarSize/8;
        if (k == BarKey.RIGHT && this.rightBarSize >= this.field.getHeight()/6) this.rightBarSize -= this.leftBarSize/8;
    }
    
    /**
     * Checks if b is inbetween the values a and c.
     * @param a first value
     * @param b second value
     * @param c third value
     */
    private boolean isBetween(int a, int b, int c) {
        return (a < b && b < c);
    }
    /**
     * Calculates the speed
     * @param ms movement of speed
     */
    
    private void calcSpeeds(int ms) {
        if (this.xSpeed == 0) this.xSpeed = ms; //init
        if (ballHitBar()) {
            if(getBallPos().getX() > this.field.getWidth()/2) {
                this.xSpeed++;
                calcXY(BarKey.RIGHT);
            } else {
                this.xSpeed--;
                calcXY(BarKey.LEFT);
            }
        }
    }
    /**
     * Calculates the position for X and Y
     * @param k the bar key
     */
    
    private void calcXY(BarKey k) {
        int barStart = getBarPos(k) - getBarHeight(k)/2; 
        int barFirst = barStart + getBarHeight(k)/5; 
        int barSecond = barFirst + getBarHeight(k)/5;
        int barThird = barSecond + getBarHeight(k)/5;
        int barFourth = barThird + getBarHeight(k)/5;
        int ballPos = (int)getBallPos().getY(); //where the ball is on the Y-axis
        if (k == BarKey.RIGHT) {
            if (isBetween(barStart-ballSize, ballPos, barFirst)) negY(2);                            //top part change alot
            else if (isBetween(barFirst, ballPos, barSecond)) negY(4);                      //topmid part change little
            else if (isBetween(barSecond, ballPos, barThird)) this.xSpeed++;                //mid part, increase speed ever so slightly
            else if (isBetween(barThird, ballPos, barFourth)) posY(4);                      //botmid part change little
            else posY(2);                                                                   //bot part change alot
        }else {
            if (isBetween(barStart-ballSize, ballPos, barFirst)) posY(2);                            //top part change alot
            else if (isBetween(barFirst, ballPos, barSecond)) posY(4);                      //topmid part change little
            else if (isBetween(barSecond, ballPos, barThird)) this.xSpeed++;                //mid part, increase speed ever so slightly
            else if (isBetween(barThird, ballPos, barFourth)) negY(4);                      //botmid part change little
            else negY(2);                                                                   //bot part change alot        
        }
        this.xSpeed = -this.xSpeed;         
    }

    private void posY(int change){ //up
        int diff = (int)this.xSpeed/change; 
        this.ySpeed += diff;
    }
    
    /**
     * 
     * @param change 
     */
    
    private void negY(int change) { //down
        int diff = (int)this.xSpeed/change;
        this.ySpeed -= diff;
    }
    
    /**
     * Checks if the ball has hit one of the bars 
     * @return true if ball hit the bar
     */
   
    private boolean ballHitBar() {  
        return ((barBeenHit(BarKey.RIGHT) && (getBallPos().getX()+ballSize+barWidth) >= (int)this.field.getWidth()) ||
                (barBeenHit(BarKey.LEFT) && (getBallPos().getX()-ballSize-barWidth) <= 0));     
    }
/**
 * Checks the Y-value of the ball whether it is true or not
 * @param k the bar key
 */
    
    private boolean barBeenHit(BarKey k) {
        return ((getBallPos().getY() - ballSize <= (getBarPos(k) + getBarHeight(k)/2)) &&
                (getBallPos().getY() + ballSize >= (getBarPos(k) - getBarHeight(k)/2)));
    }

    /**
     *  Function for moving the ball
     * @param ms speed of movement
     */
    
    private void moveBall(int ms){
        calcSpeeds(ms);
        edgeHit();
        this.ball.move((int)(getBallPos().getX() + this.xSpeed/2), (int)(getBallPos().getY() + this.ySpeed/2)); 
    }
    
    /**
     * Checks if the edge of the bar has been hit
     */
    
    private void edgeHit() {
        if (getBallPos().getY() - ballSize <= 0) {                      //top edge hit
            if (this.ySpeed < 0) this.ySpeed = -this.ySpeed;
        } 
        if (getBallPos().getY() + ballSize >= this.field.getHeight()) { //bottom edge hit
            if (this.ySpeed > 0) this.ySpeed = -this.ySpeed;
        }
    }
    
    /**
     * Checks the position for the bar when it goes up 
     * @param k the bar key
     * @return the position of the bar
     */

    private boolean checkBarUp(BarKey k) {
        return getBarPos(k) >= (getBarHeight(k)/2);
    }
    
    /**
     * Checks the position for the bar when it goes down
     * @param k the bar key 
     * @return the position of the bar
     */
    
    private boolean checkBarDown(BarKey k) {
        return getBarPos(k) < ((int)this.field.getHeight() - getBarHeight(k)/2);
    }

   /**
     * getters that take a BarKey LEFT or RIGHT
     * and return positions of the various items on the board
     * 
     * Used in PongView
     * @param k the bar key
     * @return the position of the bar
     */  
    
    @Override
    public int getBarPos(BarKey k) {
        switch(k) {            
            case LEFT: return leftBarPos; 
            case RIGHT: return rightBarPos; 
            default: return 0; 
        }
    }
    
    /**
     * Gets the height of the bar 
     * @param k the bar key
     * @return the height of the bar 
     */

    @Override
    public int getBarHeight(BarKey k) {
        switch(k) {            
            case LEFT: return leftBarSize;   
            case RIGHT: return rightBarSize;
            default: return 0;
        }
    }

    /**
     * Will get the balls position 
     * @return the balls position
     */
    
    @Override
    public Point getBallPos() {
        return this.ball; 
    }
    
  /**
     * Will output information about the state of the game to be
     * displayed to the players
     * 
     * Used in PongView
     * @return the message
     */
    @Override
    public String getMessage() {
        return this.message;    
    }
   /**
     * getter for the scores.
     * 
     * Used in PongView
     * @param k the bar key 
     * @return the score 
     */
    @Override
    public String getScore(BarKey k) {
        switch (k) {
            case LEFT: return String.valueOf(leftScore);
            case RIGHT: return String.valueOf(rightScore);
            default: return "0";
        }
    }
   /**
     * a valid implementation of the model will keep the field size
     * will remain constant forever.
     * @return the size of the field
     */
        
    @Override
    public Dimension getFieldSize() {
        return this.field;
    }
    
}
   
