package game.tile.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewUserActivity extends Activity{

	
    /**
     * Called when the activity is first created.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_main);

        
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
