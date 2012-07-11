package game.tile.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class GameBoardActivity extends Activity{
	private AppModel model;
	private int gameID;
	private GameState state;

	/**
	 * Called when the activity is first created.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_board);

		model = new AppModel();
		model.setSocket(SocketHolder.getS());
		model.setUserName((String) this.getIntent().getExtras().get("userName"));

		model.initIO();

		gameID = Integer.valueOf(this.getIntent().getExtras().get("game").toString());
		
		run();
	}
    
    
    private void run() {
    	state = model.getState(String.valueOf(gameID));
    	
    	DrawingView v = (DrawingView)findViewById(R.id.boardLayout);
    	
		model.drawBoard(state, v);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//boolean clicked;

		float x = event.getX();
		float y = event.getY();
		System.out.println(x + " " + y);
		
/*
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			clicked = true;
			break;
		case MotionEvent.ACTION_MOVE:
			clicked = true;
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
		*/
		model.handleTouchEvent(x, y);

		return true; // processed
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
