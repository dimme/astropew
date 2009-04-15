package client;


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
	
	public GameClient(SocketAddress address, String playername) {
		
		try {
			new Vector3f();
		} catch (NoClassDefFoundError e) {
			System.err.println("Couldn't find class Vector3f. Please make sure jME_2.0.jar is included in your classpath.");
			System.exit(1);
		}
		
		try {
			Game game = new Game(this);
			socket = new DatagramSocket();
			reader = new PacketReaderThread(socket);
			sender = new PacketSender(socket, address, reader);
			reader.addPacketObserver(new GamePlayObserver(this, game));
			reader.addPacketObserver(new ConsoleNetworkObserver());
			reader.addPacketObserver(new AckingObserver(sender, address));
			reader.start();
			connect(playername);
		} catch (SocketException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}
	
	private void connect(String playername) {
		sender.controlledSend(PacketDataFactory.createJoin(playername));
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
