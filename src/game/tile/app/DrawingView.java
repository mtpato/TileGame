package game.tile.app;

import java.util.HashMap;
import java.util.Observable;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View{
	




	private GameState state;
	
	private HashMap<RectF, Integer> screenBoard = new HashMap<RectF, Integer>();
	
	private AppModel model;
	
	public DrawingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	

	public void setModel(AppModel model) {
		this.model = model;
	}

	 /**
     * this is how the graph is drawn every time invalidate is called 
     * 
     * @param graphModel the graphModel to set
     */
    public void setGameState(GameState state) {
        this.state = state;
	}
    
    

	public void setScreenBoard(HashMap<RectF, Integer> screenBoard) {
		this.screenBoard = screenBoard;
	}

	// this method is called when the View is displayed
	// or when Òinvalidate()Ó is called
	protected void onDraw(Canvas canvas) {
		TileGameState s = (TileGameState) state;
		
		screenBoard.clear();
		
		int topBuffer = 30 ;
		
		
		int h = this.getWidth();
		int w = this.getHeight();
		
		int hStep = h / s.height + 1;
		int vStep = (w - topBuffer * 2)/ s.width + 1;
		
		 Paint paint = new Paint();
	        
	     
	     paint.setTextSize(30);
		
	     int printBuffer = 1;
	     
	     System.out.println("about to print the scores");
	     for(int p : s.players) {
	    	 System.out.println("drawingScore: " + p);
	    	 paint.setTextScaleX(1);
	    	 if(p == model.getUserID()) {
	    		 if(p == s.turn) {
	    			 paint.setTextScaleX(2);
	    			 
	    		 }
	    		 
	    		 paint.setColor(Color.RED);
		    	 canvas.drawText(model.getUserName() + ": " + s.scores.get(p), printBuffer, topBuffer, paint);
		 	    
	    	 } else  {
	    		 if(p == s.turn) {
	    			 paint.setTextScaleX(2);
	    			 
	    		 }
	    		 
	    		 paint.setColor(Color.CYAN);
		    	 canvas.drawText(model.getOpName() + ": " + s.scores.get(p), printBuffer, topBuffer, paint);

	    	 }
	    		 
	    	 
	    	 printBuffer+=250;
	     }
		
		
		for(TileNode n : s.tiles.values()) {
		
			drawNode(s, n, canvas, hStep, vStep, topBuffer);
			
		}
		
		
		if(s.over) {
			paint.setTextScaleX(2);
			
			String winner = model.getWinner(s);

			
		}
		
		
	}

    private void drawNode(TileGameState s, TileNode n, Canvas canvas, int hStep, int vStep, int topBuffer) {
        Paint paint = new Paint();
        
        paint.setColor(Color.RED);
      
    	int buffer = 10;
    	
		RectF oval = new RectF(n.tileX * hStep - buffer, 
							  n.tileY * vStep + buffer+ topBuffer, 
							  n.tileX * hStep + hStep + buffer, 
							  n.tileY * vStep + vStep - buffer + topBuffer);
    	
		if(n.owner == model.getUserID()) {
			
			if(n.active) {
				paint.setColor(Color.RED);
			} else {
				
				paint.setColor(Color.rgb(255, 140, 140));
			}
					
		} else if(n.active){
			
			
				paint.setColor(Color.CYAN);
			
			
		} else {
			paint.setColor(Color.LTGRAY);
		}
		
		canvas.drawOval(oval, paint);
    	
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
    	
    	//canvas.drawText(String.valueOf(n.owner), oval.centerX(), oval.centerY(), paint);
    	
    	
    	screenBoard.put(oval, n.nodeID);
    	

    	
		
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean clicked = false;

		float x = event.getX();
		float y = event.getY();
		System.out.println(x + " " + y);
		

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			clicked = false;
			break;
		case MotionEvent.ACTION_MOVE:
			clicked = false;
			break;
		case MotionEvent.ACTION_UP:
			clicked = true;
			break;
		case MotionEvent.ACTION_CANCEL:
			clicked = false;
			break;
		case MotionEvent.ACTION_OUTSIDE:
			clicked = false;
			break;
		default:
		}
		
		if(clicked) {
			handleTouchEvent(x, y);

			DrawingView v = (DrawingView)findViewById(R.id.boardLayout);

			model.drawBoard(state, v);
		}

		return true; // processed
	}
	
	public void handleTouchEvent(float x, float y) {
		
		
		
		for (RectF o : screenBoard.keySet()) {
			
			if (o.contains(x, y) && model.validMove(screenBoard.get(o), state)) {
				model.makeMove(screenBoard.get(o),this);
			}

		}
	
	}
    

	// @Override
    public void update(Observable arg0, Object arg1) {
        this.invalidate();
        
    }
    
}
