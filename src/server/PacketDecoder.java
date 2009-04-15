package server;

import java.net.SocketAddress;

import server.clientdb.ClientDB;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import common.ClientPacketType;
import common.OffsetConstants;
import common.Util;
import common.network.PacketObserver;

public class PacketDecoder implements PacketObserver {

	private final Game game;
	private final ClientDB cdb;

	public PacketDecoder(Game game, ClientDB cdb) {
		this.game = game;
		this.cdb = cdb;
	}

	public boolean packetReceived(byte[] data, SocketAddress sender) {
		final byte ptype = Util.packetType(data);

		switch (ptype) {
		case ClientPacketType.JOINING:
			final String name = Util.getString(data, 2);
			game.clientJoining(name, sender);
			break;
		case ClientPacketType.LEAVING:
			game.clientLeaving(sender);
			break;
		case ClientPacketType.PLAYER_UPDATE:
			long time = Util.getLong(data, OffsetConstants.PLAYER_UPDATE_TIME_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.PLAYER_UPDATE_POS_OFFSET, new Vector3f());
			Quaternion ort = Util.getQuaternion(data, OffsetConstants.PLAYER_UPDATE_ORT_OFFSET, new Quaternion());
			Vector3f dir = Util.getVector3f(data, OffsetConstants.PLAYER_UPDATE_DIR_OFFSET, new Vector3f());
			int id = cdb.getClient(sender).getID();
			game.updatePlayer(id, pos, ort, dir, time);
			break;
		default:
			return false;
		}
		return true;
	}
}
