package common.network;

import java.net.SocketAddress;

import common.GameException;
import common.Util;

public class DataOutput implements PacketObserver {

	public boolean packetReceived(byte[] data, SocketAddress addr)
			throws GameException {
		System.out.println(addr + " - " + Util.hex(data));
		return false;
	}

}
