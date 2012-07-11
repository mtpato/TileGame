package game.tile.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameBoardActivity extends Activity{
	private AppModel model;
	
    /**
     * Called when the activity is first created.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);

        
        model = new AppModel();
        
        // if(this.getIntent().getExtras().get("socket").equals("true")){
         model.setSocket(SocketHolder.getS());
         model.setUserName((String) this.getIntent().getExtras().get("userName"));
         
         model.initIO();
        
         System.out.println(this.getIntent().getExtras().get("game"));
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
