package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomingConnectionServer extends Thread {
	
	boolean running = true;
	DatagramSocket sock;
	PacketDecoder pd;
	
	public IncomingConnectionServer(int port, PacketDecoder pd) throws SocketException {
		sock = new DatagramSocket(port);
		this.pd = pd;
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
				
				pd.newConnection(b, p.getSocketAddress());
				
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
	}
}
