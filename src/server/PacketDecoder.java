package server;

import java.net.SocketAddress;

import common.ClientPacketType;
import common.Util;
import common.network.AbstractPacketObserver;

public class PacketDecoder extends AbstractPacketObserver {

	private final GameAdministration gadm;
	private final Game game;

	public PacketDecoder(GameAdministration gadm, Game game) {
		this.gadm = gadm;
		this.game = game;
	}

	public boolean packetReceived(byte[] data, SocketAddress sender) {
		final byte ptype = packetType(data);

		switch (ptype) {
		case ClientPacketType.LEAVING:
			gadm.leaving(sender);
			break;
		case ClientPacketType.JOINING:
			final String name = Util.getString(data, 2);
			gadm.newConnection(name, sender);
			break;
		default:
			return false;
		}
		return true;
	}
}
