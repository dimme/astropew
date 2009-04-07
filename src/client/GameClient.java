package client;


import common.CatastrophicException;
import common.PacketReaderThread;
import common.PacketSender;
import common.Util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
	private ServerPacketSender sender;
	private PacketReaderThread reader;

	private DatagramSocket socket;
	private DatagramPacket sendPacket;

	private String playername;
	private boolean isInitialized = false;

	public synchronized void initialized( SocketAddress newAddr) throws CatastrophicException {
		try {
			sender.setSocketAddress(newAddr);
			isInitialized = true;
			notify();
		} catch (SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			throw new CatastrophicException(e);
		}
	}
	
	public synchronized void connect() {
		while(!isInitialized ) {
			try {
				sender.send(playername.getBytes());
				wait(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public GameClient(SocketAddress address, String playername) {
		this.playername = playername;
		try {
			socket = new DatagramSocket();
			sender = new ServerPacketSender(socket, address);
			reader = new PacketReaderThread(socket);
			reader.addPacketObserver(new GamePlayObserver(this));
			reader.addPacketObserver(new ConsoleNetworkObserver());

			reader.start();
			connect();
		} catch (SocketException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	public static void main(String[] args) {
		if(args.length < 3 ) {
			System.out.println("Usage: host port username");
			System.exit(1);
		}
		int port = Integer.parseInt(args[1]);
		SocketAddress addr = new InetSocketAddress(args[0], port);

		new GameClient(addr, args[2]);
	}
}
