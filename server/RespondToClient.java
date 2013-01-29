/* Handles responses to a client
 * @author Andrew Goss
 * @version 1.0
 */

package server;

import java.io.*;
import java.net.*;
//import uk.co.gossfunkel.playground.server.*;

public class RespondToClient implements Runnable {

	private Socket socky;
	private BufferedReader inComing;
	private PrintWriter outGoing;
	private String name;

	private String clientData;

	public Boolean closing = false;

	RespondToClient(ServerSocket serverSocket) {
		try {
			socky = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed for " + serverSocket + " because " + e);
		}
		try {
			inComing = new BufferedReader(new InputStreamReader(socky.getInputStream()));
			outGoing = new PrintWriter(socky.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Locking to inComing/outGoing failed: " + e);
			System.exit(-1);
		}
		try {
			name = inComing.readLine();
		} catch (IOException e) {
			System.err.println("Read failed: " + e);
		}
		System.out.println(socky + " LOGIN: " + name);
	}
	
	@Override
	/* runs thread
	 */
	public void run() {

		Boolean looping = true;
		while (looping) {
			if (ServerPlay.closing) {
				closing = true;
			}
			try {
				clientData = inComing.readLine();
				clientData.trim();
			}
			catch (IOException e) {
				System.err.println("Read failed");
			}
			try{
				ServerPlay.messages.offer(name + ": " + clientData);
				if (clientData.equals(null)) {
					System.err.println("NULL INPUT");
					outGoing.println("ILLEGAL INPUT: NULL");
				} else if (clientData.equals("exit") || closing) {
					System.out.println(socky + " is disconnecting.");
					try {
						inComing.close();
						outGoing.close();
						socky.close();
					} catch (IOException e) {
						System.err.println("IOException closing inComing/outGoing/socky: " + e);
					}
					ServerPlay.clients.remove(this);
					return;
				} else if (clientData.startsWith("serv: ")) {
					ServerPlay.servinp = clientData.substring(6);
					return;
				} else {
					// outGoing.println("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
				}
			} //end try
			catch (Exception e) {
				System.out.println("Failed: " + e);
				System.exit(-1);
			}
		}

	}
	
	/* getter for name
	 * @return string name (the name of the appropriate client)
	 */
	public String getName() {
		return name;
	}

	/* sends message to outGoing
	 * @param message string sent to appropriate client
	 */
	public void sayToClient(String message) {

		// kind of like a setter.
 
 		outGoing.println(message);
 
 	}
 	
 	public String toString() {
 	
 		// fill this completely later.
 		String toStringReturn = getName() + "\n";
 		return toStringReturn;
 	
 	}
}
