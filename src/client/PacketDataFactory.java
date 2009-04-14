package client;

import common.ClientPacketType;
import common.Util;
import common.network.OffsetConstants;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMovement(long time, Ship s){
		byte[] b = new byte[OffsetConstants.PLAYER_MOVEMENT_SIZE];
		b[OffsetConstants.PACKET_TYPE_OFFSET] = ClientPacketType.PLAYER_MOVEMENT;
		b[OffsetConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_MOVEMENT_TICK_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetConstants.PLAYER_MOVEMENT_ORT_OFFSET);
		Util.put(s.getMovement(), b,OffsetConstants.PLAYER_MOVEMENT_DIR_OFFSET);
		return b;
	}
}
