/* Gets information from the user and sends it to the server
 * @author Andrew Goss
 * @version 1.0
 */

package client;

import java.io.*;
import java.net.*;
//import uk.co.gossfunkel.playground.server.*;

public class GOut implements Runnable {

	private Console c;
	private Socket socky;

	private PrintWriter outGoing;
	private String input;

	public GOut(Console c, Socket socky) {

		this.c = c;
		this.socky = socky;

	}

	@Override
	/* runs thread
	 */
	public void run() {

		try {
	   		outGoing = new PrintWriter(socky.getOutputStream(), true);
    	} catch(IOException e) {
  	  		System.err.println("Could not construct outGoing: " + e);
    		System.exit(1);
    	}

		outGoing.println(Client.name());

		Boolean looping = true;
		while(looping) {
			input = c.readLine();
			outGoing.println(input);

			if (input.equals("exit") || Client.isClosing()) {
				System.out.println("Shutting down...");
				try {
					outGoing.println(input);
					socky.close();
				} catch (Exception e) {
					System.err.println ("Error closing socky socket!\n Reason: " + e);
					System.exit(0);
   				}
				looping = false;
				Client.close();
				outGoing.close();
			}
		}
		Client.outJoin();
		Client.inJoin();
		Client.close();
	}
}
