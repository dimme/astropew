package server;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.clientdb.ClientDB;
import server.clientdb.ClientDatabase;

import common.network.PacketReaderThread;

public class JoinableServer extends Thread {

	public static void main(String[] args) {

		new JoinableServer();

	}

	public JoinableServer() {

		try {

			final ClientDB cdb = new ClientDatabase();

			final DatagramSocket sock = new DatagramSocket(34567); // TODO: configurable port and Service Announcer
			final PacketReaderThread pread = new PacketReaderThread(sock);
			final PacketSender ps = new PacketSender(cdb, sock, pread);

			final Game game = new Game(ps);
			final GameAdministration gadm = new GameAdministration(cdb, ps,
					game);

			final PacketDecoder pd = new PacketDecoder(gadm, game);

			pread.addPacketObserver(pd);
			pread.addPacketFilter(new AckingFilter(ps, cdb));

			// IncomingConnectionServer ics = new
			// IncomingConnectionServer(34567, pd);

			pread.start();
			// ics.start();

		} catch (final SocketException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					e.getMessage(), e);
			System.exit(1);
		}
	}
}
