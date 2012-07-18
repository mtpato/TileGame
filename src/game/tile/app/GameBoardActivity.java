package game.tile.app;


import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
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
		model.setSocket(SocketHolder.getS());
		model.setUserName((String) b.get("userName"));
		model.setUserID(b.getInt("userID"));
		
		model.setOpName((String) b.get("opName"));
		model.initIO();

		gameID = Integer.valueOf(b.get("game").toString());
		
		run();
	}
    
    
    private void run() {
    	state = model.getState(String.valueOf(gameID));
    	
    	DrawingView v = (DrawingView)findViewById(R.id.boardLayout);
    	
    	v.setModel(model);
    	model.setGameID(gameID);
		model.drawBoard(state, v);
		
		
		 

	}
    
    
    

    public void refreshButtonClick(View view) {
    	run();

    	
     }

	

	/**
	 * newUserButtonclick
	 * 
	 * @param view
	 */
    public void cancelButtonClick(View view) {
       /* Intent data = new Intent();

        data.putExtra("celsius", celsius);
        data.putExtra("lowerThresh", lowerThresh);
        data.putExtra("upperThresh", upperThresh);

        setResult(RESULT_OK, data);
		*/
        finish();
    }
    
}
