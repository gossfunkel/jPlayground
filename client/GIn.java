/* Gets information from the server
 * @author Andrew Goss
 * @version 1.0
 */

package client;

import java.io.*;
import java.net.*;
//import uk.co.gossfunkel.playground.server.*;

public class GIn implements Runnable {

	private BufferedReader inFromServer;
	private String input;
	private String previousInput = "...";

	private Socket socky;

	public GIn(Socket socky) {

		this.socky = socky;

	}

	@Override
	/* runs thread
	 */
	public void run() {

		try {
			inFromServer = new BufferedReader(new InputStreamReader(socky.getInputStream()));
		} catch (IOException e) {
			System.err.println("Reading server input failing! " + e);
		}
		
		Boolean looping = true;
		while(looping) {
			try {
				input = inFromServer.readLine();
			} catch(IOException e) {
				if (e.equals("java.net.SocketException: Socket closed")) {
					System.out.println("Closing...");
				} else {
					System.err.println("Cannot read inFromServer: " + e);
				}
			}
			
			if (input == null) {
				System.out.println("Server is down.");
				Client.close();
			}

			if (Client.isClosing()) {
				System.out.println("Closing.");
				try {
					inFromServer.close();
					socky.close();
				} catch (IOException e) {
					System.err.println("IOException closing inFromServer/socky: " + e);
				}
				looping = false;
				break;
			} else if (input.equals(null) && previousInput.equals(null)) {
				System.out.println("Server has shut down.");
				Client.close();
			}else { 
				// If not closing, do this:

				System.out.println(input);
			}

			previousInput = input;
		}
		Client.inJoin();
		Client.outJoin();
		Client.close();
	}

}
