package client;

import java.net.SocketAddress;

import client.command.AddMissileCommand;
import client.command.AddPlayerCommand;
import client.command.DestroyObjectCommand;
import client.command.RemovePlayerCommand;
import client.command.SpawnCommand;
import client.command.UpdatePositionCommand;

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
			final String name = new String(data, OffsetConstants.PLAYER_JOINED_STRING_OFFSET, data.length-OffsetConstants.PLAYER_JOINED_STRING_OFFSET);
			final int id = Util.getInt(data, OffsetConstants.PLAYER_JOINED_ID_OFFSET);
			final int shipid = Util.getInt(data, OffsetConstants.PLAYER_JOINED_SHIPID_OFFSET);
			game.addCommand(new AddPlayerCommand(id, name, shipid));
		} else if (packettype == ServerPacketType.PLAYERS_INFO) {
			int i = OffsetConstants.PLAYERS_INFO_DATA_START;
			while(i < data.length) {
				final int id = Util.getInt(data, i + OffsetConstants.PLAYERS_DATA_ID_OFFSET);
				final int shipid = Util.getInt(data, i+OffsetConstants.PLAYERS_DATA_SHIPID_OFFSET);
				final byte namelen = data[i + OffsetConstants.PLAYERS_DATA_NAME_LENGTH_OFFSET];
				final String name = new String(data, i + OffsetConstants.PLAYERS_DATA_NAME_OFFSET, namelen);
				i += OffsetConstants.PLAYERS_DATA_NAME_OFFSET + namelen;
				game.addCommand(new AddPlayerCommand(id, name, shipid));
			}
		} else if (packettype == ServerPacketType.PLAYER_LEFT) {
			final int id = Util.getInt(data, OffsetConstants.PLAYER_LEFT_ID_OFFSET);
			game.addCommand(new RemovePlayerCommand(id));
		} else if (packettype == ServerPacketType.PLAYER_POSITIONS) {
			final float time = Util.getFloat(data, OffsetConstants.PLAYER_POSITIONS_TIME_OFFSET);
			int i = OffsetConstants.PLAYER_POSITIONS_DATA_START;
			while(i < data.length) {
				final int id = Util.getInt(data, i + OffsetConstants.PLAYER_POSITIONS_ID_OFFSET);
				final Vector3f pos = Util.getVector3f(data, i + OffsetConstants.PLAYER_POSITIONS_POS_OFFSET, new Vector3f());
				final Quaternion ort = Util.getQuaternion(data, i + OffsetConstants.PLAYER_POSITIONS_ORT_OFFSET, new Quaternion());
				final Vector3f dir = Util.getVector3f(data, i + OffsetConstants.PLAYER_POSITIONS_DIR_OFFSET, new Vector3f());
				i += OffsetConstants.PLAYER_POSITIONS_ONE_SIZE;
				game.addCommand(new UpdatePositionCommand(id, pos, ort, dir, time));
			}
		} else if (packettype == ServerPacketType.SPAWN) {
			System.out.println(Util.hex(data));
			float t = Util.getFloat(data, OffsetConstants.SPAWN_TIME_OFFSET);
			int id = Util.getInt(data, OffsetConstants.SPAWN_PLAYERID_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.SPAWN_POS_OFFSET, new Vector3f());
			Quaternion ort = Util.getQuaternion(data, OffsetConstants.SPAWN_ORT_OFFSET, new Quaternion());
			Vector3f dir = Util.getVector3f(data, OffsetConstants.SPAWN_DIR_OFFSET, new Vector3f());
			game.addCommand(new SpawnCommand(id,pos,ort,dir,t));
		} else if (packettype == ServerPacketType.MISSILE) {
			float time = Util.getFloat(data, OffsetConstants.MISSILE_TIME_OFFSET);
			int ownerid = Util.getInt(data, OffsetConstants.MISSILE_OWNER_OFFSET);
			int id = Util.getInt(data, OffsetConstants.MISSILE_ID_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.MISSILE_POS_OFFSET, new Vector3f() );
			Vector3f dir = Util.getVector3f(data, OffsetConstants.MISSILE_DIR_OFFSET, new Vector3f() );
			game.addCommand(new AddMissileCommand(time, id, pos,dir,ownerid));
		} else if (packettype == ServerPacketType.DESTROY_OBJECT) {
			int objid = Util.getInt(data, OffsetConstants.DESTROY_OBJECT_ID_OFFSET);
			game.addCommand(new DestroyObjectCommand(objid));
		} else {
			return false;
		}
		return true;
	}

}
