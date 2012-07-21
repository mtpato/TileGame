package game.tile.app;



import java.util.HashSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GamesMenuActivity extends Activity{
	private AppModel model;
	private HashSet<Button> buttons;
	
    /**
     * Called when the activity is first created.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_menu);

        model = new AppModel();
        model.setConnectMan((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        
       // if(this.getIntent().getExtras().get("socket").equals("true")){
        model.setSocket(SocketHolder.getS());
        model.setUserName((String) this.getIntent().getExtras().get("userName"));
        
        buttons = new HashSet<Button>();
       
        if(!model.initIO()) {
        	finish();
        }
  
       
        

       
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        LinearLayout v = (LinearLayout)findViewById(R.id.gamesMenuLayout);
 	   

        removeGameButtons(v);
        
        run();
        
    }

	private void removeGameButtons(LinearLayout v) {
		for(Button b: buttons) {
        	v.removeView(b);
        }
        buttons.clear();
	}
    
    
   private void run() {
	   	if(model.getGames()) {
	   		displayGames();
	   	} else {
	   		finish();
	   	}
	   	
		

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
		    
		    
		    buttons.add(btn);
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
        i.putExtra("userID", model.getUserID());
        String[] temp = ((Button)v).getText().toString().split(":");
        
        i.putExtra("game", temp[0]);
        i.putExtra("opName", temp[1]);
        startActivity(i);
		
	}
	
	public void newGameButtonClick(View view) {

		makeNewGame();


	}
	
	
	private void makeNewGame() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Create New Game");
		alert.setMessage("Please enter the Username of the person you woudl like to play with");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Make Game", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String in = input.getText().toString();
		  		makeGameOnServer(in);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
		
	}


	protected void makeGameOnServer(String in) {

		
		
		if (model.makeNewgame(in)) {

			model.doToast("Game was created!", this);
			
	        LinearLayout v = (LinearLayout)findViewById(R.id.gamesMenuLayout);
	  	   

	        removeGameButtons(v);
	        
	        run();
			

		} else {

			model.doToast("There was a problem creating the game.\n" +
					"Please try again.", this);
		}

	}

	/**
	 * newUserButtonclick
	 * 
	 * @param view
	 */
	public void logoutButtonClick(View view) {
		/*
		 * Intent data = new Intent();
		 * 
		 * data.putExtra("celsius", celsius); data.putExtra("lowerThresh",
		 * lowerThresh); data.putExtra("upperThresh", upperThresh);
		 * 
		 * setResult(RESULT_OK, data);
		 */

		model.signOut(this);

		finish();
	}
	
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	System.out.println("Destroy: GAMESMENU");

    	
    }

    
}
