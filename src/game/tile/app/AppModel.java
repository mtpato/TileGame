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
	private int gameID;


	
	
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

	public String getReply() {
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

		
		if(string.length > 1) {
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

		

	

		v.setGameState(state);
		
		v.invalidate();

		//gameModel.printState(state);

	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
		
	}


	public void makeMove(int nodeID, DrawingView v) {
		sendMsg("makeMove:" + gameID + "," + nodeID);

		String compState = getReply();

		System.out.println(compState);
		
		drawBoard(gameModel.parseGameState(compState), v);
		
		
	}



	




}
