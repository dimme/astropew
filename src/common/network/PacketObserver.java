/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.network;

import java.net.SocketAddress;

import common.GameException;

public interface PacketObserver {

	/**
	 * Make the {@link PacketObserver} handle the received data
	 * 
	 * @param data
	 *            the received data
	 * @param addr
	 *            the {@link SocketAddress} from which the data came
	 */
	public boolean packetReceived(byte[] data, SocketAddress addr)
			throws GameException;
}
