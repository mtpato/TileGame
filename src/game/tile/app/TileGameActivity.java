package game.tile.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}