package server;

import java.net.SocketAddress;

import server.clientdb.Client;
import server.clientdb.ClientDB;

public class GameAdministration {

	private final ClientDB cdb;
	PacketSender ps;
	Game game;

	public GameAdministration(ClientDB cdb, PacketSender ps, Game game) {
		this.cdb = cdb;
		this.ps = ps;
		this.game = game;
	}

	public void newConnection(String name, SocketAddress saddr) {

		Client c = cdb.getClient(saddr);

		if (c == null) {
			c = cdb.createClient(name, saddr);

			game.newClient(c);

			final byte[] data = PacketDataFactory.createPlayerJoined(c.getID(),
					name);

			sendToAll(data);
		}

		ps.controlledSend(PacketDataFactory.createInitializer(12345, c.getID(), name), c);
		sendPlayersInfo(c);
	}

	private void sendPlayersInfo(Client c) {
		final byte[] tmp = PacketDataFactory.createPlayersInfo(cdb, c);
		if (tmp.length > 2) {
			ps.controlledSend(tmp, c);
		}
	}

	public void leaving(SocketAddress saddr) {
		final Client removed = cdb.removeClient(saddr);

		game.clientLeaving(removed);

		final byte[] data = PacketDataFactory.createPlayerLeft(removed.getID());
		sendToAll(data);
	}

	private void sendToAll(byte[] data) {
		ps.sendToAll(data);
	}
}
