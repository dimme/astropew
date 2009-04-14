package client;

import common.ClientPacketType;
import common.Util;
import common.world.Ship;

public class PacketDataFactory {

	public static byte[] createMovement(long time, Ship s){
		byte[] b = new byte[33]; //1 + 8 + 2*3*4
		b[0] = ClientPacketType.PLAYER_MOVEMENT;
		b[1] = 0; //Seq number
		Util.put(time, b, 2);
		Util.put(s.getLocalTranslation(), b, 10);
		Util.put(s.getMovement(), b, 22);
		return b;
	}
}
