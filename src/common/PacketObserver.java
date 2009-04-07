/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.net.SocketAddress;

public interface PacketObserver {
	
	/**
	 * Make the {@link PacketObserver} handle the received data
	 * @param data the received data
	 * @param addr the {@link SocketAddress} from which the data came
	 */
	public void packetReceived(byte[] data, SocketAddress addr) throws GameException;
}
