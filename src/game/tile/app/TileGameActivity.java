package game.tile.app;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TileGameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    
    /**
     * newUserButtonclick
     * 
     * @param view
     */
    public void newUserButtonClick(View view) {
        System.out.println("newUserClick");
        Intent newUser = new Intent(TileGameActivity.this, NewUserActivity.class);
        
        //graph.putExtra("statsTime", model.getStatsTime());
        startActivity(newUser);
    }
    
    
    public void loginButtonClick(View view) {
        System.out.println("newUserClick");
        TextView userView = (TextView) findViewById(R.id.userField);
        TextView passwordView = (TextView) findViewById(R.id.passwordField);
        
        if(checkLogin(userView.getText().toString(),passwordView.getText().toString())) {
        	//go to the games screen
        	
        	Intent newUser = new Intent(TileGameActivity.this, NewUserActivity.class);
            
            //graph.putExtra("statsTime", model.getStatsTime());
            startActivity(newUser);
        	
        	
        } else {
        	
        	userView.setTag("");
        	passwordView.setText("");
        	
        	
        	Context context = getApplicationContext();
        	CharSequence text = "UserName and Password were incorrect\nPlease try again";
        	int duration = Toast.LENGTH_SHORT;
        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
        }
        

    }


	private boolean checkLogin(String userName, String Password) {
		//check here for login info
		//send it to server for confirmation
		
		if(userName.length() > 1) {
			return true;
		}
		return false;
	}
}