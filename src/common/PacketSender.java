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
		addTask( new SendTask(data, dgp) );
	}
	
	protected void addTask(Runnable task) {
		exec.submit(task);
	}
	
	public void stop() {
		exec.shutdown();
	}

	
	protected abstract class AbstractSendTask implements Runnable {
		
		protected byte[] data;
		
		public AbstractSendTask(byte[] data) {
			this.data = data;
		}
		
		public final void run() {
			try {
				perform();
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				throw new RuntimeException(new CatastrophicException(e));
			}
		}
		
		protected abstract void perform() throws IOException;
		
		protected void send(DatagramPacket dgp) throws IOException {
			sock.send(dgp);
		}
	}
	
	private class SendTask extends AbstractSendTask {
		
		private DatagramPacket dgp;
		
		public SendTask(byte[] data, DatagramPacket dgp) {
			super(data);
			this.dgp = dgp;
		}
		
		public void perform() throws IOException {
			dgp.setData(data);
			send(dgp);
			dgp.setData(null, 0, 0);
		}
			
	}
}