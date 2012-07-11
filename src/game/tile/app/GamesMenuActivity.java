package game.tile.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GamesMenuActivity extends Activity{
	AppModel model;
	
    /**
     * Called when the activity is first created.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_menu);

        model = new AppModel();
        
       // if(this.getIntent().getExtras().get("socket").equals("true")){
        model.setSocket(SocketHolder.getS());

        
        model.initIO();
        run();
        
    }
    
    
   private void run() {
		// TODO Auto-generated method stub
		
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
    	
    	model.sendMsg("signOut");
    	
        finish();
    }
    
}
