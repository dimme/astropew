/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.AbstractExecutorTask;
import common.CatastrophicException;
import common.GameException;
import common.Util;

/**
 * 
 * @author jonsturk
 */
public class PacketReaderThread extends Thread {
	private final ConcurrentLinkedQueue<PacketObserver> observers;
	private final ConcurrentLinkedQueue<PacketFilter> filters;
	private final DatagramSocket socket;
	private final DatagramPacket readPacket;
	private boolean running;
	private final ExecutorService exec;

	public PacketReaderThread(DatagramSocket socket) {
		this.socket = socket;
		observers = new ConcurrentLinkedQueue<PacketObserver>();
		filters = new ConcurrentLinkedQueue<PacketFilter>();
		final byte[] buff = new byte[Util.PACKET_SIZE];
		readPacket = new DatagramPacket(buff, Util.PACKET_SIZE);
		exec = Executors.newSingleThreadExecutor();
	}
	
	public void addPacketFilter(PacketFilter pf) {
		filters.add(pf);
	}
	
	public void removePacketFilter(PacketFilter pf) {
		filters.remove(pf);
	}
	
	public void addPacketObserver(PacketObserver po) {
		observers.add(po);
	}

	public void removePacketObserver(PacketObserver po) {
		observers.remove(po);
	}

	private void notifyPacketObservers(byte[] data, SocketAddress addr) throws GameException {
		boolean allow = true;
		for (PacketFilter pf : filters) {
			if (!pf.accept(data, addr)) {
				allow = false;
			}
		}
		if (allow) {
			boolean handled = false;
			for (final PacketObserver po : observers) {
				try {
					if (po.packetReceived(data, addr)) {
						handled = true;
					}
				} catch (final CatastrophicException e) {
					throw e;
				} catch (final GameException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
	
			if (!handled) {
				//Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unhandled packet type: " + data[0]);
			}
		}
	}

	public void run() {
		running = true;
		while (running) {
			try {
				readPacket.setLength(readPacket.getData().length);
				socket.receive(readPacket);
				final byte[] data = new byte[readPacket.getLength()];
				System.arraycopy(readPacket.getData(), 0, data, 0, data.length);
				exec.submit(new NotifyTask(data, readPacket.getSocketAddress()));
			} catch (final IOException ex) {
				if (running) {
					Logger.getLogger(PacketReaderThread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
				}
			} catch (final RejectedExecutionException e) {
				if (running) {
					Logger.getLogger(PacketReaderThread.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}

	public void halt() {
		running = false;
		exec.shutdown();
	}

	private class NotifyTask extends AbstractExecutorTask {
		byte[] data;
		SocketAddress addr;

		NotifyTask(byte[] data, SocketAddress addr) {
			this.data = data;
			this.addr = addr;
		}

		public void execute() {
			try {
				notifyPacketObservers(data, addr);
			} catch (final GameException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
