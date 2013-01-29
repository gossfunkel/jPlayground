/* Connects and sends data to playground.server
 * @author Andrew Goss
 * @version 1.0
 */

package client;

import java.io.*;
import java.net.*;
//import uk.co.gossfunkel.playground.server.*;

public class Client {

	private Socket socky;
	private Console c;

	private static Thread in;
	public static void inJoin() { 
		try {
			in.join();
		} catch(InterruptedException e) {
			System.exit(1);
			System.out.println("Interrupted joining");
		}; 
	}
	private static Thread out;
	public static void outJoin() { 		try {
			out.join();
		} catch(InterruptedException e){
			System.exit(1);
			System.out.println("Interrupted joining");
		}; 
	}

	private static String name;
	public static String name() { return name; }
	private static String host;
	public static String host() { return host; }

	private static Boolean closing = false;
	public static Boolean isClosing() { return closing; }
	public static void close() { closing = true; }

	Client() throws FileNotFoundException, IOException {
		// create a console input system
		c = System.console();
		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}

		name = c.readLine("Login as: ");
		tOnic();

	}
	
	/* sends user input to socket. so called because it goes with gin.
	 */
	public void tOnic() {
		
		host = c.readLine("Enter hostname: ");
		try {
			socky = new Socket(host,1042);
		} catch(Exception e) {
			System.err.println("Creating socket failed: " + e);
			System.exit(1);
		}
		System.out.println("Connected to host at " + host);

		GIn inputThread = new GIn(socky);
		in = new Thread(inputThread);
		in.setDaemon(true);
		in.start();

		GOut outputThread = new GOut(c, socky);
		out = new Thread(outputThread);
		out.setDaemon(true);
		out.start();

		while (!closing) {
			// don't exit
		};
		System.out.println("past while loop in Client");
		
	}

	public static void main(String args[]) throws FileNotFoundException, 
	  IOException 	{
		System.out.println("Startup initiated");
		@SuppressWarnings("unused")
		Client client = new Client();
	}

}
