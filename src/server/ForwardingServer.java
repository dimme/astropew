package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForwardingServer extends Thread {
	private DatagramSocket sock;
	
	public ForwardingServer(int port) throws SocketException {
		sock = new DatagramSocket(port);
	}
	
	public void run() {
		DatagramPacket p = new DatagramPacket(new byte[65000], 65000);
		try {
			
			while(true) {
				sock.receive(p);
			}

		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
		
	}
}
