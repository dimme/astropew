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

	public void addPacketObserver(PacketObserver po) {
		observers.add(po);
	}

	public void removePacketObserver(PacketObserver po) {
		observers.remove(po);
	}

	public void notifyPacketObservers(byte[] data, SocketAddress addr) {
		for( PacketObserver po : observers ) {
			po.packetReceived(data, socket.getRemoteSocketAddress());
		}
	}

	public PacketReaderThread(DatagramSocket socket) {
		this.socket = socket;
		observers = new LinkedList<PacketObserver>();
		byte[] buff = new byte[Util.PACKET_SIZE];
		readPacket = new DatagramPacket(buff, Util.PACKET_SIZE);
	}

	public void run() {
		running = true;
		while(running) {
			try {
				socket.receive(readPacket);
				notifyPacketObservers(readPacket.getData(), readPacket.getSocketAddress());
			} catch (IOException ex) {
				Logger.getLogger(PacketReaderThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void halt() {
		running = false;
	}
}
