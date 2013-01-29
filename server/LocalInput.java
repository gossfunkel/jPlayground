/* Gets and parses local input
 * @author Andrew Goss
 * @version 1.0
 */

package server;

//import uk.co.gossfunkel.playground.server.*;

public class LocalInput implements Runnable {

	private String input;
	private RespondToClient[] clnts;

	@Override
	/* runs thread
	 */
	public void run() {

		Boolean looping = true;
		while (looping) {

			input = ServerPlay.c.readLine().toLowerCase();

			if (input.equals("exit now")) {
				System.out.println("Exiting under server command");
				ServerPlay.closing = true;
				looping = false;
				break;
			} else if (input.startsWith("kick ")) {
				String player = input.substring(5);
				System.out.println("Kicking player " + player);
				clnts = new RespondToClient[ServerPlay.clients.size()];
				ServerPlay.clients.toArray(clnts);
				String cliName;
				for (RespondToClient client : clnts) {
					cliName = client.getName();
					if (cliName.equals(player)) {
						client.closing = true;
					};
				}
			}
		}
	}

}
