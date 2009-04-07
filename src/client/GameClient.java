package client;


import common.PacketReaderThread;
import common.PacketSender;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
	private PacketSender sender;
	private PacketReaderThread reader;

	private DatagramSocket socket;

	private String playername;

	public GameClient(SocketAddress address, String playername) {
		this.playername = playername;
		try {
			socket = new DatagramSocket(address);
			sender = new PacketSender(socket);
			reader = new PacketReaderThread(socket);

			reader.start();
		} catch (SocketException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	public void sendConnectPacket() {

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
