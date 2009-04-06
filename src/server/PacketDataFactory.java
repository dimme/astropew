package server;

import common.PackageType;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[1 + 1 + sb.length];
		
		b[0] = PackageType.MESSAGE;
		b[1] = msgtype;
		put(sb, b, 2);
		
		return b;
	}
	
	public static byte[] createInitializer(int id, long worldseed) {
		byte[] b = new byte[1+4+8];
		
		b[0] = PackageType.INITIALIZER;
		put(id, b, 1);
		put(worldseed, b, 5);
		
		return b;
	}
	
	public static byte[] createPlayerJoined(int id, String name) {
		byte[] sb = name.getBytes();
		byte[] b = new byte[1 + 4 + sb.length];
		
		b[0] = PackageType.PLAYER_JOINED;
		put(id,b,1);
		put(sb,b,5);
		
		return b;
	}
	
	
	private static void put(int i, byte[] dst, int offset) {
		dst[offset+0] = (byte)( (i >> 24) % 256 );
		dst[offset+1] = (byte)( (i >> 16) % 256 );
		dst[offset+2] = (byte)( (i >>  8) % 256 );
		dst[offset+3] = (byte)( (i      ) % 256 );
	}
	
	private static void put(long l, byte[] dst, int offset) {
		dst[offset+0] = (byte)( (l >> 56) % 256 );
		dst[offset+1] = (byte)( (l >> 48) % 256 );
		dst[offset+2] = (byte)( (l >> 40) % 256 );
		dst[offset+3] = (byte)( (l >> 32) % 256 );
		dst[offset+4] = (byte)( (l >> 24) % 256 );
		dst[offset+5] = (byte)( (l >> 16) % 256 );
		dst[offset+6] = (byte)( (l >>  8) % 256 );
		dst[offset+7] = (byte)( (l      ) % 256 );
	}
	
	private static void put(byte[] src, byte[] dst, int offset) {
		System.arraycopy(src, 0, dst, offset, src.length);
	}
}
