/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		
		intro();
		setup();
		generateRandomXComponentVelocity();
		
		while(true){
			moveBall();
			checkForCollisionWithWalls();
			checkForCollisionPaddleBricks();
			pause(0);
		}
	}
	
	private void intro(){
		GLabel label=new GLabel("Samarth's BREAKOUT!!!");
		label.setFont("helvetica-36");
		double x=(WIDTH-label.getWidth())/2;
		double y=(HEIGHT-label.getAscent())/2;
		add(label,x,y);
		pause(3000);
		remove(label);
	}
	
	
	/** SETUP method definition. it sets up the bricks and 
	 * the paddle, and adds the mouse listeners
	 */
	private void setup(){
		createTwoRows(Color.RED);
		createTwoRows(Color.ORANGE);
		createTwoRows(Color.YELLOW);
		createTwoRows(Color.GREEN);
		createTwoRows(Color.CYAN);
		createPaddle();
		createBall();
		addMouseListeners();
		
	}
	
	
	/** creates two rows of the bricks using the color passed on from setup
	 * as its argument
	 * @param col the color passed on to it of which the two rows of bricks will be
	 */
	private void createTwoRows(Color col){
		/* x here indicates the x co-ordinate of the top-left corner of the first brick 
		 * of first row, similarly y indicates the y co-ordinate of top-left corner of first
		 * brick of first row
		 */
		double x= (WIDTH*0.5) - (((NBRICK_ROWS*BRICK_WIDTH)+((NBRICK_ROWS-1)*BRICK_SEP))*0.5);
		double y= BRICK_Y_OFFSET;
		for(int j=0;j<2;j++){	
			for(int i=0;i<10;i++){
				GRect rect=new GRect(BRICK_WIDTH,BRICK_HEIGHT);
				rect.setFilled(true);
				rect.setColor(col);
				rect.setFillColor(col);
				add(rect, x+(BRICK_WIDTH+BRICK_SEP)*i,y+(row_no*(BRICK_HEIGHT+BRICK_SEP)));
			}
			row_no++;
		}
	}
	
	/** generates the paddle in the middle towards the bottom end 
	 * of the game window
	 */
	private void createPaddle(){
		paddle=new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle,(WIDTH-PADDLE_WIDTH)*0.5,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
		last=paddle.getX();
	}
	
	/** tracks the x-cordinate of the mouse movement and synchronises the movement in the
	 * x direction of the paddle with the movement of mouse
	 * IT ALSO TAKES CARE SUCH THAT THE PADDLE DOESN'T CROSS THE RIGHT WALL OF THE WINDOW
	 * PRE-CONDITION-mouse listeners must be added
	 */
	
	public void mouseMoved(MouseEvent e){
		
			paddle.move(e.getX()-last, 0);
			last=e.getX();
			if(last>(WIDTH-PADDLE_WIDTH)){
				paddle.setLocation(WIDTH-PADDLE_WIDTH,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
			}
		
	}
	/** creates the ball right at the middle of the screen */
	private void createBall(){
		ball=new GOval(2*BALL_RADIUS,2*BALL_RADIUS);
		ball.setColor(Color.RED);
		ball.setFilled(true);
		add(ball,(WIDTH-ball.getWidth())/2,(HEIGHT-ball.getHeight())/2);
	}
	
	/** generates the random value of x-component of the velocity at the start of the program
	 * 
	 */
	private void generateRandomXComponentVelocity(){
		RandomGenerator rgen=RandomGenerator.getInstance();
		vx=rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean(0.5)){
			vx=(-vx);
		}
	}
	
	
	/** generates the code for moving the ball around the world */
	private void moveBall(){
		
		ball.move(vx, vy);
	}
	
	/** checks for the collisions of the ball with the four walls of the application window and
	 * bounces off the ball if there happens to be a collision, except the lower wall, in which
	 * case, the ball disappears and a life is lost. 
	 */
	private void checkForCollisionWithWalls(){
		if(ball.getX()>WIDTH-ball.getWidth()){
			vx=-vx;
			double diff=ball.getX()-(WIDTH-ball.getWidth());
			ball.move(-2*diff, 0);
		}
		else if(ball.getY()<0){
			vy=-vy;
			double diff=ball.getY();
			ball.move(0,-2*diff);
		}
		else if(ball.getX()<0){
			vx=-vx;
			double diff=ball.getX();
			ball.move(-2*diff, 0);
		}
		else if(ball.getY()>HEIGHT-ball.getHeight()){
			lifeLost();
		}
	}
	
	/** checks if the ball, has collided with the paddle or the bricks, if the ball has collided
	 * with the paddle, it bounces off from the paddle and if any brick is the colliding
	 * object, it removes the brick and again makes it bounce off
	 */
	private void checkForCollisionPaddleBricks(){
		GObject collider=getCollidingObject();
		
		if(collider==paddle){
			vy=-vy;
			
		}
		else
		if(collider!=null)	
		{
			remove(collider);
			vy=-vy;
			bricks_remaining--;
			if(bricks_remaining==0){
				GLabel label=new GLabel("YOU WIN ");
				label.setFont("Times new roman-36");
				label.setColor(Color.RED);
				add(label,(WIDTH-label.getWidth())/2,(HEIGHT-label.getAscent())/2);
			}
		}
	}
	
	/** takes the four corners of the square a the reference points and gets the element 
	 * on those points, if any of the four points collide with an object, it returns that object,
	 *  if not, then it returns a null value
	 * @return
	 */
	private GObject getCollidingObject(){
		GObject gobj1,gobj2,gobj3,gobj4;
		gobj1=getElementAt(ball.getX(),ball.getY());
		gobj2=getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY());
		gobj3=getElementAt(ball.getX(),ball.getY()+(2*BALL_RADIUS));
		gobj4=getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY()+(2*BALL_RADIUS));
		
		if(gobj1!=null){
			return gobj1;
		}
		else if(gobj2!=null){
			return gobj2;
		}
		else if(gobj3!=null){
			return gobj3;
		}
		else if(gobj4!=null){
			return gobj4;
		}
		else return gobj1;
	}
	
	/** each time the payer drops the ball, this method reduces one life from the number of 
	 * remaining lives and displays to the player no. of lives left with him, if the player 
	 * does not have any, it declares the game over.
	 */
	private void lifeLost(){
		if(lives_remaining>0){
			remove(ball);
			GLabel label=new GLabel("lives remaning "+(lives_remaining-1));
			add(label,(WIDTH-label.getWidth())/2,(HEIGHT-label.getAscent())/2);
			pause(1000);
			
			remove(label);
			generateRandomXComponentVelocity();
			createBall();
			lives_remaining--;
		}
		else{
			remove(ball);
			GLabel label=new GLabel("GAME OVER");
			add(label,(WIDTH-label.getWidth())/2,(HEIGHT-label.getAscent())/2);
			pause(1000);
			remove(label);
		}
	}
	
	
	/** keeps track of the row number using 0 as its initial value
	 * 0 indicates the first row
	 */
	private int row_no=0;
	
	/** keeps the track of the last recorded x  coordinate of the paddle */
	private double last;
	
	/** paddle and ball are used within multiple methods, that is why it is kept as an instance variable*/
	private GRect paddle;
	private GOval ball;
	
	/** indicates the velocity of the ball in x-direction which will get a random velocity
	 * in the method moveBall */
	private double vx;
	
	/** indicates the velocity of the ball in y-direction which starts with initial velocity 3.0 */
	private double vy=3.0;
	
	/**keeps track of the number of lives remaining with the player and is decreased by one if 
	 * the plater drops the ball
	 */
	private int lives_remaining=NTURNS;
	
	/**keeps track of the number of bricks present in the game window, takes in intitial value
	 * of the total number of bricks in the game window
	 */
	private int bricks_remaining=NBRICKS_PER_ROW*NBRICK_ROWS;
}
