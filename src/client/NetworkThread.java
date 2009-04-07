/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.CatastrophicException;
import common.PackageType;
import common.Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonsturk
 */
public class NetworkThread extends Thread {
	private DatagramPacket rec;
	private DatagramSocket sock;
	private DatagramPacket send;

	private boolean connected;
	private boolean running = true;
	
	private String name; //TODO: Move to a better place.
	private String hostname;
	private int port;

	private List<NetworkObserver> networkObservers;

	public void addNetworkObserver(NetworkObserver nwo) {
		networkObservers.add(nwo);
	}

	public void notifyNetworkObservers(byte[] data) {
		for( NetworkObserver nwo : networkObservers ) {
			nwo.packetReceived(data);
		}
	}

	public NetworkThread(String name, String hostname, int port)  throws CatastrophicException {
		this.name = name;
		this.hostname = hostname;
		this.port = port;
		setConnected(false);
		
		networkObservers = new LinkedList<NetworkObserver>();
		
		rec = new DatagramPacket(new byte[65000], 65000);
		
		try {
			sock = new DatagramSocket();
		} catch (SocketException e) {
			Logger.getLogger(NetworkThread.class.getName()).log(Level.SEVERE, null, e);
			throw new CatastrophicException(e);
		}
	}

	private void connect() throws CatastrophicException {
		if( isConnected() ) {
			return;
		}
		try {
			InetAddress iaddr = InetAddress.getByName(hostname);
			byte[] buf = name.getBytes();
			send = new DatagramPacket(buf, buf.length, iaddr, port);
			
			sock.send(send);
		} catch (Exception ex) {
			Logger.getLogger(NetworkThread.class.getName()).log(Level.SEVERE, null, ex);
			throw new CatastrophicException(ex);
		} 
	}

	public void run() {
		try {
			connect();
		} catch (CatastrophicException e) {
			throw new RuntimeException(e);
		}
		while(isRunning()){
			try {
				sock.setSoTimeout(5000);
				rec.setLength(rec.getData().length);
				sock.receive(rec);
				if( rec.getData()[0] == PackageType.INITIALIZER ){
					notifyNetworkObservers(rec.getData());
					sock.setSoTimeout(0);
					setConnected(true);
					break;
				}
			} catch (SocketTimeoutException e) {
				// just loop and try again
				try {
					connect();
				} catch (CatastrophicException ex) {
					throw new RuntimeException(ex);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		while (isRunning()) {
			try {
				rec.setLength(rec.getData().length); // reset length
				sock.receive(rec);
				byte[] bt = new byte[rec.getLength()];
				System.arraycopy(rec.getData(), 0, bt, 0, bt.length);
				notifyNetworkObservers(bt);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connected the connected to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
		if( running == false && sock != null && sock.isConnected()) {
			sock.close();
		}
	}
}
