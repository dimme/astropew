package client;


import common.CatastrophicException;
import common.ClientPacketType;
import common.network.PacketReaderThread;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;

public class GameClient {
	private PacketSender sender;
	private PacketReaderThread reader;
	
	private DatagramSocket socket;
	private boolean isInitialized;
	
	public GameClient(SocketAddress address, String playername) {
		isInitialized = false;
		
		try {
			new Vector3f();
		} catch (NoClassDefFoundError e) {
			System.err.println("Couldn't find class Vector3f. Please make sure jME_2.0.jar is included in your classpath.");
			System.exit(1);
		}
		
		try {
			new ClientFrame(this);
			Game game = new Game();
			socket = new DatagramSocket();
			sender = new PacketSender(socket, address);
			reader = new PacketReaderThread(socket);
			reader.addPacketObserver(new GamePlayObserver(this, game));
			reader.addPacketObserver(new ConsoleNetworkObserver());
			reader.start();
			connect(playername);
			reader.addPacketObserver(new AckingObserver(sender, sender.sendPacket));
		} catch (SocketException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

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
	
	private synchronized void connect(String playername) {
		while(!isInitialized ) {
			try {
				sender.send(playername.getBytes());
				wait(5000);
			} catch (InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
	
	public void stop() {
		sender.send(new byte[]{ClientPacketType.LEAVING});
		reader.halt();
		sender.stop();
		
		socket.close();
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
