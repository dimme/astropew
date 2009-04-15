package client;

import common.ClientPacketType;
import common.OffsetConstants;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createPlayerUpdate(long time, Ship s) {
		final byte[] b = new byte[OffsetConstants.PLAYER_UPDATE_SIZE];
		b[0] = ClientPacketType.PLAYER_UPDATE;
		b[1] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_UPDATE_TIME_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetConstants.PLAYER_UPDATE_POS_OFFSET);
		Util.put(s.getLocalRotation(), b, OffsetConstants.PLAYER_UPDATE_ORT_OFFSET);
		Util.put(s.getMovement(), b, OffsetConstants.PLAYER_UPDATE_DIR_OFFSET);
		return b;
	}

	public static byte[] createJoin(String playername) {
		final byte[] sb = playername.getBytes();
		final byte[] b = new byte[2 + sb.length];

		b[0] = ClientPacketType.JOINING;
		b[1] = 0;
		Util.put(sb, b, 2);

		return b;
	}
}
