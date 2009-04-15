package server;

import common.OffsetConstants;
import common.ServerPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[OffsetConstants.MESSAGE_STRING_OFFSET + sb.length];
		
		b[0] = ServerPacketType.MESSAGE;
		b[1] = 0;
		b[OffsetConstants.MESSAGE_MESSAGE_TYPE_OFFSET] = msgtype;
		Util.put(sb, b, OffsetConstants.MESSAGE_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createInitializer(long worldseed, int id, String name) {
		byte[] namebytes = name.getBytes();
		byte[] b = new byte[OffsetConstants.INITIALIZER_STRING_OFFSET + namebytes.length];
		
		b[0] = ServerPacketType.INITIALIZER;
		b[1] = 0;
		Util.put(worldseed, b, OffsetConstants.INITIALIZER_RANDOM_SEED_OFFSET);
		Util.put(id, b, OffsetConstants.INITIALIZER_ID_OFFSET);
		Util.put(namebytes, b, OffsetConstants.INITIALIZER_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createPlayerJoined(int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[OffsetConstants.PLAYER_JOINED_STRING_OFFSET + sb.length];
		
		b[0] = ServerPacketType.PLAYER_JOINED;
		b[1] = 0;
		Util.put(id,b,OffsetConstants.PLAYER_JOINED_ID_OFFSET);
		Util.put(sb,b,OffsetConstants.PLAYER_JOINED_STRING_OFFSET);
		
		return b;
	}
	
	public static byte[] createPlayerLeft(int id) {
		byte[] b = new byte[OffsetConstants.PLAYER_LEFT_SIZE];
		b[0] = ServerPacketType.PLAYER_LEFT;
		b[1] = 0;
		Util.put(id, b, OffsetConstants.PLAYER_LEFT_ID_OFFSET);
		return b;
	}

	public static byte[] createPosition(long time, Ship s) {
		byte[] b = new byte[OffsetConstants.PLAYER_POSITION_SIZE];
		
		b[0] = ServerPacketType.PLAYER_POSITION;
		b[1] = 0;
		Util.put(time, b, OffsetConstants.PLAYER_POSITION_TICK_OFFSET);
		Util.put(s.getOwner().getID(), b, OffsetConstants.PLAYER_POSITION_ID_OFFSET);
		Util.put(s.getLocalTranslation(), b, OffsetConstants.PLAYER_POSITION_POS_OFFSET);
		Util.put(s.getLocalRotation().getRotationColumn(2), b, OffsetConstants.PLAYER_POSITION_DIR_OFFSET);
		Util.put(s.getMovement(), b, OffsetConstants.PLAYER_POSITION_ORT_OFFSET);
		
		return b;
	}
	
	public static byte[] createPlayersInfo(int[] ids, String[] names){
		int size = 2;
		
		byte[][] byteNames = new byte[names.length][];
		
		
		
		for (int i = 0; i<names.length; i++){
			byteNames[i]  = names[i].getBytes();
			size += 5 + byteNames[i].length;
		}
		//TODO: if size > vad vi kan skicka i ett packet?
		byte b[] = new byte[size];
		
		b[0] = ServerPacketType.PLAYERS_INFO;
		b[1] = 0;
		
		int offset = 2;
		for(int i = 0; i < names.length; i ++){
			Util.put(ids[i], b, offset);
			offset += 4;
			b[offset]=(byte)byteNames[i].length;
			offset++;
			Util.put(byteNames[i], b, offset);
			offset += byteNames[i].length;
		}
		
		return b;
	}
}
