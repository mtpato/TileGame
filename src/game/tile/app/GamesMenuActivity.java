package game.tile.app;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class GamesMenuActivity extends Activity{
	private AppModel model;
	
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
        model.setUserName((String) this.getIntent().getExtras().get("userName"));
        
        model.initIO();
        

        run();
        
    }
    
    
   private void run() {
	   	model.getGames();
	   	
		displayGames();

	}

	private void displayGames() {
		
	    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gamesMenuLayout);
	    
		System.out.println("about to add buttons");
		for(int gID: model.games.keySet()) {
			
			Button btn = new Button(this); 
		    btn.setText(gID + ":" + model.games.get(gID)); 
		   
	         btn.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 enterGame(v);
	             }
	         });
		    
		    
		    
		    linearLayout.addView(btn);
		}
		

	}

	protected void enterGame(View v) {
		System.out.println(((Button)v).getText());
		
		Intent i = new Intent(GamesMenuActivity.this, GameBoardActivity.class);
        
    	
    	
    	//put the i/o in to the IOholder for passing

    	SocketHolder.setS(model.getSocket());
    	
    	
        i.putExtra("socket", true);
        i.putExtra("userName", model.getUserName());
        String[] temp = ((Button)v).getText().toString().split(":");
        
        i.putExtra("game", temp[0]);
        startActivity(i);
		
	}


	/**
	 * newUserButtonclick
	 * 
	 * @param view
	 */
	public void cancelButtonClick(View view) {
		/*
		 * Intent data = new Intent();
		 * 
		 * data.putExtra("celsius", celsius); data.putExtra("lowerThresh",
		 * lowerThresh); data.putExtra("upperThresh", upperThresh);
		 * 
		 * setResult(RESULT_OK, data);
		 */

		model.sendMsg("signOut");

		finish();
	}
    
}
