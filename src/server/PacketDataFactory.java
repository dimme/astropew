package server;

import common.ServerPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[3 + sb.length]; //1+1+1+name
		
		b[0] = ServerPacketType.MESSAGE;
		b[1] = 0; //Seq nbr
		b[2] = msgtype;
		Util.put(sb, b, 3);
		
		return b;
	}
	
	public static byte[] createInitializer(long worldseed, int id, String name) {
		byte[] namebytes = name.getBytes();
		byte[] b = new byte[14 + namebytes.length];//1 + 1 + 8 + 4 + l
		
		b[0] = ServerPacketType.INITIALIZER;
		b[1] = 0; //Seq nbr
		Util.put(worldseed, b, 2);
		Util.put(id, b, 10);
		Util.put(namebytes, b, 14);
		
		return b;
	}
	
	public static byte[] createPlayerJoined(int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[6 + sb.length]; // 1 + 1 + 4 + l
		
		b[0] = ServerPacketType.PLAYER_JOINED;
		b[1] = 0; //Seq nbr
		Util.put(id,b,2);
		Util.put(sb,b,6);
		
		return b;
	}
	
	public static byte[] createPlayerLeft(int id) {
		byte[] b = new byte[6]; // 1 + 1 + 4
		b[0] = ServerPacketType.PLAYER_LEFT;
		b[1] = 0; //Seq nbr
		Util.put(id, b, 2);
		return b;
	}

	public static byte[] createPosition(long time, Ship s) {
		byte[] b = new byte[50]; //1 + 1 + 8 +4 + 3*3*4
		
		b[0] = ServerPacketType.PLAYER_POSITION;
		b[1] = 0; //Seq nbr
		Util.put(time, b, 2);
		Util.put(s.getOwner().getID(), b, 10);
		Util.put(s.getLocalTranslation(), b, 14);
		Util.put(s.getLocalRotation().getRotationColumn(2), b, 26);
		Util.put(s.getMovement(), b, 38);
		
		return b;
	}
}
