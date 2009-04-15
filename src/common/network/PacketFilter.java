package common.network;

import java.net.SocketAddress;

public interface PacketFilter {
	
	/**
	 * @param data
	 * @param saddr
	 * @return true if the packet is allowed through
	 */
	public boolean accept(byte[] data, SocketAddress saddr);
	
}
