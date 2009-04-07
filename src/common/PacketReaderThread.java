/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public void notifyPacketObservers(byte[] data, SocketAddress addr) throws GameException {
		for( PacketObserver po : observers ) {
			try {
				po.packetReceived(data, addr);
			}catch (CatastrophicException e) {
				throw e;
			}catch (GameException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
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
				// TODO: throw runtime exception?
				Logger.getLogger(PacketReaderThread.class.getName()).log(Level.SEVERE, null, ex);
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
