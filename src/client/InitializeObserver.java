package client;

import java.net.SocketAddress;
import java.util.LinkedList;

import common.CatastrophicException;
import common.GameException;
import common.OffsetConstants;
import common.Pair;
import common.ServerPacketType;
import common.Util;
import common.network.PacketObserver;
import common.network.PacketReaderThread;

public class InitializeObserver implements PacketObserver {

	private final GameClient client;
	private final PacketReaderThread reader;
	private final LinkedList<Pair<byte[],SocketAddress>> unhandled = new LinkedList<Pair<byte[],SocketAddress>>(); 
	
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

			try {
				final Game game = new FlyingGame(id, name, shipid, seed, client, ltime, ftime);
				//final Game game = new DumbDummySenderGame(id, name, client);
				//final Game game = new ObserverGame(id, name, client);
				reader.removePacketObserver(this);
				GamePlayObserver gpo = new GamePlayObserver(client, game);
				reader.addPacketObserver( gpo );
				game.startInThread();
				
				for (Pair<byte[],SocketAddress> p: unhandled) {
					gpo.packetReceived(p.item1, p.item2);
				}
			} catch (Exception e) {
				throw new CatastrophicException(e);
			}
			return true;
		} else {
			unhandled.add(new Pair<byte[],SocketAddress>(data, addr));
		}
		return false;
	}

}
