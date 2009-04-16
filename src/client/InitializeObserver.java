package client;

import java.net.SocketAddress;

import common.CatastrophicException;
import common.GameException;
import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.network.PacketObserver;
import common.network.PacketReaderThread;

public class InitializeObserver implements PacketObserver {

	private final GameClient client;
	private final PacketReaderThread reader;

	public InitializeObserver(PacketReaderThread reader, GameClient client) {
		this.client = client;
		this.reader = reader;
	}

	public boolean packetReceived(byte[] data, SocketAddress addr) throws GameException{
		final byte packettype = Util.packetType(data);
		if (packettype == ServerPacketType.INITIALIZER) {
			final String name = new String(data, OffsetConstants.INITIALIZER_STRING_OFFSET, data.length - OffsetConstants.INITIALIZER_STRING_OFFSET);
			final int id = Util.getInt(data, OffsetConstants.INITIALIZER_ID_OFFSET);
			try {
				final Game game = new FlyingGame(id, name, client);
				reader.removePacketObserver(this);
				reader.addPacketObserver( new GamePlayObserver(client, game) );
				game.startInThread();
			} catch (Exception e) {
				throw new CatastrophicException(e);
			}
			return true;
		} 
		return false;
	}

}
