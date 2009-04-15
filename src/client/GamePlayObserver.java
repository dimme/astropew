package client;

import java.net.SocketAddress;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.GameException;
import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.network.AbstractPacketObserver;

public class GamePlayObserver extends AbstractPacketObserver {

	private final GameClient client;
	private final Game game;
	private final Player self = null;

	public GamePlayObserver(GameClient client, Game game) {
		this.client = client;
		this.game = game;
	}

	public boolean packetReceived(byte[] data, SocketAddress addr)
			throws GameException {
		final byte packettype = packetType(data);
		if (packettype == ServerPacketType.INITIALIZER) {
			final String name = new String(data,
					OffsetConstants.INITIALIZER_STRING_OFFSET, data.length
							- OffsetConstants.INITIALIZER_STRING_OFFSET);
			final int id = Util.getInt(data,
					OffsetConstants.INITIALIZER_ID_OFFSET);
			game.addPlayer(id, name);
		} else if (packettype == ServerPacketType.PLAYER_JOINED) {
			final String name = new String(data,
					OffsetConstants.PLAYER_JOINED_STRING_OFFSET, data.length
							- OffsetConstants.PLAYER_JOINED_STRING_OFFSET);
			final int id = Util.getInt(data,
					OffsetConstants.PLAYER_JOINED_ID_OFFSET);
			game.addPlayer(id, name);
		} else if (packettype == ServerPacketType.PLAYERS_INFO) {
			for (int i = 2; i < data.length;) {
				final int id = Util.getInt(data, i);
				i += 4;
				final byte tmp = data[i];
				i++;
				final String name = new String(data, i, tmp);
				i += tmp;
				game.addPlayer(id, name);
			}
		} else if (packettype == ServerPacketType.PLAYER_LEFT) {
			final int id = Util.getInt(data,
					OffsetConstants.PLAYER_LEFT_ID_OFFSET);
			game.removePlayer(id);
		} else if (packettype == ServerPacketType.PLAYER_POSITION) {
			final int id = Util.getInt(data, OffsetConstants.PLAYER_POSITION_ID_OFFSET);
			final long tick = Util.getLong(data,OffsetConstants.PLAYER_POSITION_TICK_OFFSET);
			final Vector3f pos = Util.getVector3f(data,OffsetConstants.PLAYER_POSITION_POS_OFFSET, new Vector3f());
			final Quaternion ort = Util.getQuaternion(data,OffsetConstants.PLAYER_POSITION_ORT_OFFSET, new Quaternion());
			final Vector3f dir = Util.getVector3f(data,OffsetConstants.PLAYER_POSITION_DIR_OFFSET, new Vector3f());
			game.updatePosition(pos, ort, dir, id, tick);
		} else {
			return false;
		}
		return true;
	}

}
