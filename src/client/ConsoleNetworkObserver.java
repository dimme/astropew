package client;

import java.net.SocketAddress;

import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.network.PacketObserver;

/**
 * 
 * @author jonsturk
 */
public class ConsoleNetworkObserver implements PacketObserver {

	public boolean packetReceived(byte[] data, SocketAddress saddr) {
		final byte ptype = Util.packetType(data);
		if (ptype == ServerPacketType.INITIALIZER) {
			final String name = new String(data,
					OffsetConstants.INITIALIZER_STRING_OFFSET, data.length
							- OffsetConstants.INITIALIZER_STRING_OFFSET);
			final int id = Util.getInt(data,
					OffsetConstants.INITIALIZER_ID_OFFSET);
			final long randSeed = Util.getLong(data,
					OffsetConstants.INITIALIZER_RANDOM_SEED_OFFSET);
			System.out.println("Established contact with server. Got id: " + id
					+ ". Got name: " + name + ". Got seed: " + randSeed);
		} else if (ptype == ServerPacketType.PLAYER_LEFT) {
			final int lid = Util.getInt(data,
					OffsetConstants.PLAYER_LEFT_ID_OFFSET);
			System.out.println("Player Left. ID = " + lid);
		} else if (ptype == ServerPacketType.PLAYER_JOINED) {
			final int nid = Util.getInt(data,
					OffsetConstants.PLAYER_JOINED_ID_OFFSET);
			final byte[] bt = new byte[data.length
					- OffsetConstants.PLAYER_JOINED_STRING_OFFSET];
			System.arraycopy(data, OffsetConstants.PLAYER_JOINED_STRING_OFFSET,
					bt, 0, bt.length);
			System.out.println("Player Joined. ID = " + nid + " Name = "
					+ new String(bt));
		} else if (ptype == ServerPacketType.PLAYERS_INFO) {
			String s = "";
			for (int i = 2; i < data.length;) {
				i += 4;
				final byte tmp = data[i];
				i++;
				final String name = new String(data, i, tmp);
				i += tmp;
				s += name + ", ";
			}
			System.out.println("Added players: " + s);
		} else if (ptype == ServerPacketType.PLAYER_POSITIONS) {
			/*
			 * System.out.println("Recieved a player_position packet");
			 * System.out.println("The time was: "+Util.getLong(data,
			 * OffsetAndSizeConstants.PLAYER_POSITION_TICK_OFFSET));
			 * System.out.println("The player was: "+Util.getInt(data,
			 * OffsetAndSizeConstants.PLAYER_POSITION_ID_OFFSET)); Vector3f v =
			 * new Vector3f(); Util.getVector3f(data,
			 * OffsetAndSizeConstants.PLAYER_POSITION_POS_OFFSET, v);
			 * System.out.println(v); Util.getVector3f(data,
			 * OffsetAndSizeConstants.PLAYER_POSITION_DIR_OFFSET, v);
			 * System.out.println(v); Util.getVector3f(data,
			 * OffsetAndSizeConstants.PLAYER_POSITION_ORT_OFFSET, v);
			 * System.out.println(v);
			 */
		} else if (ptype == ServerPacketType.MESSAGE) {
			System.out
					.println("Recieved msg: "
							+ new String(
									data,
									OffsetConstants.MESSAGE_STRING_OFFSET,
									data.length
											- OffsetConstants.MESSAGE_STRING_OFFSET));
		} else {
			/*
			 * System.out.println("Unhandled packet type: " + unmaskedData +
			 * ", length: " + data.length); System.out.println("\t" + "Data: " +
			 * hex(data));
			 */
		}
		return false;
	}

}
