package client;

import common.ClientPacketType;
import common.OffsetAndSizeConstants;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMovement(long time, Ship s){
		byte[] b = new byte[OffsetAndSizeConstants.PLAYER_MOVEMENT_SIZE];
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ClientPacketType.PLAYER_MOVEMENT;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(time, b, OffsetAndSizeConstants.PLAYER_MOVEMENT_TICK_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetAndSizeConstants.PLAYER_MOVEMENT_ORT_OFFSET);
		Util.put(s.getMovement(), b,OffsetAndSizeConstants.PLAYER_MOVEMENT_DIR_OFFSET);
		return b;
	}
}
