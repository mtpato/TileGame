package game.tile.app;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class GameBoardActivity extends Activity{
	private AppModel model;
	private int gameID;
	private GameState state;
	private String opName;


	/**
	 * Called when the activity is first created.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_board);

		Bundle b = this.getIntent().getExtras();
		
		model = new AppModel();
		model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
		
		model.setUserName((String) b.get("userName"));
		model.setUserID(b.getInt("userID"));
		
		model.setOpName((String) b.get("opName"));
		gameID = Integer.valueOf(b.get("game").toString());
		

	}
	
	 @Override
	    public void onResume() {
	        super.onResume();
	        
			model.setSocket(SocketHolder.getS());

			
			
		   // System.out.println("befor init model");   
	        if(!model.initIO()) {
	       // 	System.out.println("didnt init");
	        	finish();
	        } else {
	        	//System.out.println("inited IO");
	        }
	       // System.out.println("after init model"); 
		

			timer();
			runBoard();
	        
	    }
    
    
    private void runBoard() {
    	//System.out.println("in run board");
    	state = model.getState(String.valueOf(gameID));
    	//System.out.println("after gettign state");
    	
    	if(state != null) {
    		//System.out.println("GOODDDDD-----------");
        	DrawingView v = (DrawingView)findViewById(R.id.boardLayout);
        	
        	v.setModel(model);
        	model.setGameID(gameID);
    		model.drawBoard(state, v);
    		
    	} else {
    		//System.out.println("FAILLLLLL-----------");
    		finish();
    		
    	}

    	//System.out.println("after drawing");
		
		 

	}
    

    /**
     *  this is the handler for the Runnable for the timer
     */
    private Handler timerHandler = new Handler();

    

    /**
     * this function initiates the runnable for the handler
     */
    public void timer() {
        
        timerHandler.removeCallbacks(getStateTimer);
        timerHandler.postDelayed(getStateTimer, 2000);

    }
    

    /**
     * 
     */
    public Runnable getStateTimer = new Runnable() {
    	public void run() {

    			runBoard();
    		    timerHandler.postDelayed(this, 2000);
    		
    	}		
    };	
    
    

    public void refreshButtonClick(View view) {
    	runBoard();
    	
    	

    	
     }
	

	/**
	 * newUserButtonclick
	 * 
	 * @param view
	 */
    public void cancelButtonClick(View view) {
    	
        finish();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	System.out.println("Destroy: GAME BOARD");
    	
    	timerHandler.removeCallbacks(getStateTimer);
    	
    }
}
