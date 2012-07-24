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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BetterDrawingView extends DrawingView{
	




	private GameState state;
	
	private HashMap<RectF, Integer> screenBoard = new HashMap<RectF, Integer>();
	
	private AppModel model;
	
	
	
	Bitmap tile;
	
	
	public BetterDrawingView(Context context) {
		super(context);
		
		Resources res = getResources();
		tile = BitmapFactory.decodeResource(res, R.drawable.tile);
		// TODO Auto-generated constructor stub
	}
	
	public BetterDrawingView(Context context, AttributeSet attrs) {
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

		
		TileGameState s = (TileGameState) state;
		
		screenBoard.clear();
		
		
		
		
		int hor = this.getWidth();
		int ver = this.getHeight();
		
		//int boarder = (hor /s.height) / 2;
		int boarder = 0;
		
		float hStep = (hor - boarder) / (float) s.width;
		float vStep = (ver - boarder) / ((float) s.height / 2);
		
		hStep = (hor - hStep) / (float) s.width;//might be able to do better
		vStep = (ver - vStep/2) / ((float) s.height / 2);//might be able to do better
	
		
		ArrayList<TileNode> nodes = new ArrayList<TileNode>(s.tiles.values()); 
		Collections.sort(nodes, new TileNodeComparator());
		
		for(TileNode n : nodes) {
			
			if(vStep < hStep) {
				drawTileState(s, n, canvas, vStep, (hStep - vStep) * s.height, boarder, true);
			} else {
				drawTileState(s, n, canvas, hStep, (vStep - hStep) * s.width, boarder, false);
			}
			
			
			
		}
		
		for(TileNode n : nodes) {
		
			if(vStep < hStep) {
				drawNode(s, n, canvas, vStep, (hStep - vStep) * s.height, boarder, true);
			} else {
				drawNode(s, n, canvas, hStep, (vStep - hStep) * s.width, boarder, false);
			}
			
			
			
		}
		
		Paint paint = new Paint();
		
		
		if(s.over) {
			paint.setTextScaleX(2);
			paint.setColor(Color.WHITE);
			String winner = model.getWinner(s);
			canvas.drawText("WINNER " + winner + "!", 25, ver/2, paint);
			
		}
		
		
	}

    private void buildBoardLayout() {
    	int w = this.getWidth();
		int h = this.getHeight();
		
		
		//game.tile.app.BetterDrawingView
		
	}

	private void drawNode(TileGameState s, TileNode n, Canvas canvas, float step, float f, int boarder, boolean vIsSmaller) {
        Paint paint = new Paint();
        
        paint.setColor(Color.RED);
      
        RectF tRect;
    	
        float halfStep = step / 2;
        
        
        
		tRect = new RectF(n.tileX * step,
				 n.tileY * halfStep,
				 (float) (n.tileX * step + step * 1.45),
				 (float) (n.tileY * halfStep + step * 1.1));

        
        //System.out.println(tRect.height() + " " + tRect.width());
        
  
		
    	
		
		
		canvas.drawBitmap(tile, null, tRect, paint);
    	
		paint.setColor(Color.GREEN);
		
		RectF click = new RectF((float) (tRect.centerX() - ((float) halfStep * 0.8)),
				 tRect.centerY() - halfStep,
				 (float) (tRect.centerX() + (float ) halfStep * 0.8),
				 tRect.centerY() + halfStep);
		
		//canvas.drawRect(click, paint);//paints click zone
		
		paint.setTextSize(20);
    	
    	//canvas.drawText(String.valueOf(n.tileX) + String.valueOf(n.tileY), oval.centerX(), oval.centerY(), paint);
    	
    	
    	screenBoard.put(click, n.nodeID);
    	

    	
		
	}

	private void drawTileState(TileGameState s, TileNode n, Canvas canvas, float vStep, float f, int boarder, boolean vIsSmaller) {
		Paint paint = new Paint();
		
		paint.setColor(Color.CYAN);
	      
        RectF oval;
    	
        float halfStep = vStep / 2;
		
		if(n.owner == model.getUserID()) {
			
			if(n.active) {
				paint.setColor(Color.CYAN);
			} else {
				
				paint.setColor(Color.rgb(204, 230, 255));
			}
					
		} else if(n.active){
			
			
				paint.setColor(Color.YELLOW);
			
			
		} else {
			paint.setColor(Color.TRANSPARENT);
	
		}
		
		oval = new RectF(n.tileX * vStep,
				 n.tileY * halfStep,
				 (float) (n.tileX * vStep + vStep * 1.45),
				 (float) (n.tileY * halfStep + vStep * 1.1));
		
		canvas.drawOval(oval, paint);
		//canvas.drawRect(oval, paint);
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean clicked = false;

		float x = event.getX();
		float y = event.getY();
		//System.out.println(x + " " + y);
		

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
				DrawingView v = (DrawingView)findViewById(R.id.boardLayout);

				((GameActivity) this.activity).runBoard();
			}

		}

		return true; // processed
	}
	
	public boolean handleTouchEvent(float x, float y) {
		
		
		
		for (RectF o : screenBoard.keySet()) {
			
			if (o.contains(x, y) && model.validMove(screenBoard.get(o), state)) {
				model.makeMove(screenBoard.get(o),this);
				
				return true;
			}

		}
		
		return false;
	
	}
    

	// @Override
    public void update(Observable arg0, Object arg1) {
        this.invalidate();
        
    }
    
}
