package game.tile.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScoreDrawingView extends DrawingView{
	




	private GameState state;
	
	private HashMap<RectF, Integer> screenBoard = new HashMap<RectF, Integer>();
	
	private AppModel model;
	
	RectF exitButton;
	
	Bitmap tile;
	
	
	
	
	
	public ScoreDrawingView(Context context) {
		super(context);
		
		Resources res = getResources();
		tile = BitmapFactory.decodeResource(res, R.drawable.tile);

	
		// TODO Auto-generated constructor stub
	}
	
	public ScoreDrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Resources res = getResources();
		tile = BitmapFactory.decodeResource(res, R.drawable.tile);
		
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
		
		int w = this.getWidth();
		int h = this.getHeight();
		
		TileGameState s = (TileGameState) state;
		
		
		System.out.println("in right draw: " +  w + " " + h);
		
		
		
		RectF opScoreRect = new RectF((w * 4) / 20,
									(h * 2) / 6,
									(w * 19) / 20,
				 					(h * 3) / 6); 
		
		RectF userScoreRect = new RectF(w / 20,
										(h * 3) / 6,
										(w * 16) / 20,
										(h * 4) / 6); 
	
		
		//System.out.println(scoreRect);
		
		Paint p = new Paint();
		
		canvas.drawBitmap(tile, null, opScoreRect, p);
		canvas.drawBitmap(tile, null, userScoreRect, p);
		
		p.setColor(Color.WHITE);
		
		p.setTextSize(32);
		
		p.setTypeface(Typeface.DEFAULT_BOLD);
		
		
		p.setColor(Color.CYAN);

		canvas.drawText(String.valueOf(s.scores.get(model.getUserID())), userScoreRect.centerX(), 
				userScoreRect.bottom - userScoreRect.height() /4, p);

		p.setColor(Color.YELLOW);
		
		
		for(int player : s.players) {
			if (player != model.getUserID()) {
				canvas.drawText(String.valueOf(s.scores.get(player)), opScoreRect.left, opScoreRect.centerY(), p);
				
			}
		}
		
		if(exitButton == null) {
			exitButton = new RectF(w / 10,
					(h * 5) / 6,
					(w * 9) / 10,
					h); 
		}

		
		
		p.setColor(Color.GRAY);
		
		canvas.drawRect(exitButton, p);
		
		p.setColor(Color.BLACK);
		canvas.drawText(String.valueOf("EXIT"), exitButton.left + w / 10, exitButton.centerY(), p);
		
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
			if(handleTouchEvent(x, y)) {

				this.activity.finish();
				
			}

		}
		 
		return true; // processed
	}
	
	public boolean handleTouchEvent(float x, float y) {
		
		if(exitButton.contains(x, y)) return true;
		else return false;
		

	
	}
    

	// @Override
    public void update(Observable arg0, Object arg1) {
        this.invalidate();
        
    }
    
}
