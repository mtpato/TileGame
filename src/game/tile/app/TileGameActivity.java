package game.tile.app;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TileGameActivity extends Activity {
	
	AppModel model;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        run();
    }
    
    
    private void run() {
    	model = new AppModel();
    	model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

    	if(!model.init()){
    		model.doToast("there was an issue connection\n" +
    				"the the server. Please check \n" +
    				"your internet connection and \n" +
    				"try again.", this);
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
    	
    	
        
        //graph.putExtra("statsTime", model.getStatsTime());
        startActivity(newUser);
    }
    
    public void quitButtonClick(View view) {
    	model.quitGame();
       finish();
    }
    
    
    public void loginButtonClick(View view) {
        System.out.println("newUserClick");
        TextView userView = (TextView) findViewById(R.id.userField);
        TextView passwordView = (TextView) findViewById(R.id.passwordField);
        
        
        
        if(model.checkLogin(userView.getText().toString(),passwordView.getText().toString(), this)) {
        	model.setUserName(userView.getText().toString());
        	
        	Intent gamesMenu = new Intent(TileGameActivity.this, GamesMenuActivity.class);
            	
        	//put the i/o in to the IOholder for passing

        	SocketHolder.setS(model.getSocket());
        	
        	
            gamesMenu.putExtra("socket", true);
            gamesMenu.putExtra("userName", model.getUserName());
            startActivity(gamesMenu);
        	
        	
        } else {
        	
        	userView.setText("");
        	passwordView.setText("");
        	
        }
        

    }



}