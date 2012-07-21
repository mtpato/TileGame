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




public class TileGameActivity extends Activity {
	
	public static final String PREF_NAME = "tilePrefs";

	AppModel model;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		model = new AppModel();
		model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

		// if(this.getIntent().getExtras().get("socket").equals("true")){
		model.setSocket(SocketHolder.getS());

	       
        if(!model.initIO()) {
        	Intent intent=new Intent();
            intent.putExtra("msg", "noInet");
            setResult(RESULT_OK, intent);
        	finish();
        }

		// run();
	}
	
	 @Override
	    public void onResume() {
	        super.onResume();
	        
			model.setSocket(SocketHolder.getS());

		       
	        if(!model.initIO()) {
	    		Intent intent=new Intent();
	            intent.putExtra("msg", "noInet");
	            setResult(RESULT_OK, intent);
	        	finish();
	        }
	        
	    }

	private void run() {

    	if(model.keyLogin(this)) {
    		SharedPreferences prefs = getSharedPreferences(TileGameActivity.PREF_NAME, 1);
    		
    		startUpGamesMenuActivity( prefs.getString("userName", "fail"));
    		
    		
    	}
	
	}


	/**
     * newUserButtonclick
     * 
     * @param view
     */
    public void newUserButtonClick(View view) {
        System.out.println("newUserClick");
        Intent newUser = new Intent(TileGameActivity.this, NewUserActivity.class);
        
    	SocketHolder.setS(model.getSocket());
    	
    	
    	if(model.getSocket() != null && model.getSocket().isConnected()) {
    		newUser.putExtra("socket", true);
    	} else {
    		newUser.putExtra("socket", false);
    	}
    	
		// graph.putExtra("statsTime", model.getStatsTime());
		startActivity(newUser);
	}

	public void quitButtonClick(View view) {
		model.quitGame();

		//Intent i = new Intent(this, SplashActivity.class);
		//i.putExtra("quit", "true");
		
		Intent intent=new Intent();
        intent.putExtra("msg", "quit");
        setResult(RESULT_OK, intent);
		
		finish();
	}

	public void loginButtonClick(View view) {
		System.out.println("newUserClick");
		TextView userView = (TextView) findViewById(R.id.userField);
		TextView passwordView = (TextView) findViewById(R.id.passwordField);
        
        
        if(model.checkLogin(userView.getText().toString(),passwordView.getText().toString(), this)) {
        	startUpGamesMenuActivity(userView.getText().toString());
        	
        	
        } else {
        	
        	userView.setText("");
        	passwordView.setText("");
        	
        }
        

    }


	private void startUpGamesMenuActivity(String userName) {
		model.setUserName(userName);
		
		Intent gamesMenu = new Intent(TileGameActivity.this, GamesMenuActivity.class);
			
		//put the i/o in to the IOholder for passing

		SocketHolder.setS(model.getSocket());
		
		
		gamesMenu.putExtra("socket", true);
		gamesMenu.putExtra("userName", model.getUserName());
		startActivity(gamesMenu);
	}
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	
    	System.out.println("Destroy: LOGIN");

    	
    }
    



}