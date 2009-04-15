package client;

import common.ClientPacketType;
import common.OffsetConstants;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMovement(long time, Ship s){
		byte[] b = new byte[OffsetConstants.PLAYER_MOVEMENT_SIZE];
		b[0] = ClientPacketType.PLAYER_MOVEMENT;
		b[1] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_MOVEMENT_TICK_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetConstants.PLAYER_MOVEMENT_ORT_OFFSET);
		Util.put(s.getMovement(), b,OffsetConstants.PLAYER_MOVEMENT_DIR_OFFSET);
		return b;
	}

	public static byte[] createJoin(String playername) {
		byte[] sb = playername.getBytes();
		byte[] b = new byte[2 + sb.length];
		
		b[0] = ClientPacketType.JOINING;
		b[1] = 0;
		Util.put(sb, b, 2);
		
		return b;
	}
}
