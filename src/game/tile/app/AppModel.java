package game.tile.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
	private int port = 4356;
	private String hostname = "10.0.2.2";

	private String gameName = "tileGame";

	private String userName;

	HashMap<Integer, String> games;

	private GameModel gameModel = new TileModel();

	private HashMap<RectF, Integer> screenBoard = new HashMap<RectF, Integer>();

	boolean fingerOn = false;
	
	public boolean init() {

		socket = connectToServer(hostname, port);

		System.out.println("got socket");
		initIO();

		if (!getReply().equals("done")) {
			return init();
		}

		sendMsg(gameName);

		if (!getReply().equals("done")) {
			return false;
		}

		return true;

	}

	private Socket connectToServer(String hostname, int port) {
		Socket s = null;

		try {
			s = new Socket(hostname, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;

	}

	public boolean checkLogin(String userName, String password) {
		// check here for login info
		// send it to server for confirmation

		sendMsg("login:" + userName.trim() + "," + password.trim());

		if (getReply().equals("done")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void initIO() {
		// log = new ErrorLogger("errorLog.txt", this.getClass().toString());//
		// init
		// the
		// error
		// logger
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

	public void sendMsg(String msg) {
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

	public void getGames() {
		games = new HashMap<Integer, String>();

		sendMsg("getGames");

		String[] string = getReply().split(":");

		String[] gamesStrings = string[1].split(",");

		for (String g : gamesStrings) {
			String[] players = g.split("\\|");

			int gameID = Integer.valueOf(players[0]);

			for (int i = 1; i < players.length; i++) {

				String user = players[i].split("-")[0];

				if (!user.equals(userName)) {
					games.put(gameID, user);
				}
			}

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
		sendMsg("gameState:" + gameID);

		String compState = getReply();

		System.out.println(compState);
		return gameModel.parseGameState(compState.split(":")[1]);
	}

	public void drawBoard(GameState state, DrawingView v) {

		

		updateScreenBoard(state, v);

		v.setGameState(state);
		v.setScreenBoard(screenBoard);
		v.invalidate();

		gameModel.printState(state);

	}

	private void updateScreenBoard(GameState state, DrawingView v) {
		TileGameState s = (TileGameState) state;

		// int buffer =

		int w = v.getWidth();
		int h = v.getHeight();

		int hStep = h / s.height + 1;
		int vStep = w / s.width + 1;

		for (TileNode n : s.tiles.values()) {

			makeRects(n, hStep, vStep);

		}

	}

	private void makeRects(TileNode n, int hStep, int vStep) {
	   	int buffer = 5;
		RectF oval = new RectF(n.tileX * hStep + buffer, 
				  n.tileY * vStep + buffer, 
				  n.tileX * hStep + hStep - buffer, 
				  n.tileY * vStep + vStep- buffer);
		
		screenBoard.put(oval, n.nodeID);
		
	}

	public void handleTouchEvent(float x, float y) {
		if(!fingerOn) {
			
			for(RectF o : screenBoard.keySet()) {
				
				if(o.contains(x, y)) {
					//make the move on the server
					//update the state
					//update the board 
				}
				
			}
			
			
			
			
		}
		
	}

}
