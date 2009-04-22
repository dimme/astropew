package client;

import java.net.SocketAddress;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.GameException;
import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.network.PacketObserver;

public class GamePlayObserver implements PacketObserver {

	private final GameClient client;
	private Game game;
	private final Player self = null;

	public GamePlayObserver(GameClient client, Game game) {
		this.client = client;
		this.game = game;
	}

	public boolean packetReceived(byte[] data, SocketAddress addr) throws GameException{
		final byte packettype = Util.packetType(data);
		if (packettype == ServerPacketType.PLAYER_JOINED) {
			final String name = new String(data, OffsetConstants.PLAYER_JOINED_STRING_OFFSET, data.length- OffsetConstants.PLAYER_JOINED_STRING_OFFSET);
			final int id = Util.getInt(data, OffsetConstants.PLAYER_JOINED_ID_OFFSET);
			final int shipid = Util.getInt(data, OffsetConstants.PLAYER_JOINED_SHIPID_OFFSET);
			game.addCommand(new AddPlayerCommand(id, name, shipid));
		} else if (packettype == ServerPacketType.PLAYERS_INFO) {
			for (int i = 2; i < data.length;) {
				final int id = Util.getInt(data, i);
				final int shipid = Util.getInt(data, i+4);
				final byte namelen = data[i + 8];
				final String name = new String(data, i + 9, namelen);
				i += 9 + namelen;
				game.addCommand(new AddPlayerCommand(id, name, shipid));
			}
		} else if (packettype == ServerPacketType.PLAYER_LEFT) {
			final int id = Util.getInt(data, OffsetConstants.PLAYER_LEFT_ID_OFFSET);
			game.addCommand(new RemovePlayerCommand(id));
		} else if (packettype == ServerPacketType.PLAYER_POSITIONS) {
			final long tick = Util.getLong(data, 2);
			for( int i = 10; i < data.length;){
				final int id = Util.getInt(data, i);
				final Vector3f pos = Util.getVector3f(data, i + 4, new Vector3f());
				final Quaternion ort = Util.getQuaternion(data, i + 16, new Quaternion());
				final Vector3f dir = Util.getVector3f(data, i + 32, new Vector3f());
				i += 44;
				game.addCommand(new UpdatePositionCommand(id, pos, ort, dir, tick));
			}
		} else if (packettype == ServerPacketType.MISSILE) {
			long time = Util.getLong(data, OffsetConstants.MISSILE_TIME_OFFSET);
			int ownerid = Util.getInt(data, OffsetConstants.MISSILE_OWNER_OFFSET);
			int id = Util.getInt(data, OffsetConstants.MISSILE_ID_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.MISSILE_POS_OFFSET, new Vector3f() );
			Vector3f dir = Util.getVector3f(data, OffsetConstants.MISSILE_DIR_OFFSET, new Vector3f() );
			game.addCommand(new AddMissileCommand(time, id, pos,dir,ownerid));
		} else {
			return false;
		}
		return true;
	}

}
