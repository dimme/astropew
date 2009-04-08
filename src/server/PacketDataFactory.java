package server;

import common.PacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[1 + 1 + sb.length];
		
		b[0] = PacketType.MESSAGE;
		b[1] = msgtype;
		Util.put(sb, b, 2);
		
		return b;
	}
	
	public static byte[] createInitializer(long worldseed, int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[1+8+4+sb.length];
		
		b[0] = PacketType.INITIALIZER;
		Util.put(worldseed, b, 1);
		Util.put(id, b, 9);
		Util.put(sb, b, 13);
		
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

	public static byte[] createPosition(long time, Ship s) {
		byte[] b = new byte[1 + 8 + 4 + 3*3*4]; //type time playerid pos/dir/ort
		
		b[0] = PacketType.PLAYER_POSITION;
		Util.put(time, b, 1);
		Util.put(s.getOwner().getID(), b, 9);
		Util.put(s.getLocalTranslation(), b, 13);
		Util.put(s.getLocalRotation().getRotationColumn(2), b, 25);
		Util.put(s.getMovement(), b, 37);
		
		return b;
	}
}
