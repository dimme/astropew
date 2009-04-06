package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForwardingServer {
	
	public ForwardingServer(int port) throws SocketException {
		DatagramSocket sock = new DatagramSocket(port);
		
		DatagramPacket p = new DatagramPacket(new byte[65000], 65000);
		try {
			sock.receive(p);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(),e);
			System.exit(1);
		}
		
	}
}
