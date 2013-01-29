/* Receives and parses input from clients
 * @author Andrew Goss
 * @version 1.0
 */

package server;

//import uk.co.gossfunkel.playground.server.*;

public class ProcessInput implements Runnable {

	private RespondToClient[] clnts;
	private String currUserInp = ""; // initialised empty to prevent compiler complaint
	private String userData = "";

	ProcessInput() {

		Runnable local = new LocalInput();
		Thread tmp = new Thread(local);
		tmp.setDaemon(true);
		tmp.start();

	}

	@Override
	/* runs thread
	 */
	public void run() {

		Boolean looping = true;
		while(looping) {
			try {
				currUserInp = ServerPlay.messages.take();
				setUserData(currUserInp);
			} catch (InterruptedException e) {
				System.err.println("Could not take message from queue: " + e);
			}

			if (currUserInp != null) {
				System.out.println(currUserInp);

				clnts = new RespondToClient[ServerPlay.clients.size()];
				ServerPlay.clients.toArray(clnts);
				for (RespondToClient client : clnts) {
					client.sayToClient(currUserInp);
				}
			} //end if
		} //end while
	} //end run

	/* getter for userData
	 * @return string userData
	 */
	public String getUserData() {
		return userData;
	}

	/* setter for userData
	 * @param userData a string of input to the server
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}

}
