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
	// or when �invalidate()� is called
	protected void onDraw(Canvas canvas) {
		TileGameState s = (TileGameState) state;
		
		//int buffer = 
		
		
		int h = this.getWidth();
		int w = this.getHeight();
		
		int hStep = h / s.height + 1;
		int vStep = w / s.width + 1;
		
		for(TileNode n : s.tiles.values()) {
			
			
			
			drawNode(n, canvas, hStep, vStep);
			
		}
		
		
	}

    private void drawNode(TileNode n, Canvas canvas, int hStep, int vStep) {
        Paint paint = new Paint();
        
        paint.setColor(Color.RED);
      
    	
    	int buffer = 10;
    	
		RectF oval = new RectF(n.tileX * hStep - buffer, 
							  n.tileY * vStep + buffer, 
							  n.tileX * hStep + hStep + buffer, 
							  n.tileY * vStep + vStep- buffer);
    	
		if(n.active) {
			paint.setColor(Color.RED);			
		} else {
			paint.setColor(Color.GRAY);
		}
		
		canvas.drawOval(oval, paint);
    	
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
    	
    	canvas.drawText(String.valueOf(n.owner), oval.centerX(), oval.centerY(), paint);
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
			clicked = true;
			break;
		case MotionEvent.ACTION_MOVE:
			clicked = false;
			break;
		case MotionEvent.ACTION_UP:
			clicked = false;
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
			
			if (o.contains(x, y)) {
				// make the move on the server
				System.out.println(o);
				System.out.println("###############in a rect--------------------------");
				model.makeMove(screenBoard.get(o),this);
				

			}

		}
	
	}
    

	// @Override
    public void update(Observable arg0, Object arg1) {
        this.invalidate();
        
    }
    
}