package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.Client;

public class IncomingConnectionServer extends Thread {
	
	boolean running = true;
	DatagramSocket sock;
	GameAdministration gadm;
	
	public IncomingConnectionServer(int port, GameAdministration gadm) throws SocketException {
		sock = new DatagramSocket(port);
		this.gadm = gadm;
	}
	
	/**
	 * Setup communication:
	 * client sends player name
	 * server sends id / world randomization seed
	 */
	
	public void run() {
		final int sz = 40;
		DatagramPacket p = new DatagramPacket(new byte[sz], sz);
		
		while (running) {
			p.setLength(sz);
			try {
				sock.receive(p);
				
				byte[] b = new byte[p.getLength()];
				System.arraycopy(p.getData(), 0, b, 0, b.length);
				String name = new String(b);
				
				gadm.newConnection(name, p.getSocketAddress());
				
				
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
	}
}
