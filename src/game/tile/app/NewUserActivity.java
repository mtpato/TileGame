package game.tile.app;


import android.app.Activity;
import android.content.Intent;
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

        model = new AppModel();
		model.setSocket(SocketHolder.getS());
		model.initIO();
        
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
           model.makeNewUser(user,pass,email)) {
        	
        	model.doToast("User was created!", this);


        	finish();
        	
        } else {
        	
        	userView.setText("");
        	passwordView.setText("");
        	emailView.setText("");
        	
        	
        	
        	model.doToast("This was not a valid Username, Password,\n" +
        				  "or email. Please try a different one ", this);
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
