package game.tile.app;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




public class SplashActivity extends Activity {
	
	public static final String PREF_NAME = "tilePrefs";
	
	AppModel model;
	
	private String appState = "start";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);
        
        
        run();
    }
    
    
    private void run() {
    	model = new AppModel();
    	model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

    	
    	conTimer();
    	
	
	}
    
    /**
     *  this is the handler for the Runnable for the timer
     */
    public Handler timerHandler = new Handler();

    

    /**
     * this function initiates the runnable for the handler
     */
    public void conTimer() {
        
        timerHandler.removeCallbacks(getConTimer);
        timerHandler.postDelayed(getConTimer, 1000);

    }
    

    /**
     * 
     */
    private Runnable getConTimer = new Runnable() {
    	public void run() {

        	if(!tryConnent()){//keep trying this every second or whatever
        		
        		timerHandler.postDelayed(this, 2000);
        	} else {
        		startGame();
        	}
    		
    			
    		    
    		
    	}		
	};

	public boolean tryConnent() {
		if (!model.init()) {// keep trying this every second or whatever
			model.doToast("there was an issue connection\n"
					+ "the the server. Please check \n"
					+ "your internet connection and \n" + "try again.", this);

			return false;
		} else {
			return true;
		}

	}
	
	protected void startGame() {
    	if(model.keyLogin(this)) {
    		SharedPreferences prefs = getSharedPreferences(TileGameActivity.PREF_NAME, 1);
    		
    		startUpGamesMenuActivity( prefs.getString("userName", "fail"));
    		
    		
    	} else {
    		startTileGameActivity();
    	}
	}
	
	private void startUpGamesMenuActivity(String userName) {
		appState = "gamesMenu";
		
		model.setUserName(userName);
		
		Intent gamesMenu = new Intent(SplashActivity.this, GamesMenuActivity.class);
			
		//put the i/o in to the IOholder for passing

		SocketHolder.setS(model.getSocket());
		
		
		gamesMenu.putExtra("socket", true);
		gamesMenu.putExtra("userName", model.getUserName());
		startActivity(gamesMenu);
	}

	protected void startTileGameActivity() {
		appState = "login"; //set to app started 
		
		Intent gamesMenu = new Intent(SplashActivity.this, TileGameActivity.class);
		
		//put the i/o in to the IOholder for passing

		SocketHolder.setS(model.getSocket());
		
		
		gamesMenu.putExtra("socket", true);
		gamesMenu.putExtra("userName", model.getUserName());
		startActivity(gamesMenu);
		
		

	}
	
	
	
    @Override
    public void onResume() {
        super.onResume();

        if(appState.equals("login")) {
        	finish();
        } else if(appState.equals("gamesMenu")){
        	startGame();
        }
       
        
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	System.out.println("Destroy: SPLASH");

    	
    }
	

}