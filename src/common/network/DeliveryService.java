package common.network;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.AbstractExecutorTask;
import common.CommonPacketType;
import common.GameException;
import common.OffsetConstants;

public class DeliveryService extends AbstractPacketObserver {
	private PriorityQueue<Task> tasks;
	private Map<SocketAddress, UDPConnection> connections;
	private PacketSender ps;
	private Task compareSingleton;
	
	private static final long TIMEOUT = 500; 
	
	public DeliveryService(PacketSender ps) {
		this.ps = ps;
		tasks = new PriorityQueue<Task>(91);
		connections = new HashMap<SocketAddress, UDPConnection>();
		Thread periodicResend = new PeriodicResend();
		periodicResend.start();
		
		compareSingleton = new Task(0, (byte)0, null);
	}
	
	public void addDelivery(byte[] data, UDPConnection udpc) {
		ps.exec.submit( new New(data, udpc) );
	}
	
	public void acknowledge(byte seq, SocketAddress saddr) {
		System.out.println("ack " + seq + " from " + saddr);
		ps.exec.submit( new Ack(saddr, seq) );
	}
	
	private class PeriodicResend extends Thread {
		private long schedule;
		
		public void start() {
			schedule = System.currentTimeMillis();
			setDaemon(true);
			super.start();
		}
		
		public void run() {
			try {
				while (true) {
					long curt = System.currentTimeMillis();
					long diff = schedule-curt;
					if (diff > 0) {
						sleep(diff);
					}
					
					ps.exec.submit(new ResendTimedOut());
					
					schedule += 50;
				}
			} catch (RejectedExecutionException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "Rejected execution of resend task: This is NOT a problem if you were shutting down.");
			} catch (InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Periodic resend thread interrupted!", e);
			}
		}
	}
	
	private static class Task implements Comparable<Task> {
		long timeout;
		byte seq;
		UDPConnection udpc;
		int left = 10;
		
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
	
	private void send(Task t) {
		t.timeout = System.currentTimeMillis()+TIMEOUT;
		t.left--;
		if (t.left >= 0) {
			ps.send(t.udpc.getData(t.seq), t.udpc.dgp);
			tasks.add( t );
		} else {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Maximum number of retries reached");
			System.exit(1);
			//TODO: inte exit! :D
		}
	}
	
	private class New extends AbstractExecutorTask {
		
		private byte[] data;
		private UDPConnection udpc;
		
		public New(byte[] data, UDPConnection udpc) {
			this.data = data;
			this.udpc = udpc;
		}
		
		public void execute() {
			try {
				byte seq = udpc.addControlledPacket(data);
				Task t = new Task(System.currentTimeMillis()+TIMEOUT, seq, udpc);
				connections.put(udpc.dgp.getSocketAddress(), udpc);
				
				send(t);
			} catch (SendWindowFullException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private class ResendTimedOut extends AbstractExecutorTask  {
		
		public void execute() {
			long curt = System.currentTimeMillis();
			Task t = tasks.peek();
			while (t!=null && t.timeout <= curt) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "Resending " + t.seq + " to " + t.udpc);
				tasks.remove(t);
				send(t);
				t = tasks.peek();
			}
		}
	}
	
	private class Ack extends AbstractExecutorTask  {
		private byte seq;
		private SocketAddress saddr;
		
		Ack(SocketAddress saddr, byte seq) {
			this.saddr=saddr;
			this.seq = seq;
		}
		
		public void execute() {
			System.out.println("ack");
			UDPConnection udpc = connections.get(saddr);
			compareSingleton.seq = seq;
			compareSingleton.udpc = udpc;
			tasks.remove(compareSingleton);
			
			if (udpc.getNumPending() == 0) {
				connections.remove(saddr);
			}
		}
	}

	public boolean packetReceived(byte[] data, SocketAddress addr)
			throws GameException {
		if (packetType(data) == CommonPacketType.ACK) {
			byte seq = data[1];
			acknowledge(seq, addr);
			return true;
		}
		return false;
	}
}
