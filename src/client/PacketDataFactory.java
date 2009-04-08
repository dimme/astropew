package client;

import common.ClientPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMovement(long time, Ship s){
		byte[] b = new byte[1 + 8 + 2*3*4];
		b[0] = ClientPacketType.PLAYER_MOVEMENT;
		Util.put(time, b, 1);
		Util.put(s.getLocalTranslation(), b, 9);
		Util.put(s.getMovement(), b, 21);
		return b;
	}
}
