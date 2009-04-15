package client;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector3f;
import common.ClientPacketType;
import common.network.DataOutput;
import common.network.PacketReaderThread;

public class GameClient {
	private PacketSender sender;
	private PacketReaderThread reader;

	private DatagramSocket socket;

	public GameClient(SocketAddress address, String playername,
			boolean dataoutput) {

		try {
			new Vector3f();
		} catch (final NoClassDefFoundError e) {
			System.err
					.println("Couldn't find class Vector3f. Please make sure jME_2.0.jar is included in your classpath.");
			System.exit(1);
		}

		try {
			final Game game = new Game(this);
			socket = new DatagramSocket();
			reader = new PacketReaderThread(socket);
			sender = new PacketSender(socket, address, reader);
			if (dataoutput) {
				reader.addPacketObserver(new DataOutput());
			}
			reader.addPacketObserver(new GamePlayObserver(this, game));
			reader.addPacketObserver(new ConsoleNetworkObserver());
			reader.addPacketObserver(new AckingObserver(sender, address));
			reader.start();
			connect(playername);
		} catch (final SocketException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE,
					null, ex);
			throw new RuntimeException(ex);
		}
	}

	private void connect(String playername) {
		sender.controlledSend(PacketDataFactory.createJoin(playername));
	}

	public void stop() {
		final Future<?> fut = sender.controlledSend(new byte[] {
				ClientPacketType.LEAVING, 0 });
		sender.stopAndFinish(fut);
		reader.halt();

		socket.close();
	}
}
