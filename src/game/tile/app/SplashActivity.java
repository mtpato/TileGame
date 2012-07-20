package game.tile.app;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;




public class SplashActivity extends Activity {
	
	public static final String PREF_NAME = "tilePrefs";
	
	AppModel model;
	
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

    	if(!model.init()){//keep trying this every second or whatever
    		model.doToast("there was an issue connection\n" +
    				"the the server. Please check \n" +
    				"your internet connection and \n" +
    				"try again.", this);
    	} else if(model.keyLogin(this)) {
    		SharedPreferences prefs = getSharedPreferences(SplashActivity.PREF_NAME, 1);
    		
    		startUpGamesMenuActivity( prefs.getString("userName", "fail"));
    		
    		
    	}
	
	}



	private void startUpGamesMenuActivity(String userName) {
		model.setUserName(userName);
		
		Intent gamesMenu = new Intent(SplashActivity.this, GamesMenuActivity.class);
			
		//put the i/o in to the IOholder for passing

		SocketHolder.setS(model.getSocket());
		
		
		gamesMenu.putExtra("socket", true);
		gamesMenu.putExtra("userName", model.getUserName());
		startActivity(gamesMenu);
	}



}