package client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Client {
	public static void main(String[] args) {
		startGame(args, false);
	}

	protected static void startGame(String[] args, boolean debugoutput) {
		if (args.length < 3) {
			System.out.println("Usage: host port username");
			System.exit(1);
		}
		final int port = Integer.parseInt(args[1]);
		final SocketAddress addr = new InetSocketAddress(args[0], port);

		new GameClient(addr, args[2], false);
	}
}
