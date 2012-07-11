package game.tile.app;

import java.util.HashMap;
import java.util.Observable;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DrawingView extends View{
	
	private GameState state;
	private HashMap<RectF,Integer> screenBoard;
	
	public DrawingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
		
		//int buffer = 
		
		
		int w = this.getWidth();
		int h = this.getHeight();
		
		int hStep = h / s.height + 1;
		int vStep = w / s.width + 1;
		
		for(TileNode n : s.tiles.values()) {
			
			
			
			drawNode(n, canvas, hStep, vStep);
			
		}
		
		
	}

    private void drawNode(TileNode n, Canvas canvas, int hStep, int vStep) {
        Paint paint = new Paint();
        
        paint.setColor(Color.RED);
      
    	
    	int buffer = 5;
    	
		RectF oval = new RectF(n.tileX * hStep + buffer, 
							  n.tileY * vStep + buffer, 
							  n.tileX * hStep + hStep - buffer, 
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
    	
    	

    	
		
	}

	// @Override
    public void update(Observable arg0, Object arg1) {
        this.invalidate();
        
    }
    
}
