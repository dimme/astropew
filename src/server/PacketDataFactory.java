package server;

import common.PackageType;

public class PacketDataFactory {

	public static byte[] createMessagePacket(byte msgtype, String str) {
		
		byte[] sb = str.getBytes();
		
		byte[] b = new byte[1 + 1 + sb.length];
		
		b[0] = PackageType.MESSAGE;
		b[1] = msgtype;
		putBytes(sb, b, 2);
		
		return b;
	}
	
	
	private static void putInt(int i, byte[] dst, int offset) {
		dst[offset+0] = (byte)( (i >> 24) % 256 );
		dst[offset+1] = (byte)( (i >> 16) % 256 );
		dst[offset+2] = (byte)( (i >>  8) % 256 );
		dst[offset+3] = (byte)( (i      ) % 256 );
	}
	
	private static void putBytes(byte[] src, byte[] dst, int offset) {
		System.arraycopy(src, 0, dst, offset, src.length);
	}
}
