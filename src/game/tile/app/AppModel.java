package game.tile.app;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author michaelpato
 *
 *	this class does all the login for the app
 *
 */
public class AppModel {

	//Communication
	private BufferedReader in;
	private BufferedWriter out;
	
	private Socket socket;	
    int port = 4356;
    String hostname = "10.0.2.2";
    
    String gameName = "tileGame";
	
	public boolean init() {
	
		socket = connectToServer(hostname, port);

		
		System.out.println("got socket");
		initIO();
		
		
		
		if(!getReply().equals("done")) {
			return init();
		} 
		
		sendMsg(gameName);
		
		if(!getReply().equals("done")) {
			return false;
		} 
		
		return true;
		
	}
	
	private Socket connectToServer(String hostname, int port) {
		Socket s = null;
		
		try {
			s = new Socket(hostname,port);
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
		//check here for login info
		//send it to server for confirmation
		
		sendMsg("login:" + userName.trim()
				+ "," + password.trim());
		
		
		
		
		if(getReply().equals("done")) {
			return true;
		}
		return false;
	}

	
	/**
	 * 
	 */
	private void initIO() {
		//log = new ErrorLogger("errorLog.txt", this.getClass().toString());// init
																			// the
																			// error
																			// logger
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			//log.log("problem creating the input buffer trace: " + e.toString());
			e.printStackTrace();
		}
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			//log.log("problem creating the output buffer trace: " + e.toString());

			e.printStackTrace();
		}
	}
	
	private String getReply() {
		String msg = null;
			
		while(msg == null) {
			try {
				if (in.ready()) {
					msg = in.readLine();

					
				}

			} catch (IOException e) {
				/*log.log("problem reading line from socket. trace: "
						+ e.toString());
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

				//log.log("problem talking to socket. trace: " + e.toString());

				e.printStackTrace();
			}
		} else {
			//log.log("msg was null when sending.");

		}
	}
}
