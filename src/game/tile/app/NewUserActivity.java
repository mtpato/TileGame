package game.tile.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewUserActivity extends Activity{
	private AppModel model;
	
    /**
     * Called when the activity is first created.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_main);

        Bundle b = this.getIntent().getExtras();
        
        model = new AppModel();
		model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        
		
        if(b.getBoolean("socket")) {
    		model.setSocket(SocketHolder.getS());
    		model.initIO();
        } else {
    		model.doToast("there was an issue connection\n" +
    				"the the server. Please check \n" +
    				"your internet connection and \n" +
    				"try again.", this);
        }


    }
    
    
    public void createUserButtonClick(View view) {
        System.out.println("newUserClick");
        TextView userView = (TextView) findViewById(R.id.userName);
        TextView passwordView = (TextView) findViewById(R.id.password);
        TextView emailView = (TextView) findViewById(R.id.emailField);
        
        String pass = passwordView.getText().toString();
        String user = userView.getText().toString();
        String email = emailView.getText().toString();
        
        if(model.isValidPass(pass) &&
           model.isValidUser(user) &&
           model.isValidEmail(email) &&
           model.makeNewUser(user,pass,email,this)) {
        	
        	model.doToast("User was created!", this);


        	finish();
        	
        } else {
        	
        	userView.setText("");
        	passwordView.setText("");
        	emailView.setText("");
        	
        }
        

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
