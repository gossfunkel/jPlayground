/* Receives data from multiple playground.client s
 * @author Andrew Goss
 * @version 1.0
 */

package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
//import uk.co.gossfunkel.playground.server.*;

public class ServerPlay {
	
	public static Console c;
	private static ServerSocket server;
	private static InetAddress group;
	private static MulticastSocket multiServ;

	private static Runnable resp;

	public static Boolean closing = false;

	public static List<Runnable> clients = new ArrayList<Runnable>();
	public static BlockingQueue<String> messages = new LinkedBlockingQueue<String>();

	public static String servinp = " ";

	ServerPlay() throws FileNotFoundException, IOException {
		
		// create a console input system
		c = System.console();
		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}

		Runnable serv = new ProcessInput();
		Thread tmp = new Thread(serv);
		tmp.setDaemon(true);
		tmp.start();

		try{
			server = new ServerSocket(1042);
			// group = InetAddress.getByName(/**/);
			// add multicast functionality for broadcast messages
		}
		catch (IOException e) {
			System.err.println("Could not listen on port 1042.");
			System.exit(1);
		}

		mainLoop();
	}

	/* main loop
	 */
	public static void mainLoop() {

		Boolean looping = true;
		while(looping) {
			if (closing) {
				System.out.println("Shutting down.");
				looping = false;
			} else {
				//System.out.println("looping");
			}
			try {
				resp = null;
				resp = new RespondToClient(server);
				new Thread(resp).start();
				clients.add(resp);
			} catch (Exception e) {
				System.err.println("Accept failed: 1042");
				System.exit(-1);
			}
		}
		exitServer();

	}

	/* cleans up and calls System.exit() with a safe return type of 0
	 */
	public static void exitServer() {
		// do garbage disposal here
		closing = true;
		try {
			server.close();
		} catch (IOException e) {
			System.err.println("Server.close in exitServer failed: " + e);
		}
		System.exit(0);
	}
	
	public static void main(String args[]) throws FileNotFoundException, 
	  IOException {
		System.out.println("Startup initiated");
		@SuppressWarnings("unused")
		ServerPlay serverPlay = new ServerPlay();
	}

}
