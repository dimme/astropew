package common.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.AbstractExecutorTask;
import common.CatastrophicException;
import common.Util;

public class PacketSender {

	private final DatagramSocket sock;
	private final DeliveryService dserv;
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
		addTask(new SendTask(data, dgp));
	}

	public Future<?> controlledSend(byte[] data, UDPConnection udpc) {
		return addTask(new ControlledSendTask(data, udpc));
	}

	protected Future<?> addTask(Runnable task) {
		return exec.submit(task);
	}

	public void stopAndFinish(Future<?> guaranteedTask) {
		try {
			guaranteedTask.get();
		} catch (final Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					e.getMessage(), e);
		}

		try {
			dserv.waitForAllDelivered();
			exec.shutdown();
			exec.awaitTermination(10, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Interrupted while waiting for executor service to finish");
		}
	}

	protected abstract class AbstractSendTask extends AbstractExecutorTask {
		protected byte[] data;

		public AbstractSendTask(byte[] data) {
			this.data = data;
		}

		public final void execute() {
			try {
				perform();
			} catch (final IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						e.getMessage(), e);
				throw new RuntimeException(new CatastrophicException(e));
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