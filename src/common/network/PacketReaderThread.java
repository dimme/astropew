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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.CatastrophicException;
import common.GameException;
import common.Util;

/**
 *
 * @author jonsturk
 */
public class PacketReaderThread extends Thread {
	private List<PacketObserver> observers;
	private DatagramSocket socket;
	private DatagramPacket readPacket;
	private boolean running;
	private ExecutorService exec;

	public void addPacketObserver(PacketObserver po) {
		observers.add(po);
	}

	public void removePacketObserver(PacketObserver po) {
		observers.remove(po);
	}

	private void notifyPacketObservers(byte[] data, SocketAddress addr) throws GameException {
		boolean handled = false;
		for( PacketObserver po : observers ) {
			try {
				if (po.packetReceived(data, addr)) {
					handled = true;
				}
			}catch (CatastrophicException e) {
				throw e;
			}catch (GameException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		if (!handled) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unhandled packet type: " + data[0]);
		}
	}

	public PacketReaderThread(DatagramSocket socket) {
		this.socket = socket;
		observers = new LinkedList<PacketObserver>();
		byte[] buff = new byte[Util.PACKET_SIZE];
		readPacket = new DatagramPacket(buff, Util.PACKET_SIZE);
		exec = Executors.newSingleThreadExecutor();
	}

	public void run() {
		running = true;
		while(running) {
			try {
				readPacket.setLength(readPacket.getData().length);
				socket.receive(readPacket);
				byte[] data = new byte[readPacket.getLength()];
				System.arraycopy(readPacket.getData(), 0, data, 0, data.length);
				exec.submit(new NotifyTask(data, readPacket.getSocketAddress()));
			} catch (IOException ex) {
				if (running) {
					Logger.getLogger(PacketReaderThread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
				}
			} catch (RejectedExecutionException e) {
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
	
	private class NotifyTask implements Runnable {
		byte[] data;
		SocketAddress addr;
		
		NotifyTask(byte[] data, SocketAddress addr) {
			this.data = data;
			this.addr = addr;
		}

		public void run() {
			try {
				notifyPacketObservers(data, addr);
			} catch (GameException e) {
				throw new RuntimeException(e);
			}
		}	
	}
}
