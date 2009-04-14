package common.network;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CommonPacketType;
import common.GameException;

public class DeliveryService implements PacketObserver {
	private PriorityQueue<Task> tasks;
	private Map<SocketAddress, UDPConnection> connections;
	private PacketSender ps;
	private ExecutorService exec;
	private Task compareSingleton;
	
	private static final long TIMEOUT = 1000; 
	
	public DeliveryService(PacketSender ps) {
		this.ps = ps;
		tasks = new PriorityQueue<Task>(91);
		connections = new HashMap<SocketAddress, UDPConnection>();
		
		Thread periodicResend = new Thread() {
			private long schedule;
			
			public void start() {
				schedule = System.currentTimeMillis();
				setDaemon(true);
			}
			
			public void run() {
				try {
					while (true) {
						long curt = System.currentTimeMillis();
						long diff = schedule-curt;
						if (diff > 0) {
							sleep(diff);
						}
						
						exec.submit(new ResendTimedOut());
						
						schedule += 50;
					}
				} catch (InterruptedException e) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "Periodic resend thread interrupted!", e);
				}
			}
		};
		periodicResend.start();
		
		exec = Executors.newSingleThreadExecutor();
		compareSingleton = new Task(0, (byte)0, null);
	}
	
	public void stop() {
		exec.shutdown();
	}
	
	public void addDelivery(byte[] data, UDPConnection udpc) {
		exec.submit( new New(data, udpc) );
	}
	
	public void acknowledge(byte seq, SocketAddress saddr) {
		System.out.println("ack " + seq + " from " + saddr);
		exec.submit( new Ack(saddr, seq) );
	}
	
	private static class Task implements Comparable<Task> {
		long timeout;
		byte seq;
		UDPConnection udpc;
		
		Task(long time, byte seq, UDPConnection udpc) {
			this.timeout = time;
			this.seq = seq;
			this.udpc = udpc;
		}

		public int compareTo(Task t) {
			if (timeout == t.timeout) {
				return 0;
			}
			
			return timeout > t.timeout ? 1 : -1;
		}
		
		public boolean equals(Object o) {
			if (o instanceof Task) {
				Task t = (Task)o;
				return (t.seq == seq && 
						t.udpc.dgp.getSocketAddress().equals(udpc.dgp.getSocketAddress()));
					
			}
			return false;
		}
	}
	
	private class New implements Runnable {
		
		private byte[] data;
		private UDPConnection udpc;
		
		public New(byte[] data, UDPConnection udpc) {
			this.data = data;
			this.udpc = udpc;
		}
		
		public void run() {
			try {
				byte seq = udpc.addControlledPacket(data);
				Task t = new Task(System.currentTimeMillis()+TIMEOUT, seq, udpc);
				tasks.add( t );
				connections.put(udpc.dgp.getSocketAddress(), udpc);
			} catch (SendWindowFullException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private class ResendTimedOut implements Runnable {
		
		public void run() {
			long curt = System.currentTimeMillis();
			Task t = tasks.peek();
			while (t!=null && t.timeout <= curt) {
				tasks.remove(t);
				ps.send(t.udpc.getData(t.seq), t.udpc);
				t.timeout = System.currentTimeMillis()+TIMEOUT;
				tasks.add( t );
				t = tasks.peek();
			}
		}
	}
	
	private class Ack implements Runnable {
		private byte seq;
		private SocketAddress saddr;
		
		Ack(SocketAddress saddr, byte seq) {
			this.saddr=saddr;
			this.seq = seq;
		}
		
		public void run() {
			UDPConnection udpc = connections.get(saddr);
			compareSingleton.seq = seq;
			compareSingleton.udpc = udpc;
			tasks.remove(compareSingleton);
			
			if (udpc.getNumPending() == 0) {
				connections.remove(saddr);
			}
		}
	}

	public void packetReceived(byte[] data, SocketAddress addr)
			throws GameException {
		if (data[OffsetConstants.PACKET_TYPE_OFFSET] == CommonPacketType.ACK) {
			byte seq = data[OffsetConstants.SEQUENCE_NUMBER_OFFSET];
			acknowledge(seq, addr);
		}
	}
}
