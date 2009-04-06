package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender {
	
	DatagramSocket sock;
	ExecutorService exec;
	
	public PacketSender() throws SocketException {
		exec = Executors.newSingleThreadExecutor();
		sock = new DatagramSocket();
	}
	
	public synchronized void send(byte[] data, Client c) {
		exec.submit( new SendTask(data, c) );
	}
	
	private class SendTask implements Runnable {
		
		private byte[] data;
		private Client c;
		
		public SendTask(byte[] data, Client c) {
			this.data = data;
			this.c = c;
		}
		
		public void run() {
			c.dg.setData(data);
			try {
				sock.send(c.dg);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
			
	}
}
