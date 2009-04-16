package client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ClientDebug {
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: host port username");
			System.exit(1);
		}
		final int port = Integer.parseInt(args[1]);
		final SocketAddress addr = new InetSocketAddress(args[0], port);

		try {
			new GameClient("client.DumbDummySenderGame", addr, args[2], true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
