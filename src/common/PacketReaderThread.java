/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jonsturk
 */
public class PacketReaderThread extends Thread {
	private List<PacketObserver> observers;
	private DatagramSocket socket;
	private boolean running;

	public void addPacketObserver( PacketObserver po ) {
		observers.add(po);
	}

	public void notifyPacketObservers(byte[] data) {
		for( PacketObserver po : observers ) {
			po.packetReceived(data, null);
		}
	}

	public PacketReaderThread(DatagramSocket socket) {
		this.socket = socket;
		observers = new LinkedList<PacketObserver>();

	}

	public void run() {
		running = true;
		while(running) {

		}
	}

	public void halt() {
		running = false;
	}
}
