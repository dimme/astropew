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
import common.Util;

public class PacketSender {

	private DatagramSocket sock;
	private DeliveryService dserv;
	ExecutorService exec;
	
	public PacketSender(PacketReaderThread reader) throws SocketException {
		this(new DatagramSocket(), reader);
	}
	
	public PacketSender(DatagramSocket sock, PacketReaderThread reader) {
		exec = Executors.newSingleThreadExecutor();
		this.sock = sock;
		dserv = new DeliveryService(this);
		reader.addPacketObserver(dserv);
	}
	
	public void send(byte[] data, DatagramPacket dgp) {
		addTask( new SendTask(data, dgp) );
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
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted while waiting for executor service to finish");
		}
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
		
		protected DatagramPacket dgp;
		
		public SendTask(byte[] data, DatagramPacket dgp) {
			super(data);
			this.dgp = dgp;
		}
		
		public void perform() throws IOException {
			dgp.setData(data);
			send(dgp);
			dgp.setData(Util.nullbytes, 0, 0);
		}
			
	}
	
	private class ControlledSendTask extends AbstractSendTask {

		protected UDPConnection udpc;
		
		public ControlledSendTask(byte[] data, UDPConnection udpc) {
			super(data);
			this.udpc = udpc;
		}
		
		public void perform() throws IOException {
			dserv.addDelivery(data, udpc);
		}
	}
}