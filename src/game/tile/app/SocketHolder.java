package game.tile.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;




public class SocketHolder {

	private static Socket s;
	
	public static Socket getS() {
		return s;
	}
	public static void setS(Socket s) {
		SocketHolder.s = s;
	}
	
	
	
	
}
