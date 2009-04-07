package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender {

	private DatagramSocket sock;
	private ExecutorService exec;
	
	
	public PacketSender() throws SocketException {
		this(new DatagramSocket());
	}
	
	public PacketSender(DatagramSocket sock) {
		exec = Executors.newSingleThreadExecutor();
		this.sock = sock;
	}
	
	public void send(byte[] data, DatagramPacket dgp) {
		exec.submit( new SendTask(data, dgp) );
	}
	
	public void stop() {
		exec.shutdown();
	}

	
	private class SendTask implements Runnable {
		
		private byte[] data;
		private DatagramPacket dgp;
		
		public SendTask(byte[] data, DatagramPacket dgp) {
			this.data = data;
			this.dgp = dgp;
		}
		
		public void run() {
			dgp.setData(data);
			try {
				sock.send(dgp);
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				throw new RuntimeException(new CatastrophicException(e));
			}
		}
			
	}
}