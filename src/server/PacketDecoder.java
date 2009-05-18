package server;

import java.net.SocketAddress;

import server.command.ChatMessageCommand;
import server.command.ClientJoiningCommand;
import server.command.ClientLeavingCommand;
import server.command.FireMissileCommand;
import server.command.PlayerUpdateCommand;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import common.ClientPacketType;
import common.OffsetConstants;
import common.Util;
import common.network.PacketObserver;

public class PacketDecoder implements PacketObserver {

	private final Game game;

	public PacketDecoder(Game game) {
		this.game = game;
	}

	public boolean packetReceived(byte[] data, SocketAddress sender) {
		final byte ptype = Util.packetType(data);

		switch (ptype) {
		case ClientPacketType.JOINING:
			final String name = Util.getString(data, 2);
			game.addCommand( new ClientJoiningCommand(name, sender) );
			break;
		case ClientPacketType.LEAVING:
			game.addCommand(new ClientLeavingCommand(sender));
			break;
		case ClientPacketType.PLAYER_UPDATE:
			float time = Util.getFloat(data, OffsetConstants.PLAYER_UPDATE_TIME_OFFSET);
			Vector3f pos = Util.getVector3f(data, OffsetConstants.PLAYER_UPDATE_POS_OFFSET, new Vector3f());
			Quaternion ort = Util.getQuaternion(data, OffsetConstants.PLAYER_UPDATE_ORT_OFFSET, new Quaternion());
			Vector3f dir = Util.getVector3f(data, OffsetConstants.PLAYER_UPDATE_DIR_OFFSET, new Vector3f());
			game.addCommand(new PlayerUpdateCommand(sender, pos, ort, dir, time) );
			break;
		case ClientPacketType.FIRE_MISSILE:
			float t = Util.getFloat(data, OffsetConstants.FIRE_MISSILE_TIME_OFFSET);
			game.addCommand(new FireMissileCommand(sender, t) );
			break;
		case ClientPacketType.CHAT_MESSAGE:
			String msg = Util.getString(data, OffsetConstants.CHAT_MESSAGE_STRING_OFFSET);
			game.addCommand(new ChatMessageCommand(sender, msg));
			break;
		default:
			return false;
		}
		return true;
	}
}
