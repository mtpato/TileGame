package game.tile.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author michaelpato
 * 
 *         this class does all the login for the app
 * 
 */
public class AppModel {
	

	// Communication
	private BufferedReader in;
	private BufferedWriter out;

	private Socket socket;
	private int port = 4356;//test server
	//private int port = 8123; // local machine
	private String hostname = "98.204.99.45";

	private String gameName = "tileGame";

	private String userName;
	private int userID;
	
	private String opName;

	HashMap<Integer, String> games;
	HashMap<String, Integer> users;

	private GameModel gameModel = new TileModel();
	private int gameID;
	
	private ConnectivityManager connectMan;

	private boolean connected;

	
	
	public boolean init() {

		
		if (isConnectedToInet()) {
			socket = connectToServer(hostname, port);

			System.out.println("got socket");
			if(socket != null && socket.isConnected()) {
				initIO();

				if (!getReply().equals("done")) {
					//return init();
				}

				
				if (!callServer(gameName).equals("done")) {
					return false;
				}
				connected = true;

				return true;
			}
			return false;
	
		}
		
		return false;

	}

	private Socket connectToServer(String hostname, int port) {
		Socket s = new Socket();
		SocketAddress sockaddr = new InetSocketAddress(hostname, port);
		
		
		try {
			s.connect(sockaddr, 2000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;

	}
	
	
	

	public boolean checkLogin(String userName, String password,  Activity a) {
		// check here for login info
		// send it to server for confirmation
		String reply = callServer("login:" + userName.trim() + "," + password.trim());

		String[] splitReply = reply.split(":");
		
		if (splitReply[0].equals("done")) {
			savePref("authKey", splitReply[1], a);
			savePref("userName", userName.trim(), a);
			
			
			return true;
		} else if(splitReply[0].equals("error")){
			if(splitReply.length == 1) {
				doToast("UserName and Password were incorrect\nPlease try again", a);
				return false;
			}else if(splitReply[1].equals("noInet") || splitReply[1].equals("noServer")){
				doToast("there was an issue connection\n" +
	    				"the the server. Please check \n" +
	    				"your internet connection and \n" +
	    				"try again.", a);
				
				return false;
			}
			
			
		} 
		
		
		return false;
	
	}



	private void savePref(String key, String value, Activity a) {
		SharedPreferences prefs = a.getSharedPreferences(TileGameActivity.PREF_NAME, 1);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
		
	}

	/**
	 * 
	 */
	public boolean initIO() {
		
		
		if (isConnectedToInet() && socket != null && socket.isConnected()) {
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			} catch (IOException e) {
				// log.log("problem creating the input buffer trace: " +
				// e.toString());
				e.printStackTrace();
			}
			try {
				out = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
			} catch (IOException e) {
				// log.log("problem creating the output buffer trace: " +
				// e.toString());

				e.printStackTrace();
			}
			
			return true;
		} else {

			return false;
		}

		
	}
	
	
	/**
	 * this method is synchronized. it calls the server and gets a
	 * reply from it. it is synchronized to make sure that the 
	 * multiple threads calling the server dont get each others 
	 * replys from the server 
	 * 
	 * 
	 * ISSUE: need to add checks here to make sure that the 
	 * server is alive and that there is an Internet connection 
	 * 
	 * 
	 * @param msg the msg to be sent to the server 
	 * @return the reply from the server
	 */
	private synchronized String callServer(String msg) {
		if(!isConnectedToInet()){
			System.out.println("no net");
			
			return "error:noInet";
		}else if(socket == null || !socket.isConnected()) {
			System.out.println("no socket");
			
			return "error:noServer";
		} else {
			
			System.out.println("send Msg");
			sendMsg(msg);			
			return getReply();
		}	
	}

	/**
	 * this checks if the device is connected to the internet 
	 * 
	 * @return if the device is connected to the internet 
	 */
	private boolean isConnectedToInet() {

		NetworkInfo netInfo = connectMan.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;

	}

	private String getReply() {
		String msg = null;

		while (msg == null) {
			try {
				if (in.ready()) {
					msg = in.readLine();

				}

			} catch (IOException e) {
				/*
				 * log.log("problem reading line from socket. trace: " +
				 * e.toString());
				 */
				e.printStackTrace();
			}
		}

		return msg;

	}

	private void sendMsg(String msg) {
		if (msg != null && !msg.equals("")) {
			try {
				out.write(msg);
				out.write(("\n"));
				out.flush();
			} catch (Exception e) {

				// log.log("problem talking to socket. trace: " + e.toString());

				e.printStackTrace();
			}
		} else {
			// log.log("msg was null when sending.");

		}
	}

	public boolean getGames() {
		games = new HashMap<Integer, String>();
		users = new HashMap<String, Integer>();
		
		

		String[] string = callServer("getGames").split(":");

		if (string[0].equals("games")) {
			
			if(string.length > 1) {
				String[] gamesStrings = string[1].split(",");

				for (String g : gamesStrings) {
					String[] players = g.split("\\|");

					int gameID = Integer.valueOf(players[0]);

					for (int i = 1; i < players.length; i++) {

						String[] user = players[i].split("-");

						if (!user[0].equals(userName)) {
							games.put(gameID, user[0]);
						} else {
							userID = Integer.valueOf(user[1]);
						}
					}

				}
			}


			return true;

		} else if(string[0].equals("error")){		
			return false;
		} else {
			return false;
		}
		


	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public GameState getState(String gameID) {
		

		String compState = callServer("gameState:" + gameID);
		
		String[] split = compState.split(":");
		
		if(split[0].equals("error")) {
			
			return null;
		} else if(split[0].equals("state")) {
			return gameModel.parseGameState(split[1]);
		} else {
			return null;
		}
		
	
	}

	public void drawBoard(GameState state, DrawingView v) {


		v.setGameState(state);
		
		v.invalidate();


	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
		
	}


	public void makeMove(int nodeID, DrawingView v) {

		String compState = callServer("makeMove:" + gameID + "," + nodeID);

		//System.out.println(compState);
		
		drawBoard(gameModel.parseGameState(compState), v);
		
		
	}

	public boolean makeNewUser(String user, String pass, String email, Activity a) {
		
		
		String reply = callServer("newUser:" + user + "," + pass + "," + email);
		
		
		if (reply.equals("done")) {
			return true;
		} else if(reply.equals("error")){
			doToast("UserName, Password, or email were incorrect\nPlease try again", a);
			return false;
		} else if(reply.equals("error:noInet") || reply.equals("error:noServer")){
			doToast("there was an issue connection\n" +
    				"the the server. Please check \n" +
    				"your internet connection and \n" +
    				"try again.", a);
			return false;
		}
		
		return false;
	}
	
	



	public void doToast(String msg, Activity a) {
		Context context = a.getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
      
		toast.show();
	}

	/**
	 * returns if the password is a valid password
	 * 
	 * ISSUE: THIS IS NOT IMPLEMENTED AWAYS RETURNS TRUE
	 * MUST BE FIXED TO GO LIVE 
	 * 
	 * 
	 * @param pass
	 * @return
	 */
	public boolean isValidPass(String pass) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * returns if the user has supplied a valid username
	 * 
	 * ISSUE: THIS IS NOT IMPLEMENTED AWAYS RETURNS TRUE
	 * MUST BE FIXED TO GO LIVE 
	 * 
	 * @param user
	 * @return
	 */
	public boolean isValidUser(String user) {
		// TODO Auto-generated method stub
		return true;
	}

	
	/**
	 * returns if the user has supplied a valid email
	 * 
	 * ISSUE: THIS IS NOT IMPLEMENTED AWAYS RETURNS TRUE
	 * MUST BE FIXED TO GO LIVE 
	 * 
	 * @param user
	 * @return
	 */
	public boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean makeNewgame(String in) {
		
		String reply = callServer("newGame:" + in);
		
		//System.out.println("reply: " + reply);
		
		if(reply.equals("done:gameCreated")) {
			return true;
		}
		return false;
	}

	public int getGameID() {
		return gameID;
	}

	public int getUserID() {
		// TODO Auto-generated method stub
		return userID;
	}

	public void setUserID(int s) {
		// TODO Auto-generated method stub
		userID = s;
		
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public boolean validMove(int nodeID, GameState state) {
		TileGameState s = (TileGameState) state;
		TileNode t = s.tiles.get(String.valueOf(nodeID));
		
		//System.out.println(nodeID);
		//System.out.println(s.tiles);
		//System.out.println(t);
		
		if(s.turn != userID || t.active || t.owner != userID ) {
			return false;
		}
		
		return true;
	}

	public void signOut(Activity a) {
		
		SharedPreferences prefs = a.getSharedPreferences(TileGameActivity.PREF_NAME, 1);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove("authKey");
		editor.remove("userName");
		editor.commit();
		
		callServer("signOut");
		
	}

	public void quitGame() {
		
		callServer("quit");
		

	}

	public void setConnectMan(ConnectivityManager connectMan) {
		this.connectMan = connectMan;
	}

	public boolean keyLogin(Activity a) {
		SharedPreferences prefs = a.getSharedPreferences(TileGameActivity.PREF_NAME, 1);
		if(prefs.contains("userName") && prefs.contains("authKey")) {
			String reply = callServer("keyLogin:" + prefs.getString("userName", "fail") + "," + prefs.getString("authKey", "fail"));
			
			if(reply.equals("done")) {
				return true;
			}
			
		} 
			return false;

	}

	public String getWinner(TileGameState s) {
		boolean tie = false;
		
		int maxScore = -1;
		int winID = -1;
		int curScore;
		
		for(int p: s.scores.keySet()) {
			curScore = s.scores.get(p);
			if(curScore == maxScore) {
				tie = true;
			} else if (curScore > maxScore) {
				maxScore = curScore;
				winID = p;
			}

		}
		
		//this only habdles 2 player
		if(tie) {
			return "TIE";
		} else if(winID == userID) {
			return userName;
		} else {
			return opName;
		}
	}
	
	
	
	
	




}
