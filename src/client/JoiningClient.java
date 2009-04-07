package client;

import common.CatastrophicException;

import java.util.logging.Level;
import java.util.logging.Logger;

import server.PacketSender;

public class JoiningClient {
	private boolean connected;
	
	//private NetworkThread nt;
	private PacketSender sender;

	public JoiningClient(String name, String hostname, int port) {
		try {
			ConsoleNetworkObserver cno = new ConsoleNetworkObserver();
			//nt = new NetworkThread(name, hostname, port);
			//nt.addNetworkObserver(cno);
			//nt.start();

			//ClientFrame cf = new ClientFrame(this);
		} /*catch (CatastrophicException ex) {
			Logger.getLogger(JoiningClient.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		} */catch (RuntimeException ex) {
			Logger.getLogger(JoiningClient.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}
	}

	public static void main(String args[]){
		if(args.length != 3){
				System.out.println("Proper use: adress port name");
				System.exit(1);
		}

		int port = Integer.parseInt(args[1]);

		new JoiningClient(args[2], args[0], port);

	   //Generate world with the seed. etc..
	}

	public void stop() {
		//nt.setRunning(false);
	}
}
