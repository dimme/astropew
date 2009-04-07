package server;

import common.PacketType;
import common.Util;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[1 + 1 + sb.length];
		
		b[0] = PacketType.MESSAGE;
		b[1] = msgtype;
		Util.put(sb, b, 2);
		
		return b;
	}
	
	public static byte[] createInitializer(int id, long worldseed) {
		byte[] b = new byte[1+4+8];
		
		b[0] = PacketType.INITIALIZER;
		Util.put(id, b, 1);
		Util.put(worldseed, b, 5);
		
		return b;
	}
	
	public static byte[] createPlayerJoined(int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[1 + 4 + sb.length];
		
		b[0] = PacketType.PLAYER_JOINED;
		Util.put(id,b,1);
		Util.put(sb,b,5);
		
		return b;
	}
	
	public static byte[] createPlayerLeft(int id) {
		byte[] b = new byte[1 + 4];
		b[0] = PacketType.PLAYER_LEFT;
		Util.put(id, b, 1);
		return b;
	}
}
