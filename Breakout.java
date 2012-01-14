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
		setup();
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
	
	
	
	
	/** keeps track of the row number using 0 as its initial value
	 * 0 indicates the first row
	 */
	private int row_no=0;
	
	/** keeps the track of the last recorded x  coordinate of the paddle */
	private double last;
	
	/** paddle and ball are used within multiple methods, that is why it is kept as an instance variable*/
	private GRect paddle;
	private GOval ball;
}
