package server;

import common.OffsetAndSizeConstants;
import common.ServerPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[OffsetAndSizeConstants.MESSAGE_STRING_OFFSET + sb.length];
		
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ServerPacketType.MESSAGE;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		b[OffsetAndSizeConstants.MESSAGE_MESSAGE_TYPE_OFFSET] = msgtype;
		Util.put(sb, b, OffsetAndSizeConstants.MESSAGE_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createInitializer(long worldseed, int id, String name) {
		byte[] namebytes = name.getBytes();
		byte[] b = new byte[OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET + namebytes.length];
		
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ServerPacketType.INITIALIZER;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(worldseed, b, OffsetAndSizeConstants.INITIALIZER_RANDOM_SEED_OFFSET);
		Util.put(id, b, OffsetAndSizeConstants.INITIALIZER_ID_OFFSET);
		Util.put(namebytes, b, OffsetAndSizeConstants.INITIALIZER_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createPlayerJoined(int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET + sb.length];
		
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ServerPacketType.PLAYER_JOINED;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(id,b,OffsetAndSizeConstants.PLAYER_JOINED_ID_OFFSET);
		Util.put(sb,b,OffsetAndSizeConstants.PLAYER_JOINED_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createPlayerLeft(int id) {
		byte[] b = new byte[OffsetAndSizeConstants.PLAYER_LEFT_SIZE];
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ServerPacketType.PLAYER_LEFT;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(id, b, OffsetAndSizeConstants.PLAYER_LEFT_ID_OFFSET);
		return b;
	}

	public static byte[] createPosition(long time, Ship s) {
		byte[] b = new byte[OffsetAndSizeConstants.PLAYER_POSITION_SIZE];
		
		b[OffsetAndSizeConstants.PACKET_TYPE_OFFSET] = ServerPacketType.PLAYER_POSITION;
		b[OffsetAndSizeConstants.SEQUENCE_NUMBER_OFFSET] = 0;
		Util.put(time, b, OffsetAndSizeConstants.PLAYER_POSITION_TIME_OFFSET);
		Util.put(s.getOwner().getID(), b, OffsetAndSizeConstants.PLAYER_POSITION_ID_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetAndSizeConstants.PLAYER_POSITION_POS_OFFSET);
		Util.put(s.getLocalRotation().getRotationColumn(2), b, OffsetAndSizeConstants.PLAYER_POSITION_DIR_OFFSET);
		Util.put(s.getMovement(), b, OffsetAndSizeConstants.PLAYER_POSITION_ORT_OFFSET);
		
		return b;
	}
}
