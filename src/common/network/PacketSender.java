package common.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CatastrophicException;

public class PacketSender {

	private DatagramSocket sock;
	private ExecutorService exec;
	private DeliveryService dserv;
	
	public PacketSender() throws SocketException {
		this(new DatagramSocket());
	}
	
	public PacketSender(DatagramSocket sock) {
		exec = Executors.newSingleThreadExecutor();
		this.sock = sock;
		dserv = new DeliveryService(this);
	}
	
	public void send(byte[] data, UDPConnection udpc) {
		addTask( new SendTask(data, udpc) );
	}
	
	public void controlledSend(byte[] data, UDPConnection udpc) {
		addTask( new ControlledSendTask(data, udpc) );
	}
	
	protected void addTask(Runnable task) {
		exec.submit(task);
	}
	
	public void stop() {
		exec.shutdown();
		try {
			exec.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected abstract class AbstractSendTask implements Runnable {
		protected final byte[] nullbytes = new byte[1];
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
			} catch (Throwable t) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, t.getMessage(), t);
				throw new RuntimeException(t);
			}
		}
		
		protected abstract void perform() throws IOException;
		
		protected void send(DatagramPacket dgp) throws IOException {
			sock.send(dgp);
		}
	}
	
	private class SendTask extends AbstractSendTask {
		
		protected UDPConnection udpc;
		
		public SendTask(byte[] data, UDPConnection udpc) {
			super(data);
			this.udpc = udpc;
		}
		
		public void perform() throws IOException {
			udpc.dgp.setData(data);
			send(udpc.dgp);
			udpc.dgp.setData(nullbytes, 0, 0);
		}
			
	}
	
	private class ControlledSendTask extends SendTask {

		public ControlledSendTask(byte[] data, UDPConnection udpc) {
			super(data, udpc);
		}
		
		public void perform() throws IOException {
			dserv.addDelivery(data, udpc);
			super.perform();
		}
	}
}