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
			final int shipid = Util.getInt(data, OffsetConstants.INITIALIZER_SHIPID_OFFSET);
			final long seed = Util.getLong(data, OffsetConstants.INITIALIZER_RANDOM_SEED_OFFSET);
			final long ltime = Util.getLong(data, OffsetConstants.INITIALIZER_CURRENT_TIME_MILLIS_OFFSET);
			final float ftime = Util.getFloat(data, OffsetConstants.INITIALIZER_CURRENT_TIME_FLOAT_OFFSET);
			System.out.println("got ftime " + ftime);
			try {
				final Game game = new FlyingGame(id, name, shipid, seed, client, ltime, ftime);
				//final Game game = new DumbDummySenderGame(id, name, client);
				//final Game game = new ObserverGame(id, name, client);
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
